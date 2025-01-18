package src.manager;

import java.util.ArrayList;
import java.util.Arrays;

import src.events.Wave;
import src.scenes.Playing;

public class WaveManager {

    private Playing playing;
    private ArrayList<Wave> waves = new ArrayList<>();
    private int spawnTickLimit = 60 * 1;
    private int spawnTick = spawnTickLimit;
    private int waveIndex;
    private int enemyIndex;

    public WaveManager(Playing playing){
        this.playing = playing;
        createWaves();
    }

    public void update(){
        if(spawnTick < spawnTickLimit)
            spawnTick++;
    }

    public int getNextEnemy(){
        spawnTick = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    private void createWaves(){
        waves.add(new Wave(new ArrayList<>(Arrays.asList(1, 1, 1, 0, 1, 2, 2, 0, 3, 3, 3, 0))));
    }

    public ArrayList<Wave> getWaves(){
        return waves;
    }

    public boolean spawnDelayOver(){
        return spawnTick >= spawnTickLimit;
    }

    public boolean isMoreEnemies(){
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    }

    public boolean isMoreWaves(){
        return waveIndex < waves.size();
    }
}
