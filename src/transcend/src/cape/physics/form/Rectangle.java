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
    
    public double getWidth(){return w;}
    public double getHeight(){return h;}
    public void setWidth(double w){this.w=w;}
    public void setHeight(double h){this.h=h;}
    
    public void draw(){
        AbstractGraph.glRectangle2d(0, 0, w, h);
    }
}
