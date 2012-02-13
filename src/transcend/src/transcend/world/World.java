/**********************\
  file: World
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

import NexT.util.SimpleSet;
import transcend.block.Block;
import transcend.entity.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import transcend.tile.Tile;
import transcend.main.Const;

public class World {
    ArrayList<Integer> ids              = new ArrayList<Integer>();
    SimpleSet<Integer,Block> blocks     = new SimpleSet<Integer,Block>();
    SimpleSet<Integer,Entity> entities  = new SimpleSet<Integer,Entity>();
    SimpleSet<Integer,Tile> tiles       = new SimpleSet<Integer,Tile>();
    HashMap<String,Integer> index       = new HashMap<String,Integer>();

    public World(){}

    public void printWorldStats(){
        Const.LOGGER.info("[World] World incorporates "+blocks.size()+" blocks, "+entities.size()+" entities and "+tiles.size()+" tiles.");
    }

    public int blockSize(){return blocks.size();}
    public int entitySize(){return entities.size();}
    public int tileSize(){return tiles.size();}
    public int size(){return ids.size();}
    public void clear(){
        blocks.clear();
        entities.clear();
        tiles.clear();
        ids.clear();
        index.clear();
    }

    public int getID(int i){return ids.get(i);}
    public int getID(String name){if(index.containsKey(name))return index.get(name);else return -1;}
    public Object[] getBlockList(){return blocks.getList().toArray();}
    public Object[] getEntityList(){return entities.getList().toArray();}
    public Object[] getTileList(){return tiles.getList().toArray();}

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

    public int addTile(Tile tile){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        tile.wID=nID;
        tile.init();
        tiles.put(nID, tile);
        return nID;
    }

    public int addBlock(Block block,String name){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        block.wID=nID;
        block.init();
        blocks.put(nID, block);
        index.put(name, nID);
        return nID;
    }

    public int addEntity(Entity entity,String name){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        entity.wID=nID;
        entity.init();
        entities.put(nID, entity);
        index.put(name, nID);
        return nID;
    }

    public int addTile(Tile tile,String name){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        tile.wID=nID;
        tile.init();
        tiles.put(nID, tile);
        index.put(name, nID);
        return nID;
    }

    public void addElement(BElement element){
        if(element.ELEMENT_ID>=Block.ELEMENT_ID)addBlock((Block)element);else
        if(element.ELEMENT_ID>=Entity.ELEMENT_ID)addEntity((Entity)element);else
        if(element.ELEMENT_ID>=Tile.ELEMENT_ID)addTile((Tile)element);
    }

    public boolean isBlock(int wID){return blocks.containsKey(wID);}
    public boolean isEntity(int wID){return entities.containsKey(wID);}
    public boolean isTile(int wID){return tiles.containsKey(wID);}
    public boolean contains(String name){return index.containsKey(name);}
    public boolean contains(int wID){return ids.contains(wID);}

    public BElement getByID(int wID){
        if(blocks.containsKey(wID))return blocks.get(wID);
        if(entities.containsKey(wID))return entities.get(wID);
        if(tiles.containsKey(wID))return tiles.get(wID);
        return null;
    }

    public BElement getByName(String name){
        int wID = getID(name);
        if(blocks.containsKey(wID))return blocks.get(wID);
        if(entities.containsKey(wID))return entities.get(wID);
        if(tiles.containsKey(wID))return tiles.get(wID);
        return null;
    }
    
    public void delByID(int wID){
        if(blocks.containsKey(wID)){blocks.remove(wID);ids.remove((Object)wID);}
        if(entities.containsKey(wID)){entities.remove(wID);ids.remove((Object)wID);}
        if(tiles.containsKey(wID)){tiles.remove(wID);ids.remove((Object)wID);}
    }

    public void delByName(String name){
        int wID = getID(name);
        if(blocks.containsKey(wID)){blocks.remove(wID);ids.remove((Object)wID);}
        if(entities.containsKey(wID)){entities.remove(wID);ids.remove((Object)wID);}
        if(tiles.containsKey(wID)){tiles.remove(wID);ids.remove((Object)wID);}
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
        for(int j=-5;j<=0;j++){
            for(int i=0;i<tiles.size();i++){
                if(tiles.getAt(i).z==j)tiles.getAt(i).draw();
            }
        }
        for(int j=-5;j<=0;j++){
            for(int i=0;i<blocks.size();i++){
                if(blocks.getAt(i).z==j)blocks.getAt(i).draw();
            }
        }

        for(int i=0;i<entities.size();i++){
            entities.getAt(i).draw();
        }

        for(int j=1;j<=5;j++){
            for(int i=0;i<tiles.size();i++){
                if(tiles.getAt(i).z==j)tiles.getAt(i).draw();
            }
        }
        for(int j=1;j<=5;j++){
            for(int i=0;i<blocks.size();i++){
                if(blocks.getAt(i).z==j){
                    blocks.getAt(i).draw();
                }
            }
        }
    }
}
