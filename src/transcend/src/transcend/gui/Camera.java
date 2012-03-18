/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.gui;

import static org.lwjgl.opengl.GL11.*;
import transcend.main.MainFrame;
import transcend.world.BElement;

public class Camera {
    private int entityID = -1;
    private double x = 0,y = 0;
    private double boundary = -1;
    private double zoom = 1;
    private int shaketimer = 0;
    private int shakeamount = 10;

    public Camera(){}
    
    public void follow(int entityID){this.entityID = entityID;}
    public void setBoundary(double boundary){this.boundary = boundary;}
    public void setZoom(double zoom){this.zoom = zoom;}

    public void update(){
        if(entityID>-1){
            BElement e = MainFrame.world.getByID(entityID);
            if(boundary>-1){
                if(e.x>x+(MainFrame.DISPLAY_WIDTH/2-boundary)/zoom)x=e.x-(MainFrame.DISPLAY_WIDTH/2-boundary)/zoom;
                if(e.x<x-(MainFrame.DISPLAY_WIDTH/2-boundary)/zoom)x=e.x+(MainFrame.DISPLAY_WIDTH/2-boundary)/zoom;
                if(e.y>y+(MainFrame.DISPLAY_HEIGHT/2-boundary)/zoom)y=e.y-(MainFrame.DISPLAY_HEIGHT/2-boundary)/zoom;
                if(e.y<y-(MainFrame.DISPLAY_HEIGHT/2-boundary)/zoom)y=e.y+(MainFrame.DISPLAY_HEIGHT/2-boundary)/zoom;
            }else{
                x=e.x;
                y=e.y;
            }
            if(shaketimer>0){
                x+=Math.round(Math.random()*shakeamount)-shakeamount/2;
                y+=Math.round(Math.random()*shakeamount)-shakeamount/2;
                shaketimer--;
            }
        }
    }

    public void shake(int steps){shaketimer=steps;}
    public void setPosition(int x,int y){this.x=x;this.y=y;}
    public void setShakeAmount(int amount){shakeamount=amount;}

    public double getX(){return x;}
    public double getY(){return y;}
    public double getRelativeX(){return x-MainFrame.DISPLAY_WIDTH/zoom/2.0;}
    public double getRelativeY(){return y-MainFrame.DISPLAY_HEIGHT/zoom/2.0;}
    public double getZoom(){return zoom;}
    public int    getFollowing(){return entityID;}

    public void camBegin(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(x-MainFrame.DISPLAY_WIDTH/zoom/2, x+MainFrame.DISPLAY_WIDTH/zoom/2, y-MainFrame.DISPLAY_HEIGHT/zoom/2, y+MainFrame.DISPLAY_HEIGHT/zoom/2, -100, 100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void camEnd(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
	glOrtho(0, MainFrame.DISPLAY_WIDTH, 0, MainFrame.DISPLAY_HEIGHT, -100, 100);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}
