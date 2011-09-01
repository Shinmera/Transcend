/**********************\
  file: Water.java
  package: block
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package block;

import NexT.util.SimpleSet;
import NexT.util.Toolkit;
import java.util.HashMap;
import org.newdawn.slick.Color;

public class Water extends Block{
    private double viscosity = 0.8;
    private Color color = new Color(50,50,220,100);

    public void setViscosity(double v){viscosity=v;}
    public void setColor(Color c){color=c;}

    public double getViscosity(){return viscosity;}
    public Color getColor(){return color;}

    public void draw(){
        color.bind();
        super.draw();
    }

    public void setOptions(HashMap<String,String> options){
        super.setOptions(options);
        if(options.containsKey("viscosity")){viscosity=Double.parseDouble(options.get("viscosity"));}
        if(options.containsKey("color")){
            java.awt.Color c = Toolkit.toColor(options.get("color"));
            color = new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        }
    }

    public SimpleSet<String,String> getOptions(){
        SimpleSet<String,String> set = new SimpleSet<String,String>();
        set.put("viscosity",viscosity+"");
        set.put("color",color.getRed()+","+color.getGreen()+","+color.getBlue());
        return set;
    }
}
