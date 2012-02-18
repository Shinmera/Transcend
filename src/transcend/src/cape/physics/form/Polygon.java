/**********************\
  file: Polygon.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;
import NexT.util.Vector2;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import org.newdawn.slick.Color;
import transcend.spare.TessCallback;
import transcend.spare.VertexData;

public class Polygon extends Form{
    ArrayList<Vector2> p = new ArrayList<Vector2>();
    private GLUtessellator tesselator = GLU.gluNewTess();
    private double w=0,h=0;
    
    public Polygon(){
        form = FORM_POLYGON;
        //Copied from http://www.java2s.com/Open-Source/Java-Document/Game/Lightweight-Java-Game-Library-2.4.2/org/lwjgl/test/glu/tessellation/TessellationTest.java.htm
        TessCallback callback = new TessCallback(false);
        tesselator.gluTessCallback(GLU.GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_COMBINE, callback);
    }
    
    public Polygon(ArrayList<Vector2> p){
        this();
        this.p=p;
    }
    
    public void determineCoordinates(){
        double minX=Double.MAX_VALUE;
        double maxX=Double.MIN_VALUE;
        double minY=Double.MAX_VALUE;
        double maxY=Double.MIN_VALUE;
        for(int i=0;i<p.size();i++){
            Vector2 p = this.p.get(i);
            if(p.getX()>maxX)maxX=p.getX();
            if(p.getX()<minX)minX=p.getX();
            if(p.getY()>maxY)maxY=p.getY();
            if(p.getY()<minY)minY=p.getY();
        }
        w=maxX-minX;
        h=maxY-minY;
        //shift to 0.
        for(int i=0;i<p.size();i++){
            p.get(i).setX(p.get(i).getX()-minX-w/2);
            p.get(i).setY(p.get(i).getY()-minY-h/2);
        }
    }
    
    public Vector2[] getPoints(){return p.toArray(new Vector2[p.size()]);}
    public void setPoints(ArrayList<Vector2> p){this.p=p;}
    public void addPoint(Vector2 p){this.p.add(p);}
    public void setPoint(int pos,Vector2 p){this.p.set(pos, p);}
    public void delPoint(Vector2 p){this.p.remove(p);}
    public void delPoint(int pos){this.p.remove(pos);}
    public void clearPoints(){p.clear();}
    public double getWidth(){return w;}
    public double getHeight(){return h;}
    
    public void draw(){
        glPushMatrix();
        
        tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE,GLU.GLU_TESS_WINDING_POSITIVE);
        tesselator.gluTessBeginPolygon(null);
            for(int i=0;i<p.size();i++){
                double data[] = {p.get(i).getX(),p.get(i).getY(),0.0,0.0,1.0,0.5,0.5};
                tesselator.gluTessVertex(data, 0, new VertexData(data));
            }
        tesselator.gluTessEndPolygon();
        glPopMatrix();

        Color.white.bind();
        glBegin(GL_LINE_LOOP);
            for(int i=0;i<p.size();i++){
                glVertex2d(p.get(i).getX(),p.get(i).getY());
            }
        glEnd();
    }
}
