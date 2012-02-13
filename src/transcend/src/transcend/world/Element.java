/**********************\
  file: Element
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

import NexT.util.Toolkit;
import transcend.entity.Entity;
import transcend.graph.Animation;
import transcend.main.MainFrame;

public class Element extends BElement{
    public static final int ELEMENT_ID = 0x0;
    public Animation drawable = new Animation();
    public double solid=1.0;
    public double health=100;
    
    public Element(){}

    public Animation getDrawable(){return drawable;}
    public double getSolid(){return solid;}
    public double getHealth(){return health;}
    public boolean isBaseElement(){return false;}

    public void setHealth(double health){this.health=health;}
    public void setSolid(double s){this.solid = s;}
    public void setDrawable(Animation drawable){this.drawable = drawable;}
    public void setSize(int w,int h){super.setSize(w,h);drawable.calcTile(w, h);}

    public void draw(){drawable.draw((int)x,(int)y,w,h);}
    
    public boolean checkInside(double ax,double ay,boolean solid){
        if(this.solid==0&&solid)return false;
        return checkInside(ax,ay);
    }
    public boolean checkInside(double ax,double ay,double minSolid){
        if(this.solid<minSolid)return false;
        return checkInside(ax,ay);
    }
    
    public Element check(double ax,double ay,double bx,double by){
        return check(ax,ay,bx,by,0.2);
    }
    
    public Element check(double ax,double ay,double bx,double by,double solidity){
        Element e=null;
        Object[] blockIDs = Toolkit.joinArray(MainFrame.world.getBlockList(),MainFrame.world.getEntityList());
        for(int i=0;i<blockIDs.length;i++){
            Element block = (Element)MainFrame.world.getByID((Integer)blockIDs[i]);
            if((block.wID!=wID)&&((block.checkInside(ax,ay)||block.checkInside(bx,by))&&block.solid>=solidity)){
                e=block;
                break;
            }
        }
        return e;
    }

    public static Entity checkEntity(int wID,double ax,double ay,double bx,double by){
        Entity e=null;
        Object[] entIDs = MainFrame.world.getEntityList();
        for(int i=0;i<entIDs.length;i++){
            if((Integer)entIDs[i]!=wID){
                Entity block = (Entity)MainFrame.world.getByID((Integer)entIDs[i]);
                if((block.checkInside(ax,ay)||block.checkInside(bx,by))){
                    e=block;
                    break;
                }
            }
        }
        return e;
    }
}