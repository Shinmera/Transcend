/**********************\
  file: ElementBuilder
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import block.*;
import entity.*;
import java.util.HashMap;
import transcend.MainFrame;

public class ElementBuilder {

    public ElementBuilder(){
        
    }

    public static void buildElement(String name,HashMap<String,String> arguments){
        name = name.trim().toLowerCase();

        if(name.equals("dirtblock")){
            DirtBlock block = new DirtBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            MainFrame.world.addBlock(block);
        }
        if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock(Integer.parseInt(arguments.get("x")),
                              Integer.parseInt(arguments.get("y")),
                              Integer.parseInt(arguments.get("w")),
                              Integer.parseInt(arguments.get("h")));
            block.setColor(arguments.get("color"));
            MainFrame.world.addBlock(block);
        }
    }
}
