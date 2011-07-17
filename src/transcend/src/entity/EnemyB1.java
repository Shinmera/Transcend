/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;
import graph.Animation;
import java.io.File;
import transcend.MainFrame;
import world.BElement;
import world.Element;

public class EnemyB1 extends Entity{
    public final static int view_distance=500;
    private Element ground = null;
    private double vydcc=0.4;
    private boolean in_reach=false;

    public EnemyB1(){
        atk=1;def=1;
        status=Entity.STATUS_IDLE;
        int[] stop = {3,13,0,0};
        int[] start = {0,0,0,0};
        int[] loop = {0,-1,0,0};
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"enemy_b1.png"),start,stop,loop);
        drawable.setReel(0);
        w=64;h=64;
    }

    public EnemyB1(double x,double y){
        this();
        this.x=x;
        this.y=y;
    }

    public void draw(){
        drawable.draw((int)x,(int)y-4,w,h);
    }

    public Element check(double ax,double ay,double bx,double by){
        Element e=null;
        for(int i=0;i<MainFrame.world.size();i++){
            BElement bel = MainFrame.world.getByID(MainFrame.world.getID(i));Element el = null;
            if(!bel.isBaseElement())el=(Element)bel;
            if((el!=null)&&(el.checkInside(ax,ay)||el.checkInside(bx,by))){
                e=el;
                break;
            }
        }
        return e;
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

        if(Math.sqrt(Math.pow(x-MainFrame.player.x,2)+Math.pow(y-MainFrame.player.y,2))<view_distance)in_reach=true;
        else in_reach=false;

        if(ground!=null&&in_reach){
            if(drawable.getReel()==0)drawable.setReel(1);
            if(MainFrame.player.x<=x){
                drawable.setDirection(Animation.DIR_LEFT);
                vx=-2;
            }else{
                drawable.setDirection(Animation.DIR_RIGHT);
                vx=2;
            }
        }else{
            vx=0;
        }

        x+=vx;
        y+=vy;
    }
}
