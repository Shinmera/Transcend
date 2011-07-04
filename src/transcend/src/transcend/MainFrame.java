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
import org.newdawn.slick.openal.SoundStore;
import org.lwjgl.openal.AL;
import graph.SoundPool;
import graph.TexturePool;
import gui.LoadHelper;
import gui.Loader;
import gui.GImage;
import gui.TrueTypeFont;
import gui.GLabel;
import org.newdawn.slick.opengl.Texture;
import gui.GTextField;
import gui.Editor;
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
import static transcend.Jitter.j;
import static transcend.Jitter.jps;

public class MainFrame implements KeyboardListener{
    public static int DISPLAY_WIDTH = Const.DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT= Const.DISPLAY_HEIGHT;
    public static final Const CONST = new Const();
    public static final File basedir = new File(".");
    public static final World world = new World();
    public static final WorldLoader worldLoader = new WorldLoader();
    public static final ElementBuilder elementBuilder = new ElementBuilder();
    public static final InputEventHandler ieh = new InputEventHandler();
    public static final Camera camera = new Camera();
    public static final TexturePool texturePool = new TexturePool();
    public static final SoundPool soundPool = new SoundPool();
    public static Loader loader;
    public static Player player;
    public static int fps = 60;
    private static int ACSIZE = 2;
    private static GPanel menu,hid;
    public static boolean pause = false;
    private Texture blurTexture;

