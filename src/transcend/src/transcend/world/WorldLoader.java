/**********************\
  file: WorldLoader
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
import NexT.util.Toolkit;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.newdawn.slick.Color;
import transcend.block.*;
import transcend.entity.EnemyB1;
import transcend.entity.EnemyC1;
import transcend.entity.RigidBody;
import transcend.gui.CameraPath;
import transcend.main.Const;
import transcend.main.MainFrame;
import transcend.particle.Emitter;
import transcend.tile.*;

public class WorldLoader extends World{
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

            for(int i=0;i<size();i++){
                BElement e = getByID(getID(i));
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
        Const.LOGGER.info("[World] Saved "+size()+" elements.");

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
            clear();
            int id = addEntity(MainFrame.player);
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
            findEdges();
        }catch(IOException e){Const.LOGGER.log(Level.SEVERE,"Failed to load World: Read exception",e);return false;}
        Const.LOGGER.info("[World] Loaded "+elementsLoaded+" elements. "+blockSize()+" Blocks "+entitySize()+" Entities "+tileSize()+" Tiles.");
        Const.LOGGER.info("[World] Current edges: xl: "+leftLimit+" yb: "+lowerLimit+" xr: "+rightLimit+" yt: "+upperLimit);
        loaded = file;
        return true;
    }
    
    
    
    HashMap<String,Class> elements = new HashMap<String,Class>();
    SimpleSet<String,String> elementData = new SimpleSet<String,String>();

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
            addTile(block);
        }
        else if(name.equals("fitblock")){
            FitBlock block = new FitBlock();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("movingblock")){
            MovingBlock block = new MovingBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("pushableblock")){
            PushableBlock block = new PushableBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("complexblock")){
            ComplexBlock block = new ComplexBlock();
            block.setOptions(args);
            block.determineCoordinates();
            addBlock(block);
        }
        else if(name.equals("blankblock")){
            BlankBlock block = new BlankBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("halfblankblock")){
            HalfBlankBlock block = new HalfBlankBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("nullblock")){
            NullBlock block = new NullBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("windblock")){
            WindBlock block = new WindBlock();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("water")){
            Water block = new Water();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("colorblock")){
            ColorBlock block = new ColorBlock();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("background")){
            Background block = new Background();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("tileset")){
            TileSet block = new TileSet();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("soundemitter")){
            SoundEmitter block = new SoundEmitter();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("emitter")){
            Emitter block = new Emitter();
            block.setOptions(args);
            addTile(block);
        }
        else if(name.equals("rigidbody")){
            RigidBody entity = new RigidBody();
            entity.setOptions(args);
            addEntity(entity);
        }
        else if(name.equals("enemyb1")){
            EnemyB1 entity = new EnemyB1();
            entity.setOptions(args);
            addEntity(entity);
        }
        else if(name.equals("enemyc1")){
            EnemyC1 entity = new EnemyC1();
            entity.setOptions(args);
            addEntity(entity);
        }
        else if(name.equals("camerapath")){
            CameraPath path = new CameraPath();
            path.setOptions(args);
            addTile(path,args.get("name"));
        }
        else if(name.equals("gameevent")){
            GameEvent block = new GameEvent();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("info")){
            Info block = new Info();
            block.setOptions(args);
            addBlock(block);
        }
        else if(name.equals("daycycle")){
            DayCycle block = new DayCycle();
            block.setOptions(args);
            addTile(block,"daycycle");
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
                    addElement(block);
                } catch (Exception ex) {
                    Const.LOGGER.log(Level.WARNING,"Failed to instantiate block '"+name+"'.",ex);
                }
            }
        }*/
    }
}
