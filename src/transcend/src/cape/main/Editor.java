/**********************\
  file: Editor.java
  package: cape.main
  author: Shinmera
  team: NexT
  license: -
\**********************/

package cape.main;

import NexT.util.Toolkit;
import NexT.util.Vector2;
import static cape.main.MainFrame.*;
import cape.physics.Block;
import cape.physics.Entity;
import cape.physics.form.Circle;
import cape.physics.form.Line;
import cape.physics.form.Polygon;
import cape.physics.form.Rectangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import transcend.event.KeyboardListener;
import transcend.event.MouseListener;
import transcend.graph.AbstractGraph;

public class Editor implements MouseListener,KeyboardListener{
    public static final int MODE_LINE = 1;
    public static final int MODE_CIRCLE = 2;
    public static final int MODE_RECTANGLE = 3;
    public static final int MODE_POLYGON = 4;
    
    public int x_off=+DISPLAY_WIDTH/2,y_off=+DISPLAY_HEIGHT/2;
    private int mode = MODE_RECTANGLE;
    private boolean blocks = false;
    private boolean shifting = false;
    private int x_cre=0,y_cre=0;
    private Polygon poly = new Polygon();

    public Editor(){
        ieh.addKeyboardListener(this);
        ieh.addMouseListener(this);
    }
    
    public int getMode(){return mode;}
    public boolean getPlace(){return blocks;}
    public String getModeS(){
        switch(mode){
            case MODE_LINE: return "Lines";
            case MODE_CIRCLE: return "Circles";
            case MODE_RECTANGLE: return "Rectangles";
            case MODE_POLYGON: return "Polygons";
        }
        return "Unknown";
    }
    public String getPlaceS(){
        return (blocks) ? "Blocks" : "Entities";
    }
    public void setMode(int m){mode=m;}
    public void setPlace(boolean blocks){this.blocks=blocks;}
    
    public void paint(){
        if(x_cre!=0&&y_cre!=0){
            switch(mode){
                case MODE_LINE:
                    new Color(1.0f,1.0f,1.0f,0.8f).bind();
                    AbstractGraph.glLine2d(x_cre-x_off,y_cre-y_off,Mouse.getX()-x_off,Mouse.getY()-y_off);
                    break;
                case MODE_CIRCLE:
                    new Color(1.0f,1.0f,1.0f,0.2f).bind();
                    AbstractGraph.glFCircle2d(x_cre-x_off,y_cre-y_off,Toolkit.pythagoras(Mouse.getX()-x_cre,Mouse.getY()-y_cre));
                    glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
                    new Color(1.0f,1.0f,1.0f,0.8f).bind();
                    AbstractGraph.glCircle2d(x_cre-x_off,y_cre-y_off,Toolkit.pythagoras(Mouse.getX()-x_cre,Mouse.getY()-y_cre));
                    glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
                    break;
                case MODE_RECTANGLE:
                    new Color(1.0f,1.0f,1.0f,0.2f).bind();
                    AbstractGraph.glRectangle2d(x_cre-x_off,y_cre-y_off,Mouse.getX()-x_cre,Mouse.getY()-y_cre);
                    glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
                    new Color(1.0f,1.0f,1.0f,0.8f).bind();
                    AbstractGraph.glRectangle2d(x_cre-x_off,y_cre-y_off,Mouse.getX()-x_cre,Mouse.getY()-y_cre);
                    glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
                    break;
                case MODE_POLYGON:
                    new Color(1.0f,1.0f,1.0f,0.2f).bind();
                    poly.draw();
                    glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
                    new Color(1.0f,1.0f,1.0f,0.8f).bind();
                    poly.draw();
                    glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
                    break;
            }
        }
    }
    
    public void mouseMoved(int x, int y) {
        if(shifting){
            x_off+=Mouse.getDX();
            y_off+=Mouse.getDY();
        }
    }
    public void mousePressed(int button) {
        if(button==0){
            if(x_cre==0&&y_cre==0){
                x_cre=Mouse.getX();
                y_cre=Mouse.getY();
            }
        }
    }
    public void mouseType(int button) {}
    public void mouseReleased(int button) {
        if(button==0){
            if(mode!=MODE_POLYGON){
                int w = Mouse.getX()-x_cre;
                int h = Mouse.getY()-y_cre;
                if(mode==MODE_RECTANGLE){ //FIX ORIENTATION
                    if(w<0){
                        w*=-1;
                        x_cre-=w;
                    }
                    if(h<0){
                        h*=-1;
                        y_cre-=h;
                    }
                }
                int x = x_cre-x_off;
                int y = y_cre-y_off;
                
                switch(mode){
                    case MODE_LINE:
                        if(blocks)world.addBlock( new Block( x,y,w,h,new Line(w,h)));
                        else      world.addEntity(new Entity(x,y,w,h,new Line(w,h)));
                        break;
                    case MODE_CIRCLE:
                        if(blocks)world.addBlock( new Block( x,y,w,h,new Circle(Toolkit.pythagoras(w,h))));
                        else      world.addEntity(new Entity(x,y,w,h,new Circle(Toolkit.pythagoras(w,h))));
                        break;
                    case MODE_RECTANGLE:
                        if(blocks)world.addBlock( new Block( x,y,w,h,new Rectangle(w,h)));
                        else      world.addEntity(new Entity(x,y,w,h,new Rectangle(w,h)));
                        break;
                }
                x_cre=0;
                y_cre=0;
            }else{
                poly.addPoint(new Vector2(Mouse.getX()-x_off,Mouse.getY()-y_off));
            }
        }
        if(button==1){
            if(mode!=MODE_POLYGON){
                int[] ids = world.getIDList();
                for(int i=0;i<ids.length;i++){
                    if(world.getByID(ids[i]).checkInside(Mouse.getX()-x_off, Mouse.getY()-y_off))
                        world.delByID(ids[i]);
                }
            }else{
                int w = Mouse.getX()-x_cre;
                int h = Mouse.getY()-y_cre;
                if(w<0){w*=-1;x_cre-=w;}
                if(h<0){h*=-1;y_cre-=h;}
                int x = x_cre-x_off;
                int y = y_cre-y_off;
                if(blocks)world.addBlock(new Block(x,y,w,h,poly));
                else      world.addEntity(new Entity(x,y,w,h,poly));
                x_cre=0;
                y_cre=0;
                poly = new Polygon();
            }
        }
    }

    public void keyPressed(int key) {}
    public void keyType(int key) {
        switch(key){
            case Keyboard.KEY_LSHIFT:shifting=true;break;
        }
    }
    public void keyReleased(int key) {
        switch(key){
            case Keyboard.KEY_LSHIFT:shifting=false;break;
            case Keyboard.KEY_1:mode=MODE_LINE;break;
            case Keyboard.KEY_2:mode=MODE_CIRCLE;break;
            case Keyboard.KEY_3:mode=MODE_RECTANGLE;break;
            case Keyboard.KEY_4:mode=MODE_POLYGON;break;
            case Keyboard.KEY_COMMA:blocks=true;break;
            case Keyboard.KEY_PERIOD:blocks=false;break;
        }
    }

}
