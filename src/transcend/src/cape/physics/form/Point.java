/**********************\
  file: Point.java
  package: cape.physics.form
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics.form;
import static org.lwjgl.opengl.GL11.*;

public class Point extends Form{
    public Point(){}
    
    public void draw(){
        glBegin(GL_POINTS);
            glVertex2f(0,0);
        glEnd();
    }
}
