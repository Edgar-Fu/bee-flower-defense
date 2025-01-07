package src.helperMethods;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImgFix {
    //combine sprites
    public static BufferedImage buildImg(BufferedImage[] imgs){
        int w = imgs[0].getWidth();
        int h = imgs[0].getHeight();

        BufferedImage newImg = new BufferedImage(w, h, imgs[0].getType());
        Graphics2D g2d = newImg.createGraphics();

        for(BufferedImage img : imgs){
            g2d.drawImage(img, 0, 0, null);
        }

        g2d.dispose();
        return newImg;
    }

    //flip sprite horizontally
    public static BufferedImage getFlipped(BufferedImage img){
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage newImg = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = newImg.createGraphics();

        g2d.drawImage(img, w, 0, w * -1, h,null);
        g2d.dispose();
        return newImg;
    }

    //flip animation array horizontally
    public static BufferedImage[] getFlipped(BufferedImage[] imgs){
        BufferedImage[] flippedImgs = new BufferedImage[imgs.length];
        for(int i = 0; i < flippedImgs.length; i++){
            flippedImgs[i] = getFlipped(imgs[i]);
        }

        return flippedImgs;
    }
}
