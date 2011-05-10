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
import org.newdawn.slick.TrueTypeFont;
import static org.lwjgl.opengl.GL11.*;

public class GLabel extends GObject{
    String text = "";
    String fontType = "Arial";
    int fontSize = 12;
    int fontWeight = Font.PLAIN;
    Color fore = new Color(0,0,0,0);
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

    public void setForeground(Color color){
        this.fore=color;
    }

    public void paint(){
        super.paint();
        //glColor4f(fore.getRed()/255f,fore.getGreen()/255f,fore.getBlue()/255f,fore.getAlpha()/255f);
        font.drawString(x+w/2-font.getWidth(text)/2, y+h/2-font.getHeight(text)/2, text, fore);
    }


}
