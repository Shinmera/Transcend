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

public class GrassBlock extends Block{

    public GrassBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"grass.png"));
        drawable.calcTile(w, h);
        solid=0.5;
        z=1;
    }
}
