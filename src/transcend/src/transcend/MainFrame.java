/**********************\
  file: MainFrame
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

//FIXME: Add loading
//FIXME: Add GUI

package transcend;
import shader.Shader;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import gui.GButton;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.net.URL;
import java.io.IOException;
import org.newdawn.slick.Color;
import gui.Camera;
import org.lwjgl.LWJGLUtil;
import event.KeyboardListener;
import entity.Player;
import world.ElementBuilder;
import java.io.File;
import world.WorldLoader;
import gui.DisplayModeChooser;
import event.InputEventHandler;
import gui.GPanel;
import world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;
import java.util.logging.Level;
import static org.lwjgl.opengl.GL11.*;

public class MainFrame implements KeyboardListener{
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static final File basedir = new File(".");
    public static final World world = new World();
    public static final WorldLoader worldLoader = new WorldLoader();
    public static final ElementBuilder elementBuilder = new ElementBuilder();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final Camera camera = new Camera();
    public static final Const CONST = new Const();
    public static int fps = 60;
    private static GPanel menu,hid;
    public static boolean pause = false;


    public static void main(String[] args){
        Const.LOGGER.info("[MF] Booting up...");
        System.setProperty("org.lwjgl.librarypath",new File(new File(basedir, "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        MainFrame mf = new MainFrame();
        try{
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            mf.CONST.loadRegistry();
            mf.create();
            mf.run();
        }
        catch(Exception ex){Const.LOGGER.log(Level.SEVERE,ex.toString(),ex);}
        finally{mf.destroy();}
    }

    public MainFrame(){}

    public void create() throws LWJGLException, IOException {
        Const.LOGGER.info("[MF] Initializing...");
        //Display
        Display.setIcon(new ByteBuffer[] {
            loadIcon(new File(basedir,"data"+File.separator+"icon_16.png").toURI().toURL()),
            loadIcon(new File(basedir,"data"+File.separator+"icon_32.png").toURI().toURL()),
            loadIcon(new File(basedir,"data"+File.separator+"icon_128.png").toURI().toURL()),
        });
        Display.setTitle("Transcend - v"+Const.VERSION);
        Display.create();
        DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
        fps=CONST.gInteger("FPS");

        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();
        initGame();
    }

    public void destroy() {
        Const.LOGGER.info("[MF] Shutting down...");
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        System.exit(0);
    }

    public void initGame(){
        //load test map
        worldLoader.loadWorld(new File("world"+File.separator+"test.tw"));

        ieh.addKeyboardListener(this);
        Player p = new Player();
        int id = world.addEntity(p);
        world.printWorldStats();
        camera.follow(id);
        camera.setBoundary(100);

        menu = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        GButton button = new GButton("Quit"){
            public void onRelease(){destroy();}
        };
        button.setBounds(10, 10, 100, 30);
        menu.add(button);

        hid = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
    }

    public void initGL() {
        //2D Initialization
       	glClearColor(1,1,1,0);
        glClearDepth(1);
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        //glDisable(GL_DITHER);
        glDisable(GL_LIGHTING);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        
        //2D Scene
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -100, 100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void update() {
        //Hook to world loop
        if(!pause){
            world.update();
            camera.update();
        }
        //Handle input
        ieh.triggerKeyboardEvent();
        ieh.triggerMouseEvent();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Color.white.bind();
        
        camera.camBegin();
            world.draw();
        camera.camEnd();

        menu.paint();
        hid.paint();
    }
    

    public void run() {
        while(!Display.isCloseRequested()) {
            if(Display.isVisible()) {
                update();
                render();
            }else {
                if(Display.isDirty())render();
                try {Thread.sleep(100);}
                catch(InterruptedException ex) {Const.LOGGER.info("Failed thread sync.");}
            }
            Display.update();
            Display.sync(fps);
        }
    }

    public void pause(){pause=true;}
    public void unpause(){pause=false;}

    public void keyPressed(int key) {}
    public void keyReleased(int key) {
        switch(key){
        case Keyboard.KEY_F11:
            try{
            if(Display.isFullscreen())Display.setFullscreen(false);
            else Display.setFullscreen(true);
            }catch(Exception e){Const.LOGGER.log(Level.WARNING, "Failed to switch to fullscreen mode.",e);}
            break;
        case Keyboard.KEY_ESCAPE:
            if(!menu.isVisible()){
                menu.setVisible(true);
                hid.setVisible(false);
                pause();
            }else{
                menu.setVisible(false);
                hid.setVisible(true);
                unpause();
            }
            break;
        }

    }
    public void keyType(int key) {}



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //SPARE FUNCTIONS

    private static ByteBuffer loadIcon(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            PNGDecoder decoder = new PNGDecoder(is);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            return bb;
        } finally {
            is.close();
        }
    }

    private int emptyTexture() {  // Create An Empty Texture
        // Create Storage Space For Texture Data (128x128x4)
        ByteBuffer data = BufferUtils.createByteBuffer(DISPLAY_WIDTH * DISPLAY_HEIGHT * 4);
        data.limit(data.capacity());

        IntBuffer buf = BufferUtils.createIntBuffer(1);
        glGenTextures(buf);
        glBindTexture(GL_TEXTURE_2D, buf.get(0));  // Bind The Texture

        // Build Texture Using Information In data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        return buf.get(0);  // Return The Texture ID
    }
}
