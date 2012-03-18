
package transcend.block;
import org.newdawn.slick.Color;
import transcend.graph.AbstractGraph;
import transcend.main.MainFrame;

public class HalfBlankBlock extends Block{

    public HalfBlankBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=0.5;z=5;
    }

    public HalfBlankBlock() {
        solid=0.5;z=5;
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        new Color(0.5f,0.5f,0.5f,0.5f).bind();
        AbstractGraph.glRectangle2d(x, y, w, h);
    }
}
