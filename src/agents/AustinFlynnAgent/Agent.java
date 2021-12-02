package agents.AustinFlynnAgent;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.sprites.Mario;

public class Agent implements MarioAgent {
    private enum mood  {
        COLLECT, KILL, PROGRESS
    }

    private mood currentMood;
    private int sense_radius = 5;

    private float[] getEnemies(MarioForwardModel model){
        return model.getEnemiesFloatPos();
    }

    private float getModelX(MarioForwardModel model){return model.getMarioFloatPos()[0];}

    private float getModelY(MarioForwardModel model){return model.getMarioFloatPos()[1];}

    private boolean enemiesNear(MarioForwardModel model){

        for(int e =0; e<getEnemies(model).length;e++) {
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
        float greed = 0; //urge to collect
        float temperance = 10; //urge to progress

        if(wrath > greed && wrath > temperance){
            currentMood = mood.KILL;
        }
        else if(greed > wrath && greed > temperance){
            currentMood = mood.COLLECT;
        }
        else currentMood = mood.PROGRESS;
    }

    private float enemyNearUtility(MarioForwardModel model){
        float utility = 0;
        float numEnemies = 0;
        if(enemiesNear(model)){
            for(int e =0; e<getEnemies(model).length;e++) {
                if (getDistance(model, getEnemies(model)[(3 * e) + 1], getEnemies(model)[(3 * e) + 2]) <= sense_radius) {
                    utility += getDistance(model, getEnemies(model)[(3 * e) + 1], getEnemies(model)[(3 * e) + 2]);
                    numEnemies++;
                }
            }
        }
        return numEnemies/utility;
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer){
        currentMood = mood.PROGRESS;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return new boolean[0];
    }

    @Override
    public String getAgentName() {
        return "AustinFlynnAgent";
    }
}
