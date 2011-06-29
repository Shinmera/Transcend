/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import world.Element;
import java.util.HashMap;
import transcend.Const;
import block.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import transcend.MainFrame;
import org.newdawn.slick.Color;
import event.MouseListener;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;

public class Editor extends GObject implements MouseListener{
    private String[] blocks = {"dirtblock","grassblock","stoneblock"};
    private boolean active=false;
    private int tilesize=64;
    private int curItem=0;

    public void paint(){
        if(!visible||!active)return;

        Color.black.bind();
        glLineWidth(0.5f);
        for(int i=tilesize-((int)MainFrame.camera.getY())%tilesize;i<MainFrame.DISPLAY_HEIGHT;i+=tilesize){
            glBegin(GL_LINES);
                glVertex2i(0,i);
                glVertex2i(MainFrame.DISPLAY_WIDTH,i);
            glEnd();
        }
        for(int i=tilesize-((int)MainFrame.camera.getX())%tilesize;i<MainFrame.DISPLAY_WIDTH;i+=tilesize){
            glBegin(GL_LINES);
                glVertex2i(i,0);
                glVertex2i(i,MainFrame.DISPLAY_HEIGHT);
            glEnd();
        }

        if(x!=0&&y!=0){
            glEnable(GL_COLOR_LOGIC_OP);
            glLogicOp(GL_XOR);

            Color.white.bind();
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            // OpenGL window coordinates are different from GDI's
            glRecti(x, y, Mouse.getX(), Mouse.getY());
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

            glDisable(GL_COLOR_LOGIC_OP);
        }
    }

    public void setActive(boolean a){active=a;}
    public boolean getActive(){ return active;}
    public void setTilesize(int a){tilesize=a;}
    public int getTilesize(){ return tilesize;}
    public void setItem(int i){curItem=i;}
    public int getItem(){return curItem;}
    public String getItemName(int i){return blocks[i];}
    public int getItemCount(){return blocks.length;}

    public void mouseMoved(int x, int y) {}

    public void mousePressed(int button) {}

    public void mouseType(int button) {
        if(!active||!visible)return;
        if(button==0){
            x=Mouse.getX();
            y=Mouse.getY();
        }
    }

    private int roundSampled(double toround,int size){
        double rest = toround%size;
        if(rest==0)return (int)toround;
        else if(rest>=size/2)return (int)(toround+size-rest);
        else return (int)(toround-rest);
    }

    public void mouseReleased(int button) {
        if(!active||!visible)return;
        if(button==0){

            x+=MainFrame.camera.getRelativeX();
            y+=MainFrame.camera.getRelativeY();
            int bx=(int) (Mouse.getX() + MainFrame.camera.getRelativeX())-x;
            int by=(int) (Mouse.getY() + MainFrame.camera.getRelativeY())-y;
            if(bx<0){bx*=-1;x-=bx;}
            if(by<0){by*=-1;y-=by;}
            if(by<tilesize)by=tilesize;
            bx=roundSampled(bx,tilesize);
            by=roundSampled(by,tilesize);
            x=roundSampled(x,tilesize);
            y=roundSampled(y,tilesize);

            if(bx>0&&by>0){
                HashMap<String,String> args = new HashMap();
                args.put("x", x+"");
                args.put("y", y+"");
                args.put("w", bx+"");
                args.put("h", by+"");
                MainFrame.elementBuilder.buildElement(blocks[curItem], args);
            }
            x=0;y=0;
        }
        if(button==1){
            Element e=null;
            for(int i=0;i<MainFrame.world.size();i++){
                if(MainFrame.world.getByID(MainFrame.world.getID(i)).checkInside((Mouse.getX() + MainFrame.camera.getRelativeX()),(Mouse.getY() + MainFrame.camera.getRelativeY()),false)){
                    e=MainFrame.world.getByID(MainFrame.world.getID(i));
                    break;
                }
            }
            if(e!=null){
                MainFrame.world.delByID(e.wID);
            }
        }
    }

}
