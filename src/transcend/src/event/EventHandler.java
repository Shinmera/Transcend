/**********************\
  file: EventHandler
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

import NexT.util.SimpleSet;
import java.util.ArrayList;
import java.util.HashMap;
import transcend.MainFrame;

public class EventHandler {
    SimpleSet<EventListener,ArrayList<Integer>> events = new SimpleSet<EventListener,ArrayList<Integer>>();

    /**
     * Register an event for the specified object.
     * @param event The type of event to register.
     * @param priority The priority of the event call.
     * @param listener The object to send data to.
     */
    public void registerEvent(int event,int priority,EventListener listener){
        if(!events.containsKey(listener))events.put(listener, new ArrayList<Integer>());
        if(!events.get(listener).contains(event))events.get(listener).add(event);
    }

    /**
     * Unregisters an event for the specified object.
     * @param event The type of event to unregister.
     * @param listener The listener to remove.
     */
    public void unregisterEvent(int event,EventListener listener){
        if(events.containsKey(listener)&&events.get(listener).contains(event))
            events.get(listener).remove((Integer)event);
    }

    /**
     * Unregisters all events for the specified object.
     * @param listener The listener to remove.
     */
    public void unregisterAllEvents(EventListener listener){
        if(events.containsKey(listener))events.remove(listener);
    }

    /**
     * Induce the event chain. If called the Handler will process the event and
     * call the right objects and functions.
     * @param event The type of event to trigger.
     * @param wID A reference pointer to the sending object.
     * @param arguments Pass additional arguments.
     */
    public void triggerEvent(int event,int wID,HashMap<String,String> arguments){
        for(int i=0;i<events.size();i++){
            if(events.getAt(i).contains(event)){
                events.getKey(i).onEvent(event,wID,arguments);
            }
        }
    }

    /**
     * Induce the event chain. If called the Handler will process the event and
     * call the right objects and functions.
     * @param event The type of event to trigger.
     * @param wID A reference pointer to the sending object.
     */
    public void triggerAnonymousEvent(int event,HashMap<String,String> arguments){
        for(int i=0;i<events.size();i++){
            if(events.getAt(i).contains(event)){
                events.getKey(i).onAnonymousEvent(event,arguments);
            }
        }
    }

    /**
     * Trigger an event for a specific object.
     * @param event The type of event to trigger.
     * @param wID A reference pointer to the sending object.
     * @param to A reference pointer to the receiving object.
     * @param arguments Pass additional arguments.
     */
    public void triggerSpecificEvent(int event,int wID, int to,HashMap<String,String> arguments){
        ((EventListener)MainFrame.world.getByID(to)).onEvent(event, wID,arguments);
    }

    /**
     * Trigger an event for a specific object anonymously.
     * @param event The type of event to trigger.
     * @param to A reference pointer to the receiving object.
     * @param arguments Pass additional arguments.
     */
    public void triggerAnonymousSpecificEvent(int event,int to,HashMap<String,String> arguments){
        ((EventListener)MainFrame.world.getByID(to)).onAnonymousEvent(event,arguments);
    }
}
