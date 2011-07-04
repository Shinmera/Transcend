/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import java.io.File;
import transcend.MainFrame;

public class StoneBlock extends Block{
    
    public StoneBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"stone.png"));
        drawable.calcTile(w, h);
        solid=1;
        z=-1;
    }
}
