package src.scenes;

import static src.main.GameStates.MENU;
import static src.main.GameStates.SetGameState;

import java.awt.Color;
import java.awt.Graphics;

import src.main.Game;
import src.ui.MyButton;

public class Settings extends GameScene implements SceneMethods{

    private MyButton bMenu;

    public Settings(Game game){
        super(game);
        initButtons();
    }

    private void initButtons(){
        bMenu = new MyButton("Menu", 2, 2, 100, 30);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(0, 0, 640, 640);

        drawButtons(g);
    }

    private void drawButtons(Graphics g){
        bMenu.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
			SetGameState(MENU);
    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
		if (bMenu.getBounds().contains(x, y))
			bMenu.setMouseOver(true);
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
			bMenu.setMousePressed(true);
    }

    @Override
    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
    }

    @Override
    public void mouseDragged(int x, int y) {
    }
}
