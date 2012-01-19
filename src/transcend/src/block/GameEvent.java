/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import NexT.util.SimpleSet;
import event.Event;
import event.EventListener;
import gui.LoadHelper;
import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GameEvent extends Block implements EventListener{
    public static final int EVENT_NONE = 0x00;
    public static final int EVENT_SWITCH_WORLD = 0x01;
    public static final int EVENT_SWITCH_CAMERA = 0x02;
    public static final int EVENT_ADVANCE_WORLD = 0x03;
    public static final int EVENT_SAVE_WORLD = 0x04;
    public static final int EVENT_QUICKSAVE = 0x05;

    private int type = EVENT_NONE;
    private String to = "";
    private String tex = "";

    public GameEvent(){
        MainFrame.eh.registerEvent(Event.PLAYER_TOUCH, 9,this);
        MainFrame.eh.registerEvent(Event.PLAYER_ATTACK, 9,this);
    }
    public GameEvent(int type){
        this();
        setType(type);
        loadTexture();
    }
    public GameEvent(int x,int y,int type){
        this(type);
        this.x=x;
        this.y=y;
    }

    public void loadTexture(){
        if(type==EVENT_SWITCH_WORLD){
            if(tex==""){
                drawable.loadTexture(MainFrame.fileStorage.getFile("portal"));
                drawable.setSpritesize(128);
                drawable.calcTile(w,h);
                drawable.setSize(w,h);
            }else{
                drawable.loadTexture(MainFrame.fileStorage.getFile(tex));
                int temp=drawable.getTexture().getImageHeight();
                drawable.setSpritesize(temp);
                drawable.calcTile(temp,temp);
                drawable.setSize(temp,temp);
            }
            h=5;
        }
        if(type==EVENT_SAVE_WORLD){
            drawable.loadTexture(MainFrame.fileStorage.getFile("savepoint"));
            drawable.setSpritesize(128);
            drawable.calcTile(128, 128);
            drawable.setSize(128,128);
            h=5;w=128;
            solid=1;
        }
    }
    
    public boolean checkInside(double ax,double ay){
        return checkInside(ax,ay,0.0001,0.0001);
    }

    public void setAdvancer(String adv){to=adv;}
    public void setType(int type){
        this.type=type;
        if(type==EVENT_QUICKSAVE)solid=0;
    }

    public String getAdvancer(){return to;}
    public int getType(){return type;}


    public void update(){
        if(type!=GameEvent.EVENT_NONE)drawable.update();
    }

    public void draw(){
        if(type!=GameEvent.EVENT_NONE)drawable.draw((int)x,(int)y);
        if(!MainFrame.editor.getActive())return;
        new Color(0.2f,0.2f,1f,0.5f).bind();
        glBegin(GL_QUADS);
            glVertex2d(x,y);
            glVertex2d(x,y+h);
            glVertex2d(x+w,y+h);
            glVertex2d(x+w,y);
        glEnd();
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.PLAYER_TOUCH){
            if(type==EVENT_SWITCH_WORLD&&!to.equals("")){
                MainFrame.loader.setHelper(new LoadHelper(){
                    public void load(){MainFrame.worldLoader.loadWorld(MainFrame.fileStorage.getFile("world/"+to));}
                });
            }
            if(type==EVENT_QUICKSAVE){
                MainFrame.player.setSetbackPoint(MainFrame.player.getX(),MainFrame.player.getY());
            }
        }
        if((event==Event.PLAYER_ATTACK)&&(MainFrame.player.ground!=null)&&(MainFrame.player.ground.wID==wID)){
            if(type==EVENT_SAVE_WORLD&&!to.equals("")){
                MainFrame.worldLoader.saveGame(MainFrame.fileStorage.getFile("save/"+to));
                MainFrame.player.setSetbackPoint(MainFrame.player.getX(),MainFrame.player.getY());
            }
            if(type==EVENT_SWITCH_WORLD&&!to.equals("")){
                MainFrame.loader.setHelper(new LoadHelper(){
                    public void load(){MainFrame.worldLoader.loadWorld(MainFrame.fileStorage.getFile("world/"+to));}
                });
                MainFrame.loader.start();
            }
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }

    public SimpleSet<String, String> getOptions() {
        SimpleSet<String, String> set = super.getOptions();
        set.put("type", type+"");
        set.put("advance",to+"");
        set.put("tex",tex);
        return set;
    }

    public void setOptions(HashMap<String, String> options) {
        super.setOptions(options);
        if(options.containsKey("tex"))tex=options.get("tex");
        if(options.containsKey("type"))setType(Integer.parseInt(options.get("type")));
        if(options.containsKey("advance"))setAdvancer(options.get("advance"));
        loadTexture();
    }

}