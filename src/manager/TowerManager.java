package src.manager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.enemies.Enemy;
import src.helperMethods.LoadSave;
import src.objects.Tower;
import src.scenes.Playing;

public class TowerManager {
    private Playing playing;
    private ArrayList<Tower> towers = new ArrayList<>();
    private int towerAmount = 0;
    private final int xOffset = 16;
    private final int yOffset = 32;

    public TowerManager(Playing playing){
        this.playing = playing;
    }

    public void update(){
        attack();
    }

    public void addTower(Tower selectedTower, int x, int y){
        towers.add(new Tower(x, y, towerAmount++, selectedTower.getTowerType()));
    }

    public void draw(Graphics g, int animationIndex){
        for(Tower t : towers)
            g.drawImage(t.getAnimation(0).get(animationIndex), t.getX() - xOffset, t.getY() - yOffset, null);
    }

    private void attack(){
        for(Tower t : towers){
            for(Enemy e : playing.getEnemyManager().getEnemies()){
                if(isInRange(t, e)){
                    e.takeDamage((int)t.getDmg());
                }
            }
        }
    }

    private boolean isInRange(Tower t, Enemy e){
        int distance = src.helperMethods.Util.GetDistance(t.getX(), t.getY(), e.getX(), e.getY());

        return distance < t.getRange();
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
