/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

import NexT.util.SimpleSet;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import tile.Tile;
import transcend.MainFrame;
import world.Element;

public class Emitter extends Tile{
    ArrayList<Force> forces = new ArrayList();
    ArrayList<Particle> particles = new ArrayList();
    private double life=-1,mlife=-1;
    private int mpart=100,spray=1;
    private Color color;

    private double avg_mlife=0.3;
    private double avg_mlife_diver=0.5;
    private double avg_size=7;
    private double avg_diver=0.5;
    private int    avg_type=ParticleForm.TYPE_GRADIENT_SPHERE_LIGHT;

    public Emitter(){System.out.println("Emitter created!");addForce(new Force(0,0.05));addForce(new Force(0.5));}
    public Emitter(double x,double y){this();this.x=x;this.y=y;}

    public void setPosition(double x,double y){this.x=x;this.y=y;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=y;}
    public void setLife(double l){this.life=l;}
    public void setMaxLife(double ml){this.mlife=ml;}
    public void setMaxParticles(int mp){this.mpart=mp;}
    public void setSpray(int spray){this.spray=spray;}
    public void setColor(Color color){this.color=color;}
    public void setAverageMaxLife(double ml){avg_mlife=ml;}
    public void setAverageMaxLifeDiversity(double mld){avg_mlife_diver=mld;}
    public void setAverageSize(double s){avg_size=s;}
    public void setAverageDiversity(double d){avg_diver=d;}
    public void setAverageType(int t){avg_type=t;}

    public double getLife(){return life;}
    public double getMaxLife(){return mlife;}
    public int getMaxParticles(){return mpart;}
    public int getSpray(){return spray;}
    public Color getColor(){return color;}
    public double getAverageMaxLife(){return avg_mlife;}
    public double getAverageMaxLifeDiversity(){return avg_mlife_diver;}
    public double getAverageSize(){return avg_size;}
    public double getAverageDiversity(){return avg_diver;}
    public int getAverageType(){return avg_type;}

    public void addForce(Force f){forces.add(f);}
    public void delForce(Force f){forces.remove(f);}
    public void clearForces(){forces.clear();}

    public void addParticle(Particle p){particles.add(p);}
    public void delParticle(Particle p){particles.remove(p);}
    public void clearParticles(){particles.clear();}

    public Force getTotalForce(){
        Force ret = new Force();
        for(int i=0;i<forces.size();i++)ret=ret.add(forces.get(i));
        ret.xacc+=(Math.random()-0.5)/1000;
        ret.yacc+=(Math.random()-0.5)/1000;
        return ret;
    }

    public void update(){
        for(int i=0;i<particles.size();i++)particles.get(i).update(this,getTotalForce());

        if(mlife>0)life+=1.0/MainFrame.fps;
        if(life>mlife)MainFrame.world.delByID(wID);
        if(particles.size()<mpart&&spray>0){
            for(int i=0;i<spray;i++){
                Particle p = new Particle(x,y);
                p.setVX((Math.random()-0.5)*avg_diver);
                p.setVY((Math.random()-0.5)*avg_diver);
                p.setScale((Math.random()-0.5)*avg_size);
                p.setMaxLife((Math.random()-0.5)*avg_mlife_diver+avg_mlife);
                p.setType(avg_type);
                if(color!=null)p.setColor(color);
                addParticle(p);
            }
        }
    }

    public void draw(){
        for(int i=0;i<particles.size();i++)particles.get(i).draw();
        if(MainFrame.editor.getActive()){
            Color.red.bind();
            MainFrame.glCircle2d(x, y, 5);
        }
    }

    public SimpleSet getOptions(){
        SimpleSet<String,String> set = new SimpleSet();
        set.put("mlife",mlife+"");
        set.put("mpart",mpart+"");
        set.put("spray",spray+"");
        if(color!=null)set.put("color",color.getRed()+","+color.getGreen()+","+color.getBlue());
        set.put("avg_diver",avg_diver+"");
        set.put("avg_size",avg_size+"");
        set.put("avg_mlife",avg_mlife+"");
        set.put("avg_mlife_diver",avg_mlife_diver+"");
        set.put("avg_type",avg_type+"");
        return set;
    }

    public boolean checkInside(Element e){
        if(checkInside(e.getX(),e.getY()))return true;
        if(checkInside(e.getX()+e.getWidth(),e.getY()))return true;
        if(checkInside(e.getX()+e.getWidth(),e.getY()+e.getHeight()))return true;
        if(checkInside(e.getX(),e.getY()+e.getHeight()))return true;
        return false;
    }
    public boolean checkInside(double ax,double ay){
        double dist = Math.sqrt(Math.pow(ax-x,2)+Math.pow(ay-y,2));
        if(dist>5)return false;
        return true;
    }
}
