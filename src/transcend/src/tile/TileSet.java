/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import NexT.util.SimpleSet;
import java.io.File;
import transcend.MainFrame;

public class TileSet extends Tile{
    String tileset;

    public TileSet(int x,int y,int w,int h,String tileset){
        super(x,y,w,h);this.z=0;this.tileset=tileset;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+tileset));
        drawable.setSize(w,h);
        drawable.calcRelative();
        depth=1;
    }

    public TileSet(int x,int y,int w,int h,String tileset,int u,int v){
        super(x,y,w,h);this.z=0;this.tileset=tileset;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+tileset));
        drawable.setSize(w,h);
        drawable.calcRelative();
        drawable.setU(u);
        drawable.setV(v);
        depth=1;
    }

    public void setU(int u){drawable.setU(u);}
    public void setV(int v){drawable.setV(v);}

    public SimpleSet<String, String> getOptions() {
        SimpleSet<String, String> set = new SimpleSet();
        set.put("tex",tileset);
        set.put("u",drawable.getU()+"");
        set.put("v",drawable.getV()+"");
        return set;
    }
}