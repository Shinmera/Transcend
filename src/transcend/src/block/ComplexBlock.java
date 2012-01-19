/**********************\
  file: ComplexBlock.java
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import graph.AbstractGraph;
import NexT.util.Toolkit;
import NexT.util.Vector;
import java.util.Arrays;
import transcend.MainFrame;
import NexT.util.Line;
import NexT.util.Ray;
import world.Element;
import spare.TessCallback;
import spare.VertexData;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.GLUtessellator;
import java.util.HashMap;
import NexT.util.SimpleSet;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import org.lwjgl.util.Point;
import static org.lwjgl.opengl.GL11.*;

public class ComplexBlock extends Block{
    private GLUtessellator tesselator;
    private ArrayList<Point> vertices = new ArrayList<Point>();
    private String texture = "";

    public ComplexBlock() {
        tesselator = GLU.gluNewTess();
        solid=1;

        //Copied from http://www.java2s.com/Open-Source/Java-Document/Game/Lightweight-Java-Game-Library-2.4.2/org/lwjgl/test/glu/tessellation/TessellationTest.java.htm
        TessCallback callback = new TessCallback(true);
        tesselator.gluTessCallback(GLU.GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_COMBINE, callback);
    }

    public ComplexBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        tesselator = GLU.gluNewTess();
        solid=1;

        //Copied from http://www.java2s.com/Open-Source/Java-Document/Game/Lightweight-Java-Game-Library-2.4.2/org/lwjgl/test/glu/tessellation/TessellationTest.java.htm
        TessCallback callback = new TessCallback(true);
        tesselator.gluTessCallback(GLU.GLU_TESS_VERTEX, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_BEGIN, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_END, callback);
        tesselator.gluTessCallback(GLU.GLU_TESS_COMBINE, callback);
    }

    public ComplexBlock(int x,int y,int w,int h,ArrayList<Point> vertices){
        this(x,y,w,h);
        this.vertices=vertices;
    }

    public void setTexture(String tex){texture=tex;}
    public void setVertices(ArrayList<Point> vertices){this.vertices=vertices;}
    public ArrayList<Point> getVertices(){return vertices;}

    public void addVertex(Point p){vertices.add(p);}
    public void addVertex(int x,int y){vertices.add(new Point(x,y));}
    public void delVertex(Point p){vertices.remove(p);}
    public void clearVertices(){vertices.clear();}


    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        SimpleSet<String,String> o = new SimpleSet<String,String>(options);
        o.sort();
        int x=Integer.MAX_VALUE,y=Integer.MAX_VALUE;
        for(int i=0;i<o.size();i++){
            if(o.getKey(i).startsWith("p")){
                if(o.getKey(i).contains("x"))x=Integer.parseInt(o.getAt(i));
                if(o.getKey(i).contains("y"))y=Integer.parseInt(o.getAt(i));
                if(x!=Integer.MAX_VALUE&&y!=Integer.MAX_VALUE){
                    addVertex(x,y);
                    x=Integer.MAX_VALUE;
                    y=Integer.MAX_VALUE;
                }
            }
        }
        if(o.containsKey("tex"))setTexture(o.get("tex"));
    }
    
    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> o = super.getOptions();
        for(int i=0;i<vertices.size();i++){
            o.put("p"+Toolkit.unifyNumberString(i,3)+"x",vertices.get(i).getX()+"");
            o.put("p"+Toolkit.unifyNumberString(i,3)+"y",vertices.get(i).getY()+"");
        }
        o.put("tex", texture);
        return null;
    }

    public void determineCoordinates(){
        int minX=Integer.MAX_VALUE;
        int maxX=Integer.MIN_VALUE;
        int minY=Integer.MAX_VALUE;
        int maxY=Integer.MIN_VALUE;
        for(int i=0;i<vertices.size();i++){
            Point p = vertices.get(i);
            if(p.getX()>maxX)maxX=p.getX();
            if(p.getX()<minX)minX=p.getX();
            if(p.getY()>maxY)maxY=p.getY();
            if(p.getY()<minY)minY=p.getY();
        }
        x=minX;
        y=minY;
        w=maxX-minX;
        h=maxY-minY;
    }

    public boolean checkInside(double x,double y,double w,double h){
        if(solid==0)return false;
        if(x>this.x+this.w||x<this.x||
           y>this.y+this.h||y<this.y)return false;
        //COPYPASTA. I HAVE NO IDEA HOW THIS ACTUALLY WORKS. SRC: http://paulbourke.net/geometry/insidepoly/
        int i, j;boolean c = false;
        for (i = 0, j = vertices.size()-1; i < vertices.size(); j = i++) {
            if ((((vertices.get(i).getY() <= y) && (y < vertices.get(j).getY())) ||
                 ((vertices.get(j).getY() <= y) && (y < vertices.get(i).getY()))) &&
                    (x < (vertices.get(j).getX() - vertices.get(i).getX()) * (y - vertices.get(i).getY()) / (vertices.get(j).getY() - vertices.get(i).getY()) + vertices.get(i).getX()))
                c = !c;
        }
        return c;
    }

    public Vector getCollisionPoint(Ray r){
        double hits = Double.MAX_VALUE;
        for(int i=1;i<vertices.size();i++){
            double hits2 = new Line(vertices.get(i-1).getX(),vertices.get(i-1).getY(),0,
                                    vertices.get(i).getX(),  vertices.get(i).getY()  ,0).getIntersection2D(r);
            if(hits2<hits&&hits2>0)hits=hits2;
        }
        if(hits>0)return r.getPoint(hits);
        return null;
    }

    public void draw(){
        glPushMatrix();
        //FIXME: TEXTURES
        
        tesselator.gluTessProperty(GLU.GLU_TESS_WINDING_RULE,GLU.GLU_TESS_WINDING_POSITIVE);
        tesselator.gluTessBeginPolygon(null);
            for(int i=0;i<vertices.size();i++){
                double data[] = {vertices.get(i).getX(),vertices.get(i).getY(),0.0,i/vertices.size(),i/vertices.size()};
                tesselator.gluTessVertex(data, 0, new VertexData(data));
            }
        tesselator.gluTessEndPolygon();
        glPopMatrix();

        if(MainFrame.editor.getActive()){
            AbstractGraph.glCross2d(x, y, 25);

            Color.white.bind();
            glBegin(GL_LINE_LOOP);
                for(int i=0;i<vertices.size();i++){
                    glVertex2i(vertices.get(i).getX(),vertices.get(i).getY());
                }
            glEnd();
        }
    }
}
