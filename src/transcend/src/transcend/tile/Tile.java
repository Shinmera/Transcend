/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.tile;

import NexT.util.SimpleSet;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.event.Event;
import transcend.event.EventListener;
import transcend.graph.AbstractGraph;
import transcend.graph.Animation;
import transcend.main.MainFrame;
import transcend.world.BElement;

public class Tile extends BElement implements EventListener{
    public static final int STATUS_CLEAN = 0x00;
    public static final int STATUS_INFECTED = 0x01;
    public static final int STATUS_CLEANING = 0x02;
    public float depth = 1;
    protected Animation drawable = new Animation();
    private int status = STATUS_CLEAN;
    private int cleancount = 0;

    public Tile(){
        MainFrame.eh.registerEvent(Event.AREA_CLEAR, 1, this);
        MainFrame.eh.registerEvent(Event.AREA_INFECT, 1, this);
    }
    public Tile(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void setDepth(float depth){this.depth=depth;}
    public float getDepth(){return depth;}
    public void setStatus(int status){this.status=status;cleancount=255;}
    public int getStatus(){return status;}

    public void draw(){
        drawable.draw((int)x,(int)y,w,h);
        if(depth!=0&&z!=0){
            if(z<0){
                new Color(0f,0f,0f,(float)(-z/7.0*depth)).bind();
                //if(z>0)new Color(1f,1f,1f,(float)(z/10.0*depth)).bind();
                AbstractGraph.glRectangle2d(x, y, w, h);
                new Color(1f,1f,1f,(float)(z/10.0*depth)).bind();
                AbstractGraph.glRectangle2d(x, y, w, h);
            }
        }
        if(status==STATUS_CLEANING||status==STATUS_INFECTED){
            if(status==STATUS_CLEANING)cleancount--;
            if(cleancount<=0)setStatus(STATUS_CLEAN);
            new Color(0,0,0,cleancount).bind();
            glBegin(GL_QUADS);
                glVertex2d(x,y);
                glVertex2d(x+w,y);
                glVertex2d(x+w,y+h);
                glVertex2d(x,y+h);
            glEnd();
        }
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
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
                setStatus(STATUS_CLEANING);
        }
        if(event==Event.AREA_INFECT){
            if(MainFrame.world.getDistance(wID, identifier)<=Double.parseDouble(arguments.get("radius")))
                setStatus(STATUS_INFECTED);
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}
}
