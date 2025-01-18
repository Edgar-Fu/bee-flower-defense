package src.manager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.enemies.*;
import src.helperMethods.ImgFix;
import src.helperMethods.LoadSave;
import src.scenes.Playing;
import static src.helperMethods.Constants.Direction.*;
import static src.helperMethods.Constants.Enemies.*;
import static src.helperMethods.Constants.Tiles.*;

public class EnemyManager {
    private Playing playing;
    //Arraylist of all enemies present in the map
    private ArrayList<Enemy> enemies = new ArrayList<>();
    //3D array of all enemy sprites, going by spriteAnimations[enemyType][direction][frame]
    private BufferedImage[][][] spriteAnimations = new BufferedImage[4][4][4];
    private int[][] lvl;
    private int walkTick;
    
    public EnemyManager(Playing playing){
        this.playing = playing;
        lvl = playing.getLevel();
        initSprites();
        
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
        //speed of their walking animation
        //increment walkTick, then divide by 6 to get the animation array index
        walkTick++;
        if(walkTick >= 24)
            walkTick = 0;

        for(int i = 0; i < enemies.size(); i++){
            if(!enemies.get(i).isAlive()){
                enemies.remove(i--);
                continue;
            }
            Pathfind(enemies.get(i));
        }
    }

    private void initSprites(){
        for(int i = 0; i < 4; i++){
            readFromSpritesheets(i);
        }
    }

    private void readFromSpritesheets(int enemyType){
        //get each enemy's spritesheet
        BufferedImage img = LoadSave.getUnitImg(enemyType, true);

        //coordinates in the spritesheet
        int x = 0; //cycles through animation frames
        int y = 0; //changes direction the enemy is facing
        int z = 48;//length of one sprite's dimension (e.g. 32x32, 64x64)

        switch (enemyType) {
            case HORNET:
                x = 6;
                z = 64; //z only changes for hornets, which have different sized sprites
                break;
            case BADGER:
            case BEAR:
                x = 3;
                y = 4;
                break;
        }

        //get the correct sprites for each direction
        if(enemyType != HORNET){
            spriteAnimations[enemyType][LEFT] = animate(img, x, y + 1, z);
            spriteAnimations[enemyType][UP] = animate(img, x, y + 3, z);
            spriteAnimations[enemyType][RIGHT] = animate(img, x, y + 2, z);
            spriteAnimations[enemyType][DOWN] = animate(img, x, y, z);
        }
        else{
            spriteAnimations[enemyType][LEFT] = ImgFix.getFlipped(animate(img, x, y, z));
            spriteAnimations[enemyType][RIGHT] = animate(img, x, y, z);
        }
    }

    //create the animation array
    private BufferedImage[] animate(BufferedImage img, int x, int y, int z){
        BufferedImage[] animation = new BufferedImage[4];
        animation[0] = img.getSubimage(x * z, y * z, z, z);
        animation[1] = img.getSubimage((x + 1) * z, y * z, z, z);
        animation[2] = img.getSubimage((x + 2) * z, y * z, z, z);
        animation[3] = img.getSubimage((x + 1) * z, y * z, z, z);
        return animation;
    }

    //drawing methods
    public void draw(Graphics g){
        for(Enemy e : enemies){
            drawEnemy(e, g);
            drawHealthBar(e, g);
        }
    }

    private void drawEnemy(Enemy e, Graphics g){
        int[] coordinates = enemyOffsetHandler(e);
        int direction = e.getLastDir();

        if(e.getEnemyType() == HORNET)
            direction = e.getHornetFacing();

        //System.out.print(direction);
        g.drawImage(spriteAnimations[e.getEnemyType()][direction][walkTick / 6], coordinates[0], coordinates[1], null);

        //drawHitbox(e, g);
    }

    private void drawHealthBar(Enemy e, Graphics g){
        int x = (int) e.getX();
        int y = (int) e.getY();

        if(e.getEnemyType() == HORNET)
            y -= 28;
        else{
            if(e.getEnemyType() == BEAR)
                y -= 12;
            else
                y -= 8;
        }

        g.setColor(Color.black);
        //g.drawRect(x + 2, y, 28, 5);
        g.fillRect(x + 2, y, 28, 5);

        g.setColor(Color.red);
        g.fillRect(x + 2, y, (int) (e.getHealthBar() * 28), 5);
    }

    private void drawHitbox(Enemy e, Graphics g){
        g.setColor(Color.black);
        g.drawRect(e.getBounds().x, e.getBounds().y, e.getBounds().width, e.getBounds().height);
    }

    //handling image offsets so every enemy is centered
    public int[] enemyOffsetHandler(Enemy e){
        int x = (int) e.getX();
        int y = (int) e.getY();

        if(e.getEnemyType() == HORNET){
            y -= 20;
            if(e.getHornetFacing() == RIGHT)
                x -= 20;
            else
                x -= 12;
        }
        else if(e.getLastDir() == RIGHT || e.getLastDir() == LEFT){
            y -= 24;
            switch (e.getEnemyType()) {
                case BADGER:
                    x -= 5;
                    break;
                case RACCOON:
                    x -= 8;
                    break;
                case BEAR:
                    x -= 5;
                    break;
            }
        }
        else{
            y -= 14;
            switch (e.getEnemyType()) {
                case BADGER:
                    x -= 5;
                    break;
                case RACCOON:
                    x -= 8;
                    break;
                case BEAR:
                    x -= 9;
                    break;
            }
        }

        return new int[]{x, y};
    }

    //pathfinding methods and helpers
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
        //if all those options fail it has reached the end
        else if(!checkForTurn(e, x, y))
            e.kill();
            
        
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

    private boolean checkForTurn(Enemy e, int x, int y){
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
            return true;
        }
        else if(e.getLastDir() != RIGHT && x - 1 >= 0 && pathTiles.contains(lvl[y][x - 1])){
            e.move(getSpeed(e.getEnemyType()), LEFT);
            return true;
        }
        else if(e.getLastDir() != UP && y + 1 < lvl.length && pathTiles.contains(lvl[y + 1][x])){
            e.move(getSpeed(e.getEnemyType()), DOWN);
            return true;
        }
        else if(e.getLastDir() != DOWN && y - 1 >= 0 && pathTiles.contains(lvl[y - 1][x])){
            e.move(getSpeed(e.getEnemyType()), UP);
            return true;
        }
        else
            return false;
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
}
