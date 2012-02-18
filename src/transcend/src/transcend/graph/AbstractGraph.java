/**********************\
  file: AbstractGraph.java
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.graph;
import org.newdawn.slick.Color;
import NexT.util.Vector;
import NexT.util.Ray;
import NexT.util.Line;
import static org.lwjgl.opengl.GL11.*;

public class AbstractGraph {
    public static void drawLine(Line l){
        glBegin(GL_LINES);
            glVertex2d(l.getA().getX(),l.getA().getY());
            glVertex2d(l.getB().getX(),l.getB().getY());
        glEnd();
    }

    public static void drawRay(Ray r){
        glBegin(GL_LINES);
            glVertex2d(r.getX(),r.getY());
            glVertex2d(r.getX()+r.getDX(),r.getY()+r.getDY());
        glEnd();
        glCircle2d(r.getX(), r.getY(), 10);
    }

    public static void drawRay(Ray r,double scale){
        glBegin(GL_LINES);
            glVertex2d(r.getX(),r.getY());
            glVertex2d(r.getX()+r.getDX()*scale,r.getY()+r.getDY()*scale);
        glEnd();
        glCircle2d(r.getX(), r.getY(), 10);
    }

    public static void drawVector(Vector v){

    }

    public static void glRectangle2d(double x,double y,double w,double h){
        glBegin(GL_QUADS);
            glVertex2d(x,y);
            glVertex2d(x+w,y);
            glVertex2d(x+w,y+h);
            glVertex2d(x,y+h);
        glEnd();
    }

    public static void glSquare2d(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        glBegin(GL_QUADS);
            glVertex2d(x1,y1);
            glVertex2d(x2,y2);
            glVertex2d(x3,y3);
            glVertex2d(x4,y4);
        glEnd();
    }

    public static void glTriangle2d(double x1,double y1,double x2,double y2,double x3,double y3){
        glBegin(GL_TRIANGLES);
            glVertex2d(x1,y1);
            glVertex2d(x2,y2);
            glVertex2d(x3,y3);
        glEnd();
    }

    public static void glCircle2d(double x,double y,double r){
        glBegin(GL_LINE_LOOP);
            for(int i = 0; i < 100; i++) {
                double angle = i*2*Math.PI/100;
                glVertex2d(x + (Math.cos(angle) * r), y + (Math.sin(angle) * r));
            }
        glEnd();
    }

    public static void glFCircle2d(double x,double y,double r){
        glBegin(GL_POLYGON);
            for(int i = 0; i < 100; i++) {
                double angle = i*2*Math.PI/100;
                glVertex2d(x + (Math.cos(angle) * r), y + (Math.sin(angle) * r));
            }
        glEnd();
    }

    public static void glCross2d(double x,double y,double s){
        glEnable(GL_COLOR_LOGIC_OP);
        glLogicOp(GL_XOR);
        Color.white.bind();
        glLineWidth(0.5f);
        glBegin(GL_LINES);
            glVertex2d(x-s,y);
            glVertex2d(x+s,y);
        glEnd();
        glBegin(GL_LINES);
            glVertex2d(x,y+s);
            glVertex2d(x,y-s);
        glEnd();
        glDisable(GL_COLOR_LOGIC_OP);
    }
    
    public static void glLine2d(double ax,double ay,double bx,double by){
        glBegin(GL_LINES);
            glVertex2d(ax,ay);
            glVertex2d(bx,by);
        glEnd();
    }
}
