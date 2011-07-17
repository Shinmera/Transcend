/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package tile;

import org.newdawn.slick.Color;
import NexT.util.SimpleSet;
import graph.Sound;
import java.io.File;
import java.util.HashMap;
import org.newdawn.slick.openal.SoundStore;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class SoundEmitter extends Tile{
    Sound playable = new Sound();
    float fade = 1;
    String file="";

    public SoundEmitter(){}
    public SoundEmitter(int x,int y){
        playable.setX(x);playable.setY(y);
    }
    public SoundEmitter(int x,int y,double near,double far){
        playable.setX(x);playable.setY(y);playable.setFar(far);playable.setNear(near);
    }

    public void update(){
        if(!playable.isMusic())return;
        if(playable.getFar()>0){
            playable.update();
        }else{
            if(!MainFrame.soundPool.isPlaying(true)){playable.play();fade=0;}
            if(fade<1){fade+=0.01f;SoundStore.get().setMusicVolume(fade);}
        }
    }
    
    public void draw(){
        if(!MainFrame.editor.getActive())return;
        Color.red.bind();
        MainFrame.glCircle2d(playable.getX(),playable.getY(),playable.getFar());
        Color.blue.bind();
        MainFrame.glCircle2d(playable.getX(),playable.getY(),playable.getNear());
    }

    public void setOptions(HashMap<String,String> options){
        if(options.containsKey("far"))playable.setFar(Double.parseDouble(options.get("far")));
        if(options.containsKey("near"))playable.setNear(Double.parseDouble(options.get("near")));
        if(options.containsKey("music"))playable.setMusic(Boolean.parseBoolean(options.get("music")));
        if(options.containsKey("loop"))playable.setLooping(Boolean.parseBoolean(options.get("loop")));
        if(options.containsKey("snd")){
            playable.loadSound(new File(MainFrame.basedir,"snd"+File.separator+options.get("snd")),playable.isMusic());
            file=options.get("snd");
        }
    }
    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> s = new SimpleSet();
        s.put("snd",file);
        s.put("far", playable.getFar()+"");
        s.put("near", playable.getNear()+"");
        s.put("music", playable.isMusic()+"");
        s.put("loop", playable.isLooping()+"");
        return s;
    }
}
