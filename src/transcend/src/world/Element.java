/**********************\
  file: Element
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import NexT.util.SimpleSet;
import graph.Animation;

public class Element {
    public static final int ELEMENT_ID = 0x0;
    public int wID = -1;
    public String name = "element";
    public double x,y,z;
    public int w,h;
    public Animation drawable = new Animation();
    public double solid;
    public int health;
    
    public Element(){}

    public String getName(){return name;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getLayer(){return z;}
    public int getWidth(){return w;}
    public int getHeight(){return h;}
    public Animation getDrawable(){return drawable;}
    public double getSolid(){return solid;}
    public int getHealth(){return health;}

    public void setHealth(int health){this.health=health;}
    public void setSolid(double s){this.solid = s;}
    public void setName(String name){this.name=name;}
    public void setPosition(int x,int y){this.x=x;this.y=y;}
    public void setLayer(int z){this.z=z;}
    public void setSize(int w,int h){this.w=w;this.h=h;drawable.calcTile(w, h);}
    public void setDrawable(Animation drawable){this.drawable = drawable;}

    public void setOptions(String[] keys,String[] values){}
    public SimpleSet getOptions(){return null;}

    public void draw(){drawable.draw((int)x,(int)y,w,h);}
    public void update(){}
    public boolean checkInside(Element e){
        if(solid==0)return false;
        if(w<=0||h<=0)return false;
        if(e.x+e.w<x)return false;
        if(e.y+e.h<y)return false;
        if(e.x>x+w)return false;
        if(e.y>y+h)return false;
        return true;
    }
    public boolean checkInside(double ax,double ay){
        if(solid==0)return false;
        if(w<=0||h<=0)return false;
        if(ax<x)return false;
        if(ay<y)return false;
        if(ax>x+w)return false;
        if(ay>y+h)return false;
        return true;
    }
    public boolean checkInside(double ax,double ay,boolean solid){
        if(this.solid==0&&solid)return false;
        if(w<=0||h<=0)return false;
        if(ax<x)return false;
        if(ay<y)return false;
        if(ax>x+w)return false;
        if(ay>y+h)return false;
        return true;
    }
}
