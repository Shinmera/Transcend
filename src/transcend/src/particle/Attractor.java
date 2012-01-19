/**********************\
  file: Attractor.java
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

import NexT.util.Vector;

public class Attractor extends VaryingForce{
    int x,y;
    public Attractor(){}
    public Attractor(int x,int y){randomfactor=Math.random();this.x=x;this.y=y;}
    public Attractor(int x,int y,double multi){this(x,y);this.multi=multi;}
    public Attractor(int x,int y,double xacc,double yacc){this(x,y);this.xacc=xacc;this.yacc=yacc;}
    public Attractor(int x,int y,double xacc,double yacc,double multi){this(x,y,xacc,yacc);this.multi=multi;}

    public double getXACC(Particle p) {
        Vector vec = new Vector(x-p.getX(),y-p.getY(),0);
        vec.normalize();vec.multiply(multi);
        return vec.getX();
    }

    public double getYACC(Particle p) {
        Vector vec = new Vector(x-p.getX(),y-p.getY(),0);
        vec.normalize();vec.multiply(multi);
        return vec.getY();
    }
}
