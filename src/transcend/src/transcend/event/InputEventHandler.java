/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.event;

import NexT.util.ConfigManager;
import NexT.util.SimpleSet;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import transcend.main.Const;
import transcend.main.MainFrame;

public class InputEventHandler {
    ArrayList<KeyboardListener> klisteners = new ArrayList();
    ArrayList<MouseListener> mlisteners = new ArrayList();
    private ArrayList<Integer> downKeys = new ArrayList();
    private ArrayList<Integer> downButtons = new ArrayList();
    private SimpleSet<String,String> keyMap = new SimpleSet();
    private SimpleSet<String,String> playerMap = new SimpleSet();
    private boolean lockKeyboard=false,lockMouse=false;

    public InputEventHandler(){
        ConfigManager man = new ConfigManager("KeyboardLayout");
        man.verbose=true;
        if(!man.loadConfig(MainFrame.fileStorage.getFile(MainFrame.CONST.gString("LAYOUT")+".kl").getAbsolutePath()))
            Const.LOGGER.warning("[InputEventHandler] Failed to load layout '"+MainFrame.CONST.gString("LAYOUT")+"'!");
        else 
            Const.LOGGER.info("[InputEventHandler] Loaded layout "+MainFrame.CONST.gString("LAYOUT")+". "+man.output().size()+" keys mapped.");
        keyMap=man.output().asSimpleSet();
        
        if(!man.loadConfig(MainFrame.fileStorage.getFile(MainFrame.CONST.gString("PLAYOUT")+".kl").getAbsolutePath()))
            Const.LOGGER.warning("[InputEventHandler] Failed to load layout '"+MainFrame.CONST.gString("PLAYOUT")+"'!");
        else 
            Const.LOGGER.info("[InputEventHandler] Loaded layout "+MainFrame.CONST.gString("PLAYOUT")+". "+man.output().size()+" keys mapped.");
        playerMap=man.output().asSimpleSet();
    }

    public SimpleSet<String,String> getPlayerMap(){
        return playerMap;
    }
    public int getPlayerKey(String name){
        return Integer.parseInt(playerMap.get(name));
    }

    public void lockKeyboard(boolean b){lockKeyboard=b;}
    public void lockMouse(boolean b){lockMouse=b;}
    public void clear(){klisteners.clear();mlisteners.clear();}

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

    public boolean isKeyDown(int key){
        return downKeys.contains(key);
    }

    public void triggerMouseEvent(){
        if(!Mouse.isInsideWindow()||!Mouse.isCreated())return;
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
        if(lockKeyboard||!Keyboard.isCreated())return;
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

    public String parseKeyToText(int key){
        if(keyMap.containsKey(key+"")){
            if(keyMap.get(key+"").length()>4&&
               MainFrame.ieh.isKeyDown(Keyboard.KEY_LMENU)&&MainFrame.ieh.isKeyDown(Keyboard.KEY_LCONTROL))
                                                            return keyMap.get(key+"").split(" ")[2];
            else if(keyMap.get(key+"").length()>2&&
               Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))     return keyMap.get(key+"").split(" ")[1];
            else                                            return keyMap.get(key+"").split(" ")[0];
        }
        if(keyMap.containsKey(Keyboard.getKeyName(key))){
            if(keyMap.get(Keyboard.getKeyName(key)).length()>4&&
               MainFrame.ieh.isKeyDown(Keyboard.KEY_LMENU)&&MainFrame.ieh.isKeyDown(Keyboard.KEY_LCONTROL))
                                                            return keyMap.get(Keyboard.getKeyName(key)).split(" ")[2];
            else if(keyMap.get(Keyboard.getKeyName(key)).length()>2&&
               Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))     return keyMap.get(Keyboard.getKeyName(key)).split(" ")[1];
            else                                            return keyMap.get(Keyboard.getKeyName(key)).split(" ")[0];
        }
        if((key>=0x10&&key<=0x19)||(key>=0x1E&&key<=0x26)||(key>=0x2C&&key<=0x32)||(key>=0x2&&key<=0xB)){
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_CAPITAL))
                return Keyboard.getKeyName(key);
            else return Keyboard.getKeyName(key).toLowerCase();
        }
        return "";
    }
}
