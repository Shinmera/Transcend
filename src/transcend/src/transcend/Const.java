package transcend;

import NexT.util.Toolkit;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
    public static final double  VERSION                 = 0.01;

    //STRINGS
    public static final String  MAINTAINER              = "NexT";
    public static final String  DEVELOPER               = "Shinmera";

    //OBJECT
    public static final Logger LOGGER                   = Logger.getLogger("TRA-PUBLIC");

    public HashMap<String,String> registry              = new HashMap();

    public Const(){
        loadRegistry();
    }

    public boolean saveRegistry(){
        try{
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(new File("constants.reg")),"UTF-8");
        PrintWriter pw = new PrintWriter(fw);

        Set entries = registry.entrySet();
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
            registry.clear();
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

    public String gString(String key){
        return registry.get(key);
    }
    public boolean gBoolean(String key){
        return Boolean.parseBoolean(registry.get(key));
    }
    public int gInteger(String key){
        return Integer.parseInt(registry.get(key));
    }
    public double gDouble(String key){
        return Double.parseDouble(registry.get(key));
    }
    public float gFloat(String key){
        return Float.parseFloat(registry.get(key));
    }
    public Color gColor(String key){
        return Toolkit.toColor(registry.get(key));
    }
}
