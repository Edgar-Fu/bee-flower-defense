package src.manager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.helperMethods.LoadSave;
import src.objects.Tile;

public class TileManager {
    public Tile GRASS, WATER, ROAD, STARFISH_WATER, UPSIDE_DOWN_STARFISH;
    public BufferedImage img;
    public ArrayList<Tile> tiles = new ArrayList<>();

    public TileManager(){
        loadEnvironment();
        //createTiles();
    }
    
    public void createTiles(){
        int id = 0;
        tiles.add(GRASS = new Tile(getSprite(23, 5), id++, "Grass"));
        tiles.add(WATER = new Tile(getSprite(21, 17), id++, "Water"));
        tiles.add(ROAD = new Tile(getSprite(8, 20), id++, "Road"));
    }

    public Tile getTile(int id){
        return tiles.get(id);
    }

    public void loadEnvironment(){
        img = LoadSave.getSpriteEnvironment();
    }

    public BufferedImage getSprite(int id){
        int x = id % 32;
        int y = id / 32;

        //System.out.println("id: " + id + ", x: " + x + ", y: " + y);
        return getSprite(x, y);
    }

    public BufferedImage getSprite(int x, int y){
        return img.getSubimage(x*32, y*32, 32, 32);
    }
}
