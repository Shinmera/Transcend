/**********************\
  file: World
  package: cape.main
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package cape.main;

import cape.physics.BElement;
import cape.physics.Block;
import cape.physics.Entity;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.procedure.TObjectProcedure;
import static org.lwjgl.opengl.GL11.*;
import transcend.main.Const;

public class World {
    private final UpdateProcedure updateProc = new UpdateProcedure();
    private final DrawProcedure drawProc = new DrawProcedure();
    TIntArrayList ids = new TIntArrayList();
    THashMap<Integer,Block> blocks     = new THashMap<Integer,Block>();
    THashMap<Integer,Entity> entities  = new THashMap<Integer,Entity>();
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
    public Object[] getBlockList(){return blocks.values().toArray();}
    public Object[] getEntityList(){return entities.values().toArray();}
    public THashMap<Integer,Block> getBlockMap(){return blocks;}
    public THashMap<Integer,Entity> getEntityMap(){return entities;}

    public double getDistance(int a,int b){
        return Math.sqrt(Math.pow(getByID(a).getX()-getByID(b).getX(),2)+Math.pow(getByID(a).getY()-getByID(b).getY(),2));
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
        if(blocks.containsKey(wID)){blocks.remove(wID);ids.remove(wID);}
        if(entities.containsKey(wID)){entities.remove(wID);ids.remove(wID);}
    }

    public void update(){
        entities.forEachValue(updateProc);
    }

    public void draw(){
        blocks.forEachValue(drawProc);
        entities.forEachValue(drawProc);
    }
    
    final class UpdateProcedure implements TObjectProcedure{
        public boolean execute(Object object) {
            ((BElement)object).update();
            return true;
        }
    }
    
    final class DrawProcedure implements TObjectProcedure{
        public boolean execute(Object object) {
            BElement o = (BElement)object;
            glPushMatrix();
            glTranslated(o.getX(),o.getY(),0);
            o.draw();
            glPopMatrix();
            return true;
        }
    }
}
