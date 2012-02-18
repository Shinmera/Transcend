/**********************\
  file: Line.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;

import NexT.util.Vector2;
import static org.lwjgl.opengl.GL11.*;

public class Line extends Form{
    protected Vector2 a;
    
    public Line(){
        form=FORM_LINE;
    }
    
    public Line(Vector2 a){
        this();
        this.a=a;
    }
    
    public Line(double ax,double ay){
        this();
        a=new Vector2(ax,ay);
    }
    
    public Vector2 getV(){return a;}
    public double getX(){return a.getX();}
    public double getY(){return a.getY();}
    public void setV(Vector2 a){this.a=a;}
    public void setX(double ax){a.setX(ax);}
    public void setY(double ay){a.setY(ay);}
    public double getWidth(){return a.getX();}
    public double getHeight(){return a.getY();}
    
    public void draw(){
        glBegin(GL_LINES);
            glVertex2d(0,0);
            glVertex2d(a.getX(),a.getY());
        glEnd();
    }
}
