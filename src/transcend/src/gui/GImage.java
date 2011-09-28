/**********************\
  file: GImage.java  package: gui
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
        //drawable.setSpritesize(drawable.getTexture().getImageWidth());
    }

    public void setBounds(int x,int y,int w,int h){
        super.setBounds(x,y,w,h);
        drawable.setRelH(1);
        drawable.setRelW(1);
        drawable.setTileH(1);
        drawable.setTileW(w/64);
    }

    public void paint(){
        drawable.draw(x, y, w, h);
    }
}
