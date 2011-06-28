/**********************\
  file: KeyboardListener.java
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

public interface KeyboardListener {
    public void keyPressed(int key);
    public void keyType(int key);
    public void keyReleased(int key);
}
