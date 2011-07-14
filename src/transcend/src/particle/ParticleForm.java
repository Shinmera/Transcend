/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: particle
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package particle;

import graph.Animation;
import java.io.File;
import transcend.MainFrame;

public class ParticleForm {
    public static final Animation gradient_sphere = new Animation();

    static{
        gradient_sphere.loadTexture(new File(MainFrame.basedir,"tex"+File.separator+"particle.png"));
    }
}
