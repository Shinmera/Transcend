/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import NexT.util.SimpleSet;
import java.util.HashMap;

public class BElement {
    public static final int ELEMENT_ID = 0x0;
    public int wID = -1;
    public String name = "element";
    public double x=0,y=0,z=0;
    public int w=0,h=0;

    public BElement(){}

    public String getName(){return name;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getLayer(){return z;}
    public int getWidth(){return w;}
    public int getHeight(){return h;}
    public int getElementID(){return this.ELEMENT_ID;}
    public boolean isBaseElement(){return true;}

    public void setName(String name){this.name=name;}
    public void setPosition(int x,int y){this.x=x;this.y=y;}
    public void setLayer(int z){this.z=z;}
    public void setSize(int w,int h){this.w=w;this.h=h;}

    public void setOptions(HashMap<String,String> options){}
    public SimpleSet<String,String> getOptions(){return null;}

    public void draw(){}
    public void update(){}

    public boolean checkInside(Element e){
        if(w<=0||h<=0)return false;
        if(e.x+e.w<x)return false;
        if(e.y+e.h<y)return false;
        if(e.x>x+w)return false;
        if(e.y>y+h)return false;
        return true;
    }
    public boolean checkInside(double ax,double ay){
        if(w<=0||h<=0)return false;
        if(ax<x)return false;
        if(ay<y)return false;
        if(ax>x+w)return false;
        if(ay>y+h)return false;
        return true;
    }
}
