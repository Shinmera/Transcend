
package block;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class BlankBlock extends Block{

    public BlankBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=1;z=5;
    }

    public BlankBlock() {
        solid=1;z=5;
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        new Color(1f,1f,1f,0.5f).bind();
        glBegin(GL_QUADS);
            glVertex2d(x,y);
            glVertex2d(x,y+h);
            glVertex2d(x+w,y+h);
            glVertex2d(x+w,y);
        glEnd();
    }
}
