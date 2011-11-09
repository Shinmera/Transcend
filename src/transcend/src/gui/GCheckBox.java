/**********************\
  file: GCheckBox.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import graph.AbstractGraph;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GCheckBox extends GButton{
    private boolean activated = false;
    private GPanel parent;

    public GCheckBox(GPanel parent){super();this.parent=parent;fontAlign=GLabel.ALIGN_LEFT;MainFrame.ieh.addMouseListener(this);}
    public GCheckBox(GPanel parent,String text){this(parent);this.text=text;}
    public GCheckBox(GPanel parent,String text,boolean activated){this(parent,text);this.activated=activated;}

    public void setActivated(boolean b){activated=b;}
    public boolean isActivated(){return activated;}


    public void onPress(){}
    public void onHold(){}
    public void onRelease(){setActivated(!activated);}

    public void paint(){
        if(!visible)return;
        Color.white.bind();
        AbstractGraph.glRectangle2d(x, y, 12, 12);
        if(activated){
            Color.black.bind();
            AbstractGraph.glRectangle2d(x+2, y+2, 8, 8);
        }
        fore.bind();
        if(fontAlign==ALIGN_LEFT)font.drawString(x+14, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_LEFT);
        if(fontAlign==ALIGN_CENTER)font.drawString(x+14+w/2, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_CENTER);
        if(fontAlign==ALIGN_RIGHT)font.drawString(x+14+w, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_RIGHT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }
}
