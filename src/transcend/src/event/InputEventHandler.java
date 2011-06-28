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
    private ArrayList<Integer> downButtons = new ArrayList();
    private boolean lockKeyboard=false,lockMouse=false;

    public InputEventHandler(){}

    public void lockKeyboard(boolean b){lockKeyboard=b;}
    public void lockMouse(boolean b){lockMouse=b;}

    public int size(){
        return mlisteners.size()+klisteners.size();
    }

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
        if(!Mouse.isInsideWindow())return;
        if(lockMouse)return;
        
        if(Mouse.isButtonDown(0)){
            if(!downButtons.contains(0)){
                for(int i=0;i<mlisteners.size();i++)
                    mlisteners.get(i).mouseType(0);
                downButtons.add(0);
            }
            for(int i=0;i<mlisteners.size();i++)
                mlisteners.get(i).mousePressed(0);
        }else if(downButtons.contains(0)){
            downButtons.remove((Object)0);
            for(int i=0;i<mlisteners.size();i++)
                mlisteners.get(i).mouseReleased(0);
        }
        
        if(Mouse.isButtonDown(1)){
            if(!downButtons.contains(1)){
                for(int i=0;i<mlisteners.size();i++)
                    mlisteners.get(i).mouseType(1);
                downButtons.add(1);
            }
            for(int i=0;i<mlisteners.size();i++)
                mlisteners.get(i).mousePressed(1);
        }else if(downButtons.contains(1)){
            downButtons.remove((Object)1);
            for(int i=0;i<mlisteners.size();i++)
                mlisteners.get(i).mouseReleased(1);
        }

        for(int i=0;i<mlisteners.size();i++)mlisteners.get(i).mouseMoved(Mouse.getX(),Mouse.getY());
    }

    public void triggerKeyboardEvent(){
        if(lockKeyboard)return;
            while(Keyboard.next()){
                int key = Keyboard.getEventKey();
                if(Keyboard.getEventKeyState()){
                    if(!downKeys.contains(key)){
                        for(int i=0;i<klisteners.size();i++)
                            klisteners.get(i).keyType(key);
                        downKeys.add(key);
                    }
                }else{
                    if(downKeys.contains(key)){
                        downKeys.remove((Object)key);
                        for(int i=0;i<klisteners.size();i++)
                            klisteners.get(i).keyReleased(key);
                    }
                }
            }
            for(int j=0;j<downKeys.size();j++){
                for(int i=0;i<klisteners.size();i++)
                    klisteners.get(i).keyPressed(downKeys.get(j));
            }
        }
}