    static{
        System.setProperty("org.lwjgl.librarypath",new File(new File(basedir, "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
    }

    public static void main(String[] args){
        Const.LOGGER.info("[MF] Booting up...");
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
        ACSIZE=CONST.gInteger("ANTIALIAS");

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
        AL.destroy();
        System.exit(0);
    }

    public void initGame(){
        loader = new Loader();

        ieh.addKeyboardListener(this);
        player = new Player();
        player.setPosition(64,128);
        int id = world.addEntity(player);
        world.printWorldStats();
        camera.follow(id);
        camera.setBoundary(300);
        camera.setPosition(DISPLAY_WIDTH/2,DISPLAY_HEIGHT/2);

        hid = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        hid.setBackground(new Color(0,0,0,0));
        final Editor editor = new Editor();
        ieh.addMouseListener(editor);
        hid.setVisible(true);

        menu = new GPanel(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
        menu.setBackground(new Color(0,0,0,150));
        
        hid.setVisible(true);

        //LOAD MENU AND WORLD
        LoadHelper helper = new LoadHelper(){
            public void load(){
        final GLabel l_block = new GLabel(editor.getItemName(editor.getItem()));
        GButton b_quit = new GButton("Quit"){
            public void onRelease(){destroy();}
        };
        GButton b_editor = new GButton("Toggle Editor"){
            public void onRelease(){if(editor.getActive()){
                editor.setActive(false);this.setBackground(Color.red);
            }else{
                editor.setActive(true);this.setBackground(Color.green);
            }}
        };
        GButton b_settings = new GButton("Settings"){
            public void onRelease(){
                if(DisplayModeChooser.showDialog("Display Mode")){
                    CONST.loadRegistry();
                    DISPLAY_WIDTH=Display.getDisplayMode().getWidth();
                    DISPLAY_HEIGHT=Display.getDisplayMode().getHeight();
                    fps=CONST.gInteger("FPS");
                    ACSIZE=CONST.gInteger("ANTIALIAS");
                    hid.setBounds(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
                    menu.setBounds(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
                }
            }
        };
        GButton b_prev = new GButton("<"){
            public void onRelease(){
                editor.setItem(editor.getItem()-1);
                if(editor.getItem()<0)editor.setItem(editor.getItemCount()-1);
                l_block.setText(editor.getItemName(editor.getItem()));
            }
        };
        GButton b_next = new GButton(">"){
            public void onRelease(){
                editor.setItem(editor.getItem()+1);
                if(editor.getItem()==editor.getItemCount())editor.setItem(0);
                l_block.setText(editor.getItemName(editor.getItem()));
            }
        };
        GTextField t_test = new GTextField(editor.getTilesize()+""){
            public void onConfirm(){
                editor.setTilesize(Integer.parseInt(getText()));
            }
        };
        GLabel l_speed = new GLabel(""){
            public void paint(){
                if(!isVisible())return;
                getForeground().bind();
                getFont().drawString(10,DISPLAY_HEIGHT-20,player.getInfo(), 1,1, TrueTypeFont.ALIGN_LEFT);
                glBindTexture(GL_TEXTURE_2D, 0); //release
            }
        };
        GButton b_save = new GButton("Save"){
            public void onRelease(){
                worldLoader.saveWorld(new File("world"+File.separator+"test.tw"));
            }
        };
        final GLabel l_zoom = new GLabel(camera.getZoom()+"");
        GButton b_zoomin = new GButton("+"){
            public void onHold(){
                if(camera.getZoom()<5)camera.setZoom(camera.getZoom()+0.01);
                l_zoom.setText(Math.round(camera.getZoom()*100)/100.0+"");
            }
        };
        GButton b_zoomout = new GButton("-"){
            public void onHold(){
                if(camera.getZoom()>0.05)camera.setZoom(camera.getZoom()-0.01);
                l_zoom.setText(Math.round(camera.getZoom()*100)/100.0+"");
            }
        };

        GImage i_logo = new GImage("logo.png");
        b_editor.setBackground(Color.red);
        b_quit.setBounds(10, 10, 100, 30);
        b_editor.setBounds(10,80,100, 30);
        b_settings.setBounds(10,45,100,30);
        t_test.setBounds(10,115,100,15);
        b_prev.setBounds(10,135,15,15);
        b_next.setBounds(30,135,15,15);
        l_block.setBounds(50,135,60,15);
        b_save.setBounds(10,155,100,30);
        b_zoomin.setBounds(10,190,15,15);
        b_zoomout.setBounds(30,190,15,15);
        l_zoom.setBounds(50,190,60,15);
        i_logo.setBounds(DISPLAY_WIDTH/2-250,DISPLAY_HEIGHT-106-50,500,106);

        menu.add(b_quit);
        menu.add(b_editor);
        menu.add(b_settings);
        menu.add(t_test);
        menu.add(b_prev);
        menu.add(b_next);
        menu.add(l_block);
        menu.add(b_save);
        menu.add(b_zoomin);
        menu.add(b_zoomout);
        menu.add(l_zoom);
        menu.add(i_logo);
        hid.add(editor);
        hid.add(l_speed);

        worldLoader.loadWorld(new File("world"+File.separator+"test.tw"));
            }
        };
        loader.setHelper(helper);
        loader.start();
    }

    public void initGL() {
        //2D Initialization
       	glClearColor(0,0,0,0);
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
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        glGetInteger(GL_VIEWPORT, viewport);

        glClear(GL_ACCUM_BUFFER_BIT);
        for (int jitter = 0; jitter < ACSIZE; jitter++) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();
            glTranslatef(j[jps[ACSIZE]][jitter].x * 4.5f / viewport.get(2),
                    j[jps[ACSIZE]][jitter].y * 4.5f / viewport.get(3), 0.0f);

            Color.white.bind();
            
            camera.camBegin();
                world.draw();

                glEnable(GL_COLOR_LOGIC_OP);
                glLogicOp(GL_XOR);
                Color.white.bind();
                glLineWidth(0.5f);
                glBegin(GL_LINES);
                    glVertex2i(-50,0);
                    glVertex2i(50,0);
                glEnd();
                glBegin(GL_LINES);
                    glVertex2i(0,50);
                    glVertex2i(0,-50);
                glEnd();
                glDisable(GL_COLOR_LOGIC_OP);
            camera.camEnd();

            glPopMatrix();
            glAccum(GL_ACCUM, 1.0f / ACSIZE);
        }
        glAccum(GL_RETURN, 1.0f);

        menu.paint();
        hid.paint();

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
        case Keyboard.KEY_DELETE:
            if(!menu.isVisible()){
                blurScreen();
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

    private void blurScreen(){
        /*render();
        blurTexture.bind();
        glCopyTexImage2D(GL_TEXTURE_2D,0,GL_RGB,0,0,DISPLAY_WIDTH, DISPLAY_HEIGHT,0);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glBindTexture(GL_TEXTURE_2D,0);

        InputStream in = new ByteArrayInputStream(blurTexture.getTextureData());
        BufferedImage image = new BufferedImage(DISPLAY_WIDTH, DISPLAY_HEIGHT,BufferedImage.TYPE_INT_RGB);
        try{image = ImageIO.read(in);
        ImageIO.write(image, "png", new File("out.png"));}catch(Exception ex){ex.printStackTrace();}
        
        final BufferedImage img = image;
        JFrame previewFrame = new JFrame("Image Preview"){
            public void paint(Graphics g){
                Graphics2D g2 = (Graphics2D)g;
                g2.setBackground(java.awt.Color.yellow);
                g2.clearRect(0, 0, getWidth(), getHeight());
                g2.drawImage(img, null, 0,0);
            }
        };
        previewFrame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        previewFrame.setVisible(true);*/
    }
}
