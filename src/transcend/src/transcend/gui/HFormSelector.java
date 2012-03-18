/**********************\
  file: HFormSelector.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import transcend.main.MainFrame;

public class HFormSelector extends GObject{
    GImage[] images = new GImage[5];
    ArrayList<Integer> available = new ArrayList<Integer>();

    public HFormSelector(){
        w=MainFrame.DISPLAY_WIDTH;
        h=MainFrame.DISPLAY_HEIGHT/10;
        x=0;y=MainFrame.DISPLAY_HEIGHT-h;
        images[0] = new GImage("form_human");
        images[1] = new GImage("form_mouse");
        images[2] = new GImage("form_pony");
        images[3] = new GImage("form_dolphin");
        images[4] = new GImage("form_eagle");
    }

    public void detectForms(){
        available.clear();
        boolean[] unlocked = MainFrame.player.getFormUnlockedState();
        for(int i=0;i<unlocked.length;i++){
            if(unlocked[i]==true)available.add(i);
        }
    }

    public void setVisible(boolean visible){
        super.setVisible(visible);
        if(visible){
            //MainFrame.pause();
            detectForms();
        }else{
            //MainFrame.unpause();
        }
    }

    public void paint(){
        if(!visible)return;
        for(int i=0;i<available.size();i++){
            images[available.get(i)].setBounds(w/2+(i-available.size()/2)*h,y,h,h);
            if(i!=MainFrame.player.getForm())images[available.get(i)].paint();
        }
        new Color(0,0,0,0.5f).bind();
        AbstractGraph.glRectangle2d(x,y,w,h);
        images[available.get(MainFrame.player.getForm())].paint();
    }
}
