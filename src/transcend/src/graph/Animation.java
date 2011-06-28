/**********************\
  file: Animation
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package graph;
import org.newdawn.slick.Color;
import transcend.Const;
import transcend.MainFrame;
import java.util.logging.Level;
import org.newdawn.slick.opengl.TextureLoader;
import java.io.FileInputStream;
import org.newdawn.slick.opengl.Texture;
import java.io.File;
import static org.lwjgl.opengl.GL11.*;

public class Animation {
    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;

    private Texture texture = null;
    private double spritesize = 64;
    private int ind_w = 0;
    private int ind_h = 0;
    private double rel_w = 1;
    private double rel_h = 1;
    private int counter = -1;
    private int pps = 30;
    private int[] start = {0};
    private int[] stop = {0};
    private int[] loop = {0};
    private int direction = DIR_RIGHT;

    public Animation(){
        
    }

    public boolean loadTexture(File f){
        if(!f.exists())return false;
        String extension = f.getName().substring(f.getName().indexOf(".")+1);
        try{
        texture = TextureLoader.getTexture(extension.toUpperCase(), new FileInputStream(f));
        }catch(Exception e){Const.LOGGER.log(Level.SEVERE,"Failed to load texture at "+f.getAbsolutePath()+".",e);return false;}
        calcRelative();
        return true;
    }

    public boolean loadTexture(File f,int[] start,int[] stop,int[] loop){
        if(!f.exists())return false;
        String extension = f.getName().substring(f.getName().indexOf(".")+1);
        try{
        texture = TextureLoader.getTexture(extension.toUpperCase(), new FileInputStream(f));
        }catch(Exception e){Const.LOGGER.log(Level.SEVERE,"Failed to load texture at "+f.getAbsolutePath()+".",e);return false;}
        calcRelative();
        setStart(start);
        setStop(stop);
        setLoop(loop);
        return true;
    }

    public void calcRelative(){
        rel_w=texture.getImageWidth()/spritesize;
        rel_h=texture.getImageHeight()/spritesize;
        start = new int[(int)rel_h];
        stop = new int[(int)rel_h];
        loop = new int[(int)rel_h];
    }

    public void setStart(int reel,int pos){start[reel]=pos;}
    public void setStop(int reel,int pos){stop[reel]=pos;}
    public void setLoop(int reel,int pos){loop[reel]=pos;}
    public void setStart(int[] pos){start=pos;}
    public void setStop(int[] pos){stop=pos;}
    public void setLoop(int[] pos){loop=pos;}

    public void setDirection(int dir){
        direction=dir;
    }

    public void setReel(int index){
        ind_h=index;
        counter=-1;
    }

    public int getReel(){
        return ind_h;
    }
    
    public void draw(){}

    public void draw(int x,int y){}

    public void draw(int x,int y,int w,int h){
        if(texture==null){
            new Color(1.0f,0.0f,0.0f,1.0f).bind();

            glBegin(GL_QUADS);
                glVertex2i(x, y);
                glVertex2i(x+w, y);
                glVertex2i(x+w, y+h);
                glVertex2i(x, y+h);
            glEnd();
        }else{
            if(counter==-1)counter=start[ind_h];

            Color.white.bind();
            glBindTexture(GL_TEXTURE_2D,texture.getTextureID());
            glPushMatrix();
            glTranslatef(x+w/2,y+h/2,0);
            glScalef(direction,1,1);
            glBegin(GL_QUADS);
                glTexCoord2d(1.0/rel_w*ind_w,1.0/rel_h*(ind_h+1));
                glVertex2i(-w/2,-h/2);
                glTexCoord2d(1.0/rel_w*(ind_w+1),1.0/rel_h*(ind_h+1));
                glVertex2i(w/2, -h/2);
                glTexCoord2d(1.0/rel_w*(ind_w+1),1.0/rel_h*ind_h);
                glVertex2i(w/2, h/2);
                glTexCoord2d(1.0/rel_w*ind_w,1.0/rel_h*ind_h);
                glVertex2i(-w/2, h/2);
            glEnd();
            glPopMatrix();
            glBindTexture(GL_TEXTURE_2D, 0); //release
            
            if(counter>MainFrame.fps/pps){
                counter=0;
                if(ind_w<stop[ind_h])ind_w++;else ind_w=loop[ind_h];
            }
            counter++;
        }
    }
}
