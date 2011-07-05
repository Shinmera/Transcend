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
import tile.Tile;
import transcend.Const;

public class World {
    ArrayList<Integer> ids              = new ArrayList<Integer>();
    SimpleSet<Integer,Block> blocks     = new SimpleSet<Integer,Block>();
    SimpleSet<Integer,Entity> entities  = new SimpleSet<Integer,Entity>();
    SimpleSet<Integer,Tile> tiles       = new SimpleSet<Integer,Tile>();

    public World(){}

    public void printWorldStats(){
        Const.LOGGER.info("[World] World incorporates "+blocks.size()+" blocks, "+entities.size()+" entities and "+tiles.size()+" tiles.");
    }

    public int blockSize(){return blocks.size();}
    public int entitySize(){return entities.size();}
    public int tileSize(){return tiles.size();}
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

    public int addTile(Tile tile){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        tile.wID=nID;
        tiles.put(nID, tile);
        return nID;
    }

    public void addElement(BElement element){
        if(element.ELEMENT_ID>=Block.ELEMENT_ID)addBlock((Block)element);else
        if(element.ELEMENT_ID>=Entity.ELEMENT_ID)addEntity((Entity)element);else
        if(element.ELEMENT_ID>=Tile.ELEMENT_ID)addTile((Tile)element);
    }

    public boolean isBlock(int identifier){return blocks.containsKey(identifier);}
    public boolean isEntity(int identifier){return entities.containsKey(identifier);}
    public boolean isTile(int identifier){return tiles.containsKey(identifier);}

    public BElement getByID(int identifier){
        if(blocks.containsKey(identifier))return blocks.get(identifier);
        if(entities.containsKey(identifier))return entities.get(identifier);
        if(tiles.containsKey(identifier))return tiles.get(identifier);
        return null;
    }
    
    public void delByID(int identifier){
        if(blocks.containsKey(identifier)){blocks.remove(identifier);ids.remove((Object)identifier);}
        if(entities.containsKey(identifier)){entities.remove(identifier);ids.remove((Object)identifier);}
        if(tiles.containsKey(identifier)){tiles.remove(identifier);ids.remove((Object)identifier);}
    }

    public void update(){
        for(int i=0;i<tiles.size();i++){
            tiles.getAt(i).update();
        }

        for(int i=0;i<blocks.size();i++){
            blocks.getAt(i).update();
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).update();
        }
    }

    public void draw(){
        for(int j=-5;j<=5;j++){
            for(int i=0;i<tiles.size();i++){
                if(tiles.getAt(i).z==j)tiles.getAt(i).draw();
            }
        }
        
        for(int j=-5;j<=5;j++){
            for(int i=0;i<blocks.size();i++){
                if(blocks.getAt(i).z==j)blocks.getAt(i).draw();
            }
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).draw();
        }
    }
}
