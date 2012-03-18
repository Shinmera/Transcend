/**********************\
  file: MagicBullet.java
  package: transcend.entity
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.entity;

import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import transcend.main.MainFrame;
import transcend.world.Element;

public class MagicBullet extends Entity{
    public static final double effectiveness=0.5;
    
    public MagicBullet(int x,int y){
        this.x=x;this.y=y;w=64;h=64;
        this.health=10;
        drawable.loadTexture(MainFrame.fileStorage.getFile("bullet"));
    }
    
    public void shoot(double vx,double vy){
        this.vx=vx;
        this.vy=vy;
    }
    
    public void setHealth(double health){
        if(health<=100)super.setHealth(health);
    }
    
    public void update(){
        this.x+=vx;
        this.y+=vy;
        
        Element e;
        if((e=check(x+vx,y+vy,x+vx,y+vy))!=null){
            if(e.solid>0.2){
                if(e instanceof Entity){
                    Entity e2 = (Entity)e;
                    e2.health-=health*effectiveness;
                }
                MainFrame.world.delByID(wID);
            }
        }
        w=(int)(64*health/100.0);
        h=w;
        
    }
    
    public void draw(){
        drawable.draw((int)x-w/2, (int)y-h/2, w, h);
    }
    
}
