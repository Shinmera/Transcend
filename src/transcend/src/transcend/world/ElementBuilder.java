/**********************\
  file: ElementBuilder
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

import NexT.util.ClassPathHacker;
import NexT.util.ConfigManager;
import NexT.util.SimpleSet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import transcend.block.*;
import transcend.entity.EnemyB1;
import transcend.entity.EnemyC1;
import transcend.entity.RigidBody;
import transcend.gui.CameraPath;
import transcend.main.Const;
import transcend.main.MainFrame;
import transcend.particle.Emitter;
import transcend.tile.*;

public class ElementBuilder {
    HashMap<String,Class> elements = new HashMap<String,Class>();
    SimpleSet<String,String> elementData = new SimpleSet<String,String>();

    public ElementBuilder(){}

    public boolean loadElement(String name){
        if(elements.containsKey(name))return true;
        try {
            File f = MainFrame.fileStorage.getFile(name);
            ClassPathHacker.addFile(f);
            f = new File(f.getAbsolutePath()+".cfg");
            if(!f.exists()){
                Const.LOGGER.log(Level.WARNING,"Failed to load block "+name+". Config not found.");
                return false;
            }
            ConfigManager man = new ConfigManager(name);
            if(!man.loadConfig(f.getAbsolutePath())){
                Const.LOGGER.log(Level.WARNING,"Failed to load block "+name+". Config read error.");
                return false;
            }
            try{
                Class aclass = MainFrame.class.getClassLoader().loadClass(name);
                elements.put(name, aclass);
                elementData = man.output().asSimpleSet();
            }catch(Exception ex){
                Const.LOGGER.log(Level.WARNING,"Failed to instantiate block "+name+".",ex);
            }
        } catch (IOException ex) {
            Const.LOGGER.log(Level.WARNING,"Failed to load block "+name+". ClassPathHacker failed.",ex);
            return false;
        }

        return true;
    }

    public void buildElement(String name,HashMap<String,String> args){
        name = name.trim().toLowerCase();
        //HARD CODED BLOCKS
        if(name.equals("tileblock")){
            TileBlock block = new TileBlock();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("fitblock")){
            FitBlock block = new FitBlock();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("movingblock")){
            MovingBlock block = new MovingBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("pushableblock")){
            PushableBlock block = new PushableBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("complexblock")){
            ComplexBlock block = new ComplexBlock();
            block.setOptions(args);
            block.determineCoordinates();
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("blankblock")){
            BlankBlock block = new BlankBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("halfblankblock")){
            HalfBlankBlock block = new HalfBlankBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("nullblock")){
            NullBlock block = new NullBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("windblock")){
            WindBlock block = new WindBlock();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("water")){
            Water block = new Water();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("background")){
            Background block = new Background();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("tileset")){
            TileSet block = new TileSet();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("soundemitter")){
            SoundEmitter block = new SoundEmitter();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("emitter")){
            Emitter block = new Emitter();
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("rigidbody")){
            RigidBody entity = new RigidBody();
            entity.setOptions(args);
            MainFrame.world.addEntity(entity);
        }
        else if(name.equals("enemyb1")){
            EnemyB1 entity = new EnemyB1();
            entity.setOptions(args);
            MainFrame.world.addEntity(entity);
        }
        else if(name.equals("enemyc1")){
            EnemyC1 entity = new EnemyC1();
            entity.setOptions(args);
            MainFrame.world.addEntity(entity);
        }
        else if(name.equals("camerapath")){
            CameraPath path = new CameraPath();
            path.setOptions(args);
            MainFrame.world.addTile(path,args.get("name"));
        }
        else if(name.equals("gameevent")){
            GameEvent block = new GameEvent();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("info")){
            Info block = new Info();
            block.setOptions(args);
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("daycycle")){
            DayCycle block = new DayCycle();
            block.setOptions(args);
            MainFrame.world.addTile(block,"daycycle");
        }
        //ATTEMPT TO DYNAMICALLY LOAD BLOCK
        /*else{
            if(loadElement(name)){
                try {
                    Element block = (Element) elements.get(name).newInstance();
                    block.setPosition(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")));
                    block.setSize(Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
                    if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
                    block.setOptions(args);
                    MainFrame.world.addElement(block);
                } catch (Exception ex) {
                    Const.LOGGER.log(Level.WARNING,"Failed to instantiate block '"+name+"'.",ex);
                }
            }
        }*/
    }
}
