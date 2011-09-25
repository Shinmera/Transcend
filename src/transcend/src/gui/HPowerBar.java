/**********************\
  file: HPowerBar.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import graph.AbstractGraph;
import org.newdawn.slick.Color;
import transcend.MainFrame;
import graph.Form;
import static org.lwjgl.opengl.GL11.*;

public class HPowerBar extends GImage{
    private GBar healthBar = new GBar();
    private GBar powerBar = new GBar();

    public HPowerBar(){
        super("health_bar");
        Form form = Form.getSquare(0, 0, 58, 22, 58, 22, 58, 0);
        form.setAlign(Form.ALIGN_RIGHT);
        healthBar.setForm(form);
        healthBar.setWidth(250);
        healthBar.setVisible(true);
        form = Form.getRectangle(0, 12);
        form.setAlign(Form.ALIGN_RIGHT);
        powerBar.setForm(form);
        powerBar.setWidth(90);
        powerBar.setHeight(12);
        powerBar.setVisible(true);
    }

    public void paint(){
        if(!isVisible())return;
        healthBar.setValue(MainFrame.player.getHealth());
        powerBar.setValue(MainFrame.player.getPower());
        
        healthBar.setBackground(new Color(1.0f,(float)(MainFrame.player.getHealth()/100.0),(float)(MainFrame.player.getHealth()/100.0)));
        powerBar.setBackground(new Color((float)(MainFrame.player.getPower()/100.0),(float)(MainFrame.player.getPower()/100.0),1.0f));
        glPushMatrix();
            glTranslated(x+255,y,0);
            healthBar.paint();
        glPopMatrix();
        glPushMatrix();
            glTranslated(x+255+60,y+25,0);
            powerBar.paint();
        glPopMatrix();

        Color.white.bind();
        for(int i=MainFrame.player.getLifes();i>0;i--){
            AbstractGraph.glFCircle2d(x+320-i*15, y+43, 3.5);
        }

        super.paint();
    }
}
