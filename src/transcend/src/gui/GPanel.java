/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class GPanel extends GObject{
    private int x,y,w,h;
    private ArrayList<GObject> regs;

    public GPanel(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void add(GObject i){
        regs.add(i);
    }

    public void remove(GObject i){
        regs.remove(i);
    }

    public void paint(){
        gluOrtho2D(x,w,y,h);
        super.paint();
        for(int i=0;i<regs.size();i++){regs.get(i).paint();}
    }
}
