/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import event.KeyboardListener;
import graph.Animation;
import java.io.File;
import org.lwjgl.input.Keyboard;
import transcend.MainFrame;

public class Player extends Entity implements KeyboardListener{
    public static final int ELEMENT_ID = 0x2;

    public Player(){
        MainFrame.ieh.addKeyboardListener(this);
        x=10;y=10;w=64;h=64;

        int[] stop = {16,0};
        int[] start = {0,0};
        int[] loop = {0,0};

        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"dash_walk_right.png"),start,stop,loop);
        //drawable.setStart();

    }

    public void draw(){
        drawable.draw(x, y, w, h);
        /*glColor4f(1.0f,0.0f,0.0f,1.0f);
        glBegin(GL_QUADS);
            glVertex2d(x-w/2,y-h/2);
            glVertex2d(x-w/2,y+h/2);
            glVertex2d(x+w/2,y+h/2);
            glVertex2d(x+w/2,y-h/2);
        glEnd();*/
    }

    public void keyPressed(int key) {
        if(MainFrame.pause)return;
        switch(key){
        case Keyboard.KEY_LEFT: x-=5;break;
        case Keyboard.KEY_RIGHT: x+=5;break;
        case Keyboard.KEY_UP: y+=5;break;
        case Keyboard.KEY_DOWN: y-=5;break;
        }
    }
    public void keyReleased(int key) {
        if(Keyboard.KEY_SPACE==key){
            if(drawable.getReel()==0)drawable.setReel(1);
            else drawable.setReel(0);
        }
        if(!Keyboard.isKeyDown(Keyboard.KEY_LEFT)&&!Keyboard.isKeyDown(Keyboard.KEY_RIGHT))drawable.setReel(1);
    }
    public void keyType(int key) {
        switch(key){
            case Keyboard.KEY_LEFT:drawable.setDirection(Animation.DIR_LEFT);drawable.setReel(0);break;
            case Keyboard.KEY_RIGHT:drawable.setDirection(Animation.DIR_RIGHT);drawable.setReel(0);break;
        }
    }
}
