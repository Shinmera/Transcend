/**********************\
  file: ElementBuilder
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import tile.ColorBlock;
import tile.StoneBlock;
import tile.GrassBlock;
import tile.DirtBlock;
import tile.Background;
import NexT.util.ClassPathHacker;
import NexT.util.ConfigManager;
import NexT.util.SimpleSet;
import block.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import tile.BrickBlock;
import tile.SoundEmitter;
import tile.TileSet;
import transcend.Const;
import transcend.MainFrame;

public class ElementBuilder {
    HashMap<String,Class> elements = new HashMap<String,Class>();
    SimpleSet<String,String> elementData = new SimpleSet<String,String>();

    public ElementBuilder(){
        
    }

    public boolean loadElement(String name){
        if(elements.containsKey(name))return true;
        try {
            File f = new File(MainFrame.basedir + "world" + File.separator + "elements" + File.separator + name);
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

    public void buildElement(String name,HashMap<String,String> arguments){
        name = name.trim().toLowerCase();
        //HARD CODED BLOCKS
        if(name.equals("dirtblock")){
            DirtBlock block = new DirtBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("grassblock")){
            GrassBlock block = new GrassBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("stoneblock")){
            StoneBlock block = new StoneBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("brickblock")){
            BrickBlock block = new BrickBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("blankblock")){
            BlankBlock block = new BlankBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("halfblankblock")){
            HalfBlankBlock block = new HalfBlankBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            block.setColor(arguments.get("color"));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("background")){
            Background block = new Background(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")),
                              arguments.get("tex"));
            if(arguments.containsKey("vsp"))block.setVSP(Double.parseDouble(arguments.get("vsp")));
            if(arguments.containsKey("tile"))block.setTiled(Boolean.parseBoolean(arguments.get("tile")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("tileset")){
            TileSet block = new TileSet(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")),
                              arguments.get("tex"));
            if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
            if(arguments.containsKey("u"))block.setU(Integer.parseInt(arguments.get("u")));
            if(arguments.containsKey("v"))block.setV(Integer.parseInt(arguments.get("v")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("soundemitter")){
            SoundEmitter block = new SoundEmitter(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")));
            block.setOptions(arguments);
            MainFrame.world.addTile(block);
        }
        //ATTEMPT TO DYNAMICALLY LOAD BLOCK
        else{
            if(loadElement(name)){
                try {
                    Element block = (Element) elements.get(name).newInstance();
                    block.setPosition(Integer.parseInt(arguments.get("x")),Integer.parseInt(arguments.get("y")));
                    block.setSize(Integer.parseInt(arguments.get("w")),Integer.parseInt(arguments.get("h")));
                    if(arguments.containsKey("z"))block.setLayer(Integer.parseInt(arguments.get("z")));
                    block.setOptions(arguments);
                    MainFrame.world.addElement(block);
                } catch (Exception ex) {
                    Const.LOGGER.log(Level.WARNING,"Failed to instantiate block '"+name+"'.",ex);
                }
            }
        }
    }
}
