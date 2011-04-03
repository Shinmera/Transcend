/**********************\
  file: ElementBuilder
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import java.util.HashMap;

public class ElementBuilder {

    public ElementBuilder(){
        
    }

    public static Element buildElement(String name,HashMap<String,String> arguments){
        name = name.trim().toLowerCase();
        
        if(name.equals("")){
        
        }

        return new Element();
    }
}
