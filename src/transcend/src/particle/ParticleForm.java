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
    public static final int TYPE_GRADIENT_SPHERE = 0x1;
    public static final int TYPE_GRADIENT_SPHERE_SMOOTH = 0x2;
    public static final int TYPE_GRADIENT_SPHERE_LIGHT = 0x3;

    public static final Animation gradient_sphere = new Animation();
    public static final Animation gradient_sphere_smooth = new Animation();
    public static final Animation gradient_sphere_light = new Animation();

    static{
        gradient_sphere.loadTexture(MainFrame.fileStorage.getFile("particle.png"));
        gradient_sphere_smooth.loadTexture(MainFrame.fileStorage.getFile("particle2.png"));
        gradient_sphere_light.loadTexture(MainFrame.fileStorage.getFile("particle3.png"));
    }

    public static Animation get(int type){
        if(type==TYPE_GRADIENT_SPHERE)return gradient_sphere;
        if(type==TYPE_GRADIENT_SPHERE_SMOOTH)return gradient_sphere_smooth;
        if(type==TYPE_GRADIENT_SPHERE_LIGHT)return gradient_sphere_light;
        return null;
    }
}
