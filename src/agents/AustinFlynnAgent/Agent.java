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

    private void reassessMood(MarioForwardModel model){
        float wrath = enemyNearUtility(model); //urge to kill
        float temperance = 0.01f; //urge to progress

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
            System.out.println("Enemy " + numEnemies / utility);
            return numEnemies / utility;
        }
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer){
        currentMood = mood.PROGRESS;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        reassessMood(model);

        switch(currentMood){
            case PROGRESS:


                actions = this.tree.optimise(model, timer);


                break;
            case KILL:


                float nearestEnemyX = 0;
                float nearestEnemyY = 0;
                boolean isSpiky = false;
                for(int e=0; e<getEnemies(model).length/3; e++){
                    if(e==0){
                        if(getEnemies(model)[3*e] == 8){
                            isSpiky = true;
                        }
                        nearestEnemyX = getEnemies(model)[(3*e)+1];
                        nearestEnemyY = getEnemies(model)[(3*e)+2];
                    }
                    else if(getDistance(model, getEnemies(model)[(3*e)+1], getEnemies(model)[(3*e)+2])<getDistance(model,nearestEnemyX, nearestEnemyY)){
                        nearestEnemyX = getEnemies(model)[(3*e)+1];
                        nearestEnemyY = getEnemies(model)[(3*e)+2];
                    }
                }
                   if(model.getMarioMode() == 2 && nearestEnemyY == model.getMarioFloatPos()[1]){
                       actions[MarioActions.SPEED.getValue()] = true;
                   }
                   else if(getDistance(model, nearestEnemyX,nearestEnemyY)<45){
                       if(!isSpiky){
                           actions[MarioActions.JUMP.getValue()] = true;
                           if(model.getMarioFloatPos()[0]<nearestEnemyX){
                               actions[MarioActions.RIGHT.getValue()] = false;
                           }
                           else if (model.getMarioFloatPos()[0]>nearestEnemyX){
                               actions[MarioActions.LEFT.getValue()] = true;
                           }
                       }
                       else if(getDistance(model, nearestEnemyX,nearestEnemyY)<30 && model.getMarioFloatPos()[0]<nearestEnemyX){
                           actions[MarioActions.JUMP.getValue()] = true;
                           actions[MarioActions.RIGHT.getValue()] = true;
                       }
                       else{
                           actions[MarioActions.RIGHT.getValue()] = true;
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
