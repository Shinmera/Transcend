/**********************\
  file: MainFrame
  package: cape.main
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package cape.main;
import NexT.script.ScriptManager;
import NexT.util.Toolkit;
import cape.physics.Block;
import cape.physics.Entity;
import cape.physics.PhysicsController;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.logging.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.event.EventHandler;
import transcend.event.InputEventHandler;
import transcend.event.KeyboardListener;
import transcend.event.MouseListener;
import transcend.graph.AbstractGraph;
import transcend.gui.TrueTypeFont;
import transcend.main.Const;
import static transcend.main.Jitter.j;
import static transcend.main.Jitter.jps;

public class MainFrame implements KeyboardListener{
    static{
        try{System.setProperty("org.lwjgl.librarypath"  ,new File(new File(new File(System.getProperty("user.dir")), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        }catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"Failed to set LWJGL library path!",ex);}
        org.lwjgl.Sys.initialize();
        Toolkit.printMenu("><Transcend Engine v"+Const.VERSION+" \n"+
                          "(c)2011-2012 "+Const.MAINTAINER+" \n"+
                          "Developed by "+Const.DEVELOPER+"\n"+
                          "http://www.tymoon.eu", "/", 1, null);
        Const.LOGGER.info("[MF] Booting up...");
    }
    public static final double VERSION = 0.1;
    public static int DISPLAY_WIDTH = 800;
    public static int DISPLAY_HEIGHT= 600;
    public static double DISPLAY_ASPECT= DISPLAY_WIDTH/DISPLAY_HEIGHT;
    public static int fps = 60, ups = 60;
    public static int ACSIZE = 2;
    public static boolean pause = false;
    public static TrueTypeFont ttf = null;
    public static final Const CONST = new Const();
    public static final File basedir = new File(System.getProperty("user.dir"));
    public static final World world = new World();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final EventHandler eh = new EventHandler();
    public static final ScriptManager scriptManager = new ScriptManager();
    public static final Editor editor = new Editor();
    public static final PhysicsController physics = new PhysicsController();
    public static final int[][] DEFAULT_DIM = {{1280,960},{1280,720},{1280,800}};//4:3 16:9 16:10
    public static long updateTime = 0;
    private final Updater updater = new Updater();

    public static void main(String[] args) throws Throwable{
        MainFrame mf = new MainFrame();
        try{
            mf.CONST.loadRegistry();
            mf.create();
            mf.run();
        }
        catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"[MF] Error in main thread!",ex);}
        finally{mf.destroy();}
    }

    public void create() throws LWJGLException, IOException {
        Const.LOGGER.info("[MF] Initializing...");
        Display.setTitle("Transcend - v"+Const.VERSION+" - CAPE v"+VERSION);
        Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
        Display.setVSyncEnabled(true);
        Display.setLocation(0, 0);
        Display.create();
        DISPLAY_WIDTH =Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
        DISPLAY_ASPECT=DISPLAY_WIDTH/(DISPLAY_HEIGHT+0.0);
        fps=CONST.gInteger("FPS");
        ups=CONST.gInteger("UPS");
        ACSIZE=CONST.gInteger("ANTIALIAS");
        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();
        initGame();
        updater.start();
    }

    public static void destroy() {
        Const.LOGGER.info("[MF] Shutting down...");
        try{
            Mouse.destroy();
            Keyboard.destroy();
            Display.destroy();
            AL.destroy();
        }catch(Exception ex){//Apparently shit can go bonkers in this.
            Const.LOGGER.log(Level.SEVERE,"Couldn't clean up properly!",ex);
        }finally{
            System.exit(0);
        }
    }

    public void initGame(){
        Const.LOGGER.info("[MF] initGame");
        ttf = new TrueTypeFont(new Font("Arial",Font.PLAIN,12),true);
        ieh.addKeyboardListener(this);
        world.addBlock(new Block(-256,-64,512,64));
    }

    public void initGL() {
        Const.LOGGER.info("[MF] initGL");
        //2D Initialization
       	glClearColor(0,0,0,0);
        glClearAccum(0,0,0,0);
        glClearDepth(1);        
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        
        //glDisable(GL_DITHER);
        glDisable(GL_LIGHTING);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        
        //2D Scene
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -10, 10);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void update() {
        //Hook to world loop
        if(!pause){
            physics.update();
            world.update();
        }
        //Handle input
        ieh.triggerKeyboardEvent();
        ieh.triggerMouseEvent();
    }

    public void render() {
        glLoadIdentity();
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        glGetInteger(GL_VIEWPORT, viewport);

        if(ACSIZE>1){
            glClear(GL_ACCUM_BUFFER_BIT);
            for (int jitter = 0; jitter < ACSIZE; jitter++) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glPushMatrix();
                glTranslatef(j[jps[ACSIZE]][jitter].x * 4.5f / viewport.get(2),
                             j[jps[ACSIZE]][jitter].y * 4.5f / viewport.get(3), 0.0f);

                renderScene();

                glPopMatrix();
                glAccum(GL_ACCUM, 1.0f / ACSIZE);
            }
            glAccum(GL_RETURN, 1.0f);
        }else{
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderScene();
        }
    }

    public void renderScene(){
        glPushMatrix();
            glTranslatef(editor.x_off,editor.y_off,0);
            Color.white.bind();
            world.draw();
            Color.white.bind();
            AbstractGraph.glCross2d(0,0,50);
            editor.paint();
        glPopMatrix();
        AbstractGraph.glCross2d(Mouse.getX(),Mouse.getY(),10);
        
        Color.red.bind();   if(pause)ttf.drawString(10, DISPLAY_HEIGHT-20, "PAUSED", 1, 1);
        Color.white.bind(); ttf.drawString(10, DISPLAY_HEIGHT-30, "Blocks: "+world.blockSize()+" Entities: "+world.entitySize()+"\n"+
                                                                  "Mode: "+editor.getModeS()+" Place: "+editor.getPlaceS()+"\n"+
                                                                  "Updatetime: "+updateTime+" Passes: "+physics.passes, 1, 1);
        
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public void run() throws Throwable {
        while(!Display.isCloseRequested()) {
            try{
                if(Display.isVisible()) {
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                    render();
                    glFlush();
                }else{
                    if(Display.isDirty())render();
                    try {Thread.sleep(100);}
                    catch(InterruptedException ex) {Const.LOGGER.info("Failed thread sync.");}
                }
                Display.update();
                Display.sync(fps);
            }catch(Throwable ex){
                Const.LOGGER.log(Level.SEVERE,"Exception in main run loop!",ex);
                org.lwjgl.Sys.alert("Transcend Engine "+Const.VERSION,"Encountered an exception in the main loop!\nStack Trace:\n"+ex.getMessage()+"\n\nGoodbye!");
                throw ex;
            }
        }
    }

    public static void pause(){pause=true;}
    public static void unpause(){pause=false;}
    public static void setClearColor(Color c){
        glClearColor(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f,c.getAlpha()/255.0f);
        glClearAccum(0,0,0,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void keyPressed(int key) {}
    public void keyReleased(int key) {
        switch(key){
        case Keyboard.KEY_F11:
            try{
            if(Display.isFullscreen())Display.setFullscreen(false);
            else                      Display.setFullscreen(true);
            }catch(Exception e){Const.LOGGER.log(Level.WARNING, "Failed to switch to fullscreen mode.",e);}
            break;
        case Keyboard.KEY_SPACE:pause=!pause;break;
        case Keyboard.KEY_ESCAPE:
        case Keyboard.KEY_DELETE:destroy();break;
        }
    }
    public void keyType(int key) {}

    class Updater extends Thread{
        public void run(){
            while(!isInterrupted()){
                try{
                    long milis = System.currentTimeMillis();
                    update();
                    updateTime = System.currentTimeMillis() - milis;
                    milis = (1000/ups)-updateTime;
                    if(milis<0)milis=0;
                    try{Thread.sleep(milis);}catch(Exception ex){Const.LOGGER.log(Level.WARNING,"Updater thread failed!",ex);}
                }catch(Throwable ex){
                    Const.LOGGER.log(Level.SEVERE,"Exception in main update loop! Attempting to continue... ",ex);
                }
            }
        }
    }
}
