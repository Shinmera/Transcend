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
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import static transcend.MainFrame.*;
import world.Element;

public class Player extends Entity implements KeyboardListener,EventListener{
    public static final int FORM_HUMAN = 0x00;
    public static final int FORM_PONY = 0x01;
    public static final int FORM_MOUSE = 0x02;
    public static final int FORM_EAGLE = 0x03;
    public static final int FORM_DOLPHIN = 0x04;

    public static final int ELEMENT_ID = 0x2;
    private Element ceiling = null,left = null,right = null;
    private boolean K_LEFT,K_RIGHT,K_SPACE,K_SHIFT,K_TAB,K_W;
    private double power=100;
    private int lifes=5;
    private int form=FORM_HUMAN;

    public Player(){}
    public void init(){
        ieh.addKeyboardListener(this);
        scriptManager.loadScript(fileStorage.getFile("scr/player"));
        x=10;y=10;w=64;h=64;

        int[] stop = {16,0};
        int[] start = {0,0};
        int[] loop = {0,0};

        drawable.loadTexture(fileStorage.getFile("dash_walk_right.png"),start,stop,loop);
        drawable.setReel(1);
        eh.registerEvent(Event.ENTITY_SEE, 0, this);
        eh.registerEvent(Event.ENTITY_ATTACK, 0, this);
        eh.registerEvent(Event.ENTITY_BLOCK, 0, this);
    }

    public Element getLeft(){return left;}
    public Element getRight(){return right;}
    public double getPower(){return power;}
    public int getLifes(){return lifes;}
    public int getForm(){return form;}

    public void draw(){
        drawable.draw((int)x-w/2, (int)y-4, w, h);
        if(editor.getActive()){
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
            Vector v = ground.getCollisionPoint(new Ray(x+w/2-3,y+h,0,0,-1,0));
            if(v!=null)y=v.getY();else
            v = ground.getCollisionPoint(new Ray(x-w/2+3,y+h,0,0,-1,0));
            if(v!=null)y=v.getY();
        }else{
            vy-=Double.parseDouble(scriptManager.getScript("player").getVariable("vydcc"));
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
        if(K_SPACE&&ground!=null&&vy==0){vy+=Double.parseDouble(scriptManager.getScript("player").getVariable("vyacc"));}
        
        if(K_LEFT){
            drawable.setDirection(Animation.DIR_LEFT);
            drawable.setReel(0);
            if(left==null)vx=-Double.parseDouble(scriptManager.getScript("player").getVariable("vxacc"));
        }else if(K_RIGHT){
            drawable.setDirection(Animation.DIR_RIGHT);
            drawable.setReel(0);
            if(right==null)vx=Double.parseDouble(scriptManager.getScript("player").getVariable("vxacc"));
        }else{
            drawable.setReel(1);
            vx=0;
        }
        if(K_W){
            eh.triggerEvent(Event.PLAYER_ATTACK, wID, null);
            health-=0.1;
        }

        if((left!=null&&right!=null)||(ground!=null&&ceiling!=null))die();

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
        System.out.println("DEATH");
        x=0;y=0;vx=0;vy=0;
    }

    public void keyPressed(int key) {
        if(pause)return;
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
        if(pause)return;
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
        if(pause)return;
        switch(key){
            case Keyboard.KEY_R:if(editor.getActive()){x=0;y=0;vx=0;vy=0;}break;
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {
    }
}
