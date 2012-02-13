/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.tile;

import NexT.util.SimpleSet;
import java.io.File;
import java.util.HashMap;
import transcend.main.MainFrame;
import transcend.world.Element;

public class Background extends Tile{
    private String tex="";
    private double vsp=1;
    private boolean tile=false;
    private boolean stretch=false;

    public Background(){}
    public Background(int x,int y,int w,int h,String tex){
        this.x=x;this.y=y;this.z=-5;this.w=w;this.h=h;
        setTexture(tex);
    }

    public Background(int x,int y,int z,int w,int h,String tex){
        this(x,y,w,h,tex);
        this.z=z;
    }

    public void setTexture(String tex){
        this.tex=tex;
        drawable.loadTexture(MainFrame.fileStorage.getFile("bg/"+tex));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        if(stretch){
            drawable.setTileH(1);
            drawable.setTileW(1);
            drawable.setRelH(1);
            drawable.setRelW(1);
        } else drawable.calcTile(w, h);
    }

    public void setVSP(double vsp){this.vsp=vsp;}
    public double getVSP(){return vsp;}
    public void setTiled(boolean tile){
        this.tile=tile;
        if(tile)drawable.calcTile(MainFrame.DISPLAY_WIDTH, MainFrame.DISPLAY_HEIGHT);
        else drawable.calcTile(w, h);
    }
    public boolean isTiled(){return tile;}

    public SimpleSet getOptions(){
        SimpleSet<String,String> set = new SimpleSet();
        set.put("tex",tex);
        set.put("vsp",vsp+"");
        set.put("tile",tile+"");
        return set;
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("tile"))setTiled(Boolean.parseBoolean(options.get("tile")));
        if(options.containsKey("stretch"))stretch = Boolean.parseBoolean(options.get("stretch"));
        if(options.containsKey("vsp"))setVSP(Double.parseDouble(options.get("vsp")));
        if(options.containsKey("tex"))setTexture(options.get("tex"));
    }

    public void draw(){
        MainFrame.camera.camEnd();
        drawable.draw(0,0,MainFrame.DISPLAY_WIDTH,MainFrame.DISPLAY_HEIGHT);
        MainFrame.camera.camBegin();
    }
}
