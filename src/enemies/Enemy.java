package src.enemies;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.helperMethods.ImgFix;
import src.helperMethods.LoadSave;
import static src.helperMethods.Constants.Direction.*;
import static src.helperMethods.Constants.Enemies.*;

public abstract class Enemy {
    private float x, y;
    private Rectangle bounds;
    private int currHealth, maxHealth;
    private int id;
    private int enemyType;
    private BufferedImage[][] animations = new BufferedImage[4][4];
    private int lastDir;
    private int hornetFacing = RIGHT;
    private boolean isAlive = true;
    

    public Enemy(float x, float y, int id, int enemyType){
        this.x = x;
        this.y = y;
        this.id = id;
        this.enemyType = enemyType;
        bounds = new Rectangle((int) x, (int) y, 64, 64);
        lastDir = -1;
        initSprites();
        setStartHealth();
    }

    public void takeDamage(int damage){
        //System.out.println("Taking " + damage + " Damage! Current health: " + currHealth);
        this.currHealth -= damage;
        if(currHealth <= 0)
            isAlive = false;
        
    }

    private void initSprites(){
        //get the current enemy's spritesheet based on its type
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

        if(enemyType != HORNET){
            animations[LEFT] = animate(img, x, y + 1, z);
            animations[UP] = animate(img, x, y + 3, z);
            animations[RIGHT] = animate(img, x, y + 2, z);
            animations[DOWN] = animate(img, x, y, z);
        }
        else{
            animations[LEFT] = ImgFix.getFlipped(animate(img, x, y, z));
            animations[RIGHT] = animate(img, x, y, z);
        }
    }

    private BufferedImage[] animate(BufferedImage img, int x, int y, int z){
        BufferedImage[] animation = new BufferedImage[4];
        animation[0] = img.getSubimage(x * z, y * z, z, z);
        animation[1] = img.getSubimage((x + 1) * z, y * z, z, z);
        animation[2] = img.getSubimage((x + 2) * z, y * z, z, z);
        animation[3] = img.getSubimage((x + 1) * z, y * z, z, z);
        return animation;
    }

    public void move(float speed, int direction){
        lastDir = direction;

        //custom direction for hornets, who can't face up or down
        //this makes a hornet moving vertically face the direction it was previously facing horizontally 
        if(enemyType == HORNET)
            if(direction != UP && direction != DOWN)
                hornetFacing = direction;

        switch(direction){
            case LEFT:
                this.x -= speed;
                break;
            case UP:
                this.y -= speed;
                break;
            case RIGHT:
                this.x += speed;
                break;
            case DOWN:
                this.y += speed;
                break;
        }
        //round to two decimal places for binary representation issue
        this.x = Math.round(x * 100) / 100f;
        this.y = Math.round(y * 100) / 100f;
    }

    /* 
    public ArrayList<BufferedImage> getMovementAnimation(){
        //get the current enemy's spritesheet based on type
        BufferedImage img = LoadSave.getUnitImg(enemyType, true);
        //coordinates in the spritesheet
        int x = 0;
        int y = 0;
        //length of one sprite's dimension
        int z = 48;
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

        return animate(img, x, y, z);
    }

    private ArrayList<BufferedImage> animate(BufferedImage img, int x, int y, int z){
        //x, y are coordinates on the full spritesheet img
            //x cycles through movement frames
            //y points to downsprite, changed based on enemy's facing direction
        //z is the size of each dimension (48x48, 64x64, etc)
        
        if(enemyType != HORNET){
            switch (lastDir) {
                case RIGHT:
                    y += 2;
                    break;
                case UP:
                    y += 3;
                    break;
                case LEFT:
                    y += 1;
                    break;
            }
        }

        ArrayList<BufferedImage> animation = new ArrayList<>();
        animation.add(img.getSubimage(x * z, y * z, z, z));
        animation.add(img.getSubimage((x + 1) * z, y * z, z, z));
        animation.add(img.getSubimage((x + 2) * z, y * z, z, z));
        animation.add(img.getSubimage((x + 1) * z, y * z, z, z));
        return animation;
    }
    */

    public BufferedImage[] getAnimation(){
        if(enemyType == HORNET)
            return animations[hornetFacing];
        return animations[lastDir];
    }

    public void resetCoordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public int getId() {
        return this.id;
    }

    public int getEnemyType() {
        return this.enemyType;
    }

    public int getLastDir(){
        return lastDir;
    }

    public int getHornetFacing(){
        return hornetFacing;
    }

    public float getHealthBar(){
        return (float)currHealth / (float)maxHealth;
    }

    public boolean isAlive(){
        return isAlive;
    }

    private void setStartHealth(){
        currHealth = src.helperMethods.Constants.Enemies.GetStartHealth(enemyType);
        maxHealth = currHealth;
    }
}
