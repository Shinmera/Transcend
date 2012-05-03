/**********************\
  file: TextureLoader.java
  package: transcend.graph
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import transcend.main.Const;
import transcend.main.MainFrame;

/**
 * A utility class to load textures for OpenGL. This source is based
 * on a texture that can be found in the Java Gaming (www.javagaming.org)
 * Wiki. It has been simplified slightly for explicit 2D graphics use.
 *
 * OpenGL uses a particular image format. Since the images that are
 * loaded from disk may not match this format this loader introduces
 * a intermediate image which the source image is copied into. In turn,
 * this image is used as source for the OpenGL texture.
 * 
 * Additionally this has been modified to match the requirements of the
 * Transcend Engine. In particular, this includes deferred texture loading
 * and the assurance of image loading.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 * @author Nicolas Hafner
 */
public class TextureLoader {
    /** To make sure images are loaded fully. */
    MediaTracker mediaTracker = new MediaTracker(MainFrame.frame);
    
    /** The table of textures that have been loaded in this loader */
    private HashMap<String, Texture> table = new HashMap<String, Texture>();
    private Queue<Texture> deferred = new LinkedList<Texture>();

    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;

    /** The colour model for the GL image */
    private ColorModel glColorModel;

    /** Scratch buffer for texture ID's */
    private IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);
    

    /**
     * Create a new texture loader based on the game panel
     */
    public TextureLoader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    }
    
    public void clearPool(){
        table.clear();
        deferred.clear();
    }

    /**
     * Create a new texture ID
     *
     * @return A new texture ID
     */
    private int createTextureID() {
        try {
            if(!Display.isCurrent()){
                Const.LOGGER.log(Level.SEVERE,"[TextureLoader] Not in current context! Attempt to create texture will fail.");
            }
        } catch (LWJGLException ex) {
            Const.LOGGER.log(Level.WARNING,"[TextureLoader] Couldn't perform context check!",ex);
        }
        glGenTextures(textureIDBuffer);
        return textureIDBuffer.get(0);
    }

    /**
     * Get the closest greater power of 2 to the fold number
     *
     * @param fold The target number
     * @return The power of 2
     */
    private static int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }
    
    public int getDeferredCount(){
        return deferred.size();
    }
    
    public int getTexturesCount(){
        return table.size()+deferred.size();
    }
    
    public Texture getNextDeferred(){
        return deferred.poll();
    }
    
    public MediaTracker getMediaTracker(){
        return mediaTracker;
    }

    /**
     * Convert the buffered image to a texture
     *
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
     * @return A buffer containing the data
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage,Texture texture) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }

        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);

        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }

        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image
     *
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private Image loadImage(String ref,int texID) throws IOException {
        URL url = TextureLoader.class.getClassLoader().getResource(ref);

        if (url == null) {
            url = MainFrame.fileStorage.getFile(ref).toURI().toURL();
            if(url == null)throw new IOException("Cannot find: " + ref);
        }

        Image img = new ImageIcon(url).getImage();
        mediaTracker.addImage(img,texID);
        
        return img;
    }
    
    private Image loadImageByImageIO(String ref,int texID) throws IOException{
        URL url = TextureLoader.class.getClassLoader().getResource(ref);

        if (url == null) {
            url = MainFrame.fileStorage.getFile(ref).toURI().toURL();
            if(url == null)throw new IOException("Cannot find: " + ref);
        }
        
        Image img = ImageIO.read(url);
        return img;
    }
    
    private BufferedImage convertImageToBuffered(Image img){
        if(img==null){
            Const.LOGGER.warning("[TextureLoader] Got NULL image, creating empty buffer.");
            img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        }
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
    
    /**
     * Load a texture
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName) throws IOException {
        Texture tex = table.get(resourceName);
        if (tex != null) return tex;
        
        tex = getTexture(resourceName,GL_TEXTURE_2D,GL_RGBA,GL_LINEAR,GL_LINEAR);
        table.put(resourceName,tex);

        return tex;
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(String resourceName,
                              int target,
                              int dstPixelFormat,
                              int minFilter,
                              int magFilter) throws IOException {
        int srcPixelFormat;

        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(target,textureID);

        // bind this texture
        glBindTexture(target, textureID);

        BufferedImage bufferedImage = convertImageToBuffered(loadImage(resourceName,textureID));
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        texture.setPixelFormat(dstPixelFormat);
        texture.setMagFilter(magFilter);
        texture.setMinFilter(magFilter);
        texture.setResourceName(resourceName);

        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
        }

        // convert that image into a byte buffer of texture data
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);

        if (target == GL_TEXTURE_2D) {
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
        }

        // produce a texture from the byte buffer
        glTexImage2D(target,
                      0,
                      dstPixelFormat,
                      get2Fold(bufferedImage.getWidth()),
                      get2Fold(bufferedImage.getHeight()),
                      0,
                      srcPixelFormat,
                      GL_UNSIGNED_BYTE,
                      textureBuffer );
        glBindTexture(texture.getTarget(),0);
        texture.setLoaded(true);
        return texture;
    }

    /**
     * Load a texture in deferred mode.
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getDeferredTexture(String resourceName) throws IOException {
        Texture tex = table.get(resourceName);
        if (tex != null) return tex;
        
        tex = getDeferredTexture(resourceName,GL_TEXTURE_2D,GL_RGBA,GL_LINEAR,GL_LINEAR);
        deferred.offer(tex);

        return tex;
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk in deferred mode.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getDeferredTexture(String resourceName,
                              int target,
                              int dstPixelFormat,
                              int minFilter,
                              int magFilter) throws IOException {

        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(target,textureID);

        Image i = loadImage(resourceName,textureID);
        mediaTracker.addImage(i, textureID);
        texture.setImage(i);
        texture.setPixelFormat(dstPixelFormat);
        texture.setMagFilter(magFilter);
        texture.setMinFilter(magFilter);
        texture.setResourceName(resourceName);
        return texture;
    }
    
    public Texture loadDeferredTexture(Texture texture){
        try {
            mediaTracker.waitForID(texture.getTextureID());
            int id = mediaTracker.statusID(texture.getTextureID(), true);
            if((id & MediaTracker.ERRORED) == MediaTracker.ERRORED){
                Const.LOGGER.log(Level.SEVERE,"[TextureLoader]["+texture.getResourceName()+"] Error during image load! Attempting ImageIO load.");
                try {texture.setImage(loadImageByImageIO(texture.getResourceName(), texture.getTextureID()));
                } catch (IOException ex) { Const.LOGGER.log(Level.SEVERE,"[TextureLoader] Failed loading through ImageIO!",ex);}
            }if((id & MediaTracker.ABORTED) == MediaTracker.ABORTED){
                Const.LOGGER.log(Level.SEVERE,"[TextureLoader]["+texture.getResourceName()+"] Image loading was aborted! Attempting ImageIO load.");
                try {texture.setImage(loadImageByImageIO(texture.getResourceName(), texture.getTextureID()));
                } catch (IOException ex) { Const.LOGGER.log(Level.SEVERE,"[TextureLoader] Failed loading through ImageIO!",ex);}
            }if((id & MediaTracker.LOADING) == MediaTracker.LOADING)
                Const.LOGGER.log(Level.WARNING,"[TextureLoader]["+texture.getResourceName()+"] Image still loading! ");
        } catch (InterruptedException ex) {
            Const.LOGGER.log(Level.SEVERE,"[TextureLoader]["+texture.getResourceName()+"] Image loading interrupted! ",ex);
        }
        
        glBindTexture(texture.getTarget(), texture.getTextureID());
        BufferedImage bufferedImage = convertImageToBuffered(texture.getImage());
        //Dispose of the image reference.
        if(texture.getImage()!=null)texture.getImage().flush();
        texture.setImage(null);
        //Update data.
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        
        int srcPixelFormat;
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
        }

        // convert that image into a byte buffer of texture data
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);

        if(texture.getTarget() == GL_TEXTURE_2D){
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, texture.getMinFilter());
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, texture.getMagFilter());
        }

        // produce a texture from the byte buffer
        glTexImage2D(texture.getTarget(),
                      0,
                      texture.getPixelFormat(),
                      get2Fold(bufferedImage.getWidth()),
                      get2Fold(bufferedImage.getHeight()),
                      0,
                      srcPixelFormat,
                      GL_UNSIGNED_BYTE,
                      textureBuffer );
        glBindTexture(texture.getTarget(),0);
        texture.setLoaded(true);
        
        if(deferred.contains(texture))deferred.remove(texture);
        table.put(texture.getResourceName(), texture);
        
        return texture;
    }

}