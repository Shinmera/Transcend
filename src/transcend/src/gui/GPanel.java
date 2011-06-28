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

public class GPanel extends GObject{
    private int x,y,w,h;
    private ArrayList<GObject> regs = new ArrayList();
    private boolean visible = false;

    public GPanel(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public void add(GObject i){
        regs.add(i);
    }

    public void remove(GObject i){
        regs.remove(i);
    }

    public void setVisible(boolean mod){visible=mod;}
    public boolean isVisible(){return visible;}

    public void paint(){
        if(visible){
            super.paint();
            for(int i=0;i<regs.size();i++){regs.get(i).paint();}
        }
    }
}
