/**********************\
  file: EnemyC1.java
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.entity;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.block.Block;
import transcend.event.Event;
import transcend.event.EventListener;
import transcend.graph.Animation;
import transcend.main.MainFrame;
import transcend.world.Element;

public class EnemyC1 extends Entity implements EventListener{

    private AI ai;
    private double vydcc=0.4;
    private Element ground;

    public EnemyC1(){
        int[] start = {0,0,0,0,0,0,0,0};
        int[] stop = {21,14,0,8,14,0,0,0};
        int[] loop = {0,-2,0,-999,-999,0,0,0};
        int[] loop2 = {0,0,0,0,0,0,0,0};
        drawable.loadTexture(MainFrame.fileStorage.getFile("enemy_c1"),start,stop,loop,loop2);
        drawable.setReel(0);
        drawable.setPPS(15);
        atk=1;def=1;health=100;
        status=Entity.STATUS_IDLE;
        solid=0;
        w=64;h=64;
    }

    public EnemyC1(double x,double y){
        this();
        this.x=x;
        this.y=y;
    }

    public void init(){
        ai = new AI(wID);
        MainFrame.eh.registerEvent(Event.PLAYER_ATTACK,9, this);
    }

    public void draw(){
        drawable.draw((int)x,(int)y-3);

        if(MainFrame.editor.getActive()){
            glBegin(GL_LINES);
                //SECONDARY DETECTORS
                Color.blue.bind();
                glVertex2d(x-w*1.5,y+h*1.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                glVertex2d(x+w*2.5,y+h*1.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                
                glVertex2d(x-w*1.5,y+h*2.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                glVertex2d(x+w*2.5,y+h*2.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                //PRIMARY DETECTORS
                Color.red.bind();
                glVertex2d(x-w*0.5,y+h*1.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                glVertex2d(x+w*1.5,y+h*1.5);
                glVertex2d(x+w*0.5,y+h*0.5);

                glVertex2d(x-w*0.5,y+h*0.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                glVertex2d(x+w*1.5,y+h*0.5);
                glVertex2d(x+w*0.5,y+h*0.5);

                glVertex2d(x-w*0.5,y-h*0.5);
                glVertex2d(x+w*0.5,y+h*0.5);
                glVertex2d(x+w*1.5,y-h*0.5);
                glVertex2d(x+w*0.5,y+h*0.5);
            glEnd();
        }
    }

    public void update(){

        drawable.update();
        if(health<=0){
            if(status!=Entity.STATUS_DIE){
                MainFrame.eh.triggerEvent(Event.ENTITY_DIE, wID, null);
                status=Entity.STATUS_DIE;
                drawable.setReel(4);
            }
            if(drawable.getReel()==0){
                MainFrame.world.delByID(wID);
            }
        }else{

        ai.update();
        ai.setGoal((int)MainFrame.player.getX(), (int)MainFrame.player.getY());
        

        Element ceiling,left,right;
        if(vy<=0)ground=check(x+3,y,x+w-3,y);else ground=null;
        if(vy>=0)ceiling=check(x+3,y+h,x+w-3,y+h);else ceiling=null;
        if(ground==null){
            vy-=vydcc;
            if(vy<0){
                Block temp = checkBlock(wID,x+2,y+vy,x+w-2,y+vy);
                if((temp!=null)&&(temp.y+temp.h-y-vy<temp.h)){
                    vy=0;
                    y=temp.y+temp.h;
                }
            }
        } else if(ground.y+ground.h-y<ground.h && vy<0) {y=ground.y+ground.h;vy = 0;
        } else if(vy<0)vy=0;
        if((ceiling!=null)&&(vy>0&&ceiling.getSolid()>0.5)){vy=0;y=ceiling.y-h-1;}

        if(vx<=0)left= check(x+vx  ,y+3,x+vx  ,y+h-3);else left=null;
        if(vx>=0)right=check(x+w+vx,y+3,x+w+vx,y+h-3);else right=null;
        if(left!=null&&vx<0){x=left.x+left.w;vx=0;}
        if(right!=null&&vx>0){x=right.x-w;vx=0;}

        if(vx>0)drawable.setDirection(Animation.DIR_RIGHT);
        else drawable.setDirection(Animation.DIR_LEFT);

        x+=vx;
        y+=vy;
        if(status==Entity.STATUS_IDLE){vx=0;return;}
        if(status==Entity.STATUS_JUMP&&ground!=null){vy=15;status=Entity.STATUS_MOVE;drawable.setReel(1);return;}
        
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.PLAYER_ATTACK){
            if(ai.getGoalDist()<=64){
                health-=0.01;
                MainFrame.eh.triggerEvent(Event.ENTITY_ATTACK, wID, null);
            }
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}
}
