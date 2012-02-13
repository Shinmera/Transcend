/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import transcend.graph.AbstractGraph;
import NexT.util.Vector;
import NexT.util.Toolkit;
import java.util.HashMap;
import NexT.util.SimpleSet;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import org.lwjgl.util.Point;
import transcend.tile.Tile;
import transcend.main.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class CameraPath extends Tile{
    private ArrayList<Point> path = new ArrayList();
    private ArrayList<Double> dist = new ArrayList();
    private double pos = 0;
    private double opos = 0;
    private double speed = 1.0;
    private double total = -1;
    private boolean unclean = true;
    private boolean stopped = true;

    public void init(){
        if(path.size()>0){
            x = path.get(0).getX();
            y = path.get(0).getY();
        }else{
            x = 0;
            y = 0;
        }
    }

    public void addPoint(Point p){path.add(p);unclean=true;}
    public void addPoint(int x,int y){path.add(new Point(x,y));unclean=true;}
    public void delPoint(Point p){path.remove(p);unclean=true;}
    public void delPoint(int i){path.remove(i);unclean=true;}

    public Point getPoint(int i){return path.get(i);}
    public void setPoint(int i,Point p){path.set(i, p);unclean=true;}
    public void setPoint(int i,int x,int y){path.set(i,new Point(x,y));unclean=true;}

    private void calculateTotal(){
        total=0;dist.clear();
        for(int i=1;i<path.size();i++){
            total+=Math.sqrt(Math.pow(path.get(i-1).getX()-path.get(i).getX(),2)+Math.pow(path.get(i-1).getY()-path.get(i).getY(),2));
            dist.add(total);
        }
        unclean=false;
    }

    public boolean isStopped(){return stopped;}
    public void start(){reset();stopped=false;}
    public void reset(){
        pos=0;
        x = path.get(0).getX();
        y = path.get(0).getY();
    }

    public Point getPoint(double pos){
        if(path.size()<=1)return null;
        if(pos>=total)return path.get(path.size()-1);
        if(pos<=0)return path.get(0);
        if(opos==pos)return new Point((int)x,(int)y);

        int i=0;
        while(dist.get(i)<pos)i++;
        Vector a = new Vector(path.get(i).getX(),path.get(i).getY());
        Vector b = new Vector(path.get(i+1).getX(),path.get(i+1).getY());
        Vector d = b.subtract(a);
        d.stretch(pos-dist.get(i));
        d = d.add(b);
        
        x=d.getX();
        y=d.getY();
        opos=pos;
        return new Point((int)x,(int)y);
    }

    public SimpleSet getOptions(){
        SimpleSet<String,String> set = new SimpleSet();
        for(int i=0;i<path.size();i++){
            set.put(i+"",path.get(i).getX()+" "+path.get(i).getY());
        }
        return set;
    }

    public void setOptions(HashMap<String,String> set){
        SimpleSet<String,String> set2 = new SimpleSet<String,String>(set);
        set2.sort();
        path.clear();
        for(int i=0;i<set2.size();i++){
            if(Toolkit.isNumeric(set2.getKey(i)))
                path.add(new Point(Integer.parseInt(set2.getAt(i).split(" ")[0]),Integer.parseInt(set2.getAt(i).split(" ")[1])));
        }
        if(set.containsKey("speed"))speed=Double.parseDouble(set.get("speed"));
        calculateTotal();
    }

    public void update(){
        if(stopped)return;
        if(unclean)calculateTotal();
        if(pos>=total)stopped=true;
        pos+=speed;
        getPoint(pos);
    }

    public void draw(){
        if(!MainFrame.editor.getActive())return;
        for(int i=0;i<path.size();i++){
            Color.white.bind();
            if(i+1<path.size()){
                glBegin(GL_LINES);
                    glVertex2d(path.get(i).getX(),path.get(i).getY());
                    glVertex2d(path.get(i+1).getX(),path.get(i+1).getY());
                glEnd();
            }
            Color.red.bind();
            AbstractGraph.glCircle2d(path.get(i).getX(),path.get(i).getY(), 2);
        }
        Color.blue.bind();
        AbstractGraph.glCircle2d(x,y,3);
    }
}
