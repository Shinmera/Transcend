/**********************\
  file: TextureRenderer.java
  package: transcend.graph
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.graph;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import transcend.main.Const;

public class TextureRenderer {
    int framebufferID = -1;
    int depthRenderBufferID = -1;
    int currentTexture = -1;
    
    public TextureRenderer(){
        if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            Const.LOGGER.warning("[TextureRenderer] FrameBufferObject NOT supported. Tiling may be slow.");
        }else {
            Const.LOGGER.info("[TextureRenderer] FrameBufferObject supported.");
            framebufferID = glGenFramebuffersEXT();                                                                         // create a new framebuffer
            depthRenderBufferID = glGenRenderbuffersEXT();                                                                  // And finally a new depthbuffer
        }
    }
    
    public void beginDrawToTexture(int width,int height){
        if(framebufferID==-1)return;
        int id = glGenTextures();
        beginDrawToTexture(id,width,height);
    }
    
    public void beginDrawToTexture(int id,int width,int height){
        if(framebufferID==-1)return;
        glBindTexture(GL_TEXTURE_2D, id);                                                                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);                                                   // make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);             // Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, id, 0);                        // attach it to the framebufferglBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);                                                            // switch to the new framebuffer
        // initialize depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);                                                    // bind the depth renderbuffer
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height);                            // get the data space for it
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID);  // bind it to the renderbuffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                                                        // Swithch back to normal framebuffer rendering
        
        glViewport (0, 0, width,height);                                                                                    // set The Current Viewport to the fbo size
        glBindTexture(GL_TEXTURE_2D, 0);                                                                                    // unlink textures because if we dont it all is gonna fail
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);                                                            // switch to rendering on our FBO
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);                                                                // Clear Screen And Depth Buffer on the fbo to red
        glLoadIdentity ();                                                                                                  // Reset The Modelview Matrix
        currentTexture=id;
    }
    
    public int endDrawToTexture(){
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                                                        // switch to rendering on the framebuffer
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);                                                                // Clear Screen And Depth Buffer on the framebuffer to black
        return currentTexture;
    }
}
