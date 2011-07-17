/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

public class Force {
    public double xacc,yacc,multi;
    public double randomfactor;

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
}
