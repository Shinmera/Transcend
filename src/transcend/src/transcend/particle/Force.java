/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.particle;

public class Force {
    double xacc,yacc,multi;
    double randomfactor;

    public Force(){randomfactor=Math.random();}
    public Force(double multi){this();this.multi=multi;}
    public Force(double xacc,double yacc){this();this.xacc=xacc;this.yacc=yacc;}
    public Force(double xacc,double yacc,double multi){this();this.xacc=xacc;this.yacc=yacc;this.multi=multi;}
    public Force add(Force f){
        Force ret = new Force();
        ret.xacc=xacc+f.xacc;
        ret.yacc=yacc+f.yacc;
        ret.multi=multi+f.multi;
        return ret;
    }
    public Force multiply(Force f){
        Force ret = new Force();
        ret.xacc=xacc*f.xacc;
        ret.yacc=yacc*f.yacc;
        ret.multi=multi*f.multi;
        return ret;
    }

    public double getXACC(){return xacc;}
    public double getYACC(){return yacc;}
    public double getMultiplier(){return multi;}
    public double getRandomFactor(){return randomfactor;}
    public void setXACC(double xacc){this.xacc=xacc;}
    public void setYACC(double yacc){this.yacc=yacc;}
    public void setMultiplier(double multi){this.multi=multi;}
    public void setRandomFactor(double random){randomfactor=random;}
}
