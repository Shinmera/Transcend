/**********************\
  file: FitBlock.java
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.tile;

import NexT.util.SimpleSet;
import transcend.graph.AbstractGraph;
import java.util.HashMap;
import transcend.main.MainFrame;
import org.newdawn.slick.Color;

public class FitBlock extends Tile{
    String tex;
    double scale=1.0;
    public FitBlock(){}

    public FitBlock(int x,int y,int w,int h,String tex){
        super(x,y,w,h);
        this.tex=tex;
        drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+tex));
    }

    public void setTexture(String tex){
        if(tex.length()==0||tex.equals("null"))return;
        this.tex=tex;
        drawable.loadTexture(MainFrame.fileStorage.getFile("tex/"+tex));
        drawable.setRelW(scale);
        drawable.setRelH(scale);
        drawable.setTileW(1);
        drawable.setTileH(1);
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("scale"))scale=Double.parseDouble(options.get("scale"));
        if(options.containsKey("tex")){
            if(options.get("tex").length()==0)MainFrame.world.delByID(wID);
            setTexture(options.get("tex"));
        }else{
            MainFrame.world.delByID(wID);
        }
    }

    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = super.getOptions();
        set.put("tex",tex);
        set.put("scale",scale+"");
        if((tex==null)||(tex.length()==0))set.put("nosave","1");
        return set;
    }

    public void draw(){
        drawable.draw((int)x,(int)y,w,h);
        if(depth!=0&&z!=0){
            if(z<0){
                new Color(0f,0f,0f,(float)(-z/7.0*depth)).bind();
                AbstractGraph.glRectangle2d(x, y, w, h);
                new Color(1f,1f,1f,(float)(z/10.0*depth)).bind();
                AbstractGraph.glRectangle2d(x, y, w, h);
            }
        }
    }
}
