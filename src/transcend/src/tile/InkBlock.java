/**********************\
  file: InkBlock.java
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import NexT.util.SimpleSet;
import event.EventListener;
import event.Event;
import java.io.File;
import java.util.HashMap;
import transcend.MainFrame;

public class InkBlock extends Tile implements EventListener{
    public InkBlock(){
        int[] start = {0,0,0,0};
        int[] stop = {0,15,0,0};
        int[] loop = {0,-2,0,0};
        int[] loop2 = {0,0,0,0};
        drawable.loadTexture(MainFrame.fileStorage.getFile("ink_earth"),start,stop,loop,loop2);
        drawable.setReel(0);
        MainFrame.eh.registerEvent(Event.AREA_CLEAR, 9, this);
        w=64;h=64;
    }

    public InkBlock(int x,int y,int w,int h){
        this();
        this.x=x;this.y=y;
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        w=64;h=64;
    }

    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        return set;
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.AREA_CLEAR){
            drawable.setReel(1);
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}
}
