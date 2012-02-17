/**********************\
  file: Entity.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import NexT.util.Toolkit;
import cape.main.MainFrame;
import cape.physics.form.Form;
import cape.physics.form.Rectangle;
import org.newdawn.slick.Color;

public class Entity extends BElement{
    public static final double GRAVITY = -0.5;
    
    private Color c;
    private double vx=0,vy=0;
    
    public Entity(){c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),0.5f);}
    public Entity(int x,int y,int w,int h){
        this();
        pos.x=x;pos.y=y;this.w=w;this.h=h;
        form = new Rectangle(w,h);
    }
    public Entity(int x,int y,int w,int h,Form form){
        this(x,y,w,h);
        this.form = form;
    }
    
    public void update(){
        vy+=GRAVITY;
        
        pos.x+=vx;
        pos.y+=vy;
        
        if(Toolkit.p(pos.x)>10000)MainFrame.world.delByID(wID);
        else if(Toolkit.p(pos.y)>10000)MainFrame.world.delByID(wID);
    }
    
    public void draw(){
        c.bind();
        form.draw();
    }
}
