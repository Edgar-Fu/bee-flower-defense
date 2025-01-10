package src.enemies;

import java.awt.Rectangle;

import static src.helperMethods.Constants.Direction.*;
import static src.helperMethods.Constants.Enemies.*;

public abstract class Enemy {
    private float x, y;
    private Rectangle bounds;
    private int currHealth, maxHealth, id, enemyType, lastDir;
    private int hornetFacing = RIGHT;
    private boolean isAlive = true;
    

    public Enemy(float x, float y, int id, int enemyType){
        this.x = x;
        this.y = y;
        this.id = id;
        this.enemyType = enemyType;
        bounds = new Rectangle((int) x, (int) y, 32, 32);
        lastDir = -1;
        setStartHealth();
    }

    public void takeDamage(int damage){
        //System.out.println("Taking " + damage + " Damage! Current health: " + currHealth);
        this.currHealth -= damage;
        if(currHealth <= 0)
            isAlive = false;
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

        updateHitbox();
    }

    public void updateHitbox(){
        bounds.x = (int) x;
        bounds.y = (int) y;
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
