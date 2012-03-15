/**********************\
  file: MainFrame
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.main;
import NexT.repo.Repository;
import NexT.script.ScriptManager;
import NexT.util.Toolkit;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
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
import transcend.world.ElementBuilder;
import transcend.world.World;
import transcend.world.WorldLoader;

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
    public static final Const CONST = new Const();
    public static final File basedir = new File(System.getProperty("user.dir"));
    public static final JFrame frame = new JFrame("Transcend - v"+Const.VERSION);
    public static final World world = new World();
    public static final WorldLoader worldLoader = new WorldLoader();
    public static final ElementBuilder elementBuilder = new ElementBuilder();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final EventHandler eh = new EventHandler();
    public static final Camera camera = new Camera();
    public static final Editor editor = new Editor();
    public static final TexturePool texturePool = new TexturePool();
    public static final TextureLoader textureLoader = new TextureLoader();
    public static final SoundPool soundPool = new SoundPool();
    public static final FontPool fontPool = new FontPool();
    public static final FileStorage fileStorage = new FileStorage();
    public static final ScriptManager scriptManager = new ScriptManager();
    public static final Repository repo = new Repository();
    public static final Player player = new Player();
    public static final int[][] DEFAULT_DIM = {{1280,960},{1280,720},{1280,800}};//4:3 16:9 16:10
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static double DISPLAY_ASPECT= DISPLAY_WIDTH/DISPLAY_HEIGHT;
    public static int fps = 60, ups = 60;
    public static int ACSIZE = 2;
    public static Texture backTileTexture = null,frontTileTexture = null;
    public static boolean pause = false;
    public static GPanel menu,hud;
    public static Loader loader;
    private static Color clearcolor = new Color(0.2f,0.2f,0.2f);
    private final Updater updater = new Updater();
    private final Canvas canvas = new Canvas();
    private TextureRenderer textureRenderer;
    private boolean closeRequested = false;
    private boolean frameChanged = false;

    public static void main(String[] args) throws Throwable{
        MainFrame mf = new MainFrame();
        try{
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            MainFrame.CONST.loadRegistry();
            mf.create();
            mf.run();
        }
        catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"[MF] Error in main thread!",ex);}
        finally{MainFrame.destroy();}
    }

    public void create() throws LWJGLException, IOException {
        Const.LOGGER.info("[MF] Initializing...");
        
        //FRAME
        canvas.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e){ frameChanged = true;}
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });
        frame.addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e){canvas.requestFocusInWindow(); }
            public void windowLostFocus(WindowEvent e) {}
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){ closeRequested = true; }
        });
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        
        List<Image> icons = new ArrayList<Image>();
        icons.add(ImageIO.read(fileStorage.getFile("icon_16")));
        icons.add(ImageIO.read(fileStorage.getFile("icon_32")));
        icons.add(ImageIO.read(fileStorage.getFile("icon_64")));
        icons.add(ImageIO.read(fileStorage.getFile("icon_128")));
        frame.setIconImages(icons);
        
        DISPLAY_WIDTH =Display.getDisplayMode().getWidth();
        DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
        DISPLAY_ASPECT=DISPLAY_WIDTH/(DISPLAY_HEIGHT+0.0);
        fps=CONST.gInteger("FPS");
        ups=CONST.gInteger("UPS");
        ACSIZE=CONST.gInteger("ANTIALIAS");
        try{repo.setURL(new URL(CONST.gString("REPO")));}
        catch(Exception e){Const.LOGGER.log(Level.SEVERE, "[MF] Repo URL malformed. Check the constants!",e);}
        
        Display.setParent(canvas);
        frame.setPreferredSize(new Dimension(DISPLAY_WIDTH,DISPLAY_HEIGHT));
        frame.setMinimumSize(new Dimension(300,200));
        frame.pack();
        frame.setVisible(true);
        Display.create();
        
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
            frame.dispose();
            AL.destroy();
        }catch(Throwable t){//Apparently shit can go bonkers in this.
            Const.LOGGER.log(Level.SEVERE,"Couldn't clean up properly!",t);
        }finally{
            System.exit(0);
        }
    }

    public void initGame(){
        Const.LOGGER.info("[MF] initGame");
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

        //LOAD MENU
        loader.setHelper(new MenuLoader());
        loader.start();
    }

    public void initGL() {
        Const.LOGGER.info("[MF] initGL");
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
        if(!worldLoader.isLoaded()&&!loader.isLoading()){
            loader.setHelper(new LoadHelper(){public void load(){
                worldLoader.loadWorld(new File("world"+File.separator+"test.tw"));
                createTileTextures();
            }});
            loader.start();
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
            glOrtho(0,64,0,64, -10, 10);
            
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glPushMatrix();
                glScalef(0.5f,0.5f,1.0f);
                world.drawBack();
            glPopMatrix();
        backTileTexture = new Texture(GL_TEXTURE_2D,textureRenderer.endDrawToTexture());
        backTileTexture.setWidth(powerWidth);
        backTileTexture.setHeight(powerHeight);
        
        textureRenderer.beginDrawToTexture(powerWidth, powerHeight);
            glPushMatrix();
                glTranslatef(world.leftLimit,world.lowerLimit,0);
                world.drawFront();
            glPopMatrix();
        frontTileTexture = new Texture(GL_TEXTURE_2D,textureRenderer.endDrawToTexture());
        frontTileTexture.setWidth(powerWidth);
        frontTileTexture.setHeight(powerHeight);
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
        while(!closeRequested) {
            try{
                if(Display.isVisible()) {
                    if(frameChanged){resize();}
                    
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                    if(loader.isLoading()){loader.run();
                    }else{render();}
                    loader.draw();
                    SoundStore.get().poll(1000/fps);
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
    
    public void resize(){
        DISPLAY_HEIGHT = frame.getHeight();
        DISPLAY_WIDTH = (int) (DISPLAY_ASPECT*DISPLAY_HEIGHT);
        frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
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
        frameChanged=false;
    }

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
                    try{Thread.sleep(1000/ups);}catch(Exception ex){Const.LOGGER.log(Level.WARNING,"Updater thread failed!",ex);}
                }catch(Throwable ex){
                    Const.LOGGER.log(Level.SEVERE,"Exception in main update loop! Attempting to continue... ",ex);
                }
            }
        }
    }
}
