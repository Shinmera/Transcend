/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;

public class GObject {
    int x,y,w,h;
    boolean visible=false;
    Color back = new Color(255,255,255,255),
          border = new Color(0,0,0,255);
    float thickness = 1.0f;

    public GObject(){ }

    public void setBounds(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }
    public void autoBounds(GObject o,int x,int y,int maxWidth,int maxHeight){
        if(o.w<maxWidth)w=o.w;else w=maxWidth;
        if(o.h<maxHeight)h=o.h;else h=maxHeight;
        this.x=x;
        this.y=y;
    }
    public void setBorder(Color color,float thickness){
        this.thickness=thickness;
        this.border=color;
    }
    public void setBackground(Color color){
        this.back=color;
    }
    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}
    public void setWidth(int w){this.w=w;}
    public void setHeight(int h){this.h=h;}

    public Color getBackground(){return back;}
    public Color getBorder(){return border;}

    public void setVisible(boolean mod){
        this.visible=mod;
    }
    public boolean isVisible(){return visible;}

    public void paint(){
        if(!visible)return;

        back.bind();
        glBegin(GL_QUADS);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();

        if(thickness>0){
            border.bind();
            glLineWidth(thickness);
            glBegin(GL_LINE_LOOP);
                glVertex2f(x,y);
                glVertex2f(x,y+h);
                glVertex2f(x+w,y+h);
                glVertex2f(x+w,y);
            glEnd();
        }
    }
}
