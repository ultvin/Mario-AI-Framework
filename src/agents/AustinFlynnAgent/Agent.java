package agents.AustinFlynnAgent;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;
import engine.sprites.Mario;
import agents.robinBaumgarten.AStarTree;

public class Agent implements MarioAgent {
    private enum mood {
        COLLECT, KILL, PROGRESS
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

    private boolean coinsNear(MarioForwardModel model){
        int[][] coins = getCoins(model);

        for(int i = 0; i <= coins.length - 1; i++){
            for(int j = 0; j <= coins[0].length -1; j++){
                int type = coins[i][j] - 16;
                if ((type == 8 || type == 15)){
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] getCoins(MarioForwardModel model){return model.getScreenSceneObservation();}

    private float coinsUtility(MarioForwardModel model){
        float coinutility = 0.0f;
        float pUputility = 0.0f;
        float numcoins = 0;
        float numpowerup = 0;
        int[][] coins = getCoins(model);

        if(coinsNear(model)){
            for(int i = 0; i <= coins.length - 1; i++){
                for(int j = 0; j <= coins[0].length -1; j++){
                    int type = coins[i][j] - 16;
                    if (type == 15){
                       numcoins++;
                    }else if(type == 8){
                        numpowerup++;
                    }
                }
            }
        }

        coinutility = numcoins/100;
        if(model.getMarioMode() == 0){
            pUputility = numpowerup/20;
        }else if(model.getMarioMode() == 1){
            pUputility = numpowerup/100;
        }else {
            pUputility = numpowerup/1000;
        }


        if(coinutility == 0 && pUputility !=0){
            System.out.println("Coins: power up " + pUputility);
            return pUputility;
        }else if(coinutility != 0 && pUputility ==0){
            System.out.println("Coins: coins " + coinutility);
            return coinutility;
        }
        System.out.println("Coins " + coinutility * pUputility);
        return coinutility * pUputility;
    }

    private void reassessMood(MarioForwardModel model){
        float wrath = enemyNearUtility(model); //urge to kill
        float greed = coinsUtility(model); //urge to collect
        float temperance = 0.01f; //urge to progress

        if(wrath > greed && wrath > temperance){
            System.out.println("KILL MODE ENGAGED");
            currentMood = mood.KILL;
        }
        else if(greed > wrath && greed > temperance){
            System.out.println("MUST COLLECT EVERYTHING");
            currentMood = mood.COLLECT;
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
                //TODO: Pathfind to goal

                actions = this.tree.optimise(model, timer);


                break;
            case KILL:

                //TODO: Pathfind to nearest enemy
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
            case COLLECT:
                //TODO: collect coins and power ups
                break;
        }
        return actions;
    }

    @Override
    public String getAgentName() {
        return "AustinFlynnAgent";
    }
}
