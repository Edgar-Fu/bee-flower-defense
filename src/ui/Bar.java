package src.ui;

import java.awt.Color;
import java.awt.Graphics;

import src.scenes.Playing;

public class Bar {

    protected int x, y, width, height;

    public Bar(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    protected void drawButtonFeedback(Graphics g, MyButton b){
        if(b.isMouseOver())
            g.setColor(Color.white);
        else
            g.setColor(Color.black);
        
        
        g.drawRoundRect(b.x, b.y, b.width, b.height, 10, 10);

        if(b.isMousePressed()){
            g.drawRoundRect(b.x + 1, b.y + 1, b.width - 2, b.height - 2, 10, 10);
            g.drawRoundRect(b.x + 2, b.y + 2, b.width - 4, b.height - 4, 10, 10);
        }
    }

}
