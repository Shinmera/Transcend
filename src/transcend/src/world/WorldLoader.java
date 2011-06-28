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
import transcend.MainFrame;

public class WorldLoader {

    public WorldLoader(){
        
    }

    public boolean loadWorld(File file){
        if(!file.exists())return false;
        boolean skip = false,inBlock = false,inMulti = false;
        int line = 1,elementsLoaded=0;
        HashMap<String,String> arguments = new HashMap<String,String>();
        String type = "";
        try{
            Const.LOGGER.info("[World] Loading World from "+file.getAbsolutePath());

            BufferedReader in = new BufferedReader(new FileReader(file));
            String read = in.readLine();
            if(!read.equals("#transcend world file")){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Invalid header");return false;}
            while((read = in.readLine()) != null){
                if(read.contains("#"))read = read.substring(0,read.indexOf("#"));
                if(read.contains("//"))read = read.substring(0,read.indexOf("//"));
                if(read.contains("/*"))skip = true;
                if(read.contains("*/"))skip = false;
                read = read.trim();

                if(!skip&&read.length() != 0){
                    //READ IN BLOCKS.
                    if(!inBlock&&read.contains("{")){
                        inBlock = true;
                        type = read.substring(0,read.indexOf("{")).trim();
                        read="";
                    }
                    if(inBlock){
                        if(read.contains("}")){
                            inBlock = false;
                            read = read.substring(0,read.indexOf("}"));
                        }
                        
                        read = read.trim();
                        if(read.length() != 0){ //value.
                            if(!inMulti){
                                if(!read.contains(":")){Const.LOGGER.log(Level.SEVERE, "Failed to load World: Parse error on line {0}", line);return false;}
                                String key = read.substring(0,read.indexOf(":"));
                                String val = read.substring(read.indexOf(":")+1);
                                arguments.put(key.trim(), val.trim());
                            }
                        }

                        if(!inBlock){ //End of block reached.
                            elementsLoaded++;
                            MainFrame.elementBuilder.buildElement(type, arguments);
                        }
                    }
                }
                line++;
            }
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Read exception",e);}
        Const.LOGGER.info("[World] Loaded "+elementsLoaded+" elements.");
        return true;
    }
}
