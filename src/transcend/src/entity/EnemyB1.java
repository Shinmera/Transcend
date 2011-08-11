/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;
import event.Event;
import event.EventListener;
import graph.Animation;
import java.io.File;
import java.util.HashMap;
import transcend.MainFrame;
import world.Element;

public class EnemyB1 extends Entity implements EventListener{
    public final static int view_distance=500;
    private Element ground = null;
    private double vydcc=0.4;
    private boolean in_reach=false;

    public EnemyB1(){
        atk=1;def=1;
        status=Entity.STATUS_IDLE;
        int[] stop = {3,15,12,0};
        int[] start = {0,0,0,0};
        int[] loop = {0,-2,0,0};
        int[] loop2 = {0,-999,0,0};
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"enemy_b1.png"),start,stop,loop,loop2);
        drawable.setReel(0);
        drawable.setPPS(15);
        w=64;h=64;
    }

    public EnemyB1(double x,double y){
        this();
        this.x=x;
        this.y=y;
    }

    public void init(){
        MainFrame.eh.registerEvent(Event.PLAYER_TOUCH, 0, this);
    }

    public void draw(){
        drawable.draw((int)x,(int)y-4,w,h);
    }

    public void update(){
        drawable.update();
        if(vy<=0)ground=check(x+3,y,x+w-3,y);else ground=null;
        if(ground==null){
            vy-=vydcc;
            if(vy<0){
                Element temp = check(x+2,y+vy,x+w-2,y+vy);
                if((temp!=null)&&(temp.y+temp.h-y-vy<temp.h)){
                    vy=0;
                    y=temp.y+temp.h;
                }
            }
        } else if(ground.y+ground.h-y<ground.h && vy<0) {y=ground.y+ground.h;vy = 0;
        } else if(vy<0)vy=0;

        if(Math.sqrt(Math.pow(x-MainFrame.player.x,2)+Math.pow(y-MainFrame.player.y,2))<view_distance){
            if(in_reach==false)MainFrame.eh.triggerEvent(Event.ENTITY_SEE, wID, null);
            in_reach=true;
        }
        else in_reach=false;

        if(ground!=null&&in_reach){
            drawable.setPlay(Animation.PLAY_FORWARD);
            if(drawable.getReel()==0)drawable.setReel(1);
            if(MainFrame.player.x<=x){
                drawable.setDirection(Animation.DIR_LEFT);
                //vx=-2;
            }else{
                drawable.setDirection(Animation.DIR_RIGHT);
                //vx=2;
            }
        }else{
            if(drawable.getReel()==1)drawable.setPlay(Animation.PLAY_BACKWARD);
            if(drawable.getReel()==2){
                drawable.setPlay(Animation.PLAY_BACKWARD);
                drawable.setReel(1);
                
            }
            vx=0;
        }

        x+=vx;
        y+=vy;
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.PLAYER_TOUCH&&MainFrame.player.status==Entity.STATUS_ATTACK){
            if(arguments.get("wID").equals(wID)){
                status=STATUS_ATTACK;
            }
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }
}
