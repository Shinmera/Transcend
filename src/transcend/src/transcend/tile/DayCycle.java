/**********************\
  file: DayCycle.java
  package: tile
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.tile;
import NexT.util.SimpleSet;
import transcend.gui.GLabel;
import java.util.HashMap;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class DayCycle extends Tile{
    public static final int PART_DAY=0x00;
    public static final int PART_EVENING=0x01;
    public static final int PART_NIGHT=0x02;
    public static final int PART_MORNING=0x03;
    public static final int MAX_TIME = 24*60*60;
    public static final Color ABS_NIGHT = new Color( 41,  1, 93);
    public static final Color ABS_DAY   = new Color(163,239,255);
    private int part = PART_EVENING;
    private int time = 0;
    private int full = 8;

    public DayCycle(){z=-5;}
    public DayCycle(int time){this();this.time=time;}

    public int getPartOfDay(){return part;}
    public int getTime(){return time;}
    public int getFull(){return full;}
    public void setTime(int time){this.time=time;}
    public void setFull(int full){this.full=full;}

    public void update(){
        time++;
        if(time>=MAX_TIME)time=0;
        if(time<MAX_TIME/full)                  part=PART_EVENING;
        else if(time<MAX_TIME/2)                part=PART_NIGHT;
        else if(time<MAX_TIME/full*(full/2+1))  part=PART_MORNING;
        else                                    part=PART_DAY;
    }

    public void draw(){
        MainFrame.camera.camEnd();
        switch(part){
            case PART_EVENING: mix(ABS_NIGHT,ABS_DAY,time*full*100.0/MAX_TIME).bind();break;
            case PART_NIGHT: ABS_NIGHT.bind();break;
            case PART_MORNING:mix(ABS_DAY,ABS_NIGHT,(time-MAX_TIME/2)*full*100.0/MAX_TIME).bind();break;
            case PART_DAY: ABS_DAY.bind();break;
        }
        glBegin(GL_QUADS);
            glVertex2i(0,0);
            glVertex2i(0                      ,MainFrame.DISPLAY_WIDTH);
            glVertex2i(MainFrame.DISPLAY_WIDTH,MainFrame.DISPLAY_WIDTH);
            glVertex2i(MainFrame.DISPLAY_WIDTH,0);
        glEnd();
        MainFrame.camera.camBegin();
    }

    public Color mix(Color a,Color b,double percentage){
        double pa = percentage/100.0;
        double pb = (100-percentage)/100.0;
        return new Color((int)(a.getRed()*pa+b.getRed()*pb),
                         (int)(a.getGreen()*pa+b.getGreen()*pb),
                         (int)(a.getBlue()*pa+b.getBlue()*pb));
    }

    public SimpleSet<String, String> getOptions() {
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        set.put("time",time+"");
        set.put("full",full+"");
        return set;
    }

    public void setOptions(HashMap<String, String> options) {
        if(options.containsKey("time"))time=Integer.parseInt(options.get("time"));
        if(options.containsKey("full"))full=Integer.parseInt(options.get("full"));
        
    }
}
