/**********************\
  file: Player.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.entity;

import NexT.script.Var;
import NexT.util.Ray;
import NexT.util.SimpleSet;
import NexT.util.Vector;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import transcend.block.Block;
import transcend.event.Event;
import transcend.event.EventListener;
import transcend.event.KeyboardListener;
import transcend.graph.AbstractGraph;
import transcend.graph.Animation;
import transcend.main.MainFrame;
import static transcend.main.MainFrame.*;
import transcend.world.Element;

public class Player extends RigidBody implements KeyboardListener,EventListener{
    public static final int FORM_HUMAN = 0;
    public static final int FORM_MOUSE = 1;
    public static final int FORM_PONY = 2;
    public static final int FORM_DOLPHIN = 3;
    public static final int FORM_EAGLE = 4;
    public static final double SWITCH_PENALTY = 25;
    public double POWER_REGENERATION = 1.0/(30.0);

    /*public Element ceiling = null,left = null,right = null,heart = null,bottom = null;*/
    private boolean K_LEFT,K_RIGHT,K_UP,K_DOWN,K_JUMP,K_RUN,K_SWITCH,K_ATTACK,K_USE,K_MAP;
    private double power=100;
    private int lifes=5;
    private int form=FORM_HUMAN;
    private boolean[] unlocked = new boolean[5];
    private int score=100;
    private double backX=0,backY=0;

    public Player(){
        unlocked[FORM_HUMAN]=true;
        unlocked[FORM_MOUSE]=true;
        unlocked[FORM_PONY]=false;
        unlocked[FORM_DOLPHIN]=false;
        unlocked[FORM_EAGLE]=false;
    }
    public void init(){
        ieh.addKeyboardListener(this);
        scriptManager.loadScript(fileStorage.getFile("scr/player"));
        POWER_REGENERATION = 1.0/(MainFrame.ups);
        x=0;y=0;z=0;

        setForm(form);
        eh.registerEvent(Event.ENTITY_SEE, 0, this);
        eh.registerEvent(Event.ENTITY_ATTACK, 0, this);
        eh.registerEvent(Event.ENTITY_BLOCK, 0, this);
    }

    public void draw(){
        super.draw();
        drawable.draw((int)(x-drawable.getSpritesize()/2), (int)y, (int)drawable.getSpritesize(), (int)drawable.getSpritesize());
        if(editor.getActive()){
            if(bottom!=null)bottom.draw();
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
        scriptManager.s("player").setVariable("k_run",new Var(Var.TYPE_BOOLEAN,K_RUN+""));
        scriptManager.s("player").setVariable("k_use",new Var(Var.TYPE_BOOLEAN,K_USE+""));
        scriptManager.s("player").setVariable("k_map",new Var(Var.TYPE_BOOLEAN,K_MAP+""));
        scriptManager.s("player").setVariable("k_switch",new Var(Var.TYPE_BOOLEAN,K_SWITCH+""));
        scriptManager.s("player").setVariable("k_jump",new Var(Var.TYPE_BOOLEAN,K_JUMP+""));
        scriptManager.s("player").setVariable("k_attack",new Var(Var.TYPE_BOOLEAN,K_ATTACK+""));
        scriptManager.s("player").setVariable("x", new Var(Var.TYPE_DOUBLE,x+""));
        scriptManager.s("player").setVariable("y", new Var(Var.TYPE_DOUBLE,y+""));
        scriptManager.s("player").setVariable("vx", new Var(Var.TYPE_DOUBLE,vx+""));
        scriptManager.s("player").setVariable("vy", new Var(Var.TYPE_DOUBLE,vy+""));
        scriptManager.s("player").setVariable("form", new Var(Var.TYPE_INTEGER,form+""));
        scriptManager.s("player").setVariable("power", new Var(Var.TYPE_DOUBLE,power+""));
        scriptManager.s("player").setVariable("lifes", new Var(Var.TYPE_INTEGER,lifes+""));
    }

    public void update(){
        drawable.update();
        if(power<100)power+=POWER_REGENERATION;
        if(power>100)power=100;
        
        //vy+=GRAVITY;
        //performCollisionChecks();
        
        //bottom
        if(((bottom=(Block)check(x-w/2+3,y+1,x+w/2-3,y+1))!=null || (bottom=(Block)check(x-w/2+3,y+vy  ,x+w/2-3,y+vy))!=null)){
            int y1=Integer.MAX_VALUE;
            int y2=Integer.MAX_VALUE;
            
            Vector v = bottom.getCollisionPoint(new Ray(x+w/2-3,y+h,0,0,-1,0));if(v!=null)y1=(int) v.getY();
                   v = bottom.getCollisionPoint(new Ray(x-w/2+3,y+h,0,0,-1,0));if(v!=null)y2=(int) v.getY();
            
            if(y1==Integer.MAX_VALUE)y1=Integer.MIN_VALUE;
            else if(y2==Integer.MAX_VALUE)y2=Integer.MIN_VALUE;
            
            y1=Math.max(y1,y2)-1;
            if(y1>Integer.MIN_VALUE)y=y1;
            vy=0;
        }else{
            vy-=scriptManager.s("player").v("vydcc").fix(form);
        }
        //CEILING
        if((top=check(x-w/2+3,y+h  ,x+w/2-3,y+h))!=null&&vy>0){
            if(top.solid>0.5){
                vy=0;
                Vector v = top.getCollisionPoint(new Ray(x,y,0,0,1,0));
                if(v!=null){
                    if((int)(v.getY())!=Integer.MAX_VALUE)y=v.getY()-h;
                }
            }
        }
        //SIDES
        if((left=check(x-w/2-1+vx,y+h/2+3,x-w/2-1+vx,y+h-3))!=null){
            if(left.solid>0.5&&vx<0){
                vx=0;
                Vector v = left.getCollisionPoint(new Ray(x,y,0,-1,0,0));
                if(v!=null){
                    if((int)(v.getX())!=Integer.MAX_VALUE)x=v.getX()+w/2;
                }
            }
            if(left.solid<=0.5)left=null;
        }

        if((right=check(x+w/2+vx,y+h/2+3,x+w/2+1+vx,y+h-3))!=null){
            if(right.solid>0.5&&vx>0){
                vx=0;
                Vector v = right.getCollisionPoint(new Ray(x,y,0,1,0,0));
                if(v!=null){
                    if((int)(v.getX())!=Integer.MAX_VALUE)
                        x=v.getX()-w/2;
                }
            }
            if(right.solid<=0.5)right=null;
        }

        //INPUT
        if(K_JUMP&&bottom!=null&&vy==0){vy+=scriptManager.s("player").v("vyacc").fix(form);}

        updateScriptVars();
        double vxacc = scriptManager.s("player").eval("getVXAcc",null).fix();
        if(!K_ATTACK){
            if(K_LEFT){
                drawable.setDirection(Animation.DIR_LEFT);
                if(vy==0)drawable.setReel(1);
            }else if(K_RIGHT){
                drawable.setDirection(Animation.DIR_RIGHT);
                if(vy==0)drawable.setReel(1);
            }else{
                if(vy==0)drawable.setReel(0);
                vx=0;
            }
        } else {
            eh.triggerEvent(Event.PLAYER_ATTACK, wID, null);
            drawable.setReel(4);
        }
        if(K_SWITCH){
            texturePool.reloadTexture(fileStorage.getFile("player"+form).getName(), fileStorage.getFile("player"+form));
        }
        if(K_RUN){
            drawable.setPPS(60);
        }else{
            drawable.setPPS(30);
        }
        if(vy>0){drawable.setReel(2);}
        if(vy<0){drawable.setReel(3);}
        if((right==null&&vxacc>0)||(left==null&&vxacc<0))vx=vxacc;

        //if(/*(left!=null&&right!=null)||*/(bottom!=null&&ceiling!=null))die();
        
        if(health<=0)die();
        x+=vx;
        y+=vy;
    }

    public void die(){
        vx=0;vy=0;
        lifes--;
        if(lifes<=0){
            //FIXME ADD SETBACK
        }else{
            health=100;
            x=backX;
            y=backY;
            status=STATUS_IDLE;
        }
    }

    public void keyPressed(int key) {
        if(pause)return;
        if(key==ieh.getPlayerKey("LEFT"))K_LEFT=true;
        else if(key == ieh.getPlayerKey("RIGHT"))K_RIGHT=true;
        else if(key==ieh.getPlayerKey("UP"))K_UP=true;
        else if(key==ieh.getPlayerKey("DOWN"))K_DOWN=true;
        else if(key==ieh.getPlayerKey("ATTACK"))K_ATTACK=true;
        else if(key==ieh.getPlayerKey("MAP"))K_MAP=true;
        else if(key==ieh.getPlayerKey("USE"))K_USE=true;
        else if(key==ieh.getPlayerKey("RUN"))K_RUN=true;
        else if(key==ieh.getPlayerKey("JUMP"))K_JUMP=true;
        else if(key==ieh.getPlayerKey("SWITCH"))K_SWITCH=true;
    }
    public void keyReleased(int key) {
        if(pause)return;
        if(key==ieh.getPlayerKey("LEFT"))K_LEFT=false;
        else if(key == ieh.getPlayerKey("RIGHT"))K_RIGHT=false;
        else if(key==ieh.getPlayerKey("UP"))K_UP=false;
        else if(key==ieh.getPlayerKey("DOWN"))K_DOWN=false;
        else if(key==ieh.getPlayerKey("ATTACK"))K_ATTACK=false;
        else if(key==ieh.getPlayerKey("MAP"))K_MAP=false;
        else if(key==ieh.getPlayerKey("USE"))K_USE=false;
        else if(key==ieh.getPlayerKey("RUN"))K_RUN=false;
        else if(key==ieh.getPlayerKey("JUMP"))K_JUMP=false;
        else if(key==ieh.getPlayerKey("SWITCH"))K_SWITCH=false;
        else if(key==Keyboard.KEY_1&&unlocked[FORM_HUMAN]&&power>SWITCH_PENALTY){power-=SWITCH_PENALTY;setForm(FORM_HUMAN);}
        else if(key==Keyboard.KEY_2&&unlocked[FORM_MOUSE]&&power>SWITCH_PENALTY){power-=SWITCH_PENALTY;setForm(FORM_MOUSE);}
    }
    public void keyType(int key) {
        if(pause)return;
        switch(key){
            case Keyboard.KEY_Q:
                HashMap<String,String> map = new HashMap<String,String>();map.put("radius", "512");
                eh.triggerEvent(Event.AREA_CLEAR, wID,map);break;
            case Keyboard.KEY_R:if(editor.getActive()){x=0;y=0;vx=0;vy=0;}break;
        }
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.ENTITY_DIE){
            score+=Integer.parseInt(arguments.get("points"));
        }
        if(event==Event.ENTITY_ATTACK){
            Entity ent = (Entity)world.getByID(identifier);
            if(getDistanceTo(ent)<=(w+h)/4){
                camera.shake(15);
            }
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
    public void setSetbackPoint(double x,double y){backX=x;backY=y;}

    public void setForm(int form){
        K_SWITCH=false;
        this.form=form;
        //switch animation
        drawable.loadTexture(fileStorage.getFile("player"+form),(int[])(((Var[])scriptManager.s("player").v("start").get())[form].get()),
                                                                (int[])(((Var[])scriptManager.s("player").v("stop") .get())[form].get()),
                                                                (int[])(((Var[])scriptManager.s("player").v("loop") .get())[form].get()),
                                                                (int[])(((Var[])scriptManager.s("player").v("loop2").get())[form].get()));
        drawable.setSpritesize(((int[])scriptManager.s("player").v("spritesize").get())[form]);
        drawable.setPPS(30);
        drawable.setReel(0);
        w=((int[])scriptManager.s("player").v("width").get())[form];
        h=((int[])scriptManager.s("player").v("height").get())[form];
        drawable.setTileH(1);
        drawable.setTileW(1);
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
        backX=Double.parseDouble(set.get("px"));
        backY=Double.parseDouble(set.get("py"));
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
