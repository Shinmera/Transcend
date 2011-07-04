/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import event.KeyboardListener;
import graph.Animation;
import java.io.File;
import org.lwjgl.input.Keyboard;
import transcend.MainFrame;
import world.BElement;
import world.Element;

public class Player extends Entity implements KeyboardListener{
    public static final int ELEMENT_ID = 0x2;
    private final double vxacc=5,vyacc=8,vydcc=0.4,vxdcc=5;
    private double vx=0,vy=0;
    private Element ground = null,ceiling = null,left = null,right = null;
    private boolean K_LEFT,K_RIGHT,K_SPACE;

    public Player(){
        MainFrame.ieh.addKeyboardListener(this);
        x=10;y=10;w=64;h=64;

        int[] stop = {16,0};
        int[] start = {0,0};
        int[] loop = {0,0};

        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"dash_walk_right.png"),start,stop,loop);
        drawable.setReel(1);
    }

    public String getInfo(){
        return "X: "+x+" Y: "+y+" VX: "+vx+" VY: "+vy;
    }

    public void draw(){
        drawable.draw((int)x, (int)y-4, w, h);
        
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

    public Element check(double ax,double ay,double bx,double by,double minSolid){
        Element e=null;
        for(int i=0;i<MainFrame.world.size();i++){
            BElement bel = MainFrame.world.getByID(MainFrame.world.getID(i));Element el = null;
            if(!bel.isBaseElement())el=(Element)bel;
            if((el!=null)&&(el.checkInside(ax,ay,minSolid)||el.checkInside(bx,by,minSolid))){
                e=el;
                break;
            }
        }
        return e;
    }

    public void update(){
        drawable.update();

        //HECK
        if(vy<=0)ground=check(x+2,y,x+w-2,y);else ground=null;
        if(vy>=0)ceiling=check(x+2,y+h,x+w-2,y+h);else ceiling=null;
        if(vx<=0)left=check(x,y+2,x,y+h-2,1);else left=null;
        if(vx>=0)right=check(x+w,y+2,x+w,y+h-2,1);else right=null;

        //LIMIT
        if(ground==null){
            vy-=vydcc;
            if(vy<0){
                Element temp = check(x+2,y+vy,x+w-2,y+vy);
                if((temp!=null)&&(temp.y+temp.h-y-vy<temp.h/2)){
                    vy=0;
                    y=temp.y+temp.h;
                }
            }
        } else if(ground.y+ground.h-y<ground.h/2 && vy<0) {y=ground.y+ground.h;vy = 0;
        } else if(vy<0)vy=0;
        if(left!=null&&vx<0){x-=vx;vx=0;}
        if(right!=null&&vx>0){x-=vx;vx=0;}
        if((ceiling!=null)&&(vy>0&&ceiling.solid>0.5))vy=0;

        //INPUT
        if(K_SPACE&&ground!=null&&vy==0)vy+=vyacc;
        
        if(K_LEFT){
            drawable.setDirection(Animation.DIR_LEFT);
            drawable.setReel(0);
            vx=-vxacc;
        }else if(K_RIGHT){
            drawable.setDirection(Animation.DIR_RIGHT);
            drawable.setReel(0);
            vx=vxacc;
        }else{
            drawable.setReel(1);
            vx=0;
        }

        //EVALUATE
        x+=vx;
        y+=vy;
    }

    public void keyPressed(int key) {
        if(MainFrame.pause)return;
        switch(key){
        case Keyboard.KEY_LEFT: K_LEFT=true;break;
        case Keyboard.KEY_RIGHT: K_RIGHT=true;break;
        case Keyboard.KEY_SPACE: K_SPACE=true;break;
        }
    }
    public void keyReleased(int key) {
        if(MainFrame.pause)return;
        switch(key){
            case Keyboard.KEY_SPACE:K_SPACE=false;break;
            case Keyboard.KEY_LEFT:K_LEFT=false;break;
            case Keyboard.KEY_RIGHT:K_RIGHT=false;break;
        }
    }
    public void keyType(int key) {
        if(MainFrame.pause)return;
        switch(key){
            case Keyboard.KEY_LEFT:break;
            case Keyboard.KEY_RIGHT:break;
        }
    }
}
