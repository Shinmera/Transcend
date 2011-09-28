/**********************\
  file: Animation
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package graph;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import java.io.File;
import static org.lwjgl.opengl.GL11.*;

public class Animation {
    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;
    public static final int PLAY_FORWARD = 1;
    public static final int PLAY_BACKWARD = -1;

    private boolean init = false;
    private Texture texture = null;
    private double spritesize = 64.0;
    private int ind_w = 0;
    private int ind_h = 0;
    private double rel_w = 1;
    private double rel_h = 1;
    private int counter = -1;
    private int pps = 30;
    private int[] start = {0};
    private int[] stop = {0};
    private int[] loop = {0};
    private int[] loop2 = {0};
    private int direction = DIR_RIGHT;
    private int play = PLAY_FORWARD;
    private double tile_w = 1;
    private double tile_h = 1;
    private int w=64,h=64,z=0;

    public Animation(){}
    public Animation(int spritesize){
        this.spritesize=spritesize;
        w=spritesize;
        h=spritesize;
        calcTile();
    }
    public Animation(int spritesize,int w,int h){
        this.spritesize=spritesize;
        this.w=w;
        this.h=h;
        calcTile();
    }

    public boolean loadTexture(File f){
        if(!f.exists())return false;
        texture = MainFrame.texturePool.loadTexture(f.getName(), f);
        return true;
    }

    public boolean loadTexture(File f,int[] start,int[] stop,int[] loop){
        if(!loadTexture(f))return false;
        setStart(start);
        setStop(stop);
        setLoop(loop);
        return true;
    }

    public boolean loadTexture(File f,int[] start,int[] stop,int[] loop,int[] loop2){
        if(!loadTexture(f,start,stop,loop))return false;
        setLoop2(loop2);
        return true;
    }

    public void calcRelative(){
        if(texture==null)return;
        rel_w=spritesize/texture.getImageWidth();
        rel_h=spritesize/texture.getImageHeight();
        if(start.length!=(int)(1.0/rel_h)){
            start = new int[(int)(1.0/rel_h)];
            stop = new int[(int)(1.0/rel_h)];
            loop = new int[(int)(1.0/rel_h)];
            loop2= new int[(int)(1.0/rel_h)];
            for(int i=0;i<start.length;i++){start[i]=0;stop[i]=0;loop[i]=0;loop2[i]=0;}
        }
    }

    public void calcTile(){calcTile(w,h);}

    public void calcTile(int w,int h){
        tile_w=w/spritesize;
        tile_h=h/spritesize;
    }

    public void setSpritesize(double spritesize){
        this.spritesize=spritesize;
        calcRelative();
        calcTile();
    }

    public void setSize(int w,int h){
        this.w=w;this.h=h;
    }

    public void setZ(int z){this.z=z;}
    public void setU(int u){ind_w=u;}
    public void setV(int v){ind_h=v;}
    public void setStart(int reel,int pos){start[reel]=pos;}
    public void setStop(int reel,int pos){stop[reel]=pos;}
    public void setLoop(int reel,int pos){loop[reel]=pos;}
    public void setLoop2(int reel,int pos){loop2[reel]=pos;}
    public void setStart(int[] pos){start=pos;}
    public void setStop(int[] pos){stop=pos;}
    public void setLoop(int[] pos){loop=pos;}
    public void setLoop2(int[] pos){loop2=pos;}
    public void setPPS(int pps){this.pps=pps;}
    public void setRelW(double w){rel_w=w;}
    public void setRelH(double h){rel_h=h;}
    public void setTileW(double w){tile_w=w;}
    public void setTileH(double h){tile_h=h;}

    public void setDirection(int dir){direction=dir;}
    public void setPlay(int play){this.play=play;}

    public void setReel(int index){
        if(index==ind_h)return;
        ind_h=index;
        if(play==PLAY_FORWARD)counter=start[ind_h];
        else counter=stop[ind_h];
    }
    public int getReel(){return ind_h;}
    public Texture getTexture(){return texture;}
    public int getU(){return ind_w;}
    public int getV(){return ind_h;}
    public double getSpritesize(){return spritesize;}
    public int getPPS(){return pps;}
    public boolean isLoaded(){return texture!=null;}

    public void update(){
        if(stop[0]<0)return;
        counter++;
        if(counter>MainFrame.fps/pps){
            counter=0;
            if(play==PLAY_FORWARD){
                if(ind_w<stop[ind_h])ind_w++;
                else{
                    if(loop[ind_h]>=0)ind_w=loop[ind_h];
                    else if(loop[ind_h]==-999) setReel(0);
                    else setReel(-1*loop[ind_h]);
                }
            }else{
                if(ind_w>start[ind_h])ind_w--;
                else{
                    if(loop2[ind_h]>=0)ind_w=loop2[ind_h];
                    else if(loop2[ind_h]==-999) setReel(0);
                    else setReel(-1*loop2[ind_h]);
                }
            }
        }
    }
    
    public void draw(){}

    public void draw(int x,int y){draw(x,y,w,h);}

    public void draw(int x,int y,int w,int h){
        if(!init){calcRelative();init=true;}
        if(texture==null){

            glBegin(GL_QUADS);
                glVertex3i(x, y,z);
                glVertex3i(x+w, y,z);
                glVertex3i(x+w, y+h,z);
                glVertex3i(x, y+h,z);
            glEnd();
        }else{
            glMatrixMode(GL_TEXTURE);
                glLoadIdentity();
                glScaled(tile_w,tile_h,1);
            glMatrixMode(GL_MODELVIEW);

            Color.white.bind();
            glBindTexture(GL_TEXTURE_2D,texture.getTextureID());
            glPushMatrix();
            glTranslatef(x+w/2,y+h/2,z);
            glScalef(direction,1,1);
            glBegin(GL_QUADS);
                glTexCoord2d(rel_w*ind_w     ,rel_h*(ind_h+1));
                glVertex2i(-w/2,-h/2);
                glTexCoord2d(rel_w*(ind_w+1) ,rel_h*(ind_h+1));
                glVertex2i(w/2, -h/2);
                glTexCoord2d(rel_w*(ind_w+1) ,rel_h*ind_h);
                glVertex2i(w/2, h/2);
                glTexCoord2d(rel_w*ind_w     ,rel_h*ind_h);
                glVertex2i(-w/2, h/2);
            glEnd();
            glPopMatrix();

            glBindTexture(GL_TEXTURE_2D, 0); //release

            glMatrixMode(GL_TEXTURE);
                glLoadIdentity();
                glScaled(1,1,1);
            glMatrixMode(GL_MODELVIEW);
        }
    }
}
