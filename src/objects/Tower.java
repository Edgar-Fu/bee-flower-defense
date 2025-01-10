package src.objects;

import static src.helperMethods.Constants.Direction.*;

public class Tower {
    private int x, y, id, towerType, dmg;
    private float range, cooldown;
    private int cdTick, attackTick;
    private int direction = LEFT;
    private boolean attacking = false;

    public Tower(int x, int y, int id, int towerType){
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        setDefaultDmg();
        setDefaultRange();
        setDefaultCooldown();
    }

    public void update(){
        cdTick++;
    }

    public boolean isCooldownOver(){
        return cdTick >= cooldown;
    }

    public void resetCooldown(){
        cdTick = 0;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getId(){
        return id;
    }

    public int getTowerType(){
        return towerType;
    }

    public int getDmg() {
        return this.dmg;
    }

    public float getRange() {
        return this.range;
    }

    public float getCooldown() {
        return this.cooldown;
    }

    public float getDirection(){
        return direction;
    }

    public boolean isAttacking(){
        return attacking;
    }

    private void setDefaultDmg(){
        dmg = src.helperMethods.Constants.Towers.GetStartDmg(towerType);
    }

    private void setDefaultRange(){
        range = src.helperMethods.Constants.Towers.GetDefaultRange(towerType);
    }

    private void setDefaultCooldown(){
        cooldown = src.helperMethods.Constants.Towers.GetDefaultCooldown(towerType);
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }
}
