/**********************\
  file: EventHandler
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

import NexT.util.OptionSet;
import java.util.ArrayList;
import java.util.HashMap;
import world.World;

public class EventHandler {
    OptionSet<Integer,ArrayList<Integer>> events = new OptionSet<Integer,ArrayList<Integer>>();
    World world;

    public EventHandler(){world = null;}
    public EventHandler(World world){this.world = world;}

    /**
     * Register an event for the specified object.
     * @param event The type of event to register.
     * @param priority The priority of the event call.
     * @param identifier A reference pointer to the object.
     */
    public void registerEvent(int event,int priority,int identifier){
        if(!events.containsKey(identifier))events.put(identifier, new ArrayList<Integer>());
        if(!events.containsValue(identifier,event))events.add(identifier, event);
    }

    /**
     * Unregisters an event for the specified object.
     * @param event The type of event to unregister.
     * @param identifier A reference pointer to the object.
     */
    public void unregisterEvent(int event,int identifier){
        if(events.containsKey(identifier)&&events.containsValue(identifier,event))
            events.get(identifier).remove(events.get(identifier).indexOf(event));
    }

    /**
     * Unregisters all events for the specified object.
     * @param identifier A reference pointer to the object.
     */
    public void unregisterAllEvents(int identifier){
        if(events.containsKey(identifier))events.remove(identifier);
    }

    /**
     * Induce the event chain. If called the Handler will process the event and
     * call the right objects and functions.
     * @param event The type of event to trigger.
     * @param identifier A reference pointer to the sending object.
     * @param arguments Pass additional arguments.
     */
    public void triggerEvent(int event,int identifier,HashMap<String,String> arguments){
        for(int i=0;i<events.size();i++){
            if(events.containsValue(events.getKey(i),event)){
                ((EventListener)world.getByID(events.getKey(i))).onEvent(event,identifier,arguments);
            }
        }
    }

    /**
     * Induce the event chain. If called the Handler will process the event and
     * call the right objects and functions.
     * @param event The type of event to trigger.
     * @param identifier A reference pointer to the sending object.
     */
    public void triggerAnonymousEvent(int event,HashMap<String,String> arguments){
        for(int i=0;i<events.size();i++){
            if(events.containsValue(events.getKey(i),event)){
                ((EventListener)world.getByID(events.getKey(i))).onAnonymousEvent(event,arguments);
            }
        }
    }

    /**
     * Trigger an event for a specific object.
     * @param event The type of event to trigger.
     * @param from A reference pointer to the sending object.
     * @param to A reference pointer to the receiving object.
     * @param arguments Pass additional arguments.
     */
    public void triggerSpecificEvent(int event,int from, int to,HashMap<String,String> arguments){
        ((EventListener)world.getByID(to)).onEvent(event, from,arguments);
    }

    /**
     * Trigger an event for a specific object anonymously.
     * @param event The type of event to trigger.
     * @param to A reference pointer to the receiving object.
     * @param arguments Pass additional arguments.
     */
    public void triggerAnonymousSpecificEvent(int event,int to,HashMap<String,String> arguments){
        ((EventListener)world.getByID(to)).onAnonymousEvent(event,arguments);
    }
}
