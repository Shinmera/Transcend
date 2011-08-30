/**********************\
  file: Block
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import java.util.HashMap;
import world.Element;
import transcend.MainFrame;

public class Block extends Element{
    public static final int ELEMENT_ID = 0x51;

    public boolean moveable;

    public Block(){}
    public Block(int x,int y,int w,int h){
        this.x=x;this.y=y;this.z=0;this.w=w;this.h=h;
    }
    
    public Block(int x,int y,int z,int w,int h){
        this.x=x;this.y=y;this.z=z;this.w=w;this.h=h;
    }

    public boolean getMoveable(){return moveable;}

    public void setMoveable(boolean m){this.moveable = m; }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("tex"))MainFrame.elementBuilder.buildElement("tileblock", options);
    }
}
