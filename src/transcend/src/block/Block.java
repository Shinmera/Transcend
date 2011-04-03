/**********************\
  file: Block
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import world.Element;

public class Block extends Element{
    public static final int ELEMENT_ID = 0x51;

    public double solid;
    public int health;
    public boolean moveable;

    public Block(int x,int y,int w,int h){
        this.x=x;this.y=y;this.z=0;this.w=w;this.h=h;
    }
    
    public Block(int x,int y,int z,int w,int h){
        this.x=x;this.y=y;this.z=z;this.w=w;this.h=h;
    }

    public double getSolid(){return solid;}
    public int getHealth(){return health;}
    public boolean getMoveable(){return moveable;}

    public void setSolid(double s){this.solid = s;}
    public void setHealth(int h){this.health = h;}
    public void setMoveable(boolean m){this.moveable = m; }
}
