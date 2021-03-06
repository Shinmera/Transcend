/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.tile;

import NexT.util.SimpleSet;
import java.io.File;
import java.util.HashMap;
import transcend.main.MainFrame;

public class TileSet extends Tile{
    String tileset;

    public TileSet(int x,int y,int w,int h,String tileset){
        super(x,y,w,h);this.z=0;this.tileset=tileset;
        drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+tileset));
        drawable.setSize(w,h);
        drawable.calcRelative();
        depth=1;
    }

    public TileSet(int x,int y,int w,int h,String tileset,int u,int v){
        this(x,y,w,h,tileset);
        drawable.setU(u);
        drawable.setV(v);
        depth=1;
    }

    public TileSet() {depth=1;z=0;}

    public void setU(int u){drawable.setU(u);}
    public void setV(int v){drawable.setV(v);}
    public void setS(double s){
        drawable.setSpritesize(s);
        drawable.setSize(w,h);
        drawable.calcRelative();
    }

    public SimpleSet<String, String> getOptions() {
        SimpleSet<String, String> set = super.getOptions();
        set.put("tex",tileset);
        set.put("u",drawable.getU()+"");
        set.put("v",drawable.getV()+"");
        set.put("s",drawable.getSpritesize()+"");
        return set;
    }

    public void setOptions(HashMap<String,String> args){
        super.setOptions(args);
        if(args.containsKey("tex")){
            tileset = args.get("tex");
            drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+args.get("tex")));
            drawable.setSize(w,h);
            drawable.calcRelative();
        }
    }
}
