/**********************\
  file: WindBlock.java
  package: block
  author: Shinmera
  team: NexT
  license: -
\**********************/

package block;

import graph.AbstractGraph;
import org.newdawn.slick.Color;
import transcend.MainFrame;

public class WindBlock extends Block{
    public WindBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=0;z=5;
    }

    public WindBlock() {
        solid=0;z=5;
    }
    
    public void update(){
        if(checkInside(MainFrame.player)){
            MainFrame.player.vy+=0.5;
            if(MainFrame.player.ground!=null){
                MainFrame.player.y+=4;
                MainFrame.player.ground=null;
            }
        }
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        new Color(0.5f,0.7f,1.0f,0.5f).bind();
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
