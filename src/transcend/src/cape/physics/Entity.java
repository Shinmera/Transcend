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
import transcend.graph.AbstractGraph;

public class Entity extends BElement{
    public static final double GRAVITY = -0.5;
    
    private Color c;
    
    public Entity(){c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),0.5f);}
    public Entity(int x,int y,int w,int h){
        this();
        pos.x=x;pos.y=y;this.w=w;this.h=h;
        form = new Rectangle(w,h);
    }
    public Entity(int x,int y,int w,int h,Form form){
        this(x,y,w,h);
        setForm(form);
    }
    
    public void update(){
        pos.x+=dir.x;
        pos.y+=dir.y;
        dir.y+=GRAVITY;
        
        if(Toolkit.p(pos.x)>10000)MainFrame.world.delByID(wID);
        else if(Toolkit.p(pos.y)>10000)MainFrame.world.delByID(wID);
    }
    
    public void draw(){
        new Color(1.0f,1.0f,1.0f,0.1f).bind();
        AbstractGraph.glRectangle2d(0, 0, w, h);
        c.bind();
        form.draw();
    }
}
