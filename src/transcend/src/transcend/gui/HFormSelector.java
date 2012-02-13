/**********************\
  file: HFormSelector.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import transcend.event.KeyboardListener;
import transcend.graph.AbstractGraph;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;

public class HFormSelector extends GObject implements KeyboardListener{
    int index = 0;
    double scroll = 0;
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
        MainFrame.ieh.addKeyboardListener(this);
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
    public void keyPressed(int key){}
    public void keyType(int key){}
    public void keyReleased(int key){
        if(!visible)return;
        switch(key){
            case Keyboard.KEY_1:if(available.contains(0))index=0;break;
            case Keyboard.KEY_2:if(available.contains(1))index=1;break;
            case Keyboard.KEY_3:if(available.contains(2))index=2;break;
            case Keyboard.KEY_4:if(available.contains(3))index=3;break;
            case Keyboard.KEY_5:if(available.contains(4))index=4;break;
        }
    }

    public void paint(){
        if(!visible)return;
        for(int i=0;i<available.size();i++){
            images[available.get(i)].setBounds(w/2+(i-available.size()/2)*h,y,h,h);
            if(i!=index)images[available.get(i)].paint();
        }
        new Color(0,0,0,0.5f).bind();
        AbstractGraph.glRectangle2d(x,y,w,h);
        images[available.get(index)].paint();
    }
}
