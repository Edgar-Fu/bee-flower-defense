package src.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.helperMethods.LoadSave;

public class Tower {
    private int x, y, id, towerType;
    private ArrayList<ArrayList<BufferedImage>> animations = new ArrayList<ArrayList<BufferedImage>>();
    private float dmg, range, cooldown;

    public Tower(int x, int y, int id, int towerType){
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        initAnimations();
        setDefaultDmg();
        setDefaultRange();
        setDefaultCooldown();
    }

    private void initAnimations(){
        BufferedImage img = LoadSave.getUnitImg(towerType, false);
        
        //idle animation
        animations.add(animate(img, 0, 8));
        //hit animation
        animations.add(animate(img, 1, 3));
        //attack animation
        animations.add(animate(img, 2, 6));
        //death animation
        animations.add(animate(img, 3, 7));
    }

    private ArrayList<BufferedImage> animate(BufferedImage img, int animationType, int frames){
        ArrayList<BufferedImage> animation = new ArrayList<>();
        for(int i = 0; i < frames; i++){
            animation.add(img.getSubimage(i * 64, animationType * 64, 64, 64));
        }
        return animation;
    }

    public ArrayList<BufferedImage> getAnimation(int animationType){
        return animations.get(animationType);
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

    public float getDmg() {
        return this.dmg;
    }

    public float getRange() {
        return this.range;
    }

    public float getCooldown() {
        return this.cooldown;
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
}
