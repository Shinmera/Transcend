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
        this.form = form;
    }
    
    public void draw(){
        c.bind();
        glLineWidth(3.0f);
        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        form.draw();
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    }
}
