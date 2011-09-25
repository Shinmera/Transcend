/**********************\
  file: MovingBlock.java
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;
import entity.Entity;
import gui.CameraPath;
import java.util.HashMap;
import NexT.util.SimpleSet;
import java.io.File;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class MovingBlock extends Block{
    private double vx=0,vy=0;
    private int cameraPath=-1;
    private String tex="";

    public MovingBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=1;
    }

    public MovingBlock(int x,int y,int w,int h,double vx,double vy){
        this(x,y,w,h);
        this.vx=vx;this.vy=vy;
    }

    public MovingBlock() {solid=1;}

    public void loadTexture(String s){
        if(s.length()==0)return;
        tex=s;
        drawable.loadTexture(MainFrame.fileStorage.getFile(s));
    }

    public void setVX(double vx){this.vx=vx;}
    public void setVY(double vy){this.vy=vy;}
    public void attach(CameraPath path){cameraPath=path.wID;}
    public void detach(){cameraPath=-1;}

    public void update(){
        if(cameraPath==-1){
            x+=vx;
            y+=vy;
            if(MainFrame.player.getGround()==this){
                MainFrame.player.x+=vx;
                MainFrame.player.y=y+h;
            }
            if(MainFrame.player.getLeft()==this){
                MainFrame.player.x=x+MainFrame.player.getWidth()/2+w;
            }
            if(MainFrame.player.getRight()==this){
                MainFrame.player.x=x-MainFrame.player.getWidth()/2;
            }
            if(Entity.check(x-1,y+h/2,x+w+1,y+h/2)!=null)vx*=-1;
            if(Entity.check(x+w/2,y-1,x+w/4*3,y-1)!=null||
               Entity.check(x+w/2,y+h+1,x+w/4*3,y+h+1)!=null)vy*=-1;
        }else{
            x=MainFrame.world.getByID(cameraPath).getX();
            y=MainFrame.world.getByID(cameraPath).getY();
        }
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
    
    public void setOptions(HashMap<String,String> args){
        if(args.containsKey("vx"))setVX(Double.parseDouble(args.get("vx")));
        if(args.containsKey("vy"))setVY(Double.parseDouble(args.get("vy")));
        if(args.containsKey("tex"))loadTexture(args.get("tex"));
        super.setOptions(args);
    }
    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = super.getOptions();
        set.put("vx", vx+"");
        set.put("vy", vy+"");
        set.put("tex", tex);
        return set;
    }
}
