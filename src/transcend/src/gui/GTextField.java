/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import org.lwjgl.input.Mouse;
import event.MouseListener;
import org.lwjgl.input.Keyboard;
import event.KeyboardListener;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GTextField extends GLabel implements KeyboardListener,MouseListener{
    private boolean focus=false;

    public GTextField(){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);}
    public GTextField(String text){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);this.text=text;}

    public void paint(){
        super.sPaint();

        fore.bind();
        font.drawString(x+5, y+h/2-font.getLineHeight()/2, text, 1,1, TrueTypeFont.ALIGN_LEFT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public void onConfirm(){}

    public void keyPressed(int key) {}
    public void keyType(int key) {
        if(!focus)return;
        switch(key){
            case Keyboard.KEY_BACK:
                if (text.length() > 0) {text = text.substring(0, text.length() - 1);}
                break;
            case Keyboard.KEY_RETURN:
                onConfirm();
                break;
        }
        if((key>=0x10&&key<=0x19)||(key>=0x1E&&key<=0x26)||(key>=0x2C&&key<=0x32)||(key>=0x2&&key<=0x9)){
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_CAPITAL))
                text+=Keyboard.getKeyName(key);
            else text+=Keyboard.getKeyName(key).toLowerCase();
        }
    }
    public void keyReleased(int key) {}

    public void mouseMoved(int x, int y) {}
    public void mousePressed(int button) {}
    public void mouseType(int button) {
        if(Mouse.getX()>x&&Mouse.getY()>y&&
                Mouse.getX()<x+w&&Mouse.getY()<y+h)
            focus=true;
        else
            focus=false;
    }
    public void mouseReleased(int button) {}

}
