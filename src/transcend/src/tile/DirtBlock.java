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

public class DirtBlock extends Tile{

    public DirtBlock(int x,int y,int w,int h){
        super(x,y,w,h);this.z=0;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"dirt.png"));
        drawable.calcTile(w, h);
    }

    public DirtBlock(int x,int y,int w,int h,boolean addGrass){
        super(x,y,w,h);this.z=0;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"dirt.png"));
        drawable.calcTile(w, h);
        if(addGrass){
            GrassBlock grass = new GrassBlock(x,y+h-64,w,64);
            MainFrame.world.addBlock(grass);
        }
    }
}
