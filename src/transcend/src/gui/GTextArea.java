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
import NexT.util.Toolkit;
import org.lwjgl.input.Mouse;
import event.MouseListener;
import org.lwjgl.input.Keyboard;
import event.KeyboardListener;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class GTextArea extends GLabel implements KeyboardListener,MouseListener{
    private boolean focus=false;
    private int cc=0,cr=0;

    public GTextArea(){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);}
    public GTextArea(String text){super();MainFrame.ieh.addKeyboardListener(this);MainFrame.ieh.addMouseListener(this);this.text=text;}

    public void paint(){
        super.sPaint();

        fore.bind();
        font.drawString(x+5, y+h-font.getLineHeight()-5, insertAt("|",cc,cr), 1,1, TrueTypeFont.ALIGN_LEFT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }

    public String getLine(int n){
        String[] text = this.text.split("\n");
        if(n<text.length)return text[n];
        else return "";
    }
    public int getLines(){return text.split("\n").length;}
    public int getPosition(int c,int r){
        String[] text = this.text.split("\n");
        int n=c;
        for(int i=0;i<r;i++)n+=text[i].length();
        return n;
    }
    public String insertAt(String t,int c,int r){
        if(getPosition(c,r)>=text.length())return text+t;
        String[] text = this.text.split("\n");
        String a = text[r].substring(0,c);
        String b = text[r].substring(c);
        text[r] = a+t+b;
        return Toolkit.implode(text,"\n");
    }
    public String insertAt(String t,int i){
        if(i>=text.length())return text+t;
        String a = text.substring(0,i);
        String b = text.substring(i);
        return a+t+b;
    }
    public String removeFrom(int n,int c,int r){
        String text = this.text.split("\n")[r];
        char[] a = Arrays.copyOfRange(text.toCharArray(), 0,c);
        char[] b = Arrays.copyOfRange(text.toCharArray(), c+n,text.length());
        return new String(a)+new String(b);
    }
    public String removeFrom(int n,int i){
        if(i+n>=text.length())return text.substring(0,text.length()-n);
        char[] a = Arrays.copyOfRange(text.toCharArray(), 0,i);
        char[] b = Arrays.copyOfRange(text.toCharArray(), i+n,text.length());
        return new String(a)+new String(b);
    }

    public void onType(){}

    public void keyPressed(int key) {}
    
    public void keyType(int key) {
        if(!focus)return;
        switch(key){
            case Keyboard.KEY_BACK:
                if (text.length() > 0) {
                    if(cc>0){
                        cc--;
                        text=removeFrom(1,getPosition(cc,cr));
                    } else if (cr > 0) {
                        cr--;
                        cc=getLine(cr).length();
                        text=removeFrom(1,getPosition(cc,cr));
                    }
                }
                break;
            case Keyboard.KEY_RETURN:
                text=insertAt(" \n",cc,cr);
                cr++;
                break;
            case Keyboard.KEY_LEFT:
                if(cc>0)cc--;
                else if(cr>0){
                    cr--;
                    cc=getLine(cr).length();
                }
                break;
            case Keyboard.KEY_RIGHT:
                if(cc<getLine(cr).length())cc++;
                else if(cr<getLines()){
                    cr++;
                    cc=0;
                }
                break;
            default:
                text=insertAt(MainFrame.ieh.parseKeyToText(key),cc,cr);
                cc++;
                break;
        }
        onType();
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