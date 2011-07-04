/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import org.newdawn.slick.Color;
import java.awt.Font;
import transcend.Const;
import java.io.IOException;
import java.util.logging.Level;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import java.io.File;
import transcend.MainFrame;
import graph.Animation;
import static org.lwjgl.opengl.GL11.*;

public class Loader{
    private boolean loading=false;
    private String current="";
    private Animation drawable = new Animation();
    private LoadHelper helper = null;
    private TrueTypeFont font = new TrueTypeFont(new Font("Arial", Font.BOLD, 20),false);

    public Loader(){
        drawable.loadTexture(new File(MainFrame.basedir,"data"+File.separator+"load.png"));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight());
    }
    public Loader(LoadHelper helper){
        drawable.loadTexture(new File(MainFrame.basedir,"data"+File.separator+"load.png"));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight());
        this.helper=helper;
    }

    public void setHelper(LoadHelper helper){this.helper=helper;}
    public boolean isLoading(){return loading;}

    public void run(){
        if (helper!=null) {
            helper.load();
            helper=null;
        }
        if (LoadingList.get().getRemainingResources() > 0) {
            DeferredResource nextResource = LoadingList.get().getNext();
            current = (LoadingList.get().getTotalResources()-LoadingList.get().getRemainingResources())*100/LoadingList.get().getTotalResources()+"%";
            try {nextResource.load();}
            catch (IOException ex) {Const.LOGGER.log(Level.SEVERE, null, ex);}
        } else {
           loading=false;
        }
    }
    public void start(){
        LoadingList.setDeferredLoading(true); 
        loading = true;
    }

    public void draw(){
        if(!loading)return;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Color.white.bind();
        drawable.draw(MainFrame.DISPLAY_WIDTH/2-drawable.getTexture().getImageWidth()/2,
                MainFrame.DISPLAY_HEIGHT/2-drawable.getTexture().getImageHeight()/2,
                drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight());
        font.drawString(MainFrame.DISPLAY_WIDTH/2,
                MainFrame.DISPLAY_HEIGHT/2-font.getLineHeight()/2-drawable.getTexture().getImageHeight()/2,
                current, 1,1,TrueTypeFont.ALIGN_CENTER);
        
    }
}
