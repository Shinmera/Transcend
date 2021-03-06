/**********************\
  file: Circle.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;

import transcend.graph.AbstractGraph;

public class Circle extends Form{
    protected double r = 0.0;
    
    public Circle(){
        form=FORM_CIRCLE;
    }
    
    public Circle(double r){
        this();
        this.r=r;
    }
    
    public double getR(){return r;}
    public void setR(double r){this.r=r;}
    public double getHeight(){return r*2;}
    public double getWidth(){return r*2;}
    
    public void draw(){
        AbstractGraph.glFCircle2d(0, 0, r);
    }
}
