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
    
    public boolean checkInside(double ax,double ay,boolean solid){
        if(this.solid==0&&solid)return false;
        return checkInside(ax,ay);
    }
    public boolean checkInside(double ax,double ay,double minSolid){
        if(this.solid<minSolid)return false;
        return checkInside(ax,ay);
    }
}
