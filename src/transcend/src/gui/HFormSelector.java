/**********************\
  file: HFormSelector.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import event.KeyboardListener;
import graph.AbstractGraph;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import transcend.MainFrame;

public class HFormSelector extends GObject implements KeyboardListener{
    int index = 0;
    GImage[] images = new GImage[5];
    ArrayList<Integer> available = new ArrayList<Integer>();

    public HFormSelector(){
        w=MainFrame.DISPLAY_WIDTH;
        h=MainFrame.DISPLAY_HEIGHT;
        x=0;y=0;
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
            MainFrame.pause();
            detectForms();
        }else{
            MainFrame.unpause();
        }
    }
    public void keyPressed(int key){}
    public void keyType(int key){}
    public void keyReleased(int key){
        if(!visible)return;
        switch(key){
            case Keyboard.KEY_LEFT:
                index--;
                if(index<0)index=available.size()-1;
                break;
            case Keyboard.KEY_RIGHT:
                index++;
                if(index>=available.size())index=0;
                break;
            case Keyboard.KEY_SPACE:
                setVisible(false);
                MainFrame.player.setForm(available.get(index));
                break;
        }
    }

    public void paint(){
        if(!visible)return;
        new Color(0,0,0,0.5f).bind();
        AbstractGraph.glRectangle2d(x,y, w, h);
        for(int i=0;i<available.size();i++){
            images[available.get(i)].setBounds((int)Math.round(w/2.0+(i-index)*150.0)-75, h/2-100,200,200);
            images[available.get(i)].paint();
        }
        Color.white.bind();
        AbstractGraph.glSquare2d(w/2-10, h/2-90, w/2, h/2-90, w/2+10, h/2-90, w/2, h/2-75);
    }
}
