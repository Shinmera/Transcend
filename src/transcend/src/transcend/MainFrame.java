/**********************\
  file: MainFrame
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend;
import NexT.repo.Repository;
import NexT.script.ScriptManager;
import graph.AbstractGraph;
import NexT.util.Toolkit;
import event.EventHandler;
import org.newdawn.slick.openal.SoundStore;
import org.lwjgl.openal.AL;
import graph.SoundPool;
import graph.TexturePool;
import gui.LoadHelper;
import gui.Loader;
import gui.Editor;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.net.URL;
import java.io.IOException;
import org.newdawn.slick.Color;
import gui.Camera;
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
import org.lwjgl.LWJGLUtil;
import java.util.logging.Level;
import static org.lwjgl.opengl.GL11.*;
import static transcend.Jitter.j;
import static transcend.Jitter.jps;

public class MainFrame implements KeyboardListener{
    static{
        System.setProperty("org.lwjgl.librarypath",new File(new File(new File("."), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
        Toolkit.printMenu("><Transcend Engine v"+Const.VERSION+"\n"+
                          "(c)2011 "+Const.MAINTAINER+" developed by "+Const.DEVELOPER+"\n"+
                          "http://www.tymoon.eu", "/", 1, null);
        Const.LOGGER.info("[MF] Booting up...");
    }

    public static final Const CONST = new Const();
    public static final File basedir = new File(".");
    public static final World world = new World();
    public static final WorldLoader worldLoader = new WorldLoader();
    public static final ElementBuilder elementBuilder = new ElementBuilder();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final EventHandler eh = new EventHandler();
    public static final Camera camera = new Camera();
    public static final Editor editor = new Editor();
    public static final TexturePool texturePool = new TexturePool();
    public static final SoundPool soundPool = new SoundPool();
    public static final FileStorage fileStorage = new FileStorage();
    public static final ScriptManager scriptManager = new ScriptManager();
    public static final Repository repo = new Repository();
    public static final Player player = new Player();
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static int fps = 60;
    public static int ACSIZE = 2;
    public static boolean pause = false;
    public static GPanel menu,hud;
    public static Loader loader;

    public static void main(String[] args){
        MainFrame mf = new MainFrame();
        try{
            if(!DisplayModeChooser.showDialog("Display Mode"))System.exit(0);
            mf.CONST.loadRegistry();
            mf.create();
            mf.run();
        }
        catch(Exception ex){Const.LOGGER.log(Level.SEVERE,"[MF] Error in main thread!",ex);}
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
        ACSIZE=CONST.gInteger("ANTIALIAS");
        try{repo.setURL(new URL(CONST.gString("REPO")));}
        catch(Exception e){Const.LOGGER.log(Level.SEVERE, "[MF] Repo URL malformed. Check the constants!",e);}
        Keyboard.create();
        Mouse.setGrabbed(false);
        Mouse.create();
        initGL();
        initGame();
    }

    public static void destroy() {
        Const.LOGGER.info("[MF] Shutting down...");
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        AL.destroy();
        System.exit(0);
    }

    public void initGame(){
        Const.LOGGER.info("[MF] initGame");
        loader = new Loader();
        ieh.addKeyboardListener(this);
        camera.setBoundary(300);
        camera.setPosition(DISPLAY_WIDTH/2,DISPLAY_HEIGHT/2);

        hud = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        hud.setBackground(new Color(0,0,0,0));
        hud.setVisible(true);

        menu = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        menu.setBackground(new Color(0,0,0,150));
        ieh.addMouseListener(editor);

        //LOAD MENU
        loader.setHelper(new MenuLoader());
        loader.start();

        editor.setActive(true);
    }

    public void initGL() {
        Const.LOGGER.info("[MF] initGL");
        //2D Initialization
       	glClearColor(0.1f,0.1f,0.2f,0);
        glClearAccum(0,0,0,0);
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

        SoundStore.get().setSoundVolume(0);
    }

    public void update() {
        //Load default world
        if(!worldLoader.isLoaded()){
            loader.setHelper(new LoadHelper(){public void load(){
                worldLoader.loadWorld(new File("world"+File.separator+"various.tw"));
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

    public void renderScene(){
        Color.white.bind();
        camera.camBegin();
            world.draw();

            if(editor.getActive())AbstractGraph.glCross2d(0,0,50);
        camera.camEnd();
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public void run() {
        while(!Display.isCloseRequested()) {
            if(Display.isVisible()) {
                if(loader.isLoading()){
                    loader.draw();
                    loader.run();
                }else{
                    render();
                    update();
                }
                SoundStore.get().poll(1000/fps);
                glFlush();
            }else {
                if(Display.isDirty())render();
                try {Thread.sleep(100);}
                catch(InterruptedException ex) {Const.LOGGER.info("Failed thread sync.");}
            }
            Display.update();
            Display.sync(fps);
        }
    }

    public static void pause(){pause=true;}
    public static void unpause(){pause=false;}

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
}
