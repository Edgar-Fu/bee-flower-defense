package src.manager;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import src.enemies.*;
import src.scenes.Playing;
import static src.helperMethods.Constants.Direction.*;
import static src.helperMethods.Constants.Enemies.*;
import static src.helperMethods.Constants.Tiles.*;

public class EnemyManager {
    private Playing playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    //private float speed = 0.5f;
    private int[][] lvl;
    
    public EnemyManager(Playing playing){
        this.playing = playing;
        lvl = playing.getLevel();
        addEnemy(0 * 32, 15 * 32, HORNET);
        addEnemy(0 * 32, 15 * 32, BADGER);
        addEnemy(0 * 32, 15 * 32, RACCOON);
        addEnemy(0 * 32, 15 * 32, BEAR);
    }

    public void addEnemy(int x, int y, int enemyType){
        switch (enemyType) {
            case HORNET:
                enemies.add(new Hornet(x, y, enemyType));
                break;
            case BADGER:
                enemies.add(new Badger(x, y, enemyType));
                break;
            case RACCOON:
                enemies.add(new Raccoon(x, y, enemyType));
                break;
            case BEAR:
                enemies.add(new Bear(x, y, enemyType));
                break;
        }
    }

    public void update(){
        for(int i = 0; i < enemies.size(); i++){
            if(!enemies.get(i).isAlive()){
                enemies.remove(i);
                return;
            }
            
            Pathfind(enemies.get(i));
        }
    }

    public void draw(Graphics g, int animationIndex){
        for(Enemy e : enemies){
            drawEnemy(e, g, animationIndex);
            drawHealthBar(e, g);
        }
    }

    private int[] enemyOffsetHandler(Enemy e){
        int[] newCoordinates = new int[2];
        int x = (int) e.getX();
        int y = (int) e.getY();

        //handling image offsets from different sized sprites
        switch (e.getEnemyType()) {
            case HORNET:
                y -= 24;
                if(e.getHornetFacing() == RIGHT)
                    x -= 20;
                else
                    x -= 12;
                break;
            case BADGER:
                x -= 5;
                y -= 30;
                break;
            case RACCOON:
                x -= 8;
                y -= 30;
                break;
            case BEAR:
                x -= 10;
                y -= 30;
                break;
        }

        newCoordinates[0] = x;
        newCoordinates[1] = y;

        return newCoordinates;
    }

    private void drawEnemy(Enemy e, Graphics g, int animationIndex){
        int[] coordinates = enemyOffsetHandler(e);
        g.drawImage(e.getAnimation()[animationIndex], coordinates[0], coordinates[1], null);
    }

    private void drawHealthBar(Enemy e, Graphics g){
        int x = (int) e.getX();
        int y = (int) e.getY();

        if(e.getEnemyType() == HORNET)
            y -= 28;
        else if(e.getLastDir() == UP || e.getLastDir() == DOWN)
            y -= 24;
        else{
            x = enemyOffsetHandler(e)[0] + 8;
            y -= 12;
        }

        g.setColor(Color.red);
        System.out.println((int) e.getHealthBar() * 28 + "px");
        g.fillRect(x + 2, y, (int) (e.getHealthBar() * 28), 5);
    }

    private void Pathfind(Enemy e){
        //Enemy's current coordinates, rounded to a whole number
        int x = (int) e.getX() / 32;
        int y = (int) e.getY() / 32;
        
        //continue moving if the enemy unit isn't at a coordinate
        if(e.getX() % 32 != 0 || e.getY() % 32 != 0){
            //if the speed doesn't allow the enemy to reach an exact coordinate (e.g. from 63.9 to 64.2, skipping 64), move to the coordinate instead
            float offset = (e.getX() % 32) + (e.getY() % 32);
            switch (e.getLastDir()) {
                case RIGHT:
                case DOWN:
                    offset = Math.abs(offset - 32);
                    break;
            }
            
            if(offset < getSpeed(e.getEnemyType()))
                e.move(offset, e.getLastDir());
            else
                e.move(getSpeed(e.getEnemyType()), e.getLastDir());
        }
        //if it is at an exact coordinate, prioritize going in the same direction
        else if(canGoStraight(e, x, y)){
            e.move(getSpeed(e.getEnemyType()), e.getLastDir());
        }
        //if it can't go straight, look for a turn
        else
            checkForTurn(e, x, y);
    }

    private boolean canGoStraight(Enemy e, int x, int y){
        if(e.getLastDir() == RIGHT){
            if(x + 1 < lvl.length && pathTiles.contains(lvl[y][x + 1])){
                return true;
            }
        }
        else if(e.getLastDir() == LEFT){
            if(x - 1 >= 0 && pathTiles.contains(lvl[y][x - 1])){
                return true;
            }
        }
        else if(e.getLastDir() == DOWN){
            if(y + 1 < lvl.length && pathTiles.contains(lvl[y + 1][x])){
                return true;
            }
        }
        else if(e.getLastDir() == UP){
            if(y - 1 >= 0 && pathTiles.contains(lvl[y - 1][x])){
                return true;
            }
        }
        return false;
    }

    private void checkForTurn(Enemy e, int x, int y){
        /*
        for each potential next direction, there are 3 if cases
            1. check if the enemy just came from there. if it did, then don't go back
            2. check if the tile in that direction is out of bounds
            3. check if the tile in that direction is part of the path
        if a direction satisfies all those conditions, then it turns to that direction
        */

        //System.out.println("Weighing options");
        if(e.getLastDir() != LEFT && x + 1 < lvl.length && pathTiles.contains(lvl[y][x + 1])){
            e.move(getSpeed(e.getEnemyType()), RIGHT);
            //System.out.println("Right");
        }
        else if(e.getLastDir() != RIGHT && x - 1 >= 0 && pathTiles.contains(lvl[y][x - 1])){
            e.move(getSpeed(e.getEnemyType()), LEFT);
            //System.out.println("Left");
        }
        else if(e.getLastDir() != UP && y + 1 < lvl.length && pathTiles.contains(lvl[y + 1][x])){
            e.move(getSpeed(e.getEnemyType()), DOWN);
            //System.out.println("Down");
        }
        else if(e.getLastDir() != DOWN && y - 1 >= 0 && pathTiles.contains(lvl[y - 1][x])){
            e.move(getSpeed(e.getEnemyType()), UP);
            //System.out.println("Up");
        }
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
}
