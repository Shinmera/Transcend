/**********************\
  file: Element
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import graph.Animation;
import org.lwjgl.util.Point;

public class Element {
    public static final int ELEMENT_ID = 0x0;
    public String name = "element";
    public int x,y,z;
    public int w,h;
    public Animation drawable;
    
    public Element(){
        drawable = new Animation();
    }

    public String getName(){return name;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getLayer(){return z;}
    public Point getPosition(){return new Point(x,y);}
    public int getWidth(){return w;}
    public int getHeight(){return h;}
    public Animation getDrawable(){return drawable;}

    public void setName(String name){this.name=name;}
    public void setPosition(int x,int y){this.x=x;this.y=y;}
    public void setLayer(int z){this.z=z;}
    public void setSize(int w,int h){this.w=w;this.h=h;}
    public void setDrawable(Animation drawable){this.drawable = drawable;}

    public void draw(){
        drawable.draw(x,y,w,h);
    }

    public void update(){

    }
}
