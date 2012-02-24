/**********************\
  file: CollisionTimeDeterminer.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import NexT.util.Line;
import NexT.util.Ray;
import NexT.util.Toolkit;
import NexT.util.Vector2;
import cape.physics.form.Circle;

public class CollisionTimeDeterminer {
    
    public static double distancePointPoint(BElement a,BElement b){
        return Double.POSITIVE_INFINITY; //Screw this, we're just assuming it never happens.
    }
    
    public static double distanceLineLine(BElement a,BElement b){
        //Point -> Line test for both points of both lines.
        return 0; //TODO: Complete function.
    }
    
    public static double distanceCircleCircle(BElement a,BElement b){
        //Add radius from b to a.
        BElement c = new BElement();
        c.setPosition(a.getX(),a.getY());
        c.setVX(a.getVX());c.setVY(a.getVY());
        c.setForm(new Circle(a.form.getWidth()/2+b.form.getWidth()/2));
        return distancePointCircle(b,c); //TODO: Complete function.
    }
    
    public static double distanceRectangleRectangle(BElement a,BElement b){
        //Rectangle -> Line test for each line.
        return 0; //TODO: Complete function.
    }
    
    public static double distancePolygonPolygon(BElement a,BElement b){
        //Polygon -> Line test for each line.
        return 0; //TODO: Complete function.
    }
    
    public static double distancePointLine(BElement a,BElement b){
        //Calculate ray / line intersection
        Ray r = new Ray(a.getX(),a.getY(),0,    a.getVX(),a.getVY(),0);
        Line l = new Line(b.getX(),b.getY(),0,  b.getForm().getWidth(),b.getForm().getHeight(),0);
        double t = l.getIntersection2D(r);
        
        if(t<=0)return 0;
        else    return t;
    }
    
    public static double distancePointCircle(BElement a,BElement b){
        //Solve huge equation from notes.
        return 0; //TODO: Complete function.
    }
    
    public static double distancePointRectangle(BElement a,BElement b){
        //Point -> Line test for all 4. Smallest wins.
        return 0; //TODO: Complete function.
    }
    
    public static double distancePointPolygon(BElement a,BElement b){
        //Point -> Line test for all lines. Smallest wins.
        return 0; //TODO: Complete function.
    }
    
    public static double distanceCircleLine(BElement circle,BElement line){
        Vector2 n = new Vector2(line.form.getWidth(),line.form.getHeight());    //B-A
        double l = n.length();
        n.normalize();
        double c  = (circle.getPos2().sub(line.getPos2())).scalar(n);
        if(c<-circle.form.getWidth()/2||c>l+circle.form.getWidth()/2)           //Check line bounds
            return PhysicsController.UPPER_LIMIT;
        
        n.ortho();                                                              //Create normal vector on line.
        double d  = (circle.getPos2().sub(line.getPos2())).scalar(n);
        double vn = (line.getDir().sub(circle.getDir())).scalar(n);
        if(d*vn<=0)return PhysicsController.UPPER_LIMIT;
        double dt = (Toolkit.p(d)-circle.form.getWidth()/2)/Toolkit.p(vn);
        return dt;
    }
    
    public static double distanceRectangleLine(BElement a,BElement b){
        return 0; //TODO: Complete function.
    }
    
    public static double distancePolygonLine(BElement a,BElement b){
        return 0; //TODO: Complete function.
    }
    
    public static double distanceCircleRectangle(BElement a,BElement b){
        //Circle -> Line test for all lines.
        return 0; //TODO: Complete function.
    }
    
    public static double distanceCirclePolygon(BElement a,BElement b){
        //Circle -> Line test for all lines.
        return 0; //TODO: Complete function.
    }
    
    public static double distanceRectanglePolygon(BElement a,BElement b){
        return 0; //TODO: Complete function.
    }
}
