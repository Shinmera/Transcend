/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;

import java.awt.Font;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;

public class GLabel extends GObject{
    String text = "";
    String fontType = "Arial";
    int fontSize = 12;
    int fontWeight = Font.PLAIN;
    Color fore = new Color(0,0,0,255);
    TrueTypeFont font = new TrueTypeFont(new Font(fontType, fontWeight, fontSize),false);

    public GLabel(){}
    public GLabel(String text){this.text=text;}
    
    public void setFont(Font f){this.font=new TrueTypeFont(f,false);}
    public void setFontWeight(int weight){
        this.fontWeight = weight;
        font = new TrueTypeFont(new Font(fontType, fontWeight, fontSize),false);
    }
    public void setFontSize(int size){
        this.fontSize = size;
        font = new TrueTypeFont(new Font(fontType, fontWeight, fontSize),false);
    }
    public void setFontType(String type){
        this.fontType = type;
        font = new TrueTypeFont(new Font(fontType, fontWeight, fontSize),false);
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

    public Color getForeground(){return fore;}

    public void sPaint(){super.paint();}
    public void paint(){
        if(!visible)return;
        super.paint();
        
        fore.bind();
        font.drawString(x+w/2, y+h/2-font.getLineHeight()/2, text, 1,1, TrueTypeFont.ALIGN_CENTER);
        glBindTexture(GL_TEXTURE_2D, 0); //release
    }


}
