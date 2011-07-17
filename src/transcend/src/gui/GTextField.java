/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import java.util.Arrays;
import org.lwjgl.input.Mouse;
import event.MouseListener;
import org.lwjgl.input.Keyboard;
import event.KeyboardListener;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GTextField extends GLabel implements KeyboardListener,MouseListener{
    private boolean focus=false;
    int cc=0;

    public GTextField(){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);}
    public GTextField(String text){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);this.text=text;}

    public void paint(){
        super.sPaint();

        fore.bind();
        font.drawString(x+5, y+h/2-font.getLineHeight()/2, insertAt("|",cc), 1,1, TrueTypeFont.ALIGN_LEFT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public String insertAt(String t,int c){
        if(c>=text.length())return text+t;
        String a = text.substring(0,c);
        String b = text.substring(c);
        return a+t+b;
    }
    public String removeFrom(int n,int c){
        if(c>=text.length())return text.substring(0,text.length()-1);
        char[] a = Arrays.copyOfRange(text.toCharArray(), 0,c);
        char[] b = Arrays.copyOfRange(text.toCharArray(), c+n,text.length());
        return new String(a)+new String(b);
    }

    public void onConfirm(){}

    public void keyPressed(int key) {}
    public void keyType(int key) {
        if(!focus)return;
        switch(key){
            case Keyboard.KEY_BACK:
                if (text.length() > 0) {
                    if(cc>0){
                        cc--;
                        text=removeFrom(1,cc);
                    }
                }
                break;
            case Keyboard.KEY_RETURN:
                onConfirm();
                break;
            case Keyboard.KEY_LEFT:
                if(cc>0)cc--;
                break;
            case Keyboard.KEY_RIGHT:
                if(cc<text.length())cc++;
                break;
            default:
                text=insertAt(MainFrame.ieh.parseKeyToText(key),cc);
                cc++;
                break;
        }
    }
    public void keyReleased(int key) {}

    public void mouseMoved(int x, int y) {}
    public void mousePressed(int button) {}
    public void mouseType(int button) {
        if(Mouse.getX()>x&&Mouse.getY()>y&&
                Mouse.getX()<x+w&&Mouse.getY()<y+h){
            MainFrame.pause=true;
            focus=true;
        } else {
            if(focus)MainFrame.pause=false;
            focus=false;
        }
    }
    public void mouseReleased(int button) {}

}
