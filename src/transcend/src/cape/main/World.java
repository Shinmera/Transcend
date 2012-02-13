/**********************\
  file: World
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package cape.main;

import NexT.util.SimpleSet;
import cape.physics.Block;
import cape.physics.Entity;
import java.util.ArrayList;
import transcend.main.Const;
import transcend.world.BElement;

public class World {
    ArrayList<Integer> ids              = new ArrayList<Integer>();
    SimpleSet<Integer,Block> blocks     = new SimpleSet<Integer,Block>();
    SimpleSet<Integer,Entity> entities  = new SimpleSet<Integer,Entity>();

    public World(){}

    public void printWorldStats(){
        Const.LOGGER.info("[World] World incorporates "+blocks.size()+" blocks, "+entities.size()+" entities.");
    }

    public int blockSize(){return blocks.size();}
    public int entitySize(){return entities.size();}
    public int size(){return ids.size();}
    public void clear(){
        blocks.clear();
        entities.clear();
        ids.clear();
    }

    public int getID(int i){return ids.get(i);}
    public Object[] getBlockList(){return blocks.getList().toArray();}
    public Object[] getEntityList(){return entities.getList().toArray();}

    public double getDistance(int a,int b){
        return Math.sqrt(Math.pow(getByID(a).x-getByID(b).x,2)+Math.pow(getByID(a).y-getByID(b).y,2));
    }

    public int addBlock(Block block){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        block.wID=nID;
        block.init();
        blocks.put(nID, block);
        return nID;
    }

    public int addEntity(Entity entity){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        entity.wID=nID;
        entity.init();
        entities.put(nID, entity);
        return nID;
    }

    public void addElement(BElement element){
        if(element.ELEMENT_ID>=Block.ELEMENT_ID)addBlock((Block)element);else
        if(element.ELEMENT_ID>=Entity.ELEMENT_ID)addEntity((Entity)element);
    }

    public boolean isBlock(int wID){return blocks.containsKey(wID);}
    public boolean isEntity(int wID){return entities.containsKey(wID);}
    public boolean contains(int wID){return ids.contains(wID);}

    public BElement getByID(int wID){
        if(blocks.containsKey(wID))return blocks.get(wID);
        if(entities.containsKey(wID))return entities.get(wID);
        return null;
    }
    
    public void delByID(int wID){
        if(blocks.containsKey(wID)){blocks.remove(wID);ids.remove((Object)wID);}
        if(entities.containsKey(wID)){entities.remove(wID);ids.remove((Object)wID);}
    }

    public void update(){
        for(int i=0;i<entities.size();i++){
            entities.getAt(i).update();
        }
    }

    public void draw(){
        for(int i=0;i<blocks.size();i++){
            blocks.getAt(i).draw();
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).draw();
        }
    }
}
