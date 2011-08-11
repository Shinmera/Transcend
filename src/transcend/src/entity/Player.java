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
import event.KeyboardListener;
import graph.Animation;
import java.io.File;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import transcend.MainFrame;
import world.BElement;
import world.Element;

public class Player extends Entity implements KeyboardListener,EventListener{
    public static final int ELEMENT_ID = 0x2;
    private final double vxacc=5,vyacc=10,vydcc=0.4,vxdcc=5;
    private Element ground = null,ceiling = null,left = null,right = null;
    private boolean K_LEFT,K_RIGHT,K_SPACE,K_SHIFT,K_TAB,K_W;

    public Player(){}
    public void init(){
        MainFrame.ieh.addKeyboardListener(this);
        x=10;y=10;w=64;h=64;

        int[] stop = {16,0};
        int[] start = {0,0};
        int[] loop = {0,0};

        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"dash_walk_right.png"),start,stop,loop);
        drawable.setReel(1);
        MainFrame.eh.registerEvent(Event.ENTITY_SEE, 0, this);
        MainFrame.eh.registerEvent(Event.ENTITY_ATTACK, 0, this);
        MainFrame.eh.registerEvent(Event.ENTITY_BLOCK, 0, this);
    }

    public String getInfo(){
        return "X: "+x+" Y: "+y+" VX: "+vx+" VY: "+vy;
    }

    public void draw(){
        drawable.draw((int)x, (int)y-4, w, h);
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
        if(vy<=0)ground=check(x+3,y,x+w-3,y);else ground=null;
        if(vy>=0)ceiling=check(x+3,y+h,x+w-3,y+h);else ceiling=null;
        //LIMIT
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
        if((ceiling!=null)&&(vy>0&&ceiling.solid>0.5)){vy=0;y=ceiling.y-h-1;}
        
        if(vx<=0)left=check(x+vx,y+3,x+vx,y+h-3,1);else left=null;
        if(vx>=0)right=check(x+w+vx,y+3,x+w+vx,y+h-3,1);else right=null;
        if(left!=null&&vx<0){x=left.x+left.w;vx=0;}
        if(right!=null&&vx>0){x=right.x-w;vx=0;}

        //INPUT
        if(K_SPACE&&ground!=null&&vy==0)vy+=vyacc;
        
        if(K_LEFT){
            drawable.setDirection(Animation.DIR_LEFT);
            drawable.setReel(0);
            if(left==null)vx=-vxacc;
        }else if(K_RIGHT){
            drawable.setDirection(Animation.DIR_RIGHT);
            drawable.setReel(0);
            if(right==null)vx=vxacc;
        }else{
            drawable.setReel(1);
            vx=0;
        }
        if(K_W){
            MainFrame.eh.triggerEvent(Event.PLAYER_ATTACK, wID, null);
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
        case Keyboard.KEY_LSHIFT: K_SHIFT=true;break;
        case Keyboard.KEY_TAB: K_TAB=true;break;
        case Keyboard.KEY_W: K_W=true;break;
        }
    }
    public void keyReleased(int key) {
        if(MainFrame.pause)return;
        switch(key){
        case Keyboard.KEY_SPACE:K_SPACE=false;break;
        case Keyboard.KEY_LEFT:K_LEFT=false;break;
        case Keyboard.KEY_RIGHT:K_RIGHT=false;break;
        case Keyboard.KEY_LSHIFT: K_SHIFT=false;break;
        case Keyboard.KEY_TAB: K_TAB=false;break;
        case Keyboard.KEY_W: K_W=false;break;
        }
    }
    public void keyType(int key) {
        if(MainFrame.pause)return;
        switch(key){
            case Keyboard.KEY_R:x=0;y=0;vx=0;vy=0;break;
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        System.out.println("[PLAYER] received signal "+event+" from ["+identifier+"].");
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }
}
