/**********************\
  file: MainFrame
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.main;
import NexT.script.ScriptManager;
import NexT.util.Toolkit;
import java.awt.Dimension;
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
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.SoundStore;
import transcend.entity.Player;
import transcend.event.EventHandler;
import transcend.event.InputEventHandler;
import transcend.event.KeyboardListener;
import transcend.graph.*;
import transcend.gui.*;
import static transcend.main.Jitter.j;
import static transcend.main.Jitter.jps;
import transcend.world.WorldLoader;

public class MainFrame implements KeyboardListener{
    public static String mode = "NULL";
    static{
        mode="INIT-0";
        try{System.setProperty("org.lwjgl.librarypath"  ,new File(new File(new File(System.getProperty("user.dir")), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        }catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"Failed to set LWJGL library path!",ex);}
        org.lwjgl.Sys.initialize();
        Toolkit.printMenu("><Transcend Engine v"+Const.VERSION+" \n"+
                          "(c)2011-2012 "+Const.MAINTAINER+" \n"+
                          "Developed by "+Const.DEVELOPER+"\n"+
                          "http://www.tymoon.eu", "/", 1, null);
        Const.LOGGER.info("[MAIN] Booting up...");
    }
    public static final Const CONST = new Const();
    public static final File basedir = new File(System.getProperty("user.dir"));
    public static final FileStorage fileStorage = new FileStorage();
    public static final TFrame frame = new TFrame("Transcend - v"+Const.VERSION);
    public static final WorldLoader world = new WorldLoader();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final EventHandler eh = new EventHandler();
    public static final Camera camera = new Camera();
    public static final Editor editor = new Editor();
    public static final TexturePool texturePool = new TexturePool();
    public static final SoundPool soundPool = new SoundPool();
    public static final FontPool fontPool = new FontPool();
    public static final ScriptManager scriptManager = new ScriptManager();
    public static final Player player = new Player();
    public static final int[][] DEFAULT_DIM = {{1280,960},{1280,720},{1280,800}};//4:3 16:9 16:10
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static double DISPLAY_ASPECT= DISPLAY_WIDTH/DISPLAY_HEIGHT;
    public static int FPS = 60, UPS = 60;
    public static int ACSIZE = 2;
    public static Texture backTileTexture = null,frontTileTexture = null;
    public static boolean pause = false;
    public static GPanel menu,hud;
    public static GLog gameLog;
    public static Loader loader;
    private static Color clearcolor = new Color(0.2f,0.2f,0.2f);
    private final Updater updater = new Updater();
    private TextureRenderer textureRenderer;

    //TODO: Automate INIT sequence.
    public static void main(String[] args) throws Throwable{
        MainFrame mf = new MainFrame();
        try{
            setMode("INIT-1");
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            mf.create();
            mf.run();
        }
        catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"[MAIN] Error in main thread!",ex);}
        finally{mf.destroy();}
    }

    public void create() throws LWJGLException, IOException {
        Const.LOGGER.info("[MAIN] Initializing...");
        
        DISPLAY_WIDTH =Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
        DISPLAY_ASPECT=DISPLAY_WIDTH/(DISPLAY_HEIGHT+0.0);
        FPS=CONST.gInteger("FPS");
        UPS=CONST.gInteger("UPS");
        ACSIZE=CONST.gInteger("ANTIALIAS");
        
        Const.LOGGER.info("[MAIN] Creating Display interface...");
        Display.setParent(frame.getCanvas());
        Display.create();
        
        Const.LOGGER.info("[MAIN] Creating Keyboard and Mouse interfaces...");
        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        
        setMode("INIT-2");
        initGL();
        initGame();
        Const.LOGGER.info("[MAIN] Launching update loop...");
        updater.start();

        frame.setPreferredSize(new Dimension(MainFrame.DISPLAY_WIDTH,MainFrame.DISPLAY_HEIGHT));
        frame.setVisible(true);
    }

    public void destroy() {
        setMode("DOWN-0");
        Const.LOGGER.info("[MAIN] Shutting down...");
        updater.interrupt();
        try{
            Mouse.destroy();
            Keyboard.destroy();
            Display.destroy();
            AL.destroy();
            frame.dispose();
        }catch(Throwable t){//Apparently shit can go bonkers in this.
            Const.LOGGER.log(Level.SEVERE,"Couldn't clean up properly!",t);
        }finally{
            setMode("DOWN-1");
            Const.LOGGER.info("[MAIN] Goodbye.");
            System.exit(0);
        }
    }

    public void initGame(){
        Const.LOGGER.info("[MAIN] initGame");
        loader = new Loader();
        ieh.addKeyboardListener(this);
        camera.setBoundary(300);
        camera.setPosition(0,0);
        double zoom;
        if(DISPLAY_ASPECT==4.0/3.0)           zoom = DISPLAY_WIDTH / (DEFAULT_DIM[0][0] + 0.0);
        else if(DISPLAY_ASPECT == 16.0 / 9.0) zoom = DISPLAY_WIDTH / (DEFAULT_DIM[1][0] + 0.0);
        else if(DISPLAY_ASPECT == 16.0 / 10.0)zoom = DISPLAY_WIDTH / (DEFAULT_DIM[2][0] + 0.0);
        else                                  zoom = DISPLAY_WIDTH / (DEFAULT_DIM[2][0] + 0.0);
        camera.setZoom(zoom*camera.getZoom());

        hud = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        hud.setBackground(new Color(0,0,0,0));

        menu = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        menu.setBackground(new Color(0,0,0,0));
        ieh.addMouseListener(editor);
        
        gameLog = new GLog();

        //LOAD MENU
        loader.setHelper(new MenuLoader());
        loader.start("Loading Menu and Base...");
    }

    public void initGL() {
        Const.LOGGER.info("[MAIN] initGL");
        //2D Initialization
       	glClearColor(clearcolor.getRed(),clearcolor.getGreen(),clearcolor.getBlue(),clearcolor.getAlpha());
        glClearAccum(0,0,0,0);
        glClearDepth(1);        
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_TEXTURE_2D);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        
        //glDisable(GL_DITHER);
        glDisable(GL_LIGHTING);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        
        //2D Scene
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -10, 10);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        textureRenderer = new TextureRenderer();
        SoundStore.get().setSoundVolume(0);
    }

    public void update() {
        //Load default world
        if(!world.isLoaded()&&!loader.isLoading()){
            loader.setHelper(new LoadHelper(){public void load(){
                world.loadWorld(new File("world"+File.separator+"test.tw"));
                //createTileTextures();
            }});
            loader.start("Loading World...");
        }
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

        hud.paint();
        menu.paint();
        
        
    }
    
    public void createTileTextures(){
        int realWorldWidth = (int) (Toolkit.p(world.leftLimit)+Toolkit.p(world.rightLimit));
        int realWorldHeight= (int) (Toolkit.p(world.upperLimit)+Toolkit.p(world.lowerLimit));
        int powerWidth = (int) Math.pow(2, Toolkit.nearestHighPowerOfTwo(realWorldWidth));
        int powerHeight = (int) Math.pow(2, Toolkit.nearestHighPowerOfTwo(realWorldHeight));
        
        if(backTileTexture==null)textureRenderer.beginDrawToTexture(powerWidth, powerHeight);
        else                     textureRenderer.beginDrawToTexture(backTileTexture.getTextureID(), powerWidth, powerHeight);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(world.leftLimit,powerWidth,world.lowerLimit,powerHeight, -10, 10);
            //FIXME: I DON'T FUCKING KNOW ANYMORE
            //FIXME: Ortho projection matrix set doesn't seem to affect FBO rendering. WTF?
            
            //FIX to bring the world to draw instead of refer to the texture again:
            backTileTexture = null;
            
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glPushMatrix();
                world.drawBack();
            glPopMatrix();
        backTileTexture = new Texture(GL_TEXTURE_2D,textureRenderer.endDrawToTexture());
        backTileTexture.setWidth(powerWidth);
        backTileTexture.setHeight(powerHeight);
        backTileTexture.setLoaded(true);
        
        textureRenderer.beginDrawToTexture(powerWidth, powerHeight);
            glPushMatrix();
                glTranslatef(world.leftLimit,world.lowerLimit,0);
                world.drawFront();
            glPopMatrix();
        frontTileTexture = new Texture(GL_TEXTURE_2D,textureRenderer.endDrawToTexture());
        frontTileTexture.setWidth(powerWidth);
        frontTileTexture.setHeight(powerHeight);
        frontTileTexture.setLoaded(true);
    }

    public void renderScene(){
        Color.white.bind();
        camera.camBegin();
            world.draw();
            if(editor.getActive())AbstractGraph.glCross2d(0,0,50);
        camera.camEnd();
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public void run() throws Throwable {
        setMode("USER-0");
        while(!frame.isCloseRequested()) {
            try{
                try {
                    //FIX for multithreading.
                    GLContext.useContext(this);
                } catch (LWJGLException ex) {
                    Const.LOGGER.log(Level.WARNING,"[MAIN] Failed to get context for texture ID generation.",ex);
                }
                if(Display.isVisible()) {
                    if(frame.isFrameChanged()){resize();}
                    
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                    if(loader.isLoading()){loader.run();
                    }else{render();}
                    loader.draw();
                    SoundStore.get().poll(1000/FPS);
                    glFlush();
                }else{
                    if(Display.isDirty())render();
                    try {Thread.sleep(100);}
                    catch(InterruptedException ex) {Const.LOGGER.info("Failed thread sync.");}
                }
                Display.update();
                Display.sync(FPS);
            }catch(Throwable ex){
                Const.LOGGER.log(Level.SEVERE,"Exception in main run loop!",ex);
                org.lwjgl.Sys.alert("Transcend Engine "+Const.VERSION,"Encountered an exception in the main loop!\nStack Trace:\n"+ex.getMessage()+"\n\nGoodbye!");
                throw ex;
            }
        }
        destroy();
    }
    
    //TODO: Save to config
    public void resize(){
        DISPLAY_HEIGHT = frame.getCanvas().getHeight();
        DISPLAY_WIDTH = frame.getCanvas().getWidth();
        //DISPLAY_WIDTH = (int) (DISPLAY_ASPECT*DISPLAY_HEIGHT);
        //frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        if(menu!=null&&hud!=null){
            //menu.clear();hud.clear();
            //new MenuLoader().load();
        }
        glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, DISPLAY_WIDTH, 0, DISPLAY_HEIGHT, -10, 10);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        frame.setFrameChanged(false);
    }

    public static void setMode(String m){mode=m;}
    public static void pause(){pause=true;Mouse.setGrabbed(false);}
    public static void unpause(){pause=false;if(!editor.getActive())Mouse.setGrabbed(true);}
    public static Color getClearColor(){return clearcolor;}
    public static void setClearColor(Color c){
        clearcolor=c;
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
        case Keyboard.KEY_ESCAPE:
        case Keyboard.KEY_DELETE:
            if(!menu.isVisible()){
                menu.setVisible(true);
                menu.get("p_main").setVisible(false);
                menu.get("p_load").setVisible(false);
                menu.get("p_settings").setVisible(false);
                menu.get("p_help").setVisible(false);
                pause();
            }else if(!menu.get("p_main").isVisible()){
                menu.setVisible(false);
                unpause();
            }
            break;
        }
    }
    public void keyType(int key) {}

    class Updater extends Thread{
        public void run(){
            while(!isInterrupted()){
                try{
                    update();
                    try{Thread.sleep(1000/UPS);}catch(Exception ex){Const.LOGGER.log(Level.WARNING,"[Updater] Updater thread failed!",ex);}
                }catch(Throwable ex){
                    Const.LOGGER.log(Level.SEVERE,"[Updater] Exception in main update loop! Attempting to continue... ",ex);
                }
            }
        }
    }
}
