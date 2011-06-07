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
import java.io.File;
import world.WorldLoader;
import java.awt.Font;
import gui.TrueTypeFont;
import gui.DisplayModeChooser;
import event.InputEventHandler;
import gui.GPanel;
import world.World;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;
import java.util.logging.Level;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;

public class MainFrame {
    public static final Logger LOGGER = Logger.getLogger("TRA-MainFrame");
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static final World world = new World();
    public static final WorldLoader worldLoader = new WorldLoader();
    private static GPanel panel;
    private InputEventHandler ieh = new InputEventHandler();
    TrueTypeFont ttf;
    public static Const CONST = new Const();

    static {
        try {LOGGER.addHandler(new FileHandler("err.log",true));}
        catch(IOException ex) {LOGGER.log(Level.WARNING,ex.toString(),ex);}
    }

    public static void main(String[] args){
        LOGGER.info("[MF] Booting up...");
        MainFrame mf = new MainFrame();
        try{
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            mf.CONST.loadRegistry();
            mf.create();
            mf.run();
        }
        catch(Exception ex){LOGGER.log(Level.SEVERE,ex.toString(),ex);}
        finally{mf.destroy();}
    }

    public MainFrame(){
    }

    public void create() throws LWJGLException {
        LOGGER.info("[MF] Initializing...");
        //Display
        Display.setTitle("Transcend - v"+Const.VERSION);
        Display.create();
        DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();

        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();

        //load test map
        worldLoader.loadWorld(new File("world"+File.separator+"test.tw"));
    }

    public void destroy() {
        LOGGER.info("[MF] Shutting down...");
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        System.exit(0);
    }

    public void initGL() {
        //2D Initialization
        //glEnable(GL_TEXTURE_2D); // Enable Texture Mapping //<!>CAUSES CONFLICT WITH GL_COLOR_MATERIAL.
        glEnable(GL_COLOR_MATERIAL);
       	glClearColor(0.5f,0.5f,0.5f,0f); // Black Background
        glDisable(GL_DITHER);
        glDisable(GL_LIGHTING);
        glEnable(GL_NORMALIZE); // calculated normals when scaling
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_COLOR_MATERIAL);
        glShadeModel(GL_SMOOTH); // Enable Smooth Shading
        
        //2D Scene
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -1, 1);

        ttf = new TrueTypeFont(new Font("Arial",Font.BOLD,46),true);
    }

    public void update() {
        //FIXME: Hook to world loop
        world.update();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glColor4f(255,255,255,255);
        glBegin(GL_QUADS);
        glVertex2f(150f,150f);
        glVertex2f(50f,150f);
        glVertex2f(50f,50f);
        glVertex2f(150f,50f);
        glEnd();

        world.draw();
        //ttf.drawString(100, 100, "HEYY",1,1);
        
    }

    public void run() {
        while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if(Display.isVisible()) {
                ieh.triggerMouseEvent();
                ieh.triggerKeyboardEvent();
                update();
                render();
            }else {
                if(Display.isDirty())render();
                try {Thread.sleep(100);}
                catch(InterruptedException ex) {LOGGER.info("Failed thread sync.");}
            }
            Display.update();
            Display.sync(60);
        }
    }
}
