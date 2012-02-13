/**********************\
  file: EventListener
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.event;

import java.util.HashMap;

public interface EventListener {
    public void onEvent(int event,int identifier,HashMap<String,String> arguments);
    public void onAnonymousEvent(int event,HashMap<String,String> arguments);
}
