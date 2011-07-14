/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

import org.newdawn.slick.Color;
import transcend.MainFrame;
import static org.lwjgl.opengl.GL11.*;

public class Particle {
    private double x=0,y=0,z=0,r=0,s=1,vx=0,vy=0;
    private double life=0,mlife=2;
    private Color color = new Color(0,0,0,0);

    public Particle(){}
    public Particle(double x,double y){this.x=x;this.y=y;}
    public Particle(double x,double y,double r,double s){this.x=x;this.y=y;this.r=r;this.s=s;}
    public Particle(double x,double y,double mlife){this.x=x;this.y=y;this.mlife=mlife;}
    public Particle(double x,double y,double r,double s,double mlife){this.x=x;this.y=y;this.r=r;this.s=s;this.mlife=mlife;}

    public void setPosition(double x,double y){this.x=x;this.y=y;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=y;}
    public void setVX(double vx){this.vx=vx;}
    public void setVY(double vy){this.vy=vy;}
    public void setLayer(double z){this.z=z;}
    public void setRotation(double r){this.r=r;}
    public void setScale(double s){this.s=s;}
    public void setLife(double l){this.life=l;}
    public void setMaxLife(double ml){this.mlife=ml;}
    public void setColor(Color c){this.color=c;}

    public double getX(){return x;}
    public double getY(){return y;}
    public double getLayer(){return z;}
    public double getVX(){return vx;}
    public double getVY(){return vy;}
    public double getRotation(){return r;}
    public double getScale(){return s;}
    public double getLife(){return life;}
    public double getMaxLife(){return mlife;}
    public Color getColor(){return color;}

    public void update(Emitter e,Force f){
        vx+=f.xacc;
        vy+=f.yacc;
        update(e);
    }

    public void update(Emitter e){
        update();
        if(life>=mlife)e.delParticle(this);
    }

    public void update(){
        x+=vx;
        y+=vy;
        life+=1/MainFrame.fps;
    }

    public void draw(){
        ParticleForm.gradient_sphere.draw((int)x-4, (int)y-4, 8, 8);
        /*
        glPushMatrix();
        glTranslated(x,y,0);
        glPushMatrix();
        glRotatef((float)r,0,0,1);
        glScaled(s,s,1);
        
        glEnable(GL_COLOR_LOGIC_OP);
        glLogicOp(GL_AND);
        color.bind();
        glBegin(GL_QUADS);
            glVertex2d(-4,-4);
            glVertex2d(+4,-4);
            glVertex2d(+4,+4);
            glVertex2d(-4,+4);
        glEnd();
        glDisable(GL_COLOR_LOGIC_OP);
        
        glPopMatrix();
        glPopMatrix();
         * 
         */
    }
}
