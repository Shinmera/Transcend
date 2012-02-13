/**********************\
  file: Info.java
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.block;

import NexT.util.SimpleSet;
import transcend.event.Event;
import transcend.event.EventListener;
import transcend.graph.AbstractGraph;
import transcend.gui.GImage;
import transcend.gui.GLabel;
import java.util.HashMap;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;

public class Info extends Block implements EventListener{
    GImage mini;
    GLabel lookbox = new GLabel("Look");
    String minitex="";
    String popuptex="";
    String readsnd="";
    String text="Look";
    float fader = 0;
    boolean touch=false,fadeout=false,autoshow=false;
    int minitex_x=0;
    int minitex_y=0;

    public Info(){
        MainFrame.eh.registerEvent(Event.PLAYER_ATTACK, 9,this);
        lookbox.setWidth(70);
        lookbox.setHeight(20);
        lookbox.setBackground(Color.white);
        lookbox.setBorder(Color.black,2);
        lookbox.setAlign(GLabel.ALIGN_CENTER);
    }

    public void init(){
        z=5;
    }

    public void loadTextures(){
        drawable.loadTexture(MainFrame.fileStorage.getFile(popuptex));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight());
        if(minitex!=null){
            mini = new GImage(minitex);
            mini.setBounds(minitex_x,minitex_y,mini.getDrawable().getTexture().getImageWidth(),mini.getDrawable().getTexture().getImageHeight());
            mini.getDrawable().setSpritesize(mini.getDrawable().getTexture().getImageWidth());
            mini.getDrawable().calcTile(mini.getDrawable().getTexture().getImageWidth(),mini.getDrawable().getTexture().getImageHeight());
            mini.setVisible(true);
        }
    }

    public SimpleSet<String, String> getOptions() {
        SimpleSet<String, String> set = super.getOptions();
        set.put("minitex",minitex);
        set.put("popuptex",popuptex);
        set.put("readsnd",readsnd);
        set.put("minitex_x",minitex_x+"");
        set.put("minitex_y",minitex_y+"");
        set.put("autoshow",autoshow+"");
        set.put("text",text);
        return set;
    }

    public void setOptions(HashMap<String, String> options) {
        super.setOptions(options);
        minitex=options.get("minitex");
        popuptex=options.get("popuptex");
        readsnd=options.get("readsnd");
        minitex_x=(int)x+w/2;
        minitex_y=(int)y+h;
        if(options.containsKey("minitex_x"))minitex_x=Integer.parseInt(options.get("minitex_x"));
        if(options.containsKey("minitex_y"))minitex_y=Integer.parseInt(options.get("minitex_y"));
        if(options.containsKey("autoshow"))autoshow=Boolean.parseBoolean(options.get("autoshow"));
        if(options.containsKey("text")){
            text=options.get("text");
            if(text!=null)lookbox.setText(text);
        }
        h=5;
        loadTextures();
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if((event==Event.PLAYER_ATTACK)&&touch){
            if(fader<=0){fader=0.001f;fadeout=false;}
            if(fader>=1){fadeout=true;}
        }
    }

    public void update(){
        if((MainFrame.player.bottom!=null)&&(MainFrame.player.bottom.wID==wID)){
            touch=true;
            if(autoshow&&fader<=0){fader=0.001f;fadeout=false;}
        }else{
            touch=false;
        }
    }

    public void draw(){
        if(minitex!=null)mini.paint();
        lookbox.setVisible(touch);
        if(touch){
            if(text!=null){
                lookbox.setX((int)(MainFrame.player.getX()+MainFrame.player.getWidth()/2));
                lookbox.setY((int)(MainFrame.player.getY()+MainFrame.player.getHeight()));
                lookbox.paint();
            }
            if(fader>0){
                if(fadeout)fader -=0.005;
                else if(fader<1) fader += 0.005;
            }
        }else{
            if(fader>0)fader-=0.005;
            fadeout=true;
        }
        MainFrame.camera.camEnd();
        drawable.draw(MainFrame.DISPLAY_WIDTH/2-drawable.getTexture().getImageWidth()/2,
                MainFrame.DISPLAY_HEIGHT/2-drawable.getTexture().getImageHeight()/2,
                drawable.getTexture().getImageWidth(),drawable.getTexture().getImageHeight(),
                new Color(1.0f*fader,1.0f*fader,1.0f*fader,fader));
        MainFrame.camera.camBegin();
        if(!MainFrame.editor.getActive())return;
        new Color(0.2f,0.2f,1f,0.5f).bind();
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
