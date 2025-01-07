package src.main;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import src.inputs.MyKeyboardListener;
import src.inputs.MyMouseListener;

public class GameScreen extends JPanel{

    private Game game;
    
    private Dimension size;
    private MyMouseListener myMouseListener;
    private MyKeyboardListener myKeyboardListener;

    public GameScreen(Game game){
        this.game = game;

        setPanelSize();
    }

    public void initInputs(){
        myMouseListener = new MyMouseListener(game);
        myKeyboardListener = new MyKeyboardListener(game);

        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
        addKeyListener(myKeyboardListener);

        requestFocus();
    }

    private void setPanelSize(){
        size = new Dimension(640, 740);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //g.drawImage(hornetSprites.get(4), 0, 0, null);

        game.getRender().render(g);
    }

    
}