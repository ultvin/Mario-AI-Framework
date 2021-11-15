package levelGenerators.AustinFlynnGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import engine.core.MarioLevel;
import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{

    private ArrayList<ArrayList<Double>> transition_matrix;

    private Random rng;


    private int MAX_CHUNKS = 6;
    private int world_dist = 0;

    private void start_chunk(MarioLevelModel model){
        for(int i=0;i<20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        for(int i=5;i<10;i++){
            model.setBlock(i, getFloor(model)-4, MarioLevelModel.COIN_BRICK);
        }
        model.setBlock(world_dist+7, getFloor(model)-7, MarioLevelModel.SPECIAL_QUESTION_BLOCK);
        model.setBlock(world_dist+14, getFloor(model)-2, MarioLevelModel.GOOMBA);

        world_dist += 20;
    }

    /**
     * returns the lowest visible height layer of the level
     * @param model the MarioLevelModel associated with this level
     * @return the highest (lowest in position) y value where objects can be placed and be visible
     */
    private int getFloor(MarioLevelModel model){
        return model.getHeight()-1;
    }

    // CHUNKS //

    private void chunk0(MarioLevelModel model){//"PIPES"
        for(int i=world_dist;i<world_dist+20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        for(int i=0;i<3;i++) {
            model.setBlock(world_dist + (i * 6), getFloor(model) - 1, MarioLevelModel.PIPE);
            model.setBlock(world_dist + (i * 6), getFloor(model) - 2, MarioLevelModel.PIPE_FLOWER);
        }
        world_dist +=20;
    }

    private void chunk1(MarioLevelModel model){//"GAPS"
        for(int i=world_dist;i<world_dist+5;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        model.setBlock(world_dist+10, getFloor(model) - 1, MarioLevelModel.GOOMBA);
        for(int i=world_dist+7;i<world_dist+13;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }

        for(int i=world_dist+15;i<world_dist+20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        world_dist +=20;
    }

    private void chunk2(MarioLevelModel model){//"STAIRS"
        for(int i=world_dist;i<world_dist+10;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        for(int i=world_dist+7;i<world_dist+10;i++){
            model.setBlock(i,getFloor((model))-1, MarioLevelModel.USED_BLOCK);
        }
        for(int i=world_dist+8;i<world_dist+10;i++){
            model.setBlock(i,getFloor((model))-2, MarioLevelModel.USED_BLOCK);
        }
        for(int i=world_dist+9;i<world_dist+10;i++){
            model.setBlock(i,getFloor((model))-3, MarioLevelModel.USED_BLOCK);
        }
        model.setBlock(world_dist+9, getFloor(model) -3, MarioLevelModel.USED_BLOCK);
        for(int i=world_dist+13; i<world_dist+20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        world_dist +=20;
    }

    private void chunk3(MarioLevelModel model){
        for(int i=world_dist; i<world_dist+5;i++){
            model.setBlock(i, getFloor(model)-3, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(i, getFloor(model)-4, MarioLevelModel.COIN);
        }
        for(int i=world_dist+5;i<world_dist+15;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
            if(!(i >= world_dist+13)) {
                model.setBlock(i + 1, getFloor(model) - 5, MarioLevelModel.NORMAL_BRICK);
                model.setBlock(i + 1, getFloor(model) - 6, MarioLevelModel.COIN);
            }
        }
        model.setBlock(world_dist+10, getFloor(model)-2, MarioLevelModel.GREEN_KOOPA);
        for(int i=world_dist+15; i<world_dist+20;i++){
            model.setBlock(i, getFloor(model)-3, MarioLevelModel.NORMAL_BRICK);
            model.setBlock(i, getFloor(model)-4, MarioLevelModel.COIN);
        }
        world_dist +=20;
    }

    private void chunk4(MarioLevelModel model){
        for(int i=world_dist;i<world_dist+20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        model.setBlock(world_dist, getFloor(model)-4, MarioLevelModel.LIFE_HIDDEN_BLOCK);
        model.setBlock(world_dist+5, getFloor(model)-1, MarioLevelModel.SPIKY);
        model.setBlock(world_dist, getFloor(model)-1, MarioLevelModel.PYRAMID_BLOCK);
        model.setBlock(world_dist+10, getFloor(model)-1, MarioLevelModel.PYRAMID_BLOCK);
        model.setBlock(world_dist+15, getFloor(model)-1, MarioLevelModel.SPIKY);
        model.setBlock(world_dist+19, getFloor(model)-1, MarioLevelModel.PYRAMID_BLOCK);
        world_dist +=20;
    }

    private void chunk5(MarioLevelModel model){
        for(int i=world_dist;i<world_dist+20;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
            if(i>world_dist+2&&i<world_dist+18){
                model.setBlock(i, getFloor(model)-1, MarioLevelModel.PYRAMID_BLOCK);
            }
            if(i>world_dist+4&&i<world_dist+16){
                model.setBlock(i, getFloor(model)-2, MarioLevelModel.PYRAMID_BLOCK);
            }
            if(i>world_dist+6&&i<world_dist+14){
                model.setBlock(i, getFloor(model)-3, MarioLevelModel.PYRAMID_BLOCK);
            }
            if(i>world_dist+8&&i<world_dist+12){
                model.setBlock(i, getFloor(model)-4, MarioLevelModel.PYRAMID_BLOCK);
            }
        }
        model.setBlock(world_dist+3, getFloor(model)-2, MarioLevelModel.RED_KOOPA);
        model.setBlock(world_dist+17, getFloor(model)-2, MarioLevelModel.RED_KOOPA);
        model.setBlock(world_dist+7, getFloor(model)-4, MarioLevelModel.RED_KOOPA);
        model.setBlock(world_dist+13, getFloor(model)-4, MarioLevelModel.RED_KOOPA);
        model.setBlock(world_dist+10, getFloor(model)-6, MarioLevelModel.RED_KOOPA);
        model.setBlock(world_dist+10, getFloor(model)-7, MarioLevelModel.SPECIAL_QUESTION_BLOCK);
        world_dist +=20;
    }
    private void end_chunk(MarioLevelModel model){
        for(int i=world_dist;i<world_dist+12;i++){
            model.setBlock(i, getFloor(model), MarioLevelModel.GROUND);
        }
        model.setBlock(world_dist+12, getFloor(model)-3, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+13, getFloor(model)-3, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+14, getFloor(model)-5, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+15, getFloor(model)-5, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+16, getFloor(model)-7, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+17, getFloor(model)-7, MarioLevelModel.PLATFORM);
        model.setBlock(world_dist+18, getFloor(model), MarioLevelModel.GROUND);
        model.setBlock(world_dist+19, getFloor(model), MarioLevelModel.GROUND);
        model.setBlock(world_dist+19, getFloor(model)-1, MarioLevelModel.MARIO_EXIT);
    }

    /**
     * semi-randomly determines the next chunk to build
     * @param currentChunk the id of the most recent chunk
     * @return the id of the next chunk to be built
     */
    private int nextChunk(int currentChunk){
        ArrayList<Double> freqs = transition_matrix.get(currentChunk);
        Double r = rng.nextDouble() % 1.0;
        Double prob = 0.0;
        for (int i=0;i<6;i++){
            prob += freqs.get(i);
            if(r <= prob){
                return i;
            }
        }
        return 0;
    }

    /**
     * calls the chunk generation function for the next chunk
     * @param chunk the id if the next chunk to be built
     * @param model the MarioLevelModel associated with the level
     */
    private void buildNext(int chunk, MarioLevelModel model){

        switch (chunk) {
            case 0:
                chunk0(model);
                break;
            case 1:
                chunk1(model);
                break;
            case 2:
                chunk2(model);
                break;
            case 3:
                chunk3(model);
                break;
            case 4:
                chunk4(model);
                break;
            case 5:
                chunk5(model);
                break;
        }

    }
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){

        //getting random number seed
        rng = new Random();
        //setting up frequency table for chunk transitions
        transition_matrix = new ArrayList<>();
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(0).add(0.0);//repeat
            transition_matrix.get(0).add(.2);
            transition_matrix.get(0).add(.15);
            transition_matrix.get(0).add(.3);
            transition_matrix.get(0).add(.05);
            transition_matrix.get(0).add(.25);
            transition_matrix.get(0).add(.05);
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(1).add(.05);
            transition_matrix.get(1).add(0.0);//repeat
            transition_matrix.get(1).add(.1);
            transition_matrix.get(1).add(.15);
            transition_matrix.get(1).add(.2);
            transition_matrix.get(1).add(.25);
            transition_matrix.get(1).add(.25);
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(2).add(.4);
            transition_matrix.get(2).add(.3);
            transition_matrix.get(2).add(0.0);//repeat
            transition_matrix.get(2).add(.2);
            transition_matrix.get(2).add(.025);
            transition_matrix.get(2).add(.025);
            transition_matrix.get(2).add(.05);
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(3).add(0.3);
            transition_matrix.get(3).add(0.7);
            transition_matrix.get(3).add(0.0);
            transition_matrix.get(3).add(0.0);//repeat
            transition_matrix.get(3).add(0.0);
            transition_matrix.get(3).add(0.0);
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(4).add(0.05);
            transition_matrix.get(4).add(0.15);
            transition_matrix.get(4).add(0.2);
            transition_matrix.get(4).add(0.2);
            transition_matrix.get(4).add(0.0);//repeat
            transition_matrix.get(4).add(0.4);
        transition_matrix.add(new ArrayList<Double>());
            transition_matrix.get(5).add(0.2);
            transition_matrix.get(5).add(0.2);
            transition_matrix.get(5).add(0.086);
            transition_matrix.get(5).add(0.314);
            transition_matrix.get(5).add(0.2);
            transition_matrix.get(5).add(0.0);//repeat

        start_chunk(model);

        int currentChunk = rng.nextInt(5);

        for(int i=0;i<MAX_CHUNKS;i++){
            currentChunk = nextChunk(currentChunk);
            buildNext(currentChunk, model);
        }

        end_chunk(model);
        return model.getMap();
    }

    public String getGeneratorName() {return "AustinFlynnLevelGenerator";}
}
