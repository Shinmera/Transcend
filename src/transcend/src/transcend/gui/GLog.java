/**********************\
  file: GLog.java
  package: transcend.gui
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.gui;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;

public class GLog extends GLabel{
    private ArrayBlockingQueue<String> log = new ArrayBlockingQueue<String>(20);
    private int timer = 0;
    private int nTimer = 0;
    
    public GLog(){
        back = new Color(0.0f,0.0f,0.0f,0.75f);
        fore = new Color(1.0f,1.0f,1.0f,1.0f );
    }
    
    public void addMessage(String msg){
        if(log.remainingCapacity()==0)log.poll();
        log.offer(msg);
        nTimer=MainFrame.fps*3;
    }
    
    public void paint(){
        if(!MainFrame.CONST.gBoolean("LOG"))return;
        if(timer<=0){timer=nTimer;nTimer=0;log.poll();}
        if(log.size()==0)return;
        h = log.size()*22+20;
        super.paint();
        
        fore.bind();
        Iterator<String> it = log.iterator();int i=0;
        while(it.hasNext()){
            font.drawString(x+10, y+10+i*22, it.next(), 1,1);
            i++;
        }
        glBindTexture(GL_TEXTURE_2D, 0); //release
        timer--;
    }
}
