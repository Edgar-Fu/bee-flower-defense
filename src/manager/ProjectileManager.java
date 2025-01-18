package src.manager;

import static src.helperMethods.Constants.Direction.*;
import static src.helperMethods.Constants.Projectiles.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.enemies.Enemy;
import src.helperMethods.LoadSave;
import src.objects.Projectile;
import src.objects.Tower;
import src.scenes.Playing;

public class ProjectileManager {
    private Playing playing;

    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage[][] projectileAnimations = new BufferedImage[4][4];
    private int proj_id = 0;

    private final int xOffset = 16;
    private final int yOffset = 32;

    public ProjectileManager(Playing playing){
        this.playing = playing;
        initSprites();
    }

    public void update(){
        for(int i = 0; i < projectiles.size(); i++){
            projectiles.get(i).move();
            if(projectileHit(projectiles.get(i))){
                projectiles.remove(i);
            }
        }
    }

    public boolean projectileHit(Projectile p){
        for(Enemy e : playing.getEnemyManager().getEnemies()){
            if(e.getBounds().contains(p.getPos())){
                e.takeDamage(p.getDamage());
                if(p.getProjectileType() == ICEBALL)
                    e.slow();
                else if(p.getProjectileType() == FIREBALL)
                    e.burn();
                return true;
            }
        }
        return false;
    }

    public void newProjectile(Tower t, Enemy e){
        //int[] coordinates = playing.getEnemyManager().enemyOffsetHandler(e);
        int xDist = (int)(t.getX() - e.getX());
        int yDist = (int)(t.getY() - e.getY());
        int totalDist = Math.abs(xDist) + Math.abs(yDist);

        float xPer = (float) Math.abs(xDist) / totalDist;

        float xSpeed = xPer * src.helperMethods.Constants.Projectiles.GetSpeed(t.getTowerType());
        float ySpeed = src.helperMethods.Constants.Projectiles.GetSpeed(t.getTowerType()) - xSpeed;

        if(t.getX() > e.getX())
            xSpeed *= -1;
        if(t.getY() > e.getY())
            ySpeed *= -1;

        float arcValue = (float) Math.atan(yDist / (float) xDist);
        float rotation = (float) Math.toDegrees(arcValue);

        if(xDist > 0)
            rotation += 180;

        projectiles.add(new Projectile(t.getX() + 16, t.getY() + 16, xSpeed, ySpeed, rotation, t.getDmg(), proj_id++, t.getTowerType()));
    }

    private void initSprites(){
        for(int i = 0; i < 4; i++){
            readFromSpritesheets(i);
        }
    }

    private void readFromSpritesheets(int projectileType){
        //get each projectile'sspritesheet
        BufferedImage img = LoadSave.getProjectileImg(projectileType);

        projectileAnimations[projectileType] = animate(img);
    }

    //create the animation array
    private BufferedImage[] animate(BufferedImage img){
        BufferedImage[] animation = new BufferedImage[4];
        for(int i = 0; i < 4; i++){
            animation[i] = img.getSubimage(i * 64, 0, 64, 64);
        }
        return animation;
    }


    public void draw(Graphics g, int projIndex){
        Graphics2D g2d = (Graphics2D) g;

        for(int i = 0; i < projectiles.size(); i++){
            g2d.translate(projectiles.get(i).getPos().x, projectiles.get(i).getPos().y);
            g2d.rotate(Math.toRadians(projectiles.get(i).getRotation()));
            g2d.drawImage(projectileAnimations[projectiles.get(i).getProjectileType()][projIndex], - 32, - 32, null);
            g2d.rotate(-Math.toRadians(projectiles.get(i).getRotation()));
            g2d.translate(-projectiles.get(i).getPos().x, -projectiles.get(i).getPos().y);
        }

        
        
    }
}
