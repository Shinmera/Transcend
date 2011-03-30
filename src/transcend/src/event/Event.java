/**********************\
  file: Event
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package event;

public class Event {
    public static final int PLAYER_TOUCH    = 0x1;
    public static final int PLAYER_HURT     = 0x2;
    public static final int PLAYER_ATTACK   = 0x3;
    public static final int PLAYER_BLOCK    = 0x4;
    public static final int PLAYER_CHANGE   = 0x5;
    public static final int PLAYER_MOVE     = 0x6;
    public static final int PLAYER_DIE      = 0x9;

    public static final int ENTITY_TOUCH    = 0x11;
    public static final int ENTITY_ATTACK   = 0x12;
    public static final int ENTITY_MOVE     = 0x13;
    public static final int ENTITY_DIE      = 0x19;

    public static final int BLOCK_MOVE      = 0x21;
    public static final int BLOCK_DESTROY   = 0x22;

    public static final int LEVEL_HIGH      = 2;
    public static final int LEVEL_MIDDLE    = 1;
    public static final int LEVEL_LOW       = 0;
    public static final int LEVEL_NONE      = -1;

    //public static final int 

    public Event(){
        
    }
}
