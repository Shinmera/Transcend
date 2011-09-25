/**********************\
  file: Player.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import NexT.script.Var;
import NexT.util.Ray;
import NexT.util.SimpleSet;
import NexT.util.Toolkit;
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
    public static final int FORM_MOUSE = 0x01;
    public static final int FORM_PONY = 0x02;
    public static final int FORM_DOLPHIN = 0x03;
    public static final int FORM_EAGLE = 0x04;

    public static final int ELEMENT_ID = 0x2;
    private Element ceiling = null,left = null,right = null,heart = null;
    private boolean K_LEFT,K_RIGHT,K_UP,K_DOWN,K_SPACE,
                    K_CTRL,K_SHIFT,K_TAB,
                    K_W,K_A,K_S,K_D,K_Q,K_E;
    private double power=100;
    private int lifes=5;
    private int form=FORM_HUMAN;
    private boolean[] unlocked = new boolean[5];
    private int score=100;

    public Player(){
        unlocked[FORM_HUMAN]=true;
        unlocked[FORM_MOUSE]=true;
        unlocked[FORM_PONY]=true;
        unlocked[FORM_DOLPHIN]=true;
        unlocked[FORM_EAGLE]=true;
    }
    public void init(){
        ieh.addKeyboardListener(this);
        scriptManager.loadScript(fileStorage.getFile("scr/player"));
        x=10;y=10;w=64;h=64;

        setForm(form);
        eh.registerEvent(Event.ENTITY_SEE, 0, this);
        eh.registerEvent(Event.ENTITY_ATTACK, 0, this);
        eh.registerEvent(Event.ENTITY_BLOCK, 0, this);
    }

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

    public void updateScriptVars(){
        scriptManager.s("player").setVariable("k_left",new Var(Var.TYPE_BOOLEAN,K_LEFT+""));
        scriptManager.s("player").setVariable("k_right",new Var(Var.TYPE_BOOLEAN,K_RIGHT+""));
        scriptManager.s("player").setVariable("k_up",new Var(Var.TYPE_BOOLEAN,K_UP+""));
        scriptManager.s("player").setVariable("k_down",new Var(Var.TYPE_BOOLEAN,K_DOWN+""));
        scriptManager.s("player").setVariable("k_space",new Var(Var.TYPE_BOOLEAN,K_SPACE+""));
        scriptManager.s("player").setVariable("k_shift",new Var(Var.TYPE_BOOLEAN,K_SHIFT+""));
        scriptManager.s("player").setVariable("k_tab",new Var(Var.TYPE_BOOLEAN,K_TAB+""));
        scriptManager.s("player").setVariable("k_ctrl",new Var(Var.TYPE_BOOLEAN,K_CTRL+""));
        scriptManager.s("player").setVariable("k_e",new Var(Var.TYPE_BOOLEAN,K_E+""));
        scriptManager.s("player").setVariable("k_q",new Var(Var.TYPE_BOOLEAN,K_Q+""));
        scriptManager.s("player").setVariable("k_w",new Var(Var.TYPE_BOOLEAN,K_W+""));
        scriptManager.s("player").setVariable("k_a",new Var(Var.TYPE_BOOLEAN,K_A+""));
        scriptManager.s("player").setVariable("k_s",new Var(Var.TYPE_BOOLEAN,K_S+""));
        scriptManager.s("player").setVariable("k_d",new Var(Var.TYPE_BOOLEAN,K_D+""));
        scriptManager.s("player").setVariable("x", new Var(Var.TYPE_DOUBLE,x+""));
        scriptManager.s("player").setVariable("y", new Var(Var.TYPE_DOUBLE,y+""));
        scriptManager.s("player").setVariable("vx", new Var(Var.TYPE_DOUBLE,vx+""));
        scriptManager.s("player").setVariable("vy", new Var(Var.TYPE_DOUBLE,vy+""));
        scriptManager.s("player").setVariable("form", new Var(Var.TYPE_INTEGER,form+""));
    }

    public void update(){
        drawable.update();

        //HEART
        if((heart=(Block)check(x-w/2+3,y+h/2,y+w/2-3,y+h/2))!=null){
        }
        //GROUND
        if(((ground=(Block)check(x-w/2+3,y+1,x+w/2-3,y+1))!=null || (ground=(Block)check(x-w/2+3,y+vy  ,x+w/2-3,y+vy))!=null) && vy<0){
            vy=0;
            Vector v = ground.getCollisionPoint(new Ray(x+w/2-3,y+h,0,0,-1,0));
            if(v!=null)y=v.getY();else
            v = ground.getCollisionPoint(new Ray(x-w/2+3,y+h,0,0,-1,0));
            if(v!=null)y=v.getY();
        }else{
            vy-=(Double)scriptManager.s("player").v("vydcc").get();
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
        if(K_SPACE&&ground!=null&&vy==0){vy+=(Double)scriptManager.s("player").v("vyacc").get();}

        updateScriptVars();
        double vxacc = scriptManager.s("player").eval("getVXAcc",null).fix();

        if(K_LEFT){
            drawable.setDirection(Animation.DIR_LEFT);
            drawable.setReel(0);
        }else if(K_RIGHT){
            drawable.setDirection(Animation.DIR_RIGHT);
            drawable.setReel(0);
        }else{
            drawable.setReel(1);
            vx=0;
        }
        if(K_W){
            eh.triggerEvent(Event.PLAYER_ATTACK, wID, null);
            health-=0.1;
        }
        if((right==null&&vxacc>0)||(left==null&&vxacc<0))vx=vxacc;

        if((left!=null&&right!=null)/*||(ground!=null&&ceiling!=null)*/)die();

        if(K_TAB)hud.get("formselector").setVisible(true);

        //EVALUATE
        x+=vx;
        y+=vy;
    }

    public void die(){
        System.out.println("DEATH");
        x=0;y=0;vx=0;vy=0;
        lifes--;
        if(lifes<=0){
            //FIXME ADD SETBACK
        }else{
            
        }
    }

    public void keyPressed(int key) {
        if(pause)return;
        switch(key){
        case Keyboard.KEY_LEFT: K_LEFT=true;break;
        case Keyboard.KEY_RIGHT: K_RIGHT=true;break;
        case Keyboard.KEY_UP: K_UP=true;break;
        case Keyboard.KEY_DOWN: K_DOWN=true;break;
        case Keyboard.KEY_SPACE: K_SPACE=true;break;
        case Keyboard.KEY_LSHIFT: K_SHIFT=true;break;
        case Keyboard.KEY_LCONTROL: K_CTRL=true;break;
        case Keyboard.KEY_TAB: K_TAB=true;break;
        case Keyboard.KEY_W: K_W=true;break;
        case Keyboard.KEY_A: K_A=true;break;
        case Keyboard.KEY_S: K_S=true;break;
        case Keyboard.KEY_D: K_D=true;break;
        case Keyboard.KEY_Q: K_Q=true;break;
        case Keyboard.KEY_E: K_E=true;break;
        }
    }
    public void keyReleased(int key) {
        if(pause)return;
        switch(key){
        case Keyboard.KEY_LEFT:K_LEFT=false;break;
        case Keyboard.KEY_RIGHT:K_RIGHT=false;break;
        case Keyboard.KEY_UP: K_UP=true;break;
        case Keyboard.KEY_DOWN: K_DOWN=true;break;
        case Keyboard.KEY_SPACE:K_SPACE=false;break;
        case Keyboard.KEY_LSHIFT: K_SHIFT=false;break;
        case Keyboard.KEY_LCONTROL: K_CTRL=true;break;
        case Keyboard.KEY_TAB: K_TAB=false;break;
        case Keyboard.KEY_W: K_W=true;break;
        case Keyboard.KEY_A: K_A=true;break;
        case Keyboard.KEY_S: K_S=true;break;
        case Keyboard.KEY_D: K_D=true;break;
        case Keyboard.KEY_Q: K_Q=true;break;
        case Keyboard.KEY_E: K_E=true;break;
        }
    }
    public void keyType(int key) {
        if(pause)return;
        switch(key){
            case Keyboard.KEY_Q:eh.triggerEvent(Event.AREA_CLEAR, wID, null);break;
            case Keyboard.KEY_R:if(editor.getActive()){x=0;y=0;vx=0;vy=0;}break;
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.ENTITY_DIE){
            score+=Integer.parseInt(arguments.get("points"));
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}

    public Element getLeft(){return left;}
    public Element getRight(){return right;}
    public double getPower(){return power;}
    public int getLifes(){return lifes;}
    public int getForm(){return form;}
    public int getScore(){return score;}
    public boolean[] getFormUnlockedState(){return unlocked;}

    public void setForm(int form){
        K_TAB=false;
        this.form=form;
        //switch animation
        drawable.loadTexture(fileStorage.getFile("player"+form),(int[])scriptManager.s("player").v("start"+form).get(),
                                                                (int[])scriptManager.s("player").v("stop"+form).get(),
                                                                (int[])scriptManager.s("player").v("loop"+form).get());
        drawable.setReel(0);
        w=((int[])scriptManager.s("player").v("width").get())[form];
        h=((int[])scriptManager.s("player").v("height").get())[form];
    }


    public SimpleSet<String,String> getStateData(){
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        set.put("px", x+"");
        set.put("py", y+"");
        set.put("ph", health+"");
        set.put("pp", power+"");
        set.put("pl", lifes+"");
        set.put("pf", form+"");
        set.put("sc", score+"");
        for(int i=0;i<unlocked.length;i++){
            set.put("f"+i, unlocked[i]+"");
        }
        return set;
    }

    public void setStateData(HashMap<String,String> set){
        x=Double.parseDouble(set.get("px"));
        y=Double.parseDouble(set.get("py"));
        health=Double.parseDouble(set.get("ph"));
        power=Double.parseDouble(set.get("pp"));
        lifes=Integer.parseInt(set.get("pl"));
        score=Integer.parseInt(set.get("sc"));
        setForm(Integer.parseInt(set.get("pf")));
        for(int i=0;i<unlocked.length;i++){
            unlocked[i]=Boolean.parseBoolean(set.get("f"+i));
        }
    }
}
