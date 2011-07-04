/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import graph.Animation;
import world.BElement;

public class Tile extends BElement{
    public static final int ELEMENT_ID = 0x101;

    public Animation drawable = new Animation();

    public Tile(){}
    public Tile(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void draw(){
        drawable.draw((int)x,(int)y,w,h);
    }
}
