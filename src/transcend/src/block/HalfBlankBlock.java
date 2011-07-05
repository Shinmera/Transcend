
package block;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class HalfBlankBlock extends Block{

    public HalfBlankBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=0.5;z=5;
    }

    public void draw(){
        if(!MainFrame.editor.getActive())return;
        Color.gray.bind();
        glBegin(GL_QUADS);
            glVertex2d(x,y);
            glVertex2d(x,y+h);
            glVertex2d(x+w,y+h);
            glVertex2d(x+w,y);
        glEnd();

        /*glLineWidth(thickness);
        glBegin(GL_LINE_LOOP);
            glColor4f(border.getRed()/255.0f,border.getGreen()/255.0f,border.getBlue()/255.0f,border.getAlpha()/255);
            glVertex2f(x,y);
            glVertex2f(x,y+h);
            glVertex2f(x+w,y+h);
            glVertex2f(x+w,y);
        glEnd();*/
    }
}
