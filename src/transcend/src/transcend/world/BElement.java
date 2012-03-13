/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

import NexT.util.Line;
import NexT.util.Ray;
import NexT.util.SimpleSet;
import NexT.util.Vector;
import java.util.HashMap;

public class BElement {
    public static final int ELEMENT_ID = 0x0;
    public int wID = -1;
    public String name = "element";
    public double x=0,y=0,z=0;
    public int w=0,h=0;

    public BElement(){}
    public void init(){}

    public String getName(){return name;}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}
    public double getLayer(){return z;}
    public int getWidth(){return w;}
    public int getHeight(){return h;}
    public int getElementID(){return this.ELEMENT_ID;}
    public boolean isBaseElement(){return true;}

    public void setName(String name){this.name=name;}
    public void setPosition(double x,double y){this.x=x;this.y=y;}
    public void setLayer(int z){this.z=z;}
    public void setSize(int w,int h){this.w=w;this.h=h;}

    public void setOptions(HashMap<String,String> options){
        if(options.containsKey("x"))x=Integer.parseInt(options.get("x"));
        if(options.containsKey("y"))y=Integer.parseInt(options.get("y"));
        if(options.containsKey("w"))w=Integer.parseInt(options.get("w"));
        if(options.containsKey("h"))h=Integer.parseInt(options.get("h"));
        if(options.containsKey("z"))z=Integer.parseInt(options.get("z"));
    }
    public SimpleSet<String,String> getOptions(){return null;}

    public void draw(){}
    public void update(){}

    public boolean checkInside(double ax,double ay,double aw,double ah){
        if(w<=0||h<=0)return false;
        if(ax+aw<x)return false;
        if(ay+ah<y)return false;
        if(ax>x+w)return false;
        if(ay>y+h)return false;
        return true;
    }
    public boolean checkInside(Element e){
        return checkInside(e.x,e.y,e.w,e.h);
    }
    public boolean checkInside(double ax,double ay){
        return checkInside(ax,ay,0.0001,0.0001);
    }
    
    public Vector getCollisionPoint(Ray r){
        //Intersect with all sides of the square, test which one is the closes to the ray origin.
        double hits_b = new Line(x,y  ,0,x+w,y  ,0).getIntersection2D(r);
        double hits_t = new Line(x,y+h,0,x+w,y+h,0).getIntersection2D(r);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        hits_t = new Line(x,y,0,x,y+h,0).getIntersection2D(r);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        hits_t = new Line(x+w,y,0,x+w,y+h,0).getIntersection2D(r);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        //System.out.println(">>> LP: "+hits_b+"");
        //Translate ray position into real-world coordinates.
        if(hits_b>=0)return r.getPoint(hits_b);
        return null;
    }
    
    public Vector getCollisionPoint(Line l){
        //Intersect with all sides of the square, test which one is the closes to the ray origin.
        double hits_b = new Line(x,y  ,0,x+w,y  ,0).getIntersection2D(l);
        double hits_t = new Line(x,y+h,0,x+w,y+h,0).getIntersection2D(l);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        hits_t = new Line(x,y,0,x,y+h,0).getIntersection2D(l);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        hits_t = new Line(x+w,y,0,x+w,y+h,0).getIntersection2D(l);
        if(hits_t<hits_b&&hits_t>=0)hits_b=hits_t;
        //Translate ray position into real-world coordinates. If it's outside of the line reach, cap it.
        //System.out.println(">>> LP: "+hits_b+"");
        if(hits_b>=0&&hits_b<=1)return l.getPoint(hits_b);
        return null;
    }

    public double getDistanceTo(BElement e){
        if(e==null)return Double.MAX_VALUE;
        return Math.sqrt(Math.pow(x-e.getX(), 2)+Math.pow(y-e.getY(),2));
    }
}
