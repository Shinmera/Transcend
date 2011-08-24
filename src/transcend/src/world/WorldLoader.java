/**********************\
  file: WorldLoader
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package world;

import NexT.util.SimpleSet;
import NexT.util.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import transcend.Const;
import transcend.MainFrame;

public class WorldLoader {
    private File loaded = null;
    public WorldLoader(){
        
    }

    public File getLoaded(){return loaded;}
    public boolean isLoaded(){if(loaded!=null)return true;else return false;}

    public boolean saveGame(File file){
        Const.LOGGER.info("[World] Save Game State to "+file.getAbsolutePath());
        StringBuilder buf = new StringBuilder();
        //INSERT PLAYER STATE

        buf.append("world=").append(loaded.getName()).append(";");
        SimpleSet<String,String> playerState = MainFrame.player.getStateData();
        for(int i=0;i<playerState.size();i++)
            buf.append(playerState.getKey(i)).append("=").append(playerState.getAt(i)).append(";");

        //ENCRYPT
        try {
            FileOutputStream out = new FileOutputStream(file);
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(buf.toString().getBytes());
            gzip.flush();
            gzip.close();
        } catch (Exception e) {Const.LOGGER.log(Level.SEVERE,"Failed to save Game State: Write Exception",e);return false;}
        return true;
    }

    public boolean loadGame(File file){
        if(!file.exists())return false;
        HashMap<String,String> arguments = new HashMap<String,String>();
        try{
            Const.LOGGER.info("[World] Loading Game State from "+file.getAbsolutePath());

            GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
            Reader decoder = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(decoder);
            StringBuilder buf = new StringBuilder();

            //DECRYPT
            String read="";
            while((read=br.readLine())!=null)buf.append(read+"\n");
            //SET PLAYER STATE
            HashMap<String,String> map = Toolkit.stringToMap(buf.toString(),";","=");
            loadWorld(new File(MainFrame.basedir,"world"+File.separator+map.get("world")));
            MainFrame.player.setStateData(map);
            
        }catch(Exception e){Const.LOGGER.log(Level.SEVERE,"Failed to load Game State: Read exception",e);return false;}
        return true;
    }

    public boolean saveWorld(File file){
        try{
            Const.LOGGER.info("[World] Save World to "+file.getAbsolutePath());
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("#!transcend world file");

            for(int i=0;i<MainFrame.world.size();i++){
                BElement e = MainFrame.world.getByID(MainFrame.world.getID(i));
                if(!e.getClass().getName().equals("entity.Player")){
                pw.println(e.getClass().getName().substring(e.getClass().getName().indexOf(".")+1)+"{");
                pw.println("x: "+(int)e.getX());
                pw.println("y: "+(int)e.getY());
                pw.println("z: "+(int)e.getZ());
                pw.println("w: "+e.getWidth());
                pw.println("h: "+e.getHeight());

                SimpleSet s = e.getOptions();
                if(s!=null){
                    for(int j=0;j<s.size();j++)
                        pw.println(s.getKey(j)+": "+s.getAt(j));
                }

                pw.println("}\n");
                }
            }
            pw.flush();
            pw.close();
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to save World: Write exception",e);}
        Const.LOGGER.info("[World] Saved "+MainFrame.world.size()+" elements.");

        return true;
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
            if((read==null)||(!read.equals("#!transcend world file"))){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Invalid header");return false;}
            MainFrame.world.clear();
            int id = MainFrame.world.addEntity(MainFrame.player);
            MainFrame.camera.follow(id);
            while((read = in.readLine()) != null){
                if(read.contains("#"))read = read.substring(0,read.indexOf("#"));
                if(read.contains("//"))read = read.substring(0,read.indexOf("//"));
                if(read.contains("/*"))skip = true;
                if(read.contains("*/"))skip = false;
                read = read.trim();

                if(!skip&&read.length() != 0){
                    //READ IN BLOCKS.
                    if(!inBlock&&read.contains("{")){
                        arguments.clear();
                        inBlock = true;
                        type = read.substring(0,read.indexOf("{")).trim().toLowerCase();
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
                            if(type.equals("player")){
                                MainFrame.player.setPosition(Integer.parseInt(arguments.get("x")),
                                                             Integer.parseInt(arguments.get("y")));
                                
                            }else
                                MainFrame.elementBuilder.buildElement(type, arguments);
                        }
                    }
                }
                line++;
            }
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Read exception",e);return false;}
        Const.LOGGER.info("[World] Loaded "+elementsLoaded+" elements. "+MainFrame.world.blockSize()+" Blocks "+MainFrame.world.entitySize()+" Entities "+MainFrame.world.tileSize()+" Tiles.");
        loaded = file;
        return true;
    }
}
