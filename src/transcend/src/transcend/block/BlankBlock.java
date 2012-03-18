
package transcend.block;
import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import transcend.main.MainFrame;

public class BlankBlock extends Block{

    public BlankBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=1;z=5;
    }

    public BlankBlock() {
        solid=1;z=5;
    }
    
    public void init() {
        z=5;
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        new Color(1f,1f,1f,0.5f).bind();
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
