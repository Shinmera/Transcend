/**********************\
  file: Entity.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import NexT.util.Toolkit;
import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import cape.main.MainFrame;
import transcend.world.BElement;

public class Entity extends BElement{
    public static final double GRAVITY = -0.5;
    
    private Color c;
    private double vx=0,vy=0;
    
    public Entity(){c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),0.5f);}
    public Entity(int x,int y,int w,int h){
        this();
        this.x=x;this.y=y;this.w=w;this.h=h;
    }
    
    public void update(){
        vy+=GRAVITY;
        
        x+=vx;
        y+=vy;
        
        if(Toolkit.p(x)>10000)MainFrame.world.delByID(wID);
        else if(Toolkit.p(y)>10000)MainFrame.world.delByID(wID);
    }
    
    public void draw(){
        c.bind();
        
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
