package src.scenes;

import src.main.Game;

public class GameScene {
    private Game game;

    public GameScene(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }

}
