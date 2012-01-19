/**********************\
  file: TessCallback.java
  package: spare
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package spare;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;
import org.newdawn.slick.Color;

public class TessCallback extends GLUtessellatorCallbackAdapter {
    private boolean textured = false;

    public TessCallback(){}

    public TessCallback(boolean textured){
        this.textured=textured;
    }

    public void begin(int type) {
        GL11.glBegin(type);
    }

    public void combine(double[] coords, Object[] data, float[] weight,
            Object[] outData) {
        for (int i = 0; i < outData.length; i++) {
            double[] combined = new double[6];
            combined[0] = coords[0];
            combined[1] = coords[1];
            combined[2] = coords[2];
            combined[3] = 1;
            combined[4] = 1;
            combined[5] = 1;

            outData[i] = new VertexData(combined);
        }
    }

    public void end() {
        GL11.glEnd();
    }

    public void vertex(Object vertexData) {
        VertexData vertex = (VertexData) vertexData;

        GL11.glVertex3d(vertex.data[0], vertex.data[1], vertex.data[2]);
        if(!textured){
            if(vertex.data.length==6)new Color((float)vertex.data[3], (float)vertex.data[4], (float)vertex.data[5]).bind();
            if(vertex.data.length==7)new Color((float)vertex.data[3], (float)vertex.data[4], (float)vertex.data[5],(float)vertex.data[6]).bind();
        }else{
            if(vertex.data.length==6)GL11.glTexCoord3d((float)vertex.data[3], (float)vertex.data[4], (float)vertex.data[5]);
            else                     GL11.glTexCoord2d((float)vertex.data[3], (float)vertex.data[4]);
        }
    }
}