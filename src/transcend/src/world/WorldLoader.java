/**********************\
  file: WorldLoader
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import transcend.Const;

public class WorldLoader {
    public WorldLoader(){
        
    }

    public boolean loadWorld(File file,World world){
        if(!file.exists())return false;
        try{
            boolean skip = false,inBlock = false,inMulti = false;
            HashMap<String,String> arguments = new HashMap<String,String>();
            String type = "";
            int line = 1;

            BufferedReader in = new BufferedReader(new FileReader(file));
            String read = in.readLine();
            if(!read.equals("#transcend world file")){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Invalid header");return false;}
            while((read = in.readLine()) != null){
                if(read.contains("#"))read = read.substring(0,read.indexOf("#"));
                if(read.contains("//"))read = read.substring(0,read.indexOf("//"));
                if(read.contains("/*"))skip = true;
                if(read.contains("*/"))skip = false;
                read = read.trim();

                if(!skip&&!read.equals("")){
                    //READ IN BLOCKS.
                    if(!inBlock&&read.contains("{")){
                        inBlock = true;
                        type = read.substring(0,read.indexOf("{"));
                    }
                    if(inBlock){
                        if(read.contains("}")){
                            inBlock = false;
                            read = read.substring(0,read.indexOf("}"));
                        }
                        
                        read = read.trim();
                        if(!read.equals("")){ //value.
                            if(!inMulti){
                            if(!read.contains(":")){Const.LOGGER.log(Level.SEVERE, "Failed to load World: Parse error on line {0}", line);return false;}
                                String key = read.substring(0,read.indexOf(":"));
                                String val = read.substring(read.indexOf(":")+1);
                                arguments.put(key.trim(), val.trim());
                            }
                        }

                        if(!inBlock){ //End of block reached.
                            world.addElement(ElementBuilder.buildElement(type, arguments));
                        }
                    }
                }
                line++;
            }
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Read exception",e);}

        return true;
    }
}
