/**********************\
  file: HPowerBar.java
  package: hud
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import graph.Form;
import static org.lwjgl.opengl.GL11.*;

public class HPowerBar extends GImage{
    GBar healthBar = new GBar();
    GBar powerBar = new GBar();

    public HPowerBar(){
        super("health_bar.png");
        Form form = Form.getSquare(0, 0, 52, 22, 53, 22, 53, 0);
        form.setAlign(Form.ALIGN_RIGHT);
        healthBar.setForm(form);
        healthBar.setWidth(250);
        healthBar.setValue(70);
        healthBar.setVisible(true);
        form = Form.getRectangle(0, 12);
        form.setAlign(Form.ALIGN_RIGHT);
        powerBar.setForm(form);
        powerBar.setWidth(90);
        powerBar.setHeight(12);
        powerBar.setValue(10);
        powerBar.setVisible(true);
    }

    public void paint(){
        glPushMatrix();
            glTranslated(x+255,y,0);
            healthBar.paint();
        glPopMatrix();
        glPushMatrix();
            glTranslated(x+255+60,y+25,0);
            powerBar.paint();
        glPopMatrix();
        super.paint();
    }
}
