/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import event.Event;
import event.EventListener;
import gui.LoadHelper;
import java.io.File;
import java.util.HashMap;
import transcend.MainFrame;

public class GameEvent extends Block implements EventListener{
    public static int EVENT_NONE = 0x00;
    public static int EVENT_SWITCH_WORLD = 0x01;
    public static int EVENT_SWITCH_CAMERA = 0x02;
    public static int EVENT_ADVANCE_WORLD = 0x03;

    private int type = EVENT_NONE;
    private String world_to = "";

    public GameEvent(){
        MainFrame.eh.registerEvent(Event.PLAYER_TOUCH, 9,this);
    }
    public GameEvent(int type){
        this();
        this.type=type;
        loadTexture();
    }
    public GameEvent(int x,int y,int type){
        this(type);
        this.x=x;
        this.y=y;
    }

    public void loadTexture(){
        if(type==EVENT_SWITCH_WORLD)drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"portal.png"));
        
    }

    public void update(){
        
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.PLAYER_TOUCH&&arguments.get("wID").equals(wID)){
            if(type==EVENT_SWITCH_WORLD&&!world_to.equals("")){
                MainFrame.loader.setHelper(new LoadHelper(){
                    public void load(){MainFrame.worldLoader.loadWorld(new File(MainFrame.basedir,"world"+File.separator+world_to));}
                });
            }
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }

}
