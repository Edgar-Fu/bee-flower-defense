package src.main;

import javax.swing.JFrame;

import src.manager.TileManager;
import src.scenes.Menu;
import src.scenes.Playing;
import src.scenes.Settings;

public class Game extends JFrame implements Runnable{

    private GameScreen gameScreen;
    private Thread gameThread;
    private TileManager tileManager;
    private Render render;
    private Menu menu;
    private Playing playing;
    private Settings settings;

    public Game(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initClasses();
        add(gameScreen);
        pack();
        setVisible(true);
    }

    private void initClasses(){
        tileManager = new TileManager();
        render = new Render(this);
        gameScreen = new GameScreen(this);
        menu = new Menu(this);
        playing = new Playing(this);
        settings = new Settings(this);
    }

    private void start(){
        gameThread = new Thread(this){};
        gameThread.start();
    }

    private void updateGame(){
        switch (GameStates.gameState){
            case MENU:
                break;
            case PLAYING:
                playing.update();
                break;
            case SETTINGS:
                break;
            default:
                break;
        }
    }

    public static void main(String[] args){
        Game game = new Game();
        game.gameScreen.initInputs();
        game.start();
    }

    @Override
    public void run(){
        double timePerFrame = 1000000000.0 / 120.0;
        double timePerUpdate = 1000000000.0 / 60.0;

        long lastFrame = System.nanoTime();
        long lastUpdate = System.nanoTime();
        long lastTimeCheck = System.currentTimeMillis();
        long now;

        int frames = 0;
        int updates = 0;

        while(true){
            now = System.nanoTime();
            //render
            if(now - lastFrame >= timePerFrame){
                repaint();
                lastFrame = now;
                frames++;
            }
            //update
            if(now - lastUpdate >= timePerUpdate){
                updateGame();
                lastUpdate = now;
                updates++;
            }
            if(System.currentTimeMillis() - lastTimeCheck >= 1000){
                System.out.println("FPS: " + frames + ", UPS: " + updates);
                frames = 0;
                updates = 0;
                lastTimeCheck = System.currentTimeMillis();
            }
        }
        

    }

    //GETTERS / SETTERS
    public Render getRender(){
        return render;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Settings getSettings() {
        return settings;
    }
    
    public TileManager getTileManager(){
        return tileManager;
    }
}

