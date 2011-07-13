/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import block.HalfBlankBlock;
import java.io.File;
import transcend.MainFrame;

public class GrassBlock extends Tile{

    public GrassBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"grass.png"));
        drawable.calcTile(w, h-1);
        HalfBlankBlock block = new HalfBlankBlock(x,y+h-16,w,16);
        MainFrame.world.addBlock(block);
        z=1;
    }
}
