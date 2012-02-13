/**********************\
  file: GList.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import org.lwjgl.input.Mouse;
import transcend.event.MouseListener;
import java.util.ArrayList;
import transcend.main.MainFrame;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class GList extends GLabel implements MouseListener{
    ArrayList<String> list = new ArrayList<String>();
    private boolean pressed = false;
    private int selected = 0;
    private double scrollPos = 0;
    private int elementHeight=50;

    public GList(){super();MainFrame.ieh.addMouseListener(this);}

    public void addListElement(String element){list.add(element);}
    public void delListElement(String element){list.remove(element);}
    public void delListElement(int i){list.remove(i);}
    public String getListElement(int i){return list.get(i);}
    public void clear(){list.clear();}

    public int getSelectedIndex(){return selected;}
    public String getSelected(){return list.get(selected);}
    public double getScrollPos(){return scrollPos;}
    public int getElementHeight(){return elementHeight;}
    public ArrayList<String> getList(){return list;}
    @Deprecated
    public String getText(){return "";}
    public int getSitze(){return list.size();}

    public void setList(ArrayList<String> list){this.list=list;}
    public void setScrollPos(double pos){scrollPos=pos;}
    public void setElementHeight(int height){elementHeight=height;}
    public void setSelected(int i){selected=i;}
    public void setSelected(String s){selected=list.indexOf(s);}
    @Deprecated
    public void setText(String text){}

    public void mouseMoved(int x, int y) {
        if(!visible)return;
        if(pressed){
            if(y>this.y&&y<this.y+h)    scrollPos=(y-this.y+0.0)/h*100.0;
            if(y>this.y+h)              scrollPos=100;
            if(y<this.y)                scrollPos=0;
        }
    }

    public void mousePressed(int button) {
        if(!visible)return;
    }

    public void mouseType(int button) {
        if(!visible)return;
        if(Mouse.getY()>y&&Mouse.getY()<y+h&&
           Mouse.getX()>x+w-30&&Mouse.getX()<x+w)
            pressed=true;
    }

    public void mouseReleased(int button) {
        if(!visible)return;
        pressed=false;
        if(Mouse.getY()>y&&Mouse.getY()<y+h&&
           Mouse.getX()>x&&Mouse.getX()<x+w-30){
            int minElements = (int)Math.floor((h+0.0)/elementHeight);
            int start=(int)Math.floor((list.size()-minElements)/100.0*scrollPos);
            selected=start+(int)Math.floor((Mouse.getY()-y)/elementHeight);
        }
    }

    public void paint(){
        if(!visible)return;
        super.paint();
        //draw scrollbar
        Color.black.bind();
        glBegin(GL_LINE_LOOP);
            glVertex2d(x+w-30,y+scrollPos*((h-100)/100.0));
            glVertex2d(x+w-30,y+scrollPos*((h-100)/100.0)+100);
            glVertex2d(x+w,   y+scrollPos*((h-100)/100.0)+100);
            glVertex2d(x+w,   y+scrollPos*((h-100)/100.0));
        glEnd();

        int minElements = (int)Math.floor((h+0.0)/elementHeight);
        //determine visible elements
        int start=0;
        if(list.size()<=minElements){start=0;}
        else{start=(int)Math.floor((list.size()-minElements)/100.0*scrollPos);}
        //draw elements
        for(int i=0;i<minElements&&i<list.size();i++){
            if(i+start==selected){
                back.darker().bind();
                glBegin(GL_QUADS);
                    glVertex2d(x+w-30,  y+i*elementHeight);
                    glVertex2d(x+w-30,  y+(i+1)*elementHeight);
                    glVertex2d(x,       y+(i+1)*elementHeight);
                    glVertex2d(x,       y+i*elementHeight);
                glEnd();
            }
            border.bind();
            glLineWidth(thickness);
            glBegin(GL_LINE_LOOP);
                    glVertex2d(x+w-30,  y+i*elementHeight);
                    glVertex2d(x+w-30,  y+(i+1)*elementHeight);
                    glVertex2d(x,       y+(i+1)*elementHeight);
                    glVertex2d(x,       y+i*elementHeight);
            glEnd();
            font.drawString(x+10,y+(int)((i+0.5)*elementHeight), list.get(i+start), 1,1);
        }
    }
}
