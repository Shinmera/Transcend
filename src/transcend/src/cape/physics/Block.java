/**********************\
  file: Block.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import transcend.world.BElement;
import static org.lwjgl.opengl.GL11.*;

public class Block extends BElement{
    private Color c;
    
    public Block(){c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.0f);}
    public Block(int x,int y,int w,int h){
        this();
        this.x=x;this.y=y;this.w=w;this.h=h;
    }
    
    public void draw(){
        c.bind();
        glLineWidth(3.0f);
        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        AbstractGraph.glRectangle2d(x, y, w, h);
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    }
}
