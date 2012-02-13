/**********************\
  file: Enemy.java
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.entity;
import transcend.event.EventListener;
import transcend.event.Event;
import java.util.HashMap;
import transcend.main.MainFrame;

public class Enemy extends Entity implements EventListener{
    public Enemy(){
        MainFrame.eh.registerEvent(Event.PLAYER_ATTACK, 5, this);
        MainFrame.eh.registerEvent(Event.PLAYER_CHANGE, 5, this);
        MainFrame.eh.registerEvent(Event.PLAYER_TOUCH, 5, this);
    }

    public void onEvent(int event, int identifier, HashMap<String, String> arguments) {
        if(event==Event.PLAYER_ATTACK){

        }
        if(event==Event.PLAYER_TOUCH){

        }
        if(event==Event.PLAYER_CHANGE){
            
        }
    }

    public void onAnonymousEvent(int event, HashMap<String, String> arguments) {}
}
