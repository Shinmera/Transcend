/**********************\
  file: Entity
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package entity;

import world.Element;

public class Entity extends Element{
    public static final int ELEMENT_ID = 0x1;

    public static final int STATUS_NONE = 0x0;
    public static final int STATUS_IDLE = 0x1;
    public static final int STATUS_MOVE = 0x2;
    public static final int STATUS_ATTACK = 0x3;
    public static final int STATUS_DEFEND = 0x4;

    public int health;
    public double atk,def;
    public int status;
    
    public Entity(){
        
    }

    public int getHealth(){return health;}
    public double getAttack(){return atk;}
    public double getDefense(){return def;}
    public int getStatus(){return status;}

    public void setHealth(int health){this.health=health;}
    public void setAttack(double atk){this.atk=atk;}
    public void setDefense(double def){this.def=def;}
    public void setStatus(int status){this.status=status;}
}
