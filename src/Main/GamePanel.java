package Main;

import GameState.GameStateManager;
import Handlers.Keys;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    //dimensions
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;

    //game thread
    private Thread thread;
    private boolean running;
    private final int TPS = 60;
    private final long OPTIMAL_TIME = 1000000000 / TPS;

    //image
    private BufferedImage image;
    private Graphics2D g;

    //game state manager
    private GameStateManager gsm;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }

    }

    private void init() {

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        g = (Graphics2D) image.getGraphics();

        running = true;

        gsm = new GameStateManager();
    }

    public void run() {
        init();
        long lastLoop = System.nanoTime();

        //game loop
        while (running) {

            update();
            draw();
            drawToScreen();

            long current = System.nanoTime();
            long updateTime = current - lastLoop;
            lastLoop = current;

            try {
                Thread.sleep((lastLoop - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void update() {
        gsm.update();
    }

    private void draw() {
        gsm.draw(g);
    }

    private void drawToScreen() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        Keys.keySet(key.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent key) {
        Keys.keySet(key.getKeyCode(), false);
    }
}
