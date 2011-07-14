/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import java.io.File;
import transcend.MainFrame;

public class BrickBlock extends Tile{
    
    public BrickBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"brick.png"));
        drawable.calcTile(w, h);
        depth=1.7f;
    }
}
