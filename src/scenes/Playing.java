package src.scenes;

import static src.helperMethods.Constants.Tiles.pathTiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import src.helperMethods.LoadSave;
import src.enemies.Enemy;
import src.helperMethods.ImgFix;
import src.main.Game;
import src.manager.EnemyManager;
import src.manager.ProjectileManager;
import src.manager.TowerManager;
import src.manager.WaveManager;
import src.objects.Tower;
import src.ui.ActionBar;


public class Playing extends GameScene implements SceneMethods{
    //level data
    private int[][] lvl;
    private int[][] layer;

    private ActionBar actionBar;

    private int mouseX, mouseY;

    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private ProjectileManager projectileManager;
    private WaveManager waveManager;

    private int projIndex = 3;
    private int tick;
    private int ANIMATION_SPEED = 5;
    private Tower selectedTower;
    
    public Playing(Game game){
        super(game);
        loadLevel();
        actionBar = new ActionBar(0, 640, 640, 100, this);
        enemyManager = new EnemyManager(this);
        towerManager = new TowerManager(this);
        projectileManager = new ProjectileManager(this);
        waveManager = new WaveManager(this);
    }

    private void loadLevel(){
        lvl = LoadSave.getLevelData(1, true);
        layer = LoadSave.getLevelData(1, false);
    }

    public void setLevel(int[][] lvl){
        this.lvl = lvl;
    }

    public void update(){
        updateTick();
        waveManager.update();

        

        if(timeToSpawn())
            spawnEnemy();
        enemyManager.update();
        towerManager.update();
        projectileManager.update();
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
        actionBar.draw(g);
        enemyManager.draw(g);
        towerManager.draw(g);
        projectileManager.draw(g, projIndex);
        drawSelectedTower(g);
    }

    private void drawHighLight(Graphics g){
        g.drawRect(mouseX, mouseY, 32, 32);
    }

    private void drawSelectedTower(Graphics g){
        if(selectedTower != null){
            if(!pathTiles.contains(lvl[mouseY / 32][mouseX / 32])){
                g.setColor(Color.white);
            }
            else
                g.setColor(Color.red);
            
            drawHighLight(g);
            g.drawImage(towerManager.getIcons().get(selectedTower.getTowerType()), mouseX - 4, mouseY - 20, null);
            
        }
    }

    private void updateTick(){
        tick++;
        if(tick >= ANIMATION_SPEED){
            tick = 0;
            projIndex++;
            if(projIndex >= 4)
                projIndex = 0;
        }
    }
    public void shootEnemy(Tower t, Enemy e) {
        projectileManager.newProjectile(t, e);
    }

    private void drawLevel(Graphics g){
        for(int y = 0; y < lvl.length; y++){
            for(int x = 0; x < lvl[y].length; x++){
                int idBack = lvl[y][x];
                int idFront = layer[y][x];

                if(idFront > -1){
                    BufferedImage[] combine = {getSprite(idBack), getSprite(idFront)};
                    g.drawImage(ImgFix.buildImg(combine), x*32, y*32, null);
                }
                else
                    g.drawImage(getSprite(idBack), x*32, y*32, null);
            }
        }
    }

    private void spawnEnemy(){
        enemyManager.addEnemy(0 * 32, 15 * 32, waveManager.getNextEnemy());
    }

    private boolean timeToSpawn(){
        if(waveManager.spawnDelayOver()){
            if(waveManager.isMoreEnemies()){
                return true;
            }
        }
        return false;
    }

    //Getters and Setters

    private BufferedImage getSprite(int spriteID){
        return getGame().getTileManager().getSprite(spriteID);
    }

    public int getTile(double x, double y){
        return lvl[(int) y][(int) x];
    }

    public int[][] getLevel(){
        return lvl;
    }

    public TowerManager getTowerManager(){
        return towerManager;
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public WaveManager getWaveManager(){
        return waveManager;
    }

    public void setSelectedTower(Tower selectedTower){
        this.selectedTower = selectedTower;
    }

    //Keyboard and Mouse Listeners

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            selectedTower = null;
    }

    @Override
    public void mouseClicked(int x, int y) {
        if(y >= 640)
            actionBar.mouseClicked(x, y);
        else{
            if(selectedTower != null){
                //can't place tower on path or on an already existing tower
                if(!pathTiles.contains(lvl[mouseY / 32][mouseX / 32]) && getTowerAt(mouseX, mouseY) == null){
                    towerManager.addTower(selectedTower, mouseX, mouseY);
                    selectedTower = null;
                }
            }
            else{
                //display info of tower on click
                actionBar.displayTower(getTowerAt(mouseX, mouseY));
            }
        }
    }

    private Tower getTowerAt(int x, int y){
        return towerManager.getTowerAt(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        if(y >= 640)
            actionBar.mouseMoved(x, y);
        else{
            mouseX = (x / 32) * 32;
            mouseY = (y / 32) * 32;
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if(y >= 640)
            actionBar.mousePressed(x, y);
        
    }

    @Override
    public void mouseReleased(int x, int y) {
        actionBar.mouseReleased(x, y);
    }

    @Override
    public void mouseDragged(int x, int y) {
    }
}
