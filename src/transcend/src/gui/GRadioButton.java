/**********************\
  file: GRadioButton.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import graph.AbstractGraph;
import org.newdawn.slick.Color;
import event.MouseListener;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GRadioButton extends GButton{
    private boolean activated = false;
    private GPanel parent;

    public GRadioButton(GPanel parent){this.parent=parent;fontAlign=GLabel.ALIGN_LEFT;MainFrame.ieh.addMouseListener(this);}
    public GRadioButton(GPanel parent,String text){this(parent);this.text=text;}
    public GRadioButton(GPanel parent,String text,boolean activated){this(parent,text);this.activated=activated;}

    public void setActivated(boolean b){
        activated=b;
        if(b){
        for(int i=0;i<parent.size();i++){
            try{
                GRadioButton but = (GRadioButton)parent.get(i);
                if(but!=this)but.setActivated(false);
            }catch(Exception e){}
        }
        }
    }

    public void onPress(){setActivated(true);}
    public void onHold(){}
    public void onRelease(){}

    public void paint(){
        if(!visible)return;
        Color.white.bind();
        AbstractGraph.glCircle2d(x, y+6, 6);
        if(activated)AbstractGraph.glFCircle2d(x, y+6, 4);
        fore.bind();
        if(fontAlign==ALIGN_LEFT)font.drawString(x+10, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_LEFT);
        if(fontAlign==ALIGN_CENTER)font.drawString(x+10+w/2, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_CENTER);
        if(fontAlign==ALIGN_RIGHT)font.drawString(x+10+w, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_RIGHT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }
}
