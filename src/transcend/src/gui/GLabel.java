/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import transcend.MainFrame;
import java.awt.Font;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class GLabel extends GObject{
    public static int ALIGN_CENTER = TrueTypeFont.ALIGN_CENTER;
    public static int ALIGN_LEFT = TrueTypeFont.ALIGN_LEFT;
    public static int ALIGN_RIGHT = TrueTypeFont.ALIGN_RIGHT;

    String text = "";
    String fontType = "Arial";
    int fontSize = 12;
    int fontWeight = Font.PLAIN;
    int fontAlign = ALIGN_CENTER;
    Color fore = new Color(0,0,0,255);
    TrueTypeFont font;

    public GLabel(){font=MainFrame.fontPool.loadFont(fontType,fontWeight,fontSize);}
    public GLabel(String text){this();this.text=text;}
    public GLabel(String text,int align){this(text);this.fontAlign=align;}
    
    public void setFont(Font f){this.font=new TrueTypeFont(f,false);}
    public void setFontWeight(int weight){
        this.fontWeight = weight;
        setFont(new Font(fontType, fontWeight, fontSize));
    }
    public void setFontSize(int size){
        this.fontSize = size;
        setFont(new Font(fontType, fontWeight, fontSize));
    }
    public void setFontType(String type){
        this.fontType = type;
        setFont(new Font(fontType, fontWeight, fontSize));
    }
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }
    public TrueTypeFont getFont(){ return font;}

    public void setForeground(Color color){
        this.fore=color;
    }
    public void setAlign(int align){
        fontAlign=align;
    }
    public int getAlign(){
        return fontAlign;
    }

    public Color getForeground(){return fore;}

    public void sPaint(){super.paint();}
    public void paint(){
        if(!visible)return;
        super.paint();
        
        fore.bind();
        if(fontAlign==ALIGN_LEFT)font.drawString(x, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_LEFT);
        if(fontAlign==ALIGN_CENTER)font.drawString(x+w/2, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_CENTER);
        if(fontAlign==ALIGN_RIGHT)font.drawString(x+w, y+h/2-font.getLineHeight()/2, text, 1,1,ALIGN_RIGHT);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }


}
