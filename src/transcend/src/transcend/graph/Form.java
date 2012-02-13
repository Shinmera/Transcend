/**********************\
  file: Form.java
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.graph;

public class Form {
    public static final int FORM_RECTANGLE = 0x00;
    public static final int FORM_SQUARE = 0x01;
    public static final int FORM_CIRCLE = 0x02;
    public static final int FORM_TRIANGLE = 0x03;
    public static final int ALIGN_LEFT = 0x04;
    public static final int ALIGN_RIGHT = 0x05;
    private int form = FORM_RECTANGLE;
    private double w,h,x1,x2,x3,x4,y1,y2,y3,y4;
    private int align = ALIGN_LEFT;

    public Form(){}

    public void setRectangle(double w,double h){
        form=FORM_RECTANGLE;
        this.w=w;this.h=h;
    }

    public void setSquare(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        form=FORM_SQUARE;
        this.x1=x1;this.x2=x2;this.x3=x3;this.x4=x4;
        this.y1=y1;this.y2=y2;this.y3=y3;this.y4=y4;
    }

    public void setCircle(double r){
        form=FORM_CIRCLE;
        this.w=r;
    }

    public void setTriangle(double x1,double y1,double x2,double y2,double x3,double y3){
        form=FORM_TRIANGLE;
        this.x1=x1;this.x2=x2;this.x3=x3;
        this.y1=y1;this.y2=y2;this.y3=y3;
    }

    public void setAlign(int align){this.align=align;}
    public void setWidth(double w){this.w=w;}
    public void setHeight(double h){this.h=h;}
    public void setRadius(double r){this.w=r;}

    public void draw(double x,double y){
        if(align==ALIGN_LEFT){
            if(form==FORM_CIRCLE)AbstractGraph.glCircle2d(x, y, w);
            if(form==FORM_RECTANGLE)AbstractGraph.glRectangle2d(x, y, w, h);
            if(form==FORM_SQUARE)AbstractGraph.glSquare2d(x1, y1, x2, y2, x3+w, y3+h, x4+w, y4+h);
            if(form==FORM_TRIANGLE)AbstractGraph.glTriangle2d(x1, y1, x2, y2, x3+w, y3+h);
        }else{
            if(form==FORM_CIRCLE)AbstractGraph.glCircle2d(x, y, w);
            if(form==FORM_RECTANGLE)AbstractGraph.glRectangle2d(x-w, y, w, h);
            if(form==FORM_SQUARE)AbstractGraph.glSquare2d(x1-w, y1, x2-w, y2, x3, y3+h, x4, y4+h);
            if(form==FORM_TRIANGLE)AbstractGraph.glTriangle2d(x1, y1, x2, y2, x3+w, y3+h);
        }
    }
    
    public static Form getRectangle(double w,double h){
        Form f = new Form();
        f.setRectangle(w, h);
        return f;
    }

    public static Form getSquare(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        Form f = new Form();
        f.setSquare(x1, y1, x2, y2, x3, y3, x4, y4);
        return f;
    }

    public static Form getCircle(double r){
        Form f = new Form();
        f.setCircle(r);
        return f;
    }

    public static Form getTriangle(double x1,double y1,double x2,double y2,double x3,double y3){
        Form f = new Form();
        f.setTriangle(x1, y1, x2, y2, x3, y3);
        return f;
    }
}
