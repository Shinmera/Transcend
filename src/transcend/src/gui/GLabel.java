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
import org.newdawn.slick.TrueTypeFont;


public class GLabel extends GObject{
    String text = "";
    String fontType = "Arial";
    int fontSize = 12;
    int fontWeight = Font.PLAIN;
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

    public void paint(){
        super.paint();
        font.drawString(x+w/2-font.getWidth(text)/2, y+h/2-font.getHeight(text)/2, text, fore);
    }


}
