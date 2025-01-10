package src.manager;

import static src.helperMethods.Constants.Direction.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.enemies.Enemy;
import src.helperMethods.ImgFix;
import src.helperMethods.LoadSave;
import src.objects.Tower;
import src.scenes.Playing;

public class TowerManager {
    private Playing playing;
    //Arraylist of all the towers on the map
    private ArrayList<Tower> towers = new ArrayList<>();

    private BufferedImage[][][] spriteAnimations = new BufferedImage[4][4][8];

    private int towerAmount = 0;
    private final int xOffset = 16;
    private final int yOffset = 32;

    public TowerManager(Playing playing){
        this.playing = playing;
        initSprites();
    }

    public void update(){
        for(Tower t : towers){
            t.update();
            attack(t);
        }
        
    }

    public void addTower(Tower selectedTower, int x, int y){
        towers.add(new Tower(x, y, towerAmount++, selectedTower.getTowerType()));
    }

    private void initSprites(){
        for(int i = 0; i < 4; i++){
            readFromSpritesheets(i);
        }
    }

    private void readFromSpritesheets(int towerType){
        //get each enemy's spritesheet
        BufferedImage img = LoadSave.getUnitImg(towerType, false);
        
        //idle left animation
        spriteAnimations[towerType][0] = ImgFix.getFlipped(animate(img, 0, 8));

        //idle right animation
        spriteAnimations[towerType][1] = animate(img, 0, 8);

        //attack left animation
        spriteAnimations[towerType][2] = ImgFix.getFlipped(animate(img, 2, 6));

        //attack right animation
        spriteAnimations[towerType][3] = animate(img, 2, 6);
    }

    //create the animation array
    private BufferedImage[] animate(BufferedImage img, int animationType, int frames){
        BufferedImage[] animation = new BufferedImage[8];
        for(int i = 0; i < frames; i++){
            animation[i] = img.getSubimage(i * 64, animationType * 64, 64, 64);
        }
        return animation;
    }

    public void draw(Graphics g, int idleIndex, int attackIndex){
        for(Tower t : towers){
            BufferedImage img;
            
            //idle
            if(!t.isAttacking()){
                //left
                if(t.getDirection() == LEFT)
                    img = spriteAnimations[t.getTowerType()][0][idleIndex];
                //right
                else
                    img = spriteAnimations[t.getTowerType()][1][idleIndex];
            }
            //attack
            else{
                //left
                if(t.getDirection() == LEFT){
                    //System.out.print(attackIndex);
                    img = spriteAnimations[t.getTowerType()][2][attackIndex];
                }
                //right
                else
                    img = spriteAnimations[t.getTowerType()][3][attackIndex];
            }

            g.drawImage(img, t.getX() - xOffset, t.getY() - yOffset, null);
        }
    }

    private void attack(Tower t){
        t.setAttacking(false);
        for(Enemy e : playing.getEnemyManager().getEnemies()){
            if(isInRange(t, e)){
                t.setAttacking(true);
                setTowerDirection(t, e);

                if(t.isCooldownOver()){
                    playing.shootEnemy(t, e);
                    t.resetCooldown();
                }

                //e.takeDamage((int)t.getDmg());
                
                
            }
        }
    }

    private boolean isInRange(Tower t, Enemy e){
        int distance = src.helperMethods.Util.GetDistance(t.getX(), t.getY(), e.getX(), e.getY());

        return distance < t.getRange();
    }

    private void setTowerDirection(Tower t, Enemy e){
        if(t.getX() < e.getX())
            t.setDirection(RIGHT);
        else
            t.setDirection(LEFT);
    }

    //get the first sprite image for each tower to use as an icon for the buttons
    public ArrayList<BufferedImage> getIcons(){
        ArrayList<BufferedImage> icons = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            BufferedImage spriteSheet = LoadSave.getUnitImg(i, false);
            icons.add(spriteSheet.getSubimage(12, 12, 40, 40));
        }
        return icons;
    }

    public Tower getTowerAt(int x, int y){
        for(Tower t : towers){
            if(t.getX() == x && t.getY() == y)
                return t;
        }
        return null;
    }
}
