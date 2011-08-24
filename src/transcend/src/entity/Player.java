/**********************\
  file: Player.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import NexT.util.Ray;
import NexT.util.SimpleSet;
import NexT.util.Vector;
import block.Block;
import event.Event;
import event.EventListener;
import event.KeyboardListener;
import graph.AbstractGraph;
import graph.Animation;
import java.io.File;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import world.Element;

public class Player extends Entity implements KeyboardListener,EventListener{
    public static final int ELEMENT_ID = 0x2;
    private final double vxacc=5,vyacc=10,vydcc=0.4,vxdcc=5;
    private Element ceiling = null,left = null,right = null;
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

    public Element getLeft(){return left;}
    public Element getRight(){return right;}

    public void draw(){
        drawable.draw((int)x-w/2, (int)y-4, w, h);
        if(MainFrame.editor.getActive()){
            if(ground!=null)ground.draw();
            if(left!=null)left.draw();
            if(right!=null)right.draw();
            Color.red.bind();
            AbstractGraph.drawRay(new Ray(x,y,0,vx,vy,0),64);
        }
    }

    public void update(){
        drawable.update();

        //GROUND
        if(((ground=(Block)check(x-w/2+3,y+1,x+w/2-3,y+1))!=null || (ground=(Block)check(x-w/2+3,y+vy  ,x+w/2-3,y+vy))!=null) && vy<0){
            vy=0;
            Vector v = ground.getCollisionPoint(new Ray(x,y+h,0,0,-1,0));
            if(v!=null){
                y=v.getY();
            }
        }else{
            vy-=vydcc;
        }
        //CEILING
        if((ceiling=(Block)check(x-w/2+3,y+h  ,x+w/2-3,y+h))!=null&&vy>0){
            if(ceiling.solid>0.5){
                vy=0;
                Vector v = ceiling.getCollisionPoint(new Ray(x,y,0,0,1,0));
                if(v!=null){
                    y=v.getY()-h;
                }
            }
        }
        //SIDES
        if((left=(Block)check(x-w/2-1+vx,y+h/2+3,x-w/2-1+vx,y+h-3))!=null&&vx<0){
            if(left.solid>0.5){
                vx=0;
                Vector v = left.getCollisionPoint(new Ray(x,y,0,-1,0,0));
                if(v!=null){
                    x=v.getX()+w/2;
                }
            }
        }

        if((right=(Block)check(x+w/2+vx,y+h/2+3,x+w/2+1+vx,y+h-3))!=null&&vx>0){
            if(right.solid>0.5){
                vx=0;
                Vector v = right.getCollisionPoint(new Ray(x,y,0,1,0,0));
                if(v!=null){
                    x=v.getX()-w/2;
                }
            }
        }

        //INPUT
        if(K_SPACE&&ground!=null&&vy==0){vy+=vyacc;}
        
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

    public SimpleSet<String,String> getStateData(){
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        set.put("px", x+"");
        set.put("py", y+"");
        set.put("ph", health+"");
        return set;
    }

    public void setStateData(HashMap<String,String> set){
        x=Double.parseDouble(set.get("px"));
        y=Double.parseDouble(set.get("py"));
        health=Double.parseDouble(set.get("ph"));
    }

    public void die(){
        
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
            case Keyboard.KEY_R:if(MainFrame.editor.getActive()){x=0;y=0;vx=0;vy=0;}break;
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }
}
