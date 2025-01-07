package src.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static src.main.GameStates.*;

import src.main.Game;
import src.main.GameStates;

public class MyKeyboardListener implements KeyListener{
    private Game game;
    public MyKeyboardListener(Game game){
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(GameStates.gameState == PLAYING)
            game.getPlaying().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}
