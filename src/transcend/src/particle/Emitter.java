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
import transcend.MainFrame;
import world.BElement;

public class Emitter extends BElement{
    ArrayList<Force> forces = new ArrayList();
    ArrayList<Particle> particles = new ArrayList();
    private double life=-1,mlife=-1;
    private int mpart=100,spray=1;

    public Emitter(){System.out.println("Emitter created!");addForce(new Force(0,0.1));}
    public Emitter(double x,double y){this();this.x=x;this.y=y;}

    public void setPosition(double x,double y){this.x=x;this.y=y;}
    public void setX(double x){this.x=x;}
    public void setY(double y){this.y=y;}
    public void setLife(double l){this.life=l;}
    public void setMaxLife(double ml){this.mlife=ml;}
    public void setMaxParticles(int mp){this.mpart=mp;}
    public void setSpray(int spray){this.spray=spray;}

    public double getLife(){return life;}
    public double getMaxLife(){return mlife;}
    public int getMaxParticles(){return mpart;}
    public int getSpray(){return spray;}

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

        if(mlife>0)life+=1/MainFrame.fps;
        if(life>mlife)MainFrame.particlePool.delEmitter(this);
        if(particles.size()<mpart&&spray>0){
            for(int i=0;i<spray;i++)addParticle(new Particle(x,y));
        }
    }

    public void draw(){
        for(int i=0;i<particles.size();i++)particles.get(i).draw();
    }

    public SimpleSet getOptions(){
        SimpleSet<String,String> set = new SimpleSet();
        set.put("mlife",mlife+"");
        set.put("mpart",mpart+"");
        set.put("spray",spray+"");
        return set;
    }
    
}
