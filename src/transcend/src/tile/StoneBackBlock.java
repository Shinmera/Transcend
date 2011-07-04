/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import block.GrassBlock;
import java.io.File;
import transcend.MainFrame;

public class StoneBackBlock extends Tile{

    public StoneBackBlock(int x,int y,int w,int h){
        super(x,y,w,h);this.z=0;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"stone_back.png"));
        drawable.calcTile(w, h);
    }
}
