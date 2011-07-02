/**********************\
  file: World
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import NexT.util.SimpleSet;
import block.Block;
import entity.Entity;
import java.util.ArrayList;
import transcend.Const;
import transcend.MainFrame;

public class World {
    ArrayList<Integer> ids              = new ArrayList<Integer>();
    SimpleSet<Integer,Block> blocks     = new SimpleSet<Integer,Block>();
    SimpleSet<Integer,Entity> entities  = new SimpleSet<Integer,Entity>();

    public World(){}

    public void printWorldStats(){
        Const.LOGGER.info("[World] World incorporates "+blocks.size()+" blocks and "+entities.size()+" entities.");
    }

    public int blockSize(){return blocks.size();}
    public int entitySize(){return entities.size();}
    public int size(){return ids.size();}

    public int getID(int i){return ids.get(i);}

    public int addBlock(Block block){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        block.wID=nID;
        blocks.put(nID, block);
        return nID;
    }

    public int addEntity(Entity entity){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        entity.wID=nID;
        entities.put(nID, entity);
        return nID;
    }

    public void addElement(Element element){
        if(element.ELEMENT_ID>Block.ELEMENT_ID)addBlock((Block)element);else
        if(element.ELEMENT_ID>Entity.ELEMENT_ID)addEntity((Entity)element);
    }

    public boolean isBlock(int identifier){return blocks.containsKey(identifier);}
    public boolean isEntity(int identifier){return entities.containsKey(identifier);}

    public Element getByID(int identifier){
        if(blocks.containsKey(identifier))return blocks.get(identifier);
        if(entities.containsKey(identifier))return entities.get(identifier);
        return null;
    }
    
    public void delByID(int identifier){
        if(blocks.containsKey(identifier)){blocks.remove(identifier);ids.remove((Object)identifier);}
        if(entities.containsKey(identifier)){entities.remove(identifier);ids.remove((Object)identifier);}
    }

    public void update(){
        for(int i=0;i<blocks.size();i++){
            blocks.getAt(i).update();
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).update();
        }
    }

    public void draw(){

        for(int j=-10;j<10;j++){
            for(int i=0;i<blocks.size();i++){
                if(blocks.getAt(i).z==j)blocks.getAt(i).draw();
            }
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).draw();
        }
    }
}
