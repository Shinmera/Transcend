/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import NexT.util.Toolkit;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class ColorBlock extends Block{
    Color c = Color.black;
    float thickness = 0.5f;

    public ColorBlock(int x,int y,int w,int h){
        super(x,y,w,h);
    }

    public void setColor(int r,int g,int b){
        c = new Color(r,g,b);
    }

    public void setColor(String color){
        java.awt.Color c = Toolkit.toColor(color);
        this.c = new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
    }

    public void setColor(Color color){
        c=color;
    }

    public void draw(){
        c.bind();
        glBegin(GL_QUADS);
            glVertex2d(x,y);
            glVertex2d(x,y+h);
            glVertex2d(x+w,y+h);
            glVertex2d(x+w,y);
        glEnd();

        /*glLineWidth(thickness);
        glBegin(GL_LINE_LOOP);
            glColor4f(border.getRed()/255.0f,border.getGreen()/255.0f,border.getBlue()/255.0f,border.getAlpha()/255);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();*/
    }
}
