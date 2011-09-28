/**********************\
  file: FontPool.java
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package graph;

import gui.TrueTypeFont;
import java.awt.Font;
import java.util.HashMap;
import transcend.Const;

public class FontPool {
    private HashMap<String,TrueTypeFont> fonts = new HashMap<String,TrueTypeFont>();

    public FontPool(){}

    public TrueTypeFont loadFont(String name,int weight,int size){
        Font f = new Font(name,weight,size);
        if(isLoaded(f))return getFont(f);
        return reloadFont(f);
    }

    public TrueTypeFont loadFont(Font f){
        if(isLoaded(f))return getFont(f);
        return reloadFont(f);
    }

    public TrueTypeFont reloadFont(Font f){
        Const.LOGGER.info("[FontPool] Reloading "+f.getFontName()+" "+f.getSize());
        TrueTypeFont font = new TrueTypeFont(f,false);
        fonts.put(f.getFamily()+f.getStyle()+f.getSize(), font);
        return font;
    }

    public boolean isLoaded(Font f){
        return fonts.containsKey(f.getFamily()+f.getStyle()+f.getSize());
    }

    public TrueTypeFont getFont(Font f){
        return fonts.get(f.getFamily()+f.getStyle()+f.getSize());
    }

    public void delFont(Font f){
        fonts.remove(f.getFamily()+f.getStyle()+f.getSize());
    }
}