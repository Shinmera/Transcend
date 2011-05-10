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
import java.awt.Font;
import gui.TrueTypeFont;
import org.newdawn.slick.Color;
import gui.DisplayModeChooser;
import event.InputEventHandler;
import gui.GButton;
import gui.GPanel;
import world.World;
import java.util.HashMap;
import NexT.util.Arguments;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
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
    private static World world;
    private static GPanel panel;
    private InputEventHandler ieh = new InputEventHandler();
    TrueTypeFont ttf;

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
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            mf.create();
            mf.run();
        }
        catch(Exception ex){LOGGER.log(Level.SEVERE,ex.toString(),ex);}
        finally{mf.destroy();}
    }

    public MainFrame(){
        this.world = new World();
        this.panel = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
    }
    public static World getWorld(){return world;}

    public void create() throws LWJGLException {
        //Display
        Display.setTitle("Transcend - v"+Const.VERSION);
        Display.create();
        DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();

        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();
    }

    public void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        System.exit(0);
    }

    public void initGL() {
        //2D Initialization
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
       	glClearColor(0.5f,0.5f,0.5f,0f); // Black Background
        glDisable(GL_DITHER);
        glDepthFunc(GL_LESS); // Depth function less or equal
        glEnable(GL_NORMALIZE); // calculated normals when scaling
        glEnable(GL_BLEND); // Enabled blending
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // selects blending method
        glEnable(GL_ALPHA_TEST); // allows alpha channels or transperancy
        glAlphaFunc(GL_GREATER, 0.1f); // sets aplha function
        glShadeModel(GL_SMOOTH); // Enable Smooth Shading
        glDepthMask(true);								// Enable Depth Mask
        
        //2D Scene
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -1, 1);

    
        /*GButton button = new GButton();
        button.setBounds(10,10,100,100);
        button.setBackground(Color.cyan);
        button.setForeground(Color.black);
        button.setText("BUTTON");
        this.panel.add(button);*/

        ttf = new TrueTypeFont(new Font("Arial",Font.BOLD,46),true);
    }

    public void update() {
        //FIXME: Hook to world loop
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();


        //world.draw();
        panel.paint();
        ttf.drawString(100, 100, "HEYY",1,1);
        
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
