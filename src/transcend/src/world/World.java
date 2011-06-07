/**********************\
  file: World
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import block.Block;
import entity.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class World {
    public static final Logger LOGGER = Logger.getLogger("TRA-World");
    ArrayList<Integer> ids              = new ArrayList<Integer>();
    HashMap<Integer,Block> blocks     = new HashMap<Integer,Block>();
    HashMap<Integer,Entity> entities  = new HashMap<Integer,Entity>();

    public World(){
        
    }

    public void printWorldStats(){
        LOGGER.info("[World] World incorporates "+blocks.size()+" blocks and "+entities.size()+" entities.");
    }

    public void addBlock(Block block){
        blocks.put(ids.size(), block);
        ids.add(ids.size());
    }

    public void addEntity(Entity entity){
        entities.put(ids.size(), entity);
        ids.add(ids.size());
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
        if(blocks.containsKey(identifier))blocks.remove(identifier);
        if(entities.containsKey(identifier))entities.remove(identifier);
    }

    public void update(){
        for(int i=0;i<blocks.size();i++){
            blocks.get(i).update();
        }

        for(int i=0;i<entities.size();i++){
            entities.get(i).update();
        }
    }

    public void draw(){
        for(int i=0;i<blocks.size();i++){
            blocks.get(i).draw();
        }

        for(int i=0;i<entities.size();i++){
            entities.get(i).draw();
        }
    }
}
