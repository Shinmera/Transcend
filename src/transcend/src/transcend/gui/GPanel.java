/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;
import NexT.util.SimpleSet;

public class GPanel extends GObject{
    private SimpleSet<String,GObject> regs = new SimpleSet<String,GObject>();

    public GPanel(int x,int y,int w,int h){
        this.x=x;this.y=y;this.w=w;this.h=h;
    }

    public int add(GObject i){
        i.setBounds(i.x+x,i.y+y,i.w,i.h);
        regs.put(regs.size()+"",i);
        return regs.size()-1;
    }
    public int add(GObject i,String name){
        i.setBounds(i.x+x,i.y+y,i.w,i.h);
        regs.put(name,i);
        return regs.size()-1;
    }

    public GObject get(String name){
        return regs.get(name);
    }
    public GObject get(int i){
        return regs.getAt(i);
    }
    
    public void clear(){
        regs.clear();
    }

    public int size(){return regs.size();}
    public int getHeight(){return h;}
    public int getWidth(){return w;}
    public int getX(){return x;}
    public int getY(){return y;}

    public void remove(GObject i){
        regs.removeAt(regs.indexOfValue(i));
    }

    public void setBounds(int x,int y,int w,int h){
        for(int i=0;i<regs.size();i++){regs.getAt(i).setBounds(regs.getAt(i).x+x-this.x,regs.getAt(i).y+y-this.y,regs.getAt(i).w,regs.getAt(i).h);}
        super.setBounds(x,y,w,h);
    }

    public void setVisible(boolean mod){
        visible=mod;
        for(int i=0;i<regs.size();i++){regs.getAt(i).setVisible(mod);}
    }

    public void paint(){
        if(!visible)return;
        super.paint();
        for(int i=0;i<regs.size();i++){regs.getAt(i).paint();}
    }
    
}
