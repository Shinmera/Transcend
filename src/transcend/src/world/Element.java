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

public class Element extends BElement{
    public static final int ELEMENT_ID = 0x0;
    public Animation drawable = new Animation();
    public double solid=0.0;
    public double health=100;
    
    public Element(){}

    public Animation getDrawable(){return drawable;}
    public double getSolid(){return solid;}
    public double getHealth(){return health;}
    public boolean isBaseElement(){return false;}

    public void setHealth(double health){this.health=health;}
    public void setSolid(double s){this.solid = s;}
    public void setDrawable(Animation drawable){this.drawable = drawable;}
    public void setSize(int w,int h){super.setSize(w,h);drawable.calcTile(w, h);}

    public void draw(){drawable.draw((int)x,(int)y,w,h);}
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
    public boolean checkInside(double ax,double ay,double minSolid){
        if(this.solid<minSolid)return false;
        if(w<=0||h<=0)return false;
        if(ax<x)return false;
        if(ay<y)return false;
        if(ax>x+w)return false;
        if(ay>y+h)return false;
        return true;
    }
}
