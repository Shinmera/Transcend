/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import org.lwjgl.util.Point;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import NexT.util.Toolkit;
import world.BElement;
import java.util.HashMap;
import transcend.MainFrame;
import org.newdawn.slick.Color;
import event.MouseListener;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;

public class Editor extends GObject implements MouseListener{
    public static final int MODE_BLOCKS = 0;
    public static final int MODE_ENTITIES = 1;
    private String[] blocks = {"blankblock","halfblankblock","tileblock","movingblock","complexblock","gameevent","water","emitter","daycycle"};
    private String[] entities = {"enemyb1","enemyc1"};
    private boolean active=false;
    private boolean inComplex=false;
    private ArrayList<Point> complexPoints = new ArrayList<Point>();
    private int tilesize=64;
    private int curItem=0;
    private int curLayer=0;
    private int mode=MODE_BLOCKS;

    public void paint(){
        if(!visible||!active)return;

        glEnable(GL_COLOR_LOGIC_OP);
        glLogicOp(GL_XOR);
        Color.gray.bind();

        {
        glLineWidth(0.1f);
        double t=this.tilesize*MainFrame.camera.getZoom();
        double y=MainFrame.camera.getRelativeY()*MainFrame.camera.getZoom();
        for(double i=t-y%t;i<MainFrame.DISPLAY_HEIGHT;i+=t){//t-(y-Math.floor(y/t)*t)
            glBegin(GL_LINES);
                glVertex2d(0,i);
                glVertex2d(MainFrame.DISPLAY_WIDTH,i);
            glEnd();
        }
        double x=MainFrame.camera.getRelativeX()*MainFrame.camera.getZoom();
        for(double i=t-x%t;i<MainFrame.DISPLAY_WIDTH;i+=t){
            glBegin(GL_LINES);
                glVertex2d(i,0);
                glVertex2d(i,MainFrame.DISPLAY_HEIGHT);
            glEnd();
        }
        }

        if(x!=0&&y!=0&&mode==MODE_BLOCKS){
            Color.white.bind();
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            // OpenGL window coordinates are different from GDI's
            glRecti(x, y, Mouse.getX(), Mouse.getY());
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

            glBegin(GL_LINE_LOOP);
                for(int i=0;i<complexPoints.size();i++){
                    glVertex2i(complexPoints.get(i).getX(),complexPoints.get(i).getY());
                }
            glEnd();

        }
        glDisable(GL_COLOR_LOGIC_OP);
    }

    public void setActive(boolean a){active=a;}
    public boolean getActive(){ return active;}
    public void setTilesize(int a){tilesize=a;}
    public int getTilesize(){ return tilesize;}
    public void setItem(int i){curItem=i;}
    public int getItem(){return curItem;}
    public String getItemName(int i){if(mode==MODE_BLOCKS)return blocks[i];else return entities[i];}
    public int getItemCount(){if(mode==MODE_BLOCKS)return blocks.length;else return entities.length;}
    public int getCurLayer(){return curLayer;}
    public void setCurLayer(int i){curLayer=i;}
    public void setMode(int m){mode=m;curItem=0;}
    public int getMode(){return mode;}

    public void mouseMoved(int x, int y) {}

    public void mousePressed(int button) {}

    public void mouseType(int button) {
        if(!active||!visible)return;
        if(Mouse.getX()<100)return;//temp fix for editor plate.
        if(button==0&&!inComplex){
            x=Mouse.getX();
            y=Mouse.getY();
            if((mode==MODE_BLOCKS)&&(blocks[curItem]=="complexblock"))inComplex=true;
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
        if(Mouse.getX()<100)return;//temp fix for editor plate.

        if(button==1&&!inComplex){
            BElement e=null;
            double x = Mouse.getX()/MainFrame.camera.getZoom() + MainFrame.camera.getRelativeX();
            double y = Mouse.getY()/MainFrame.camera.getZoom() + MainFrame.camera.getRelativeY();
            Object[] ids = new Object[0];
            if(mode==MODE_BLOCKS)ids = Toolkit.joinArray(MainFrame.world.getBlockList(),MainFrame.world.getTileList());
            if(mode==MODE_ENTITIES)ids = MainFrame.world.getEntityList();
            for(int i=ids.length-1;i>=0;i--){
                if(MainFrame.world.getByID((Integer)ids[i]).checkInside(x,y)){
                    e=MainFrame.world.getByID((Integer)ids[i]);
                    break;
                }
            }
            if(e!=null){
                MainFrame.world.delByID(e.wID);
            }
        }

        if(button==0&&inComplex){
            int x = Mouse.getX();
            int y = Mouse.getY();
            complexPoints.add(new Point(x,y));
            return;
        }
        if(button==0||(button==1&&inComplex)){
            x/=MainFrame.camera.getZoom();
            y/=MainFrame.camera.getZoom();
            x+=MainFrame.camera.getRelativeX();
            y+=MainFrame.camera.getRelativeY();
            int bx=(int) (Mouse.getX()/MainFrame.camera.getZoom() + MainFrame.camera.getRelativeX())-x;
            int by=(int) (Mouse.getY()/MainFrame.camera.getZoom() + MainFrame.camera.getRelativeY())-y;
            if(bx<0){bx*=-1;x-=bx;}
            if(by<0){by*=-1;y-=by;}
            if(by<tilesize)by=tilesize;
            bx=roundSampled(bx,tilesize);
            by=roundSampled(by,tilesize);
            x=roundSampled(x,tilesize);
            y=roundSampled(y,tilesize);

            if(bx>0&&by>0){
                HashMap<String,String> args = new HashMap<String,String>();
                args.putAll(Toolkit.stringToMap(((GTextArea)((GPanel)MainFrame.hud.get("p_editor")).get("args")).getText()));
                if(inComplex){
                    if(complexPoints.size()<3)return;//not enough points.
                    for(int i=0;i<complexPoints.size();i++){
                        int tx=complexPoints.get(i).getX(),ty=complexPoints.get(i).getY();
                        tx/=MainFrame.camera.getZoom();
                        ty/=MainFrame.camera.getZoom();
                        tx+=MainFrame.camera.getRelativeX();
                        ty+=MainFrame.camera.getRelativeY();
                        args.put("p"+Toolkit.unifyNumberString(i,3)+"x",tx+"");
                        args.put("p"+Toolkit.unifyNumberString(i,3)+"y",ty+"");
                    }
                    complexPoints.clear();
                    inComplex=false;
                }
                args.put("x", x+"");
                args.put("y", y+"");
                args.put("z", curLayer+"");
                args.put("w", bx+"");
                args.put("h", by+"");
                args.put("a", "");
                if(mode==MODE_BLOCKS){
                    if(blocks[curItem].equals("gameevent")){
                        args.put("type",JOptionPane.showInputDialog("GameEvent Type? 0=none, 1=switch world, 2=switch camera, 3=progress world, 4=save game"));
                        args.put("advance",JOptionPane.showInputDialog("Advance Argument"));
                    }
                    if(blocks[curItem].equals("tileblock")&&!((GTextArea)((GPanel)MainFrame.hud.get("p_editor")).get("args")).getText().contains("tex=")){
                        String tex = JOptionPane.showInputDialog("Texture File");
                        args.put("tex",tex);
                        ((GTextArea)((GPanel)MainFrame.hud.get("p_editor")).get("args")).append("tex="+tex);
                    }
                    MainFrame.elementBuilder.buildElement(blocks[curItem], args);
                }
                else MainFrame.elementBuilder.buildElement(entities[curItem], args);
            }
            x=0;y=0;
        }
    }

}
