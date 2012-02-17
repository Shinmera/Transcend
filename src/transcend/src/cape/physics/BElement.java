/**********************\
  file: BElement.java
  package: cape.physics
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package cape.physics;

import NexT.util.SimpleSet;
import NexT.util.Vector;
import cape.physics.form.Form;
import cape.physics.form.Rectangle;
import java.util.HashMap;
import transcend.world.Element;

public class BElement {
    public static final int ELEMENT_ID = 0x0;
    public int wID = -1;
    protected Vector pos = new Vector();
    protected int w=0,h=0;
    protected Form form = new Rectangle();

    public BElement(){}
    public void init(){}

    public final double getX(){return pos.getX();}
    public final double getY(){return pos.getY();}
    public final double getZ(){return pos.getZ();}
    public final double getLayer(){return pos.getZ();}
    public final int getWidth(){return w;}
    public final int getHeight(){return h;}
    public final int getElementID(){return this.ELEMENT_ID;}
    public final boolean isBaseElement(){return true;}
    public final Form getForm(){return form;}

    public final void setPosition(double x,double y){pos.setX(x);pos.setY(y);}
    public final void setX(double x){pos.setX(x);}
    public final void setY(double y){pos.setY(y);}
    public final void setZ(double z){pos.setZ(z);}
    public final void setLayer(int z){pos.setZ(z);}
    public final void setSize(int w,int h){this.w=w;this.h=h;}
    public final void setW(int w){this.w=w;}
    public final void setH(int h){this.h=h;}
    public final void setForm(Form f){this.form=f;}

    public void setOptions(HashMap<String,String> options){
        if(options.containsKey("x"))pos.setX(Integer.parseInt(options.get("x")));
        if(options.containsKey("y"))pos.setY(Integer.parseInt(options.get("y")));
        if(options.containsKey("z"))pos.setZ(Integer.parseInt(options.get("z")));
        if(options.containsKey("w"))w=Integer.parseInt(options.get("w"));
        if(options.containsKey("h"))h=Integer.parseInt(options.get("h"));
    }
    public SimpleSet<String,String> getOptions(){return null;}

    public void draw(){}
    public void update(){}

    public boolean checkInside(double ax,double ay,double aw,double ah){
        if(w<=0||h<=0)return false;
        if(ax+aw<pos.getX())return false;
        if(ay+ah<pos.getY())return false;
        if(ax>pos.getX()+w)return false;
        if(ay>pos.getY()+h)return false;
        return true;
    }
    public boolean checkInside(Element e){
        return checkInside(e.x,e.y,e.w,e.h);
    }
    public boolean checkInside(double ax,double ay){
        return checkInside(ax,ay,0.0001,0.0001);
    }
}
