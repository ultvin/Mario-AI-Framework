package agents.AustinFlynnAgent;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;
import engine.sprites.Mario;
import agents.robinBaumgarten.AStarTree;

public class Agent implements MarioAgent {
    private enum mood {
        KILL, PROGRESS
    }

    private mood currentMood;
    private AStarTree tree = new AStarTree();

    private boolean[] actions = new boolean[MarioActions.numberOfActions()];
    private int sense_radius = 50;
    private long totalTime = 0;

    private float[] getEnemies(MarioForwardModel model){
        return model.getEnemiesFloatPos();
    }

    private float getModelX(MarioForwardModel model){return model.getMarioFloatPos()[0];}

    private float getModelY(MarioForwardModel model){return model.getMarioFloatPos()[1];}

    private boolean enemiesNear(MarioForwardModel model){

        for(int e =0; e<getEnemies(model).length/3;e++) {
            if (getDistance(model, getEnemies(model)[(3 * e) + 1], getEnemies(model)[(3 * e) + 2]) <= sense_radius) {
                return true;
            }
        }
        return false;
    }

    private float getDistance(MarioForwardModel model, float x, float y){
        return (float)Math.sqrt(((getModelX(model)-x) * (getModelX(model)-x) + ((getModelY(model)-y) * (getModelY(model)-y))));
    }

    private void reassessMood(MarioForwardModel model, MarioTimer timer){
        float wrath = enemyNearUtility(model); //urge to kill
        float temperance = progressUtility(model, timer); //urge to progress

        if(wrath > temperance){
            System.out.println("KILL MODE ENGAGED");
            currentMood = mood.KILL;
        }

        else currentMood = mood.PROGRESS;
    }

    private float enemyNearUtility(MarioForwardModel model){
        float utility = 0;
        float numEnemies = 0;
        if(enemiesNear(model)){
            for(int e =0; e<getEnemies(model).length/3;e++) {
                if (getDistance(model, getEnemies(model)[(3 * e) + 1], getEnemies(model)[(3 * e) + 2]) <= sense_radius) {
                    utility += getDistance(model, getEnemies(model)[(3 * e) + 1], getEnemies(model)[(3 * e) + 2]);
                    numEnemies++;
                }
            }
        }
        if(model.getMarioMode() > 0){
            utility *= 1.25;
            if(utility > 1.0){
                utility = 1.0f;
            }
        }
        if(utility == 0){
            return 0;
        }
        else {
            System.out.println("Enemy " + numEnemies / Math.log(utility));
            return numEnemies / (float) Math.log(utility);
        }
    }

    private float progressUtility(MarioForwardModel model, MarioTimer timer){
        float utility =0.0f;
        float disToFlag = model.getCompletionPercentage();
        long timeRemaining = timer.getRemainingTime()/ totalTime;

        System.out.println("progress " + disToFlag * timeRemaining);
        utility = disToFlag * timeRemaining;
        return utility;
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer){
        currentMood = mood.PROGRESS;
        totalTime = timer.getRemainingTime();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        reassessMood(model, timer);

        switch(currentMood){
            case PROGRESS:


                actions = this.tree.optimise(model, timer);


                break;
            case KILL:


                float nearestEnemyX = 0;
                float nearestEnemyY = 0;
                int nearestEnemyIndex = 0;
                boolean isSpiky = false;
                boolean isWinged = false;
                for(int e=0; e<getEnemies(model).length/3; e++){
                    if(e==0){

                        nearestEnemyX = getEnemies(model)[(3*e)+1];
                        nearestEnemyY = getEnemies(model)[(3*e)+2];
                    }
                    else if(getDistance(model, getEnemies(model)[(3*e)+1], getEnemies(model)[(3*e)+2])<getDistance(model,nearestEnemyX, nearestEnemyY)){
                        nearestEnemyX = getEnemies(model)[(3*e)+1];
                        nearestEnemyY = getEnemies(model)[(3*e)+2];
                        nearestEnemyIndex = e;
                    }
                }
                if(getEnemies(model)[3*nearestEnemyIndex] == 8){
                    isSpiky = true;
                }
                if(getEnemies(model)[3*nearestEnemyIndex] == 3f || getEnemies(model)[3*nearestEnemyIndex] == 5f ||
                        getEnemies(model)[3*nearestEnemyIndex] == 7f || getEnemies(model)[3*nearestEnemyIndex] == 9f){
                    isWinged = true;
                }
                   if(model.getMarioMode() == 2 && nearestEnemyY == model.getMarioFloatPos()[1]){
                       actions[MarioActions.SPEED.getValue()] = true;
                   }
                   else if(getDistance(model, nearestEnemyX,nearestEnemyY)<45){
                       if(!isSpiky && !isWinged){
                           actions[MarioActions.SPEED.getValue()] = false;
                           actions[MarioActions.JUMP.getValue()] = true;
                           if(model.getMarioFloatPos()[0]<nearestEnemyX){
                               actions[MarioActions.LEFT.getValue()] = false;
                               actions[MarioActions.RIGHT.getValue()] = true;
                           }
                           else if (model.getMarioFloatPos()[0]>nearestEnemyX){
                               actions[MarioActions.RIGHT.getValue()] = false;
                               actions[MarioActions.LEFT.getValue()] = true;
                           }
                       }
                       else if(getDistance(model, nearestEnemyX,nearestEnemyY)<30 && model.getMarioFloatPos()[0]<nearestEnemyX && isSpiky){
                           actions[MarioActions.JUMP.getValue()] = true;
                           actions[MarioActions.RIGHT.getValue()] = true;
                       }
                       else if(isWinged){
                           if(nearestEnemyY == model.getMarioFloatPos()[1]){
                               if(model.getMarioFloatPos()[0]<nearestEnemyX){
                                   actions[MarioActions.RIGHT.getValue()] = true;
                               }
                               else if (model.getMarioFloatPos()[0]>nearestEnemyX){
                                   actions[MarioActions.LEFT.getValue()] = true;
                               }
                           }
                           else{
                               actions[MarioActions.RIGHT.getValue()] = true;
                           }
                       }
                   }
                   break;

        }
        return actions;
    }

    @Override
    public String getAgentName() {
        return "AustinFlynnAgent";
    }
}
