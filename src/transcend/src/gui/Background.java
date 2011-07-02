/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import graph.Animation;
import java.io.File;
import transcend.MainFrame;

public class Background extends GObject{
    private Animation drawable = new Animation();

    public Background(String file){
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+file));
    }

    public Background(String file,int w,int h){
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+file));
        drawable.calcTile(w, h);
        this.w=w;this.h=h;
    }

    public void paint(){
        drawable.draw(0, 0, w, h);
    }
}
