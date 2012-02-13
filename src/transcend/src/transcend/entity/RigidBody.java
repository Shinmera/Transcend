package transcend.entity;

import NexT.util.Line;
import NexT.util.Toolkit;
import NexT.util.Ray;
import NexT.util.Vector;
import transcend.block.Block;
import transcend.graph.AbstractGraph;
import transcend.gui.TrueTypeFont;
import java.awt.Font;
import org.newdawn.slick.Color;
import transcend.main.MainFrame;
import transcend.world.Element;

public class RigidBody extends Entity{
    public static final double GRAVITY = -0.4;
    private double weight = 1.0;
    public Element left,right,top,bottom;
    
    public RigidBody(){}
    
    public void performCollisionChecks(){
        bottom=null;top=null;left=null;right=null;
        
        bottom=           check(x-w/2+3+vx,y-h/2+1,    x+w/2-3+vx,y-h/2+1);
        if(bottom==null)
            bottom=       check(x-w/2+3+vx,y-h/2+vy,    x+w/2-3+vx,y-h/2+vy);
        top=              check(x-w/2+3+vx,y+h/2+vy,    x+w/2-3+vx,y+h/2+vy);
        left=             check(x-w/2-1+vx,y+h/2-3+vy,    x-w/2-1+vx,y-h/2+3+vy);
        right=            check(x+w/2+1+vx,y+h/2-3+vy,    x+w/2+1+vx,y-h/2+3+vy);
        if(left!=null)onCollisionLeft(left);
        if(right!=null)onCollisionRight(right);
        if(bottom!=null)onCollisionBottom(bottom);
        if(top!=null)onCollisionTop(top);
    }
    
    public void onCollisionLeft(Element left){
        if(left.solid>0.5&&vx<0){
            int x1=Integer.MAX_VALUE;
            int x2=Integer.MAX_VALUE;

            Vector v = left.getCollisionPoint(new Ray(x,y+h/2-3,0,-1,0,0));if(v!=null)x1=(int) v.getX();
                   v = left.getCollisionPoint(new Ray(x,y-h/2+3,0,-1,0,0));if(v!=null)x2=(int) v.getX();

            if(Toolkit.p(x1)>Toolkit.p(x+vx)+w+20)x1=Integer.MAX_VALUE;
            if(Toolkit.p(x1)>Toolkit.p(x+vx)+w+20)x2=Integer.MAX_VALUE;

            x1=Math.min(x1,x2);
            //if(x1<Integer.MAX_VALUE)x=x1+w/2-1;
            vx=0;
        }
        if(left.solid<=0.5)left=null;
    }
    
    public void onCollisionRight(Element right){
        if(right.solid>0.5){
            int x1=Integer.MAX_VALUE;
            int x2=Integer.MAX_VALUE;

            Vector v = right.getCollisionPoint(new Ray(x,y+h/2-3,0,1,0,0));if(v!=null)x1=(int) v.getX();
                   v = right.getCollisionPoint(new Ray(x,y-h/2+3,0,1,0,0));if(v!=null)x2=(int) v.getX();

            if(x1==Integer.MAX_VALUE)x1=Integer.MIN_VALUE;
            else if(x2==Integer.MAX_VALUE)x2=Integer.MIN_VALUE;

            x1=Math.max(x1,x2)-1;
            //if(x1>Integer.MIN_VALUE)x=x1-w/2;
            vx=0;
        }
        if(right.solid<=0.5)left=null;
    }
    
    public void onCollisionTop(Element top){
        if(top.solid>0.5){
            int y1=Integer.MAX_VALUE;
            int y2=Integer.MAX_VALUE;

            Vector v = top.getCollisionPoint(new Ray(x+w/2-3,y-h/2,0,0,1,0));if(v!=null)y1=(int) v.getY();
                   v = top.getCollisionPoint(new Ray(x-w/2+3,y-h/2,0,0,1,0));if(v!=null)y2=(int) v.getY();

            if(y1==Integer.MAX_VALUE)y1=Integer.MIN_VALUE;
            else if(y2==Integer.MAX_VALUE)y2=Integer.MIN_VALUE;

            y1=Math.max(y1,y2)-1;
            if(y1>Integer.MIN_VALUE)y=y1-h/2;
            vy=0;
        }
    }
    
    public void onCollisionBottom(Element bottom){
        //Determine testing x positions
        int x1=Integer.MIN_VALUE;
        int x2=Integer.MIN_VALUE;
        Vector v = bottom.getCollisionPoint(new Line(x-w/2+3,y-h/2,0,x,y-h/2,0));if(v!=null)x1=(int)v.getX();
               v = bottom.getCollisionPoint(new Line(x+w/2-3,y-h/2,0,x,y-h/2,0));if(v!=null)x2=(int)v.getX();
        int y1=Integer.MIN_VALUE;
        int y2=Integer.MIN_VALUE;
        //Determine height
               v = bottom.getCollisionPoint(new Ray(x1,y+h/2,0,0,-1,0));if(v!=null)y1=(int)v.getY();
               v = bottom.getCollisionPoint(new Ray(x2,y+h/2,0,0,-1,0));if(v!=null)y2=(int)v.getY();
        System.out.println(">> Y1: "+y1+" Y2: "+y2);
        y1=Math.max(y1,y2)-1;
        if(y1>Integer.MIN_VALUE)y=y1-h/2;
        vy=0;
    }

    public void update() {
        vy+=GRAVITY;
        performCollisionChecks();
        
        x+=vx;
        y+=vy;
    }

    public void draw() {
        Color.red.bind();
        TrueTypeFont ttf = MainFrame.fontPool.loadFont("Arial", Font.BOLD, 12);
        ttf.drawString((float)(x+w/2+2),(float)(y+h/2), "X: "+x+"\nY: "+y+"\nVX: "+vx+"\nVY: "+vy+"\nBTLR: "+bottom+" "+top+" "+left+" "+right, 1, 1, TrueTypeFont.ALIGN_LEFT);
        
        new Color(1.0f,0.0f,0.0f,0.5f).bind();
        AbstractGraph.glRectangle2d(x-w/2, y-h/2, w, h);
        Color.blue.bind();
        AbstractGraph.glCircle2d(x-w/2+3+vx, y-h/2+vy, 2);
        AbstractGraph.glCircle2d(x+w/2-3+vx, y-h/2+vy, 2);
        Color.blue.bind();
        AbstractGraph.glCircle2d(x-w/2+3+vx, y+h/2+vy, 2);
        AbstractGraph.glCircle2d(x+w/2-3+vx, y+h/2+vy, 2);
        Color.green.bind();
        AbstractGraph.glCircle2d(x-w/2-1+vx, y-h/2+3+vy, 2);
        AbstractGraph.glCircle2d(x-w/2-1+vx, y+h/2-3+vy, 2);
        Color.green.bind();
        AbstractGraph.glCircle2d(x+w/2+1+vx, y-h/2+3+vy, 2);
        AbstractGraph.glCircle2d(x+w/2+1+vx, y+h/2-3+vy, 2);
    }
    
    
    public boolean checkInside(double ax,double ay,double aw,double ah){
        if(w<=0||h<=0)return false;
        if(ax+aw<x-w/2)return false;
        if(ay+ah<y-h/2)return false;
        if(ax>x+w/2)return false;
        if(ay>y+h/2)return false;
        return true;
    }
    
}
