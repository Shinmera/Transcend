/**********************\
  file: World
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.procedure.TObjectProcedure;
import static org.lwjgl.opengl.GL11.*;
import transcend.block.Block;
import transcend.entity.Entity;
import transcend.main.Const;
import transcend.tile.Tile;

public class World {
    private final UpdateProcedure updateProc            = new UpdateProcedure();
    private final DrawProcedure drawProc                = new DrawProcedure();
    private final DrawProcedureLayered drawProcLayered  = new DrawProcedureLayered();
    private final TIntArrayList ids                     = new TIntArrayList();
    private final THashMap<String,Integer> index        = new THashMap<String,Integer>();
    private final THashMap<Integer,Block> blocks        = new THashMap<Integer,Block>();
    private final THashMap<Integer,Entity> entities     = new THashMap<Integer,Entity>();
    private final THashMap<Integer,Tile> tiles          = new THashMap<Integer,Tile>();
    private static int currentDrawLayer = 0;

    public World(){}

    public void printWorldStats(){
        Const.LOGGER.info("[World] World incorporates "+blocks.size()+" blocks, "+entities.size()+" entities and "+tiles.size()+" tiles.");
    }

    public int blockSize(){return blocks.size();}
    public int entitySize(){return entities.size();}
    public int tileSize(){return tiles.size();}
    public int size(){return ids.size();}
    public void clear(){
        synchronized(blocks){blocks.clear();}
        synchronized(entities){entities.clear();}
        synchronized(tiles){tiles.clear();}
        ids.clear();
        index.clear();
    }

    public int getID(int i){return ids.get(i);}
    public int getID(String name){if(index.containsKey(name))return index.get(name);else return -1;}
    public Block[] getBlockList()       {synchronized(blocks){      return blocks.values().toArray(     new Block[blocks.size()]);}}
    public Entity[] getEntityList()     {synchronized(entities){    return entities.values().toArray(   new Entity[blocks.size()]);}}
    public Tile[] getTileList()         {synchronized(tiles){       return tiles.values().toArray(      new Tile[blocks.size()]);}}
    public Integer[] getBlockIDList()   {synchronized(blocks){      return blocks.keySet().toArray(     new Integer[blocks.size()]);}}
    public Integer[] getEntityIDList()  {synchronized(entities){    return entities.keySet().toArray(   new Integer[blocks.size()]);}}
    public Integer[] getTileIDList()    {synchronized(tiles){       return tiles.keySet().toArray(      new Integer[blocks.size()]);}}

    public double getDistance(int a,int b){
        return Math.sqrt(Math.pow(getByID(a).x-getByID(b).x,2)+Math.pow(getByID(a).y-getByID(b).y,2));
    }

    public int addBlock(Block block){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        block.wID=nID;
        block.init();
        synchronized(blocks){blocks.put(nID, block);}
        return nID;
    }

    public int addEntity(Entity entity){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        entity.wID=nID;
        entity.init();
        synchronized(entities){entities.put(nID, entity);}
        return nID;
    }

    public int addTile(Tile tile){
        int nID=0;if(ids.size()>0)nID=ids.get(ids.size()-1)+1;
        ids.add(nID);
        tile.wID=nID;
        tile.init();
        synchronized(tiles){tiles.put(nID, tile);}
        return nID;
    }

    public int addBlock(Block block,String name){
        int nID=addBlock(block);
        index.put(name, nID);
        return nID;
    }

    public int addEntity(Entity entity,String name){
        int nID=addEntity(entity);
        index.put(name, nID);
        return nID;
    }

    public int addTile(Tile tile,String name){
        int nID=addTile(tile);
        index.put(name, nID);
        return nID;
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
        return getByID(wID);
    }
    
    public void delByID(int wID){
        if(blocks.containsKey(wID)){synchronized(blocks){blocks.remove(wID);}ids.remove(wID);return;}
        if(entities.containsKey(wID)){synchronized(entities){entities.remove(wID);}ids.remove(wID);return;}
        if(tiles.containsKey(wID)){synchronized(tiles){tiles.remove(wID);}ids.remove(wID);return;}
    }

    public void delByName(String name){
        int wID = getID(name);
        delByID(wID);
    }

    public void update(){
        synchronized(tiles){tiles.forEachValue(updateProc);}
        synchronized(blocks){blocks.forEachValue(updateProc);}
        synchronized(entities){entities.forEachValue(updateProc);}
    }

    public void draw(){
        for(currentDrawLayer=-5;currentDrawLayer<=0;currentDrawLayer++){
            synchronized(tiles){tiles.forEachValue(drawProcLayered);}
        }
        for(currentDrawLayer=-5;currentDrawLayer<=0;currentDrawLayer++){
            synchronized(blocks){blocks.forEachValue(drawProcLayered);}
        }
        
        synchronized(entities){entities.forEachValue(drawProc);}
        
        for(currentDrawLayer=1;currentDrawLayer<=5;currentDrawLayer++){
            synchronized(blocks){blocks.forEachValue(drawProcLayered);}
        }
        for(currentDrawLayer=1;currentDrawLayer<=5;currentDrawLayer++){
            synchronized(tiles){tiles.forEachValue(drawProcLayered);}
        }
    }
    
    
    final class UpdateProcedure implements TObjectProcedure{
        public boolean execute(Object object) {
            ((BElement)object).update();
            return true;
        }
    }
    
    final class DrawProcedureLayered implements TObjectProcedure{
        public boolean execute(Object object){
            BElement o = (BElement)object;
            if(o.z==currentDrawLayer){
                //glPushMatrix();
                //glTranslated(o.getX(),o.getY(),0);
                o.draw();
                //glPopMatrix();
            }
            return true;
        }
    }
    
    final class DrawProcedure implements TObjectProcedure{
        public boolean execute(Object object) {
            BElement o = (BElement)object;
            //glPushMatrix();
            //glTranslated(o.getX(),o.getY(),0);
            o.draw();
            //glPopMatrix();
            return true;
        }
    }
}
