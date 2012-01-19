/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import transcend.Const;

public class SoundPool {
    HashMap<String,Audio> sounds = new HashMap();
    private String curMusic = "";
    private String curSound = "";

    public SoundPool(){}
    
    public void clearPool(){sounds.clear();curMusic="";curSound="";}

    public Audio loadSound(String name,File f){
        return loadSound(name,f,false);
    }

    public Audio loadSound(String name,File f,boolean asStream){
        if(isLoaded(name))return getSound(name);
        return reloadSound(name,f,asStream);
    }

    public Audio reloadSound(String name,File f){
        return reloadSound(name,f,false);
    }

    public Audio reloadSound(String name,File f,boolean asStream){
        if(!f.exists()){Const.LOGGER.warning("[SoundPool] Trying to load non-existant texture '"+name+"'.");return null;}
        Const.LOGGER.info("[SoundPool] Reloading "+name+" at "+f.getAbsolutePath());
        try{
            String extension = f.getName().substring(f.getName().indexOf(".")+1);
            Audio audio;
            if(asStream&&extension.toUpperCase().equals("OGG"))audio = AudioLoader.getStreamingAudio(extension.toUpperCase(),f.toURI().toURL());
            else audio = AudioLoader.getAudio(extension.toUpperCase(), new FileInputStream(f));
            sounds.put(name, audio);
            return audio;
        }catch (IOException ex) {Const.LOGGER.log(Level.SEVERE,"[SoundPool] Failed to load sound at "+f.getAbsolutePath()+".",ex);return null;}
    }

    public boolean isLoaded(String name){
        return sounds.containsKey(name);
    }

    public Audio getSound(String name){
        return sounds.get(name);
    }

    public void delSound(String name){
        sounds.remove(name);
    }

    public void playSound(String name,boolean music,boolean loop){
        if(!sounds.containsKey(name))return;
        if(music){
            if(!curMusic.equals(""))sounds.get(name).stop();
            sounds.get(name).playAsMusic(1, 1, loop);
            curMusic=name;
        } else {
            if(!curSound.equals(""))sounds.get(name).stop();
            sounds.get(name).playAsSoundEffect(1, 1, loop);
            curSound=name;
        }
    }

    public void stopSound(String name){
        if(!isPlaying(name))return;
        sounds.get(name).stop();
        if(curMusic.equals(name))curMusic="";
        if(curSound.equals(name))curSound="";
    }

    public boolean isPlaying(){
        if(!curMusic.equals(""))return true;
        if(!curSound.equals(""))return true;
        return false;
    }

    public boolean isPlaying(boolean music){
        if(!curMusic.equals("")&&music)return true;
        if(!curSound.equals("")&&!music)return true;
        return false;
    }

    public boolean isPlaying(String name){
        if(curMusic.equals(name))return true;
        if(curSound.equals(name))return true;
        return false;
    }
}
