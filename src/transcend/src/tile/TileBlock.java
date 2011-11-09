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

    public void setTexture(String tex){
        if(tex.length()==0||tex.equals("null"))return;
        this.tex=tex;
        drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+tex));
        drawable.calcTile(w, h);
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("tex")){
            if(options.get("tex").length()==0)MainFrame.world.delByID(wID);
            setTexture(options.get("tex"));
        }else{
            MainFrame.world.delByID(wID);
        }
    }

    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = super.getOptions();
        set.put("tex",tex);
        if((tex==null)||(tex.length()==0))set.put("nosave","1");
        return set;
    }
}
