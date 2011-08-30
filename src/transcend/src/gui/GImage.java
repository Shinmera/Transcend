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
import transcend.MainFrame;

public class GImage extends GObject{
    private Animation drawable = new Animation();

    public GImage(String file){
        drawable.loadTexture(MainFrame.fileStorage.getFile(file));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
    }

    public void setBounds(int x,int y,int w,int h){
        super.setBounds(x,y,w,h);
        drawable.calcTile(w, h);
    }

    public void paint(){
        drawable.draw(x, y, w, h);
    }
}
