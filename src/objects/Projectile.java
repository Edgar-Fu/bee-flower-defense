package src.objects;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Projectile {

    private Point2D.Float pos;
    private int id, projectileType, damage;
    private float xSpeed, ySpeed, rotation;
    private boolean active;

    public Projectile(float x, float y, float xSpeed, float ySpeed, float rotation, int damage, int id, int projectileType){
        pos = new Point2D.Float(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.rotation = rotation;
        this.damage = damage;
        this.id = id;
        this.projectileType = projectileType;
        active = true;
    }

    public void move(){
        pos.x += xSpeed;
        pos.y += ySpeed;
    }

    public Point2D.Float getPos() {
        return this.pos;
    }

    public void setPos(Point2D.Float pos) {
        this.pos = pos;
    }

    public int getDamage(){
        return damage;
    }

    public int getId() {
        return this.id;
    }

    public int getProjectileType() {
        return this.projectileType;
    }

    public float getRotation(){
        return rotation;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
