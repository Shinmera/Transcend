/**********************\
  file: Block
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.block;

import NexT.util.SimpleSet;
import transcend.event.EventListener;
import transcend.event.Event;
import java.util.HashMap;
import transcend.world.Element;
import transcend.main.MainFrame;

public class Block extends Element implements EventListener{
    public static final int ELEMENT_ID = 0x51;
    public static final int STATUS_CLEAN = 0x00;
    public static final int STATUS_INFECTED = 0x01;

    public boolean moveable;
    private int status=STATUS_CLEAN;

    public Block(){
        MainFrame.eh.registerEvent(Event.AREA_CLEAR, 1, this);
        MainFrame.eh.registerEvent(Event.AREA_INFECT, 1, this);
    }
    public Block(int x,int y,int w,int h){
        this();this.x=x;this.y=y;this.z=0;this.w=w;this.h=h;
    }
    
    public Block(int x,int y,int z,int w,int h){
        this.x=x;this.y=y;this.z=z;this.w=w;this.h=h;
    }

    public boolean getMoveable(){return moveable;}
    public int getStatus(){return status;}

    public void setMoveable(boolean m){this.moveable = m; }
    public void setStatus(int status){this.status=status; }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        //if(options.containsKey("tex"))MainFrame.elementBuilder.buildElement("tileblock", options);
        if(options.containsKey("status"))setStatus(Integer.parseInt(options.get("status")));
    }
    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> o = new SimpleSet<String,String>();
        o.put("status", status+"");
        return o;
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.AREA_CLEAR){
            if(MainFrame.world.getDistance(wID, identifier)<=Double.parseDouble(arguments.get("radius")))
                setStatus(STATUS_CLEAN);
        }
        if(event==Event.AREA_INFECT){
            if(MainFrame.world.getDistance(wID, identifier)<=Double.parseDouble(arguments.get("radius")))
                setStatus(STATUS_INFECTED);
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}
}
