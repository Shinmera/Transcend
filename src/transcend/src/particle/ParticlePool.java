/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

import java.util.ArrayList;

public class ParticlePool {
    ArrayList<Emitter> emitters = new ArrayList();

    public void addEmitter(Emitter e){emitters.add(e);}
    public void delEmitter(Emitter e){emitters.remove(e);}
    public void clearEmitters(){emitters.clear();}

    public void update(){
        for(int i=0;i<emitters.size();i++)emitters.get(i).update();
    }

    public void draw(){
        for(int i=0;i<emitters.size();i++)emitters.get(i).draw();
    }

}
