/**********************\
  file: ElementBuilder
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import NexT.util.ClassPathHacker;
import NexT.util.ConfigManager;
import NexT.util.SimpleSet;
import block.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
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
            MainFrame.world.addBlock(block);
        }
        else if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            block.setColor(arguments.get("color"));
            MainFrame.world.addBlock(block);
        }
        //ATTEMPT TO DYNAMICALLY LOAD BLOCK
        else{
            if(loadElement(name)){
                try {
                    Element block = (Element) elements.get(name).newInstance();
                    block.setPosition(Integer.parseInt(arguments.get("x")),Integer.parseInt(arguments.get("y")));
                    block.setSize(Integer.parseInt(arguments.get("w")),Integer.parseInt(arguments.get("h")));
                    block.setOptions(arguments.keySet().toArray(new String[arguments.keySet().size()]),
                                     arguments.values().toArray(new String[arguments.keySet().size()]));
                    MainFrame.world.addElement(block);
                } catch (Exception ex) {

                }
            }
        }
    }
}
