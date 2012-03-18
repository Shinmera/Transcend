/**********************\
  file: TextureRenderer.java
  package: transcend.graph
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.graph;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import transcend.main.Const;

public class TextureRenderer {
    int framebufferID = -1;
    int currentTexture = -1;
    
    public TextureRenderer(){
        if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            Const.LOGGER.warning("[TextureRenderer] FrameBufferObject NOT supported. Tiling may be slow.");
        }else {
            Const.LOGGER.info("[TextureRenderer] Generating FrameBufferObject");
            IntBuffer buffer = ByteBuffer.allocateDirect(1*4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
            EXTFramebufferObject.glGenFramebuffersEXT( buffer ); // generate 
            framebufferID = buffer.get();
        }
    }
    
    public void beginDrawToTexture(int width,int height){
        if(framebufferID==-1)return;
        int id = glGenTextures();
        Const.LOGGER.info("[TextureRenderer] Generated new texture ID: "+id);
        Const.LOGGER.info("[TextureRenderer] Allocating texture space for "+width+"x"+height+" ...");
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        beginDrawToTexture(id,width,height);
    }
    
    public void beginDrawToTexture(int id,int width,int height){
        if(framebufferID==-1)return;
        currentTexture=id;
        
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glClear (GL_COLOR_BUFFER_BIT);
        
        glBindFramebufferEXT( GL_FRAMEBUFFER_EXT, framebufferID );
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, id, 0);
        glPushAttrib(GL_VIEWPORT_BIT);
        
        glClearColor(0.0f,0.0f,0.0f,0.0f);
        glClear (GL_COLOR_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glViewport (0, 0, width, height);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        Const.LOGGER.info("[TextureRenderer] Switched to FBO render mode.");
    }
    
    public int endDrawToTexture(){
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glPopAttrib();
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Const.LOGGER.info("[TextureRenderer] Switched to Screen render mode.");
        return currentTexture;
    }
}
