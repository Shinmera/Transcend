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
import particle.Emitter;
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

    public void buildElement(String name,HashMap<String,String> args){
        name = name.trim().toLowerCase();
        //HARD CODED BLOCKS
        if(name.equals("dirtblock")){
            DirtBlock block = new DirtBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("grassblock")){
            GrassBlock block = new GrassBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")),args.containsKey("a"));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("stoneblock")){
            StoneBlock block = new StoneBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("brickblock")){
            BrickBlock block = new BrickBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("blankblock")){
            BlankBlock block = new BlankBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("halfblankblock")){
            HalfBlankBlock block = new HalfBlankBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            block.setColor(args.get("color"));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("background")){
            Background block = new Background(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")),args.get("tex"));
            if(args.containsKey("vsp"))block.setVSP(Double.parseDouble(args.get("vsp")));
            if(args.containsKey("tile"))block.setTiled(Boolean.parseBoolean(args.get("tile")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("tileset")){
            TileSet block = new TileSet(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")),Integer.parseInt(args.get("w")),Integer.parseInt(args.get("h")),args.get("tex"));
            if(args.containsKey("z"))block.setLayer(Integer.parseInt(args.get("z")));
            if(args.containsKey("u"))block.setU(Integer.parseInt(args.get("u")));
            if(args.containsKey("v"))block.setV(Integer.parseInt(args.get("v")));
            if(args.containsKey("s"))block.setS(Double.parseDouble(args.get("s")));
            MainFrame.world.addTile(block);
        }
        else if(name.equals("soundemitter")){
            SoundEmitter block = new SoundEmitter(Integer.parseInt(args.get("x")),Integer.parseInt(args.get("y")));
            block.setOptions(args);
            MainFrame.world.addTile(block);
        }
        else if(name.equals("emitter")){
            Emitter block = new Emitter(Double.parseDouble(args.get("x")),Double.parseDouble(args.get("y")));
            if(args.containsKey("mlife"))block.setMaxLife(Double.parseDouble(args.get("mlife")));
            if(args.containsKey("mpart"))block.setMaxParticles(Integer.parseInt(args.get("mpart")));
            if(args.containsKey("spray"))block.setSpray(Integer.parseInt(args.get("spray")));
            MainFrame.particlePool.addEmitter(block);
        }
        //ATTEMPT TO DYNAMICALLY LOAD BLOCK
        else{
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
        }
    }
}
