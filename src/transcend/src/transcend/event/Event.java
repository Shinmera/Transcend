/**********************\
  file: Event
  package: event
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.event;

public class Event {
    public static final int PLAYER_TOUCH    = 0x01;
    public static final int PLAYER_HURT     = 0x02;
    public static final int PLAYER_ATTACK   = 0x03;
    public static final int PLAYER_BLOCK    = 0x04;
    public static final int PLAYER_CHANGE   = 0x05;
    public static final int PLAYER_MOVE     = 0x06;
    public static final int PLAYER_DIE      = 0x0F;

    public static final int ENTITY_SEE      = 0x10;
    public static final int ENTITY_MOVE     = 0x11;
    public static final int ENTITY_TOUCH    = 0x12;
    public static final int ENTITY_ATTACK   = 0x13;
    public static final int ENTITY_BLOCK    = 0x14;
    public static final int ENTITY_DIE      = 0x1F;

    public static final int BLOCK_MOVE      = 0x20;
    public static final int BLOCK_DESTROY   = 0x21;

    public static final int AREA_CLEAR      = 0x30;
    public static final int AREA_INFECT     = 0x31;

    public static final int LEVEL_HIGH      = 2;
    public static final int LEVEL_MIDDLE    = 1;
    public static final int LEVEL_LOW       = 0;
    public static final int LEVEL_NONE      = -1;

    //public static final int 

    public Event(){}
}
