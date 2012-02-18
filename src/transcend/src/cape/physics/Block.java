/**********************\
  file: Block.java
  package: cape.physics
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.physics;

import cape.physics.form.Form;
import cape.physics.form.Rectangle;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;

public class Block extends BElement{
    private Color c;
    
    public Block(){c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.0f);}
    public Block(int x,int y,int w,int h){
        this();
        pos.x=x;pos.y=y;this.w=w;this.h=h;
        form = new Rectangle(w,h);
    }
    public Block(int x,int y,int w,int h,Form form){
        this(x,y,w,h);
        setForm(form);
    }
    
    public void draw(){
        new Color(1.0f,1.0f,1.0f,0.1f).bind();
        AbstractGraph.glRectangle2d(0, 0, w, h);
        c.bind();
        glLineWidth(1.5f);
        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        form.draw();
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    }
}
