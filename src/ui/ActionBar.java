package src.ui;

import static src.helperMethods.Constants.Towers.FIRE;
import static src.helperMethods.Constants.Towers.FROST;
import static src.helperMethods.Constants.Towers.QUEEN;
import static src.helperMethods.Constants.Towers.WORKER;
import static src.helperMethods.Constants.Towers.getTowerName;
import static src.main.GameStates.MENU;
import static src.main.GameStates.SetGameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.objects.Tower;
import src.scenes.Playing;

public class ActionBar extends Bar{
    private MyButton bMenu;
    private Playing playing;
    private MyButton[] towerButtons;
    private ArrayList<BufferedImage> icons;
    private Tower selectedTower;
    private Tower displayedTower;

    public ActionBar(int x, int y, int width, int height, Playing playing){
        super(x, y, width, height);
        this.playing = playing;
        initButtons();
    }

    

    private void initButtons(){
        bMenu = new MyButton("Menu", 2, 642, 100, 30);
        towerButtons = new MyButton[4];

        int w = 50;
        int h = 50;
        int xStart = 110;
        int yStart = 650;
        int xOffset = (int) (w * 1.1f);
        
        for(int i = 0; i < towerButtons.length; i++){
            towerButtons[i] = new MyButton("", xStart + xOffset * i, yStart, w, h, i);
        }
    }

    private void drawButtons(Graphics g){
        bMenu.draw(g);
        icons = playing.getTowerManager().getIcons();
        for(MyButton b : towerButtons){
            g.setColor(Color.gray);
            g.fillRoundRect(b.x, b.y, b.width, b.height, 10, 10);
            g.drawImage(icons.get(b.getId()), b.x, b.y, b.width, b.height, null);
            drawButtonFeedback(g, b);
        }
    }

    public void draw(Graphics g){
        g.setColor(new Color(231, 154, 63));
        g.fillRect(x, y, width, height);

        drawButtons(g);
        drawDisplayedTower(g);
    }

    private void drawDisplayedTower(Graphics g){
        if(displayedTower != null){
            switch (displayedTower.getTowerType()) {
                case WORKER:
                    g.setColor(Color.yellow);
                    break;
                case FIRE:
                    g.setColor(Color.orange);
                    break;
                case FROST:
                    g.setColor(Color.cyan);
                    break;
                case QUEEN:
                    g.setColor(Color.pink);
                    break;    
                default:
                g.setColor(Color.gray);
                    break;
            }
            g.fillRoundRect(340, 640, 220, 100, 10, 10);
            g.setColor(Color.black);
            g.drawRoundRect(340, 640, 220, 100, 10, 10);
            g.drawRoundRect(350, 650, 50, 50, 10, 10);
            g.drawImage(icons.get(displayedTower.getTowerType()), 350, 650, 50, 50, null);
            g.setFont(new Font("LucidaSans", Font.BOLD, 15));
            g.drawString("" + getTowerName(displayedTower.getTowerType()), 410, 660);
            g.drawString("ID: " + displayedTower.getId(), 410, 675);

            //g.
            drawDisplayedTowerBorder(g);
            drawDisplayedTowerRange(g);
        }
    }

    private void drawDisplayedTowerRange(Graphics g){
        int range = (int) displayedTower.getRange() * 2;
        int offset = range / 2 - 16;
        g.setColor(Color.black);
        g.drawOval(displayedTower.getX() - offset, displayedTower.getY() - offset, range, range);
    }

    private void drawDisplayedTowerBorder(Graphics g){
        g.setColor(Color.black);
        g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);
    }

    public void displayTower(Tower t){
        displayedTower = t;
    }

    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
			SetGameState(MENU);
        else{
            for(MyButton b : towerButtons){
                if(b.getBounds().contains(x, y)){
                    selectedTower = new Tower(0, 0, -1, b.getId());
                    playing.setSelectedTower(selectedTower);
                    return;
                }
            }
        }
    }

    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        for(MyButton b : towerButtons)
            b.setMouseOver(false);

		if (bMenu.getBounds().contains(x, y))
			bMenu.setMouseOver(true);
        else{
            for(MyButton b : towerButtons){
                if(b.getBounds().contains(x, y)){
                    b.setMouseOver(true);
                    return;
                }
            }
        }
        if(y > 650)
            playing.setSelectedTower(null);
    }
  
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
			bMenu.setMousePressed(true);
        else{
            for(MyButton b : towerButtons){
                if(b.getBounds().contains(x, y)){
                    b.setMousePressed(true);
                    return;
                }
            }
        }
    }

    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        for(MyButton b : towerButtons)
            b.resetBooleans();
    }
}
