/**********************\
  file: MovingBlock.java
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;
import java.io.File;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class MovingBlock extends Block{
    private double vx=0,vy=0;

    public MovingBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=1;
    }

    public MovingBlock(int x,int y,int w,int h,double vx,double vy){
        this(x,y,w,h);
        this.vx=vx;this.vy=vy;
    }

    public void loadTexture(String s){
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+s));
    }

    public void update(){
        
    }

    public void draw(){
        if(drawable.isLoaded()){
            drawable.draw((int)x,(int)y,w,h);
        }else{
            new Color(0.5f,0.5f,1f,1f).bind();
            glBegin(GL_QUADS);
                glVertex2d(x,y);
                glVertex2d(x,y+h);
                glVertex2d(x+w,y+h);
                glVertex2d(x+w,y);
            glEnd();
        }
    }
}
