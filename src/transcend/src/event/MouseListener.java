/**********************\
  file: MouseListener.java
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

public interface MouseListener {
    public static final int BUTTON_LEFT = 1;
    public static final int BUTTON_RIGHT = 2;

    public void mouseMoved(int x,int y);
    public void mousePressed(int button);
    public void mouseReleased(int button);
}
