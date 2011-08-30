/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import NexT.util.SimpleSet;
import java.util.HashMap;
import transcend.MainFrame;

public class TileBlock extends Tile{
    String tex;
    public TileBlock(){}

    public TileBlock(int x,int y,int w,int h,String tex){
        super(x,y,w,h);
        this.tex=tex;
        drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+tex));
        drawable.calcTile(w, h);
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("tex")){
            this.tex=tex;
            drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+options.get("tex")));
            drawable.calcTile(w, h);
            
        }
    }

    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        set.put("tex",tex);
        return set;
    }
}
