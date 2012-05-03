/**********************\
  file: WorldLoader
  package: world
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.world;

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
import org.newdawn.slick.Color;
import transcend.main.Const;
import transcend.main.MainFrame;

public class WorldLoader extends ElementBuilder{
    private File loaded = null;

    public File getLoaded(){return loaded;}
    public boolean isLoaded(){return(loaded!=null);}

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
        try{
            Const.LOGGER.info("[World] Loading Game State from "+file.getAbsolutePath());

            GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
            Reader decoder = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(decoder);
            StringBuilder buf = new StringBuilder();

            //DECRYPT
            String read="";
            while((read=br.readLine())!=null)buf.append(read+"\n");
            br.close();
            //SET PLAYER STATE
            HashMap<String,String> map = Toolkit.stringToMap(buf.toString(),";","=");
            loadWorld(MainFrame.fileStorage.getFile("world/"+map.get("world")));
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
            pw.println("world{");
            pw.println("bgc: "+MainFrame.getClearColor().getRed()+
                              ","+MainFrame.getClearColor().getGreen()+
                              ","+MainFrame.getClearColor().getBlue()+
                              ","+MainFrame.getClearColor().getAlpha());
            if(MainFrame.camera.getFollowing()==-1)
                 pw.println("camera: fixed "+MainFrame.camera.getZoom());
            else pw.println("camera: follow "+MainFrame.camera.getZoom());
            pw.println("}\n\nplayer{");
            pw.println("x:"+MainFrame.player.getX());
            pw.println("y:"+MainFrame.player.getY());
            pw.println("}\n");

            for(int i=0;i<MainFrame.world.size();i++){
                BElement e = MainFrame.world.getByID(MainFrame.world.getID(i));
                if(!e.getClass().getName().equals("entity.Player")){
                    SimpleSet s = e.getOptions();
                    if((s!=null)&&(!s.containsKey("nosave"))){
                        pw.println(e.getClass().getName().substring(e.getClass().getName().lastIndexOf('.')+1)+"{");
                        pw.println("x: "+(int)e.getX());
                        pw.println("y: "+(int)e.getY());
                        pw.println("z: "+(int)e.getZ());
                        pw.println("w: "+e.getWidth());
                        pw.println("h: "+e.getHeight());
                        if(s!=null){
                            for(int j=0;j<s.size();j++)
                                pw.println(s.getKey(j)+": "+s.getAt(j));
                        }
                        pw.println("}\n");
                    }
                }
            }
            pw.flush();
            pw.close();
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to save World: Write exception",e);}
        Const.LOGGER.info("[World] Saved "+MainFrame.world.size()+" elements.");

        return true;
    }

    public boolean loadWorld(File file){
        if((file==null)||(!file.exists()))return false;
        boolean skip = false,inBlock = false,inMulti = false;
        int line = 1,elementsLoaded=0;
        HashMap<String,String> arguments = new HashMap<String,String>();
        String type = "";
        try{
            MainFrame.loader.setDisplayed(true);
            Const.LOGGER.info("[World] Clearing sound pool");
            MainFrame.soundPool.clearPool();
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
                                String key = read.substring(0,read.indexOf(':')).trim();
                                String val = read.substring(read.indexOf(':')+1).trim();
                                if(val.equals("null"))val=null;
                                arguments.put(key, val);
                            }
                        }

                        if(!inBlock){ //End of block reached.
                            elementsLoaded++;
                            if(type.equals("player")){
                                MainFrame.player.setPosition(Double.parseDouble(arguments.get("x")),
                                                             Double.parseDouble(arguments.get("y")));
                                
                            }else if(type.equals("world")){
                                if(arguments.containsKey("bgc")){
                                    java.awt.Color c = Toolkit.toColor(arguments.get("bgc"));
                                    MainFrame.setClearColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()));
                                }
                                if(arguments.containsKey("camera")){
                                    String[] cam = arguments.get("camera").split(" ");
                                    if(cam[0].equals("fixed"))MainFrame.camera.follow(-1);
                                    if(cam[0].equals("follow"))MainFrame.camera.follow(MainFrame.player.wID);
                                    if(Toolkit.isNumeric(cam[1]))MainFrame.camera.setZoom(MainFrame.camera.getZoom()*Double.parseDouble(cam[1]));
                                }
                            }else
                                buildElement(type, arguments);
                        }
                    }
                }
                line++;
            }
            in.close();
            MainFrame.world.findEdges();
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Read exception",e);return false;}
        Const.LOGGER.info("[World] Loaded "+elementsLoaded+" elements. "+MainFrame.world.blockSize()+" Blocks "+MainFrame.world.entitySize()+" Entities "+MainFrame.world.tileSize()+" Tiles.");
        Const.LOGGER.info("[World] Current edges: xl: "+MainFrame.world.leftLimit+" yb: "+MainFrame.world.lowerLimit+" xr: "+MainFrame.world.rightLimit+" yt: "+MainFrame.world.upperLimit);
        loaded = file;
        return true;
    }
}
