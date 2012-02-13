/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.graph;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import transcend.main.Const;

public class TexturePool {
    private HashMap<String,Texture> textures = new HashMap();
    
    public TexturePool(){}
    
    public void clearPool(){
        Iterator it = textures.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ((Texture)pairs.getValue()).release();
        }
        textures.clear();
    }

    public Texture loadTexture(String name,File f){
        if(isLoaded(name))return getTexture(name);
        else              return reloadTexture(name,f);
    }

    public Texture reloadTexture(String name,File f){
        if(!f.exists()){Const.LOGGER.warning("[TexturePool] Trying to load non-existant texture '"+name+"'.");return null;}
        Const.LOGGER.info("[TexturePool] Reloading "+name+" at "+f.getAbsolutePath());
        try{
            String extension = f.getName().substring(f.getName().indexOf(".")+1);
            Texture texture = TextureLoader.getTexture(extension.toUpperCase(), new FileInputStream(f));
            textures.put(name, texture);
            return texture;
        }catch(Exception e){Const.LOGGER.log(Level.SEVERE,"[TexturePool] Failed to load texture at "+f.getAbsolutePath()+".",e);return null;}
    }

    public boolean isLoaded(String name){
        return textures.containsKey(name);
    }

    public Texture getTexture(String name){
        if(!textures.containsKey(name))Const.LOGGER.warning("Accessing inexistant texture '"+name+"'!");
        return textures.get(name);
    }

    public void delTexture(String name){
        textures.remove(name);
    }
}
