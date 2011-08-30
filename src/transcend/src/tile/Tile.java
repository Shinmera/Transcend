/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import graph.Animation;
import org.newdawn.slick.Color;
import world.BElement;
import static org.lwjgl.opengl.GL11.*;

public class Tile extends BElement{
    public static final int ELEMENT_ID = 0x101;
    public float depth = 1;
    public Animation drawable = new Animation();

    public Tile(){}
    public Tile(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void setDepth(float depth){this.depth=depth;}
    public float getDepth(){return depth;}

    public void draw(){
        drawable.draw((int)x,(int)y,w,h);
        if(depth!=0&&z!=0){
            if(z<0)new Color(0f,0f,0f,(float)(-z/10.0*depth)).bind();
            //if(z>0)new Color(1f,1f,1f,(float)(z/10.0*depth)).bind();
            glBegin(GL_QUADS);
                glVertex2d(x,y);
                glVertex2d(x+w,y);
                glVertex2d(x+w,y+h);
                glVertex2d(x,y+h);
            glEnd();
        }
    }
}
