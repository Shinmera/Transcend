/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputEventHandler {
    ArrayList<KeyboardListener> klisteners = new ArrayList();
    ArrayList<MouseListener> mlisteners = new ArrayList();
    private ArrayList<Integer> downKeys = new ArrayList();

    public InputEventHandler(){}

    public void addKeyboardListener(KeyboardListener kl){
        if(!klisteners.contains(kl))klisteners.add(kl);
    }

    public void delKeyboardListener(KeyboardListener kl){
        if(klisteners.contains(kl))klisteners.remove(kl);
    }

    public void addMouseListener(MouseListener ml){
        if(!mlisteners.contains(ml))mlisteners.add(ml);
    }

    public void delMouseListener(MouseListener ml){
        if(mlisteners.contains(ml))mlisteners.remove(ml);
    }

    public void triggerMouseEvent(){
        if(Mouse.isButtonDown(0)){
            for(int i=0;i<mlisteners.size();i++){
                mlisteners.get(i).mousePressed(0);
            }
        }
        if(Mouse.isButtonDown(1)){
            for(int i=0;i<mlisteners.size();i++){
                mlisteners.get(i).mousePressed(1);
            }
        }
    }

    public void triggerKeyboardEvent(){
        if(Keyboard.getEventKey()!=Keyboard.CHAR_NONE){
            ArrayList<Integer> keysPressedNow = new ArrayList();
            while(Keyboard.next()){
                int key = Keyboard.getEventKey();
                keysPressedNow.add(key);
                if(!downKeys.contains(key)){
                    downKeys.add(key);
                    for(int i=0;i<klisteners.size();i++){
                        klisteners.get(i).keyTyped(key);
                    }
                }
                for(int i=0;i<klisteners.size();i++){
                    klisteners.get(i).keyPressed(key);
                }
            }
            //remove unpressed keys
            for(int i=0;i<downKeys.size();i++){
                int key = downKeys.get(i);
                if(!keysPressedNow.contains(key)){
                    downKeys.remove(key);
                    for(int j=0;j<klisteners.size();j++){
                        klisteners.get(j).keyReleased(key);
                    }
                }
            }
        }
    }
}
