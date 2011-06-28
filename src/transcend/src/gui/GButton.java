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
import org.lwjgl.input.Mouse;
import transcend.MainFrame;
import event.MouseListener;
import static org.lwjgl.opengl.GL11.*;

public class GButton extends GLabel implements MouseListener{
    private boolean pressed = false;

    public GButton(){super();MainFrame.ieh.addMouseListener(this);}
    public GButton(String text){super(text);MainFrame.ieh.addMouseListener(this);}

    public void onPress(){}
    public void onRelease(){}

    public void paint() {
        if(pressed)back.darker().bind();else back.bind();
        glBegin(GL_QUADS);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();

        border.bind();
        glLineWidth(thickness);
        glBegin(GL_LINE_LOOP);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();

        fore.bind();
        font.drawString(x+w/2, y+h/2-font.getLineHeight()/2, text, 1,1, TrueTypeFont.ALIGN_CENTER);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }


    public final void mouseMoved(int x, int y) {}
    public final void mousePressed(int button) {
        if(Mouse.getX()>x&&Mouse.getX()<x+w&&
           Mouse.getY()>y&&Mouse.getY()<y+h){
            if(button==0)pressed=true;
        }else pressed=false;
    }
    public final void mouseReleased(int button) {
        if(button==0){
            if(Mouse.getX()>x&&Mouse.getX()<x+w&&
               Mouse.getY()>y&&Mouse.getY()<y+h)
                onRelease();
            pressed = false;
        }
    }
    public final void mouseType(int button) {
        if(Mouse.getX()>x&&Mouse.getX()<x+w&&
           Mouse.getY()>y&&Mouse.getY()<y+h){
            if(button==0){
                pressed=true;
                onPress();
            }
        }
    }

}
