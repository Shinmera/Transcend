/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import event.MouseListener;
import static org.lwjgl.opengl.GL11.*;

public class GButton extends GLabel implements MouseListener{

    public GButton(){}

    public void paint(){
        super.paint();
    }

    public void mouseMoved(int x, int y) {}
    public void mousePressed(int button) {}
    public void mouseReleased(int button) {}

}
