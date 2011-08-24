/**********************\
  file: Entity
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import block.Block;
import transcend.MainFrame;
import world.Element;

public class Entity extends Element{
    public static final int ELEMENT_ID = 0x1;

    public static final int STATUS_NONE = 0x0;
    public static final int STATUS_IDLE = 0x1;
    public static final int STATUS_MOVE = 0x2;
    public static final int STATUS_JUMP = 0x3;
    public static final int STATUS_ATTACK = 0x4;
    public static final int STATUS_DEFEND = 0x5;
    public static final int STATUS_DIE = 0xF;

    public double atk,def,vx,vy;
    public int status=STATUS_NONE;
    public Block ground;
    
    public Entity(){}

    public double getAttack(){return atk;}
    public double getDefense(){return def;}
    public int getStatus(){return status;}
    public Block getGround(){return ground;}

    public void setAttack(double atk){this.atk=atk;}
    public void setDefense(double def){this.def=def;}
    public void setStatus(int status){this.status=status;}

    public void update(){
        drawable.update();
    }

    public void draw(){
        drawable.draw((int)x,(int)y,w,h);
    }

    public static Element check(double ax,double ay,double bx,double by){
        Element e=null;
        Object[] blockIDs = MainFrame.world.getBlockList();
        for(int i=0;i<blockIDs.length;i++){
            Block block = (Block)MainFrame.world.getByID((Integer)blockIDs[i]);
            if(block.checkInside(ax,ay)||block.checkInside(bx,by)){
                e=block;
                break;
            }
        }
        return e;
    }
}
