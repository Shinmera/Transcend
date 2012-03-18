/**********************\
  file: Texture.java
  package: transcend.graph
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.graph;

import java.awt.Image;
import static org.lwjgl.opengl.GL11.*;
import transcend.main.Const;
import transcend.main.MainFrame;

/**
 * A texture to be bound within OpenGL. This object is responsible for
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 *
 * Since textures need to be powers of 2 the actual texture may be
 * considerably bigged that the source image and hence the texture
 * mapping coordinates need to be adjusted to matchup drawing the
 * sprite against the texture.
 * 
 * Modified to match the requirements of the Transcend Engine and support
 * deferred loading and additional texture information.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 * @author Nicolas Hafner
 */
public class Texture {

    /** The GL target type */
    private int		target;
    /** The GL texture ID */
    private int		textureID;
    /** The height of the image */
    private int		height;
    /** The width of the image */
    private int		width;
    /** The width of the texture */
    private int		texWidth;
    /** The height of the texture */
    private int		texHeight;
    /** The ratio of the width of the image to the texture */
    private float	widthRatio;
    /** The ratio of the height of the image to the texture */
    private float	heightRatio;
    private boolean loaded = false;
    private int pixelFormat = GL_RGBA;
    private int minFilter = GL_LINEAR;
    private int magFilter = GL_LINEAR;
    private Image imageData = null;
    private String resourceName = null;

    /**
    * Create a new texture
    *
    * @param target The GL target
    * @param textureID The GL texture ID
    */
    public Texture(int target, int textureID) {
        this.target = target;
        this.textureID = textureID;
    }

    /**
    * Bind the specified GL context to a texture
    */
    public void bind() {
        if(!loaded){
            Const.LOGGER.warning("[Texture]["+resourceName+"] Early loading of deferred texture!");
            load();
        }
        glBindTexture(target, textureID);
    }

    /**
    * Set the height of the image
    *
    * @param height The height of the image
    */
    public void setHeight(int height) {
        this.height = height;
        setHeight();
    }

    /**
    * Set the width of the image
    *
    * @param width The width of the image
    */
    public void setWidth(int width) {
        this.width = width;
        setWidth();
    }

    /**
    * Get the height of the original image
    *
    * @return The height of the original image
    */
    public int getImageHeight() {
        if(!loaded){
            Const.LOGGER.warning("[Texture]["+resourceName+"] Early loading of deferred texture!");
            load();
        }
        return height;
    }

    /**
    * Get the width of the original image
    *
    * @return The width of the original image
    */
    public int getImageWidth() {
        if(!loaded){
            Const.LOGGER.warning("[Texture]["+resourceName+"] Early loading of deferred texture!");
            load();
        }
        return width;
    }

    /**
    * Get the height of the physical texture
    *
    * @return The height of physical texture
    */
    public float getHeight() {
        if(!loaded){
            Const.LOGGER.warning("[Texture]["+resourceName+"] Early loading of deferred texture!");
            load();
        }
        return heightRatio;
    }

    /**
    * Get the width of the physical texture
    *
    * @return The width of physical texture
    */
    public float getWidth() {
        if(!loaded){
            Const.LOGGER.warning("[Texture]["+resourceName+"] Early loading of deferred texture!");
            load();
        }
        return widthRatio;
    }

    public int getTextureID(){
        return textureID;
    }

    public int getTarget(){
        return target;
    }

    /**
    * Set the height of this texture
    *
    * @param texHeight The height of the texture
    */
    public void setTextureHeight(int texHeight) {
        this.texHeight = texHeight;
        setHeight();
    }

    /**
    * Set the width of this texture
    *
    * @param texWidth The width of the texture
    */
    public void setTextureWidth(int texWidth) {
        this.texWidth = texWidth;
        setWidth();
    }

    /**
    * Set the height of the texture. This will update the
    * ratio also.
    */
    private void setHeight() {
        if (texHeight != 0) {
                heightRatio = ((float) height) / texHeight;
        }
    }

    /**
    * Set the width of the texture. This will update the
    * ratio also.
    */
    private void setWidth() {
        if (texWidth != 0) {
                widthRatio = ((float) width) / texWidth;
        }
    }

    public void setLoaded(boolean loaded){
        this.loaded=true;
    }

    public boolean isLoaded(){
        return loaded;
    }

    public void setPixelFormat(int format){
        pixelFormat=format;
    }

    public int getPixelFormat(){
        return pixelFormat;
    }

    public void setMinFilter(int filter){
        minFilter=filter;
    }

    public int getMinFilter(){
        return minFilter;
    }

    public void setMagFilter(int filter){
        magFilter=filter;
    }

    public int getMagFilter(){
        return magFilter;
    }

    public void setImage(Image img){
        imageData=img;
    }

    public Image getImage(){
        return imageData;
    }

    public void setResourceName(String name){
        resourceName = name;
    }

    public String getResourceName(){
        return resourceName;
    }
    
    public void setTextureID(int id){
        textureID=id;
    }

    public void load(){
        if(!loaded)MainFrame.textureLoader.loadDeferredTexture(this);
    }
}