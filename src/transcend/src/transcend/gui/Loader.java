/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;
import transcend.graph.AbstractGraph;
import org.newdawn.slick.Color;
import java.awt.Font;
import transcend.main.Const;
import java.util.logging.Level;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import transcend.main.MainFrame;
import transcend.graph.Animation;
import static org.lwjgl.opengl.GL11.*;

public class Loader{
    public static final int DELAY_TIMER = 2;
    private boolean loading=false;
    private String current="";
    private Animation drawable = new Animation();
    private LoadHelper helper = null;
    private TrueTypeFont font;
    private int delay = 0;
    private float fader = 1;
    private boolean display = true;

    public Loader(){
        font = MainFrame.fontPool.loadFont(new Font("Arial", Font.BOLD, 20));
        drawable.loadTexture(MainFrame.fileStorage.getFile("load.png"));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight());
    }
    public Loader(LoadHelper helper){
        this();
        this.helper=helper;
    }

    public void setHelper(LoadHelper helper){this.helper=helper;}
    public boolean isLoading(){return loading;}
    public boolean isDisplayed(){return display;}
    public void setDisplayed(boolean b){display=b;}

    public void run(){
        if(!loading)return;
        if(delay<DELAY_TIMER){delay++;return;}
        if (helper!=null) {
            Const.LOGGER.info("[Loader] Loading...");
            helper.load();
            helper=null;
        }
        if (LoadingList.get().getRemainingResources() > 0) {
            DeferredResource nextResource = LoadingList.get().getNext();
            String currentt = current;
            current = (LoadingList.get().getTotalResources()-LoadingList.get().getRemainingResources())*100/LoadingList.get().getTotalResources()+"%";
            if(!current.equals(currentt))Const.LOGGER.info("[Loader] "+current);
            try {nextResource.load();}
            catch (Exception ex) {Const.LOGGER.log(Level.SEVERE,"[Loader] Failed to load resource!", ex);}
        } else {
            Const.LOGGER.info("[Loader] Finished loading. Ready.");
            loading=false;
            display=false;
        }
    }
    public void start(){
        LoadingList.get().setDeferredLoading(true);
        loading = true;
        fader = 1;
        delay = 0;
    }

    public void draw(){
        if(fader<=0||!display)return;
        new Color(0.0f*fader,0.0f*fader,0.0f*fader,fader).bind();
        AbstractGraph.glRectangle2d(0,0,MainFrame.DISPLAY_WIDTH,MainFrame.DISPLAY_HEIGHT);
        drawable.draw(MainFrame.DISPLAY_WIDTH/2-drawable.getTexture().getImageWidth()/2,
                MainFrame.DISPLAY_HEIGHT/2-drawable.getTexture().getImageHeight()/2,
                drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight(),
                new Color(1.0f*fader,1.0f*fader,1.0f*fader,fader));
        font.drawString(MainFrame.DISPLAY_WIDTH/2,
                MainFrame.DISPLAY_HEIGHT/2-font.getLineHeight()/2-drawable.getTexture().getImageHeight()/2,
                current, 1,1,TrueTypeFont.ALIGN_CENTER);

        if(!loading)fader-=0.005;
    }
}
