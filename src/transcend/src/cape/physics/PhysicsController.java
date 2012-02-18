/**********************\
  file: PhysicsController.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import NexT.util.Toolkit;
import static cape.main.MainFrame.*;
import cape.physics.form.Form;

public class PhysicsController {
    public int passes = 0;
    
    public double determineTimeFrame(Entity a,BElement b){
        //BoundingBox QuickCheck
        if(checkBoundingBox(a,b)){
            //System.out.println(a.wID+" AX: "+a.getX()+" AY: "+a.getY()+" AW: "+a.getWidth()+" AH: "+a.getHeight()+" AVX: "+a.vx+" AVY: "+a.vy+"\n"+
            //                   b.wID+" BX: "+b.getX()+" BY: "+b.getY()+" BW: "+b.getWidth()+" BH: "+b.getHeight());
            switch(a.form.getType()){
                case Form.FORM_POINT:
                    switch(b.form.getType()){
                        case Form.FORM_POINT:    return CollisionTimeDeterminer.distancePointPoint(a, b);
                        case Form.FORM_LINE:     return CollisionTimeDeterminer.distancePointLine(a, b);
                        case Form.FORM_CIRCLE:   return CollisionTimeDeterminer.distancePointCircle(a, b);
                        case Form.FORM_RECTANGLE:return CollisionTimeDeterminer.distancePointRectangle(a, b);
                        case Form.FORM_POLYGON:  return CollisionTimeDeterminer.distancePointPolygon(a, b);
                    }
                    break;
                case Form.FORM_LINE:
                    switch(b.form.getType()){
                        case Form.FORM_POINT:    return CollisionTimeDeterminer.distanceLinePoint(a, b);
                        case Form.FORM_LINE:     return CollisionTimeDeterminer.distanceLineLine(a, b);
                        case Form.FORM_CIRCLE:   return CollisionTimeDeterminer.distanceLineCircle(a, b);
                        case Form.FORM_RECTANGLE:return CollisionTimeDeterminer.distanceLineRectangle(a, b);
                        case Form.FORM_POLYGON:  return CollisionTimeDeterminer.distanceLinePolygon(a, b);
                    }
                    break;
                case Form.FORM_CIRCLE:
                    switch(b.form.getType()){
                        case Form.FORM_POINT:    return CollisionTimeDeterminer.distanceCirclePoint(a, b);
                        case Form.FORM_LINE:     return CollisionTimeDeterminer.distanceCircleLine(a, b);
                        case Form.FORM_CIRCLE:   return CollisionTimeDeterminer.distanceCircleCircle(a, b);
                        case Form.FORM_RECTANGLE:return CollisionTimeDeterminer.distanceCircleRectangle(a, b);
                        case Form.FORM_POLYGON:  return CollisionTimeDeterminer.distanceCirclePolygon(a, b);
                    }
                    break;
                case Form.FORM_RECTANGLE:
                    switch(b.form.getType()){
                        case Form.FORM_POINT:    return CollisionTimeDeterminer.distanceRectanglePoint(a, b);
                        case Form.FORM_LINE:     return CollisionTimeDeterminer.distanceRectangleLine(a, b);
                        case Form.FORM_CIRCLE:   return CollisionTimeDeterminer.distanceRectangleCircle(a, b);
                        case Form.FORM_RECTANGLE:return CollisionTimeDeterminer.distanceRectangleRectangle(a, b);
                        case Form.FORM_POLYGON:  return CollisionTimeDeterminer.distanceRectanglePolygon(a, b);
                    }
                    break;
                case Form.FORM_POLYGON:
                    switch(b.form.getType()){
                        case Form.FORM_POINT:    return CollisionTimeDeterminer.distancePolygonPoint(a, b);
                        case Form.FORM_LINE:     return CollisionTimeDeterminer.distancePolygonLine(a, b);
                        case Form.FORM_CIRCLE:   return CollisionTimeDeterminer.distancePolygonCircle(a, b);
                        case Form.FORM_RECTANGLE:return CollisionTimeDeterminer.distancePolygonRectangle(a, b);
                        case Form.FORM_POLYGON:  return CollisionTimeDeterminer.distancePolygonPolygon(a, b);
                    }
                    break;
            }
        }
        return Double.POSITIVE_INFINITY;
    }
    
    private boolean checkBoundingBox(Entity a,BElement b){
        if(a.getY()+a.getHeight()+a.vy<b.getY()+b.vy)return false;
        if(a.getY()+a.vy>b.getY()+b.getHeight()+b.vy)return false;
        if(a.getX()+a.getWidth()+a.vx<b.getX()+b.vx)return false;
        if(a.getX()+a.vx>b.getX()+b.getWidth()+b.vx)return false;
        return true;
    }
    
    public void handleCollision(double time,Entity a,BElement b){
        a.vx*=-1;
        a.vy*=-1;
    }
    
    public void update(){
        double minTime = 1000000000;
        Entity elA = null;
        BElement elB = null;
        passes=0;
        //Run as long as there are collisions.
        while(minTime<Double.POSITIVE_INFINITY){
            minTime=Double.POSITIVE_INFINITY;
            //Determine shortest time frame.
            for(int i=0;i<world.entitySize();i++){
                int idA=world.getEntityList()[i].wID;
                for(int j=0;j<world.size();j++){
                    int idB=world.getID(j);
                    
                    if(idB!=idA){
                        double thisTime = determineTimeFrame(world.getEntityList()[i],world.getByID(idB));
                        if(thisTime<minTime&&thisTime!=0.0){
                            minTime=thisTime;
                            elA=world.getEntityList()[i];
                            elB=world.getByID(idB);
                        }
                    }
                }
            }
            
            //We have a collision!
            if(minTime<Double.POSITIVE_INFINITY&&elA!=null&&elB!=null){
                handleCollision(minTime,elA,elB);
            }
            passes++;
        }
    }
}
