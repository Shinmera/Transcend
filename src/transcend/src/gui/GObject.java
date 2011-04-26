/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class GObject {
    int x,y,w,h;
    Color back = new Color(0,0,0,0),
          fore = new Color(0,0,0,0),
          border = new Color(0,0,0,0);
    float thickness = 0;

    public GObject(){ }

    public void setBounds(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void setBorder(Color color,float thickness){
        this.thickness=thickness;
        this.border=color;
    }

    public void setBackground(Color color){
        this.back=color;
    }

    public void setForeground(Color color){
        this.fore=color;
    }

    public void paint(){
        glColor4f(back.getRed()/255.0f,back.getGreen()/255.0f,back.getBlue()/255.0f,back.getAlpha()/255);
        glBegin(GL_QUADS);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();

        glColor4f(border.getRed()/255.0f,border.getGreen()/255.0f,border.getBlue()/255.0f,back.getAlpha()/255);
        glLineWidth(thickness);
        glBegin(GL_LINE_LOOP);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();
    }
}
