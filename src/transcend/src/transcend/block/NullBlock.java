/**********************\
  file: NullBlock.java
  package: block
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.block;

import transcend.graph.AbstractGraph;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;

public class NullBlock extends Block{
    public NullBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=0;z=5;
    }

    public NullBlock() {
        solid=0;z=5;
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        new Color(0.25f,0.25f,0.25f,0.5f).bind();
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
