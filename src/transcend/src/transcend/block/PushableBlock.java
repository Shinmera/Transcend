/**********************\
  file: PushableBlock.java
  package: transcend.block
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.block;

import NexT.util.SimpleSet;
import java.util.HashMap;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;
import transcend.world.Element;

public class PushableBlock extends Block{
    private String tex="";
    private double vx,vy;
    
    public PushableBlock(){solid=1;}
    
    public PushableBlock(int x,int y,int w,int h){
        super(x,y,w,h);
        solid=1;
    }
    
    public void loadTexture(String s){
        if(s.length()==0)return;
        tex=s;
        drawable.loadTexture(MainFrame.fileStorage.getFile(s));
    }
    
    public void update(){
        Element left=check(x-2,y+3,x-2,y+w-3);
        Element right=check(x+w+2,y+3,x+w+2,y+w-3);
        Element bottom=check(x+2,y,x+w-2,y);
        
        if(right!=null){
            if(right.wID==MainFrame.player.wID&&MainFrame.player.vx<0&&left==null){
                MainFrame.player.vx=-2;
                vx=-2;
            }else vx=0;
        }else 
        if(left!=null){
            if(left.wID==MainFrame.player.wID&&MainFrame.player.vx>0&&right==null){
                MainFrame.player.vx=2;
                vx=2;
            }else vx=0;
        }else vx=0;
        
        if(bottom!=null)vy=0;
        else vy-=0.2;
        
        x+=vx;
        y+=vy;
    }
    
    public void draw(){
        if(drawable.isLoaded()){
            drawable.draw((int)x,(int)y,w,h);
        }else{
            new Color(0.5f,1.0f,0.5f,0.5f).bind();
            glBegin(GL_QUADS);
                glVertex2d(x,y);
                glVertex2d(x,y+h);
                glVertex2d(x+w,y+h);
                glVertex2d(x+w,y);
            glEnd();
        }
    }
    
    public void setOptions(HashMap<String,String> args){
        if(args.containsKey("tex"))loadTexture(args.get("tex"));
        super.setOptions(args);
    }
    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = super.getOptions();
        set.put("tex", tex);
        return set;
    }
}
