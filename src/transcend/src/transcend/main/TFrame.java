/**********************\
  file: TFrame.java
  package: transcend.main
  author: Shinmera
  team: NexT
  license: -
\**********************/

package transcend.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TFrame extends JFrame{
    private final Canvas canvas = new Canvas();
    private boolean frameChanged = false;
    private boolean closeRequested = false;
    
    public TFrame(String title){
        super(title);
        canvas.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e){ frameChanged = true;}
            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });
        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e){canvas.requestFocusInWindow(); }
            public void windowLostFocus(WindowEvent e) {}
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){ closeRequested = true; }
        });
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
        
        Const.LOGGER.info("[TFrame] Loading icons...");
        List<Image> icons = new ArrayList<Image>();
        try{
            icons.add(ImageIO.read(MainFrame.fileStorage.getFile("icon_16")));
            icons.add(ImageIO.read(MainFrame.fileStorage.getFile("icon_32")));
            icons.add(ImageIO.read(MainFrame.fileStorage.getFile("icon_64")));
            icons.add(ImageIO.read(MainFrame.fileStorage.getFile("icon_128")));
        }catch(IOException e){Const.LOGGER.log(Level.WARNING,"[TFrame] Icon loading failed.",e);}
        setIconImages(icons);
        
        setMinimumSize(new Dimension(300,200));
        pack();
    }
    
    public Canvas getCanvas(){return canvas;}
    public boolean isFrameChanged(){return frameChanged;}
    public boolean isCloseRequested(){return closeRequested;}
    public void setFrameChanged(boolean b){frameChanged=b;}
    public void setCloseRequested(boolean b){closeRequested=b;}
}
