/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: graph
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.graph;

import java.io.File;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.SoundStore;
import transcend.main.MainFrame;

public class Sound {
    private String audio = "";
    private int x=0,y=0;
    private double far=0,near=0;
    private boolean music=false,loop=false;
    private float vol=1;
    private int a = 0;

    public Sound(){}
    public Sound(int x,int y){this.x=x;this.y=y;}
    public Sound(int x,int y,boolean music){this.x=x;this.y=y;this.music=music;}
    public Sound(int x,int y,double far,double near){this.x=x;this.y=y;this.far=far;this.near=near;}
    public Sound(int x,int y,double far,double near,boolean music){this.x=x;this.y=y;this.far=far;this.near=near;this.music=music;}

    public boolean loadSound(File f){
        if(!f.exists())return false;
        audio=f.getName();
        MainFrame.soundPool.loadSound(f.getName(), f);
        return true;
    }

    public boolean loadSound(File f,boolean stream){
        if(!f.exists())return false;
        audio=f.getName();
        MainFrame.soundPool.loadSound(f.getName(), f,stream);
        return true;
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public double getFar(){return far;}
    public double getNear(){return near;}
    public float getVolume(){return vol;}
    public boolean isMusic(){return music;}
    public boolean isLooping(){return loop;}
    public boolean isPlaying(){
        if(audio=="")return false;
        return MainFrame.soundPool.isPlaying(audio);
    }

    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}
    public void setFar(double far){this.far=far;}
    public void setNear(double near){this.near=near;}
    public void setMusic(boolean music){this.music=music;}
    public void setLooping(boolean loop){this.loop=loop;}

    public void update(){
        if(a==0)a=(int)(Math.random()*100);
        if(far!=0){
            double d = Math.sqrt(Math.pow(x-MainFrame.player.x,2)+Math.pow(y-MainFrame.player.y,2));
            if(d>=far){
                if(MainFrame.soundPool.isPlaying(audio)){
                    System.out.println(a+" TURNING OFF!");
                    stop();
                }return;
            } else if(!MainFrame.soundPool.isPlaying(audio)){
                System.out.println(a+" TURNING ON!");
                play();
            }
            if(d<=near){vol=1;return;}
            vol=1.0f - (float) (d/far);
            if(music)SoundStore.get().setMusicVolume(vol);
            else SoundStore.get().setSoundVolume(vol);
        }
    }

    public void play(){
        if(audio=="")return;
        MainFrame.soundPool.playSound(audio, music, loop);
    }

    public void stop(){
        if(audio=="")return;
        MainFrame.soundPool.stopSound(audio);
    }
}
