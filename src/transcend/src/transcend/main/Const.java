package transcend.main;

import NexT.util.Toolkit;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Supplies constants throughout the project.
 * @author Shinmera
 */
public class Const {
    //INTEGERS
    public static final int     DISPLAY_HEIGHT          = 480;
    public static final int     DISPLAY_WIDTH           = 640;

    //DOUBLES
    public static final double  VERSION                 = 1.35;

    //STRINGS
    public static final String  MAINTAINER              = "NexT";
    public static final String  DEVELOPER               = "Shinmera";

    //OBJECT
    public static final Logger LOGGER                   = Logger.getLogger("+");
    public static final Random RAND                     = new Random();

    public HashMap<String,String> registry              = new HashMap();

    static {
        try {
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(new XLogger(new File("err.log")));
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }
    }

    public Const(){
        setDefaults();
        loadRegistry();
    }

    public boolean saveRegistry(){
        try{
        LOGGER.info("[CONST] Saving");
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File("constants.reg")),"UTF-8");
        PrintWriter pw = new PrintWriter(fw);

        Map<String,String> tempmap = new TreeMap<String,String>(((Map)registry));
        Set entries = tempmap.entrySet();
        Iterator entriesIterator = entries.iterator();
        while(entriesIterator.hasNext()){
            Map.Entry mapping = (Map.Entry) entriesIterator.next();
            if(mapping.getKey()!=null)
                pw.println(mapping.getKey()+": "+mapping.getValue());
        }

        pw.flush();
        pw.close();
        }catch(Exception e){return false;}
        return true;
    }

    public boolean loadRegistry(){
        try{
            LOGGER.info("[CONST] Loading");
            BufferedReader in = new BufferedReader(new FileReader(new File("constants.reg")));
            String read = "";
            while ((read = in.readLine()) != null) {
                read.trim();
                if(read.length()>0){
                    String key = read.substring(0,read.indexOf(": "));
                    String value = read.substring(read.indexOf(": ")+2);
                    registry.put(key,value);
                }
            }
            in.close();
        }catch(Exception e){return false;}
        return true;
    }

    public void setDefaults(){
        registry.put("FPS","60");
        registry.put("UPS","60");
        registry.put("ANTIALIAS","2");
        registry.put("FORCED_W","0");
        registry.put("FORCED_H","0");
        registry.put("LAYOUT","swiss");
        registry.put("PLAYOUT","lefty");
        registry.put("REPO","http://repo.tymoon.eu/v2/transcend");
    }

    public String gString(String key){
        if(registry.containsKey(key))return registry.get(key);else return null;
    }
    public boolean gBoolean(String key){
        if(registry.containsKey(key))return Boolean.parseBoolean(registry.get(key));else return false;
    }
    public int gInteger(String key){
        if(registry.containsKey(key))return Integer.parseInt(registry.get(key));else return 0;
    }
    public double gDouble(String key){
        if(registry.containsKey(key))return Double.parseDouble(registry.get(key));else return 0;
    }
    public float gFloat(String key){
        if(registry.containsKey(key))return Float.parseFloat(registry.get(key));else return 0;
    }
    public Color gColor(String key){
        if(registry.containsKey(key))return Toolkit.toColor(registry.get(key));else return null;
    }

    public void sString(String key,String value){
        registry.put(key, value);
    }
    public void sBoolean(String key,boolean value){
        if(value)registry.put(key, "true");else registry.put(key,"false");
    }
    public void sInteger(String key,int value){
        registry.put(key, value+"");
    }
    public void sDouble(String key,double value){
        registry.put(key, value+"");
    }
    public void sFloat(String key,float value){
        registry.put(key, value+"");
    }
    public void sColor(String key,Color value){
        registry.put(key, value.getRed()+","+value.getGreen()+","+value.getBlue());
    }
}
