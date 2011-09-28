/**********************\
  file: VaryingForce.java
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

public class VaryingForce extends Force{
    public double getXACC(Particle p){return xacc;}
    public double getYACC(Particle p){return yacc;}
    public double getMultiplier(Particle p){return multi;}
    public double getRandomFactor(Particle p){return randomfactor;}
}
