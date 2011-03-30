/**********************\
  file: MainFrame
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend;
import java.util.HashMap;
import NexT.util.Arguments;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;
import java.util.logging.Level;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class MainFrame {
    public static final Logger LOGGER = Logger.getLogger("TRA-MainFrame");
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;


    static {
        try {LOGGER.addHandler(new FileHandler("err.log",true));}
        catch(IOException ex) {LOGGER.log(Level.WARNING,ex.toString(),ex);}
    }

    public static void main(String[] args){
        Arguments arg = new Arguments();
        //arg.addArgument('m', "map file", "path");
        arg.addArgument('w', "windowed", "Run in window mode", null);
        arg.addArgument('c', "config","Change the configuration file.","path");
        String flags = arg.eval(args);
        HashMap<Character,String> vals = arg.getVals();


        MainFrame mf = new MainFrame();
        try{
            mf.create(!flags.contains("w"));
            mf.run();
        }
        catch(Exception ex){LOGGER.log(Level.SEVERE,ex.toString(),ex);}
        finally{mf.destroy();}
    }

    public MainFrame(){
        
    }

    public void create(boolean fs) throws LWJGLException {
        //Display
        Display.setFullscreen(fs);
        if(fs){ DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
                DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();}
        else    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        Display.setTitle("Transcend - v"+Const.VERSION);
        Display.create();

        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();
        resizeGL();
    }

    public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
    }

    public void initGL() {
    //2D Initialization
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
    }

    public void processKeyboard() {
    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {}
    }

    public void processMouse() {
    }

    public void update() {
    }

    public void render() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();

    }

    public void resizeGL() {
    //2D Scene
    glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0f,DISPLAY_WIDTH,0.0f,DISPLAY_HEIGHT);
    glPushMatrix();

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
    }

    public void run() {
    while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      if(Display.isVisible()) {
        processKeyboard();
        processMouse();
        update();
        render();
      }
      else {
        if(Display.isDirty()) {
          render();
        }
        try {
          Thread.sleep(100);
        }
        catch(InterruptedException ex) {
        }
      }
      Display.update();
      Display.sync(60);
    }
    }
}
