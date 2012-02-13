/**********************\
  file: GBar.java
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import transcend.graph.Form;

public class GBar extends GLabel{
    private Form form;
    private double value=0,max=100,min=0;
    private boolean drawText=false;

    public GBar(){}
    public GBar(Form form){this.form=form;}

    public void setMaximum(double max){this.max=max;}
    public void setMinimum(double min){this.min=min;}
    public void setValue(double value){this.value=value;}
    public void setDrawText(boolean flag){drawText=flag;}
    public void setForm(Form form){this.form=form;}

    public double getMaximum(){return max;}
    public double getMinimum(){return min;}
    public double getValue(){return value;}
    public double getPercentage(){return (value-min)/(max-min)*100.0;}
    public Form getForm(){return form;}
    public boolean isTextDrawn(){return drawText;}

    public void paint(){
        if(!visible)return;
        back.bind();
        form.setWidth(w*getPercentage()/100.0);
        form.setHeight(h);
        form.draw(x, y);

        if(drawText){
            fore.bind();
            if(text.equals(""))font.drawString(x+w/2, y+h/2, getPercentage()+"%", 1,1);
            else               font.drawString(x+w/2, y+h/2, text, 1,1);
        }
    }
}
