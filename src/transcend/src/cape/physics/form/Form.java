/**********************\
  file: Form.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;

public class Form {
    public static final int FORM_POINT = -1;
    public static final int FORM_LINE = 0;
    public static final int FORM_CIRCLE = 1;
    public static final int FORM_RECTANGLE = 2;
    public static final int FORM_POLYGON = 3;
    
    protected int form = FORM_POINT;
    protected double r = 0;
    
    public void draw(){
        
    }
}
