/**********************\
  file: Animation
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package graph;
import static org.lwjgl.opengl.GL11.*;

public class Animation {
    public Animation(){
        
    }

    public void draw(){

    }

    public void draw(int x,int y){

    }

    public void draw(int x,int y,int w,int h){
        glColor3f(0.5f,0.5f,0.5f);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x+w, y);
        glVertex2i(x+w, y+h);
        glVertex2i(x, y+h);
        glEnd();
    }
}
