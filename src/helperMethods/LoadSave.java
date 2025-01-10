package src.helperMethods;

import static src.helperMethods.Constants.Enemies.*;
import static src.helperMethods.Constants.Towers.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class LoadSave {
    public static BufferedImage getSpriteEnvironment(){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream("res/environment.png");

        try{
            img = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }

        return img;
    }

    public static BufferedImage getUnitImg(int unitType, boolean enemy){
        String unitString;
        if(enemy){
            switch (unitType) {
                case HORNET:
                    unitString = "hornets";
                    break;
                case BADGER:
                    unitString = "badgers";
                    break;
                case RACCOON:
                    unitString = "raccoons";
                    break;
                case BEAR:
                    unitString = "bears";
                    break;
                default:
                unitString = "";
                    break;
            }
        }
        else{
            switch (unitType) {
                case WORKER:
                    unitString = "worker_bee";
                    break;
                case FIRE:
                    unitString = "fire_bee";
                    break;
                case FROST:
                    unitString = "frost_bee";
                    break;
                case QUEEN:
                    unitString = "queen_bee";
                    break;
                default:
                    unitString = "";
                    break;
            }
        }
        
        BufferedImage img = null;
        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream("res/" + unitString + ".png");
        try{
            img = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }
        return img;
    }

    public static BufferedImage getProjectileImg(int projectileType){
        
        BufferedImage img = null;
        InputStream is = LoadSave.class.getClassLoader().getResourceAsStream("res/Projectiles/Projectile " + (projectileType + 1) + ".png");
        try{
            img = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }
        return img;
    }

    public static int[][] getLevelData(int level, boolean background){
        File lvlFile;
        if(background)
            lvlFile = new File("res/level " + level + ".txt");
        else
            lvlFile = new File("res/level " + level + " layer.txt");

        if(lvlFile.exists()){
            return ReadLevelFile(lvlFile);
        }
        else{
            System.out.println("File: level " + level + ".txt does not exist");
            return null;
        }
    }
    
    private static int[][] ReadLevelFile(File file){
        int[][] level = new int[20][20];
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            int y = 0;
            while((line = br.readLine()) != null){
                String[] values = line.split(", ");
                
                for(int i = 0; i < values.length; i++){
                    level[y][i] = Integer.parseInt(values[i]);
                }
                y++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return level;
    }

}
