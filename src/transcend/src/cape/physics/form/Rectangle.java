/**********************\
  file: Rectangle.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;

import transcend.graph.AbstractGraph;

public class Rectangle extends Form{
    protected double w=0,h=0;
    
    public Rectangle(){
        form = FORM_RECTANGLE;
    }
    
    public Rectangle(double w,double h){
        this();
        this.w=w;
        this.h=h;
    }
    
    public double getW(){return w;}
    public double getH(){return h;}
    public void setW(double w){this.w=w;}
    public void setH(double h){this.h=h;}
    
    public void draw(){
        AbstractGraph.glRectangle2d(0, 0, w, h);
    }
}
