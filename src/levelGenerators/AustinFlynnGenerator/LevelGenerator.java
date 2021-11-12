package levelGenerators.AustinFlynnGenerator;

import java.util.ArrayList;
import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator{

    private ArrayList<ArrayList<Double>> transition_matrix;

    private Random rng;

    private int n_chunks = 0;
    private int MAX_CHUNKS = 5;

    private void start_chunk(){

    }

    private void chunk0(){

    }

    private void chunk1(){

    }

    private void chunk2(){

    }

    private void chunk3(){

    }

    private void chunk4(){

    }

    private void chunk5(){


    }
    private void end_chunk(){

    }

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

    private void buildNext(int chunk){
        if(n_chunks < MAX_CHUNKS) {
            switch (nextChunk(chunk)) {
                case 0:
                    chunk0();
                    break;
                case 1:
                    chunk1();
                    break;
                case 2:
                    chunk2();
                    break;
                case 3:
                    chunk3();
                    break;
                case 4:
                    chunk4();
                    break;
                case 5:
                    chunk5();
                    break;
            }

        }
        else{
            end_chunk();
        }
    }
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer){
        //getting random number seed
        rng = new Random();
        //setting up frequency table for chunk transitions
        transition_matrix = new ArrayList<>();
        transition_matrix.add(new ArrayList<Double>());
        transition_matrix.add(new ArrayList<Double>());
        transition_matrix.add(new ArrayList<Double>());
        transition_matrix.add(new ArrayList<Double>());
        transition_matrix.add(new ArrayList<Double>());
        transition_matrix.add(new ArrayList<Double>());

        start_chunk();
        switch(rng.nextInt(5)){
            case 0:
                chunk0();
                break;
            case 1:
                chunk1();
                break;
            case 2:
                chunk2();
                break;
            case 3:
                chunk3();
                break;
            case 4:
                chunk4();
                break;
            case 5:
                chunk5();
                break;
        }
        return model.getMap();
    }

    public String getGeneratorName() {return "AustinFlynnLevelGenerator";}
}
