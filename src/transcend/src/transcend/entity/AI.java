/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: entity
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.entity;
import transcend.main.MainFrame;
import transcend.main.Const;
import transcend.gui.GLabel;
import java.util.ArrayList;
import transcend.block.Block;
import static transcend.main.MainFrame.*;

public class AI {
    public static final int CHOICE_TL = 0x01;
    public static final int CHOICE_TR = 0x02;
    public static final int CHOICE_ML = 0x03;
    public static final int CHOICE_MR = 0x04;
    public static final int CHOICE_BL = 0x05;
    public static final int CHOICE_BR = 0x06;
    public static final int CHOICE_L  = 0x07;
    public static final int CHOICE_R  = 0x08;
    public static final int TRESHOLD = 100;
    public static final int FORCE_REFRESH = 6;
    public static final double FORCE_TIMEOUT = MainFrame.fps*0.45;

    private int goal_x=0,goal_y=0;
    private int wID = -1;
    private int refresh = 0;
    private int timeout = 0;
    private ArrayList<Integer> choices = new ArrayList<Integer>();
    private double dist = Double.MAX_VALUE;

    public AI(int wID){
        this.wID=wID;
    }

    public void setGoal(int x,int y){goal_x=x;goal_y=y;}
    public int getGoalX(){return goal_x;}
    public int getGoalY(){return goal_y;}
    public double getGoalDist(){return dist;}

    public void update(){
        if(timeout<=FORCE_TIMEOUT){
            timeout++;
            return;
        }
        refresh++;
        timeout=0;
        Entity e = (Entity)world.getByID(wID);
        dist = Math.sqrt(Math.pow(e.x-goal_x+e.w*0.5,2)+Math.pow(e.y+e.h*0.5-goal_y,2));
        if(dist<=TRESHOLD){e.status=Entity.STATUS_IDLE;this.choices.clear();return;}
        if(refresh>=FORCE_REFRESH){this.choices.clear();refresh=0;}

        //get blocks. We only need to check those.
        Object[] blockIDs = world.getBlockList();
        //set parameters...
        ArrayList<Integer> choices = new ArrayList<Integer>();
        for(int i=0;i<blockIDs.length;i++){
            Block block = (Block)world.getByID((Integer)blockIDs[i]);
            if((block.checkInside(e.x-e.w*0.5,   e.y+e.h*1.5)||block.checkInside(e.x-e.w*1.5,   e.y+e.h*1.5)))choices.add(CHOICE_TL);
            if((block.checkInside(e.x+e.w*1.5,   e.y+e.h*1.5)||block.checkInside(e.x+e.w*2.5,   e.y+e.h*1.5)))choices.add(CHOICE_TR);
            if(block.checkInside(e.x-e.w*0.5,   e.y+e.h*0.5))choices.add(CHOICE_ML);
            if(block.checkInside(e.x+e.w*1.5,   e.y+e.h*0.5))choices.add(CHOICE_MR);
            if(!block.checkInside(e.x-e.w*0.5,  e.y-e.h*0.5))choices.add(CHOICE_BL);else choices.add(CHOICE_L);
            if(!block.checkInside(e.x+e.w*1.5,  e.y-e.h*0.5))choices.add(CHOICE_BR);else choices.add(CHOICE_R);
        }
        //make sure we got choices at all.
        if(choices.size()<=1)return;

        //assign weights.
        double[] weights = new double[choices.size()];
        double adist=0;double min=Double.MAX_VALUE;int minc=0;
        for(int i=0;i<weights.length;i++){
            switch(choices.get(i)){
                case CHOICE_TL:adist = Math.sqrt(Math.pow(e.x-goal_x-e.w*0.5,2)  +Math.pow(e.y-goal_y+e.h*1.5,2))+Const.RAND.nextInt(TRESHOLD)+10;break;
                case CHOICE_TR:adist = Math.sqrt(Math.pow(e.x-goal_x+e.w*1.5,2)  +Math.pow(e.y-goal_y+e.h*1.5,2))+Const.RAND.nextInt(TRESHOLD)+10;break;
                case CHOICE_ML:adist = Math.sqrt(Math.pow(e.x-goal_x-e.w*0.5,2)  +Math.pow(e.y-goal_y+e.h*0.5,2))+Const.RAND.nextInt(TRESHOLD);break;
                case CHOICE_MR:adist = Math.sqrt(Math.pow(e.x-goal_x+e.w*1.5,2)  +Math.pow(e.y-goal_y+e.h*0.5,2))+Const.RAND.nextInt(TRESHOLD);break;
                case CHOICE_BL:adist = Math.sqrt(Math.pow(e.x-goal_x-e.w*0.5,2)  +Math.pow(e.y-goal_y-e.h*0.5,2))+Const.RAND.nextInt(TRESHOLD);break;
                case CHOICE_BR:adist = Math.sqrt(Math.pow(e.x-goal_x+e.w*1.5,2)  +Math.pow(e.y-goal_y-e.h*0.5,2))+Const.RAND.nextInt(TRESHOLD);break;
                case CHOICE_L: adist = Math.sqrt(Math.pow(e.x-goal_x-e.w*0.5,2)  +Math.pow(e.y-goal_y,2))+Const.RAND.nextInt(TRESHOLD);break;
                case CHOICE_R: adist = Math.sqrt(Math.pow(e.x-goal_x+e.w*1.5,2)  +Math.pow(e.y-goal_y,2))+Const.RAND.nextInt(TRESHOLD);break;
            }
            weights[i]=adist;
            if(weights[i]<min){min=weights[i];minc=i;}
        }
        //evaluate
        switch(choices.get(minc)){
                case CHOICE_TL:e.status=Entity.STATUS_JUMP;e.vx=-2;break;
                case CHOICE_TR:e.status=Entity.STATUS_JUMP;e.vx=2;break;
                case CHOICE_ML:e.status=Entity.STATUS_JUMP;e.vx=-5;break;
                case CHOICE_MR:e.status=Entity.STATUS_JUMP;e.vx=5;break;
                case CHOICE_BL:e.status=Entity.STATUS_MOVE;e.vx=-5;break;
                case CHOICE_BR:e.status=Entity.STATUS_MOVE;e.vx=5;break;
                case CHOICE_L:e.status=Entity.STATUS_MOVE;e.vx=-5;break;
                case CHOICE_R:e.status=Entity.STATUS_MOVE;e.vx=5;break;
        }

        //((GLabel)hid.get("hidinfo")).setText("Choices: "+choices.size()+" Old Situation: "+(choices.equals(this.choices))+" Current choice: "+choices.get(minc)+" Current action: "+e.status);
        this.choices=choices;
    }
}
