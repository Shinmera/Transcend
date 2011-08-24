/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import NexT.util.SimpleSet;
import java.io.File;
import java.util.HashMap;
import transcend.MainFrame;
import world.Element;

public class Background extends Tile{
    private String tex="";
    private double vsp=1;
    private boolean tile=false;

    public Background(){}
    public Background(int x,int y,int w,int h,String tex){
        this.x=x;this.y=y;this.z=-5;this.w=w;this.h=h;
        this.tex=tex;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+tex));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(w, h);
    }

    public Background(int x,int y,int z,int w,int h,String tex){
        this.x=x;this.y=y;this.z=z;this.w=w;this.h=h;
        this.tex=tex;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+tex));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(w, h);
    }

    public void setTexture(String tex){
        this.tex=tex;
        drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+tex));
        drawable.setSpritesize(drawable.getTexture().getImageWidth());
        drawable.calcTile(w, h);
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
        if(options.containsKey("tex")){
            drawable.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+options.get("tex")));
            drawable.setSpritesize(drawable.getTexture().getImageWidth());
            drawable.calcTile(w, h);
        }
        if(options.containsKey("tile"))setTiled(Boolean.parseBoolean(options.get("tile")));
        if(options.containsKey("vsp"))setVSP(Double.parseDouble(options.get("vsp")));
    }

    public void draw(){
        drawable.draw((int)MainFrame.camera.getRelativeX(),(int)MainFrame.camera.getRelativeY(),
                      (int)(MainFrame.DISPLAY_WIDTH/MainFrame.camera.getZoom()),(int)(MainFrame.DISPLAY_HEIGHT/MainFrame.camera.getZoom()));
    }
}
