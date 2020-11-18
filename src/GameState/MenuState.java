package GameState;

import Handlers.Keys;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private TileMap.Background bg;
    private int currentChoice = 0;
    private String[] options = {
            "Start",
            "Quit"
    };
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState(GameStateManager gsm) {

        this.gsm = gsm;
        try {
            bg = new Background("/Backgrounds/menubg.gif", 0.5);
            bg.setVector(-1.0, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

    }

    public void update() {
        //handle keys
        handleInput();

        bg.update();
    }

    public void draw(Graphics2D g) {
        //draw background
        bg.draw(g);

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Dragon Game", 80, 70);

        ///draw menu options
        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 145, 140 + i * 15);
        }
    }

    private void select() {
        if (currentChoice == 0) {
            gsm.setState(GameStateManager.LEVEL1STATE);
            //start
        }
        if (currentChoice == 1) {
            System.exit(0);
        }
    }

    public void handleInput() {
        if (Keys.isPressed(Keys.ENTER)) select();
        if (Keys.isPressed(Keys.JUMP)) {
            if (currentChoice > 0) {
                currentChoice--;
            }
        }
        if (Keys.isPressed(Keys.DOWN)) {
            if (currentChoice < options.length - 1) {
                currentChoice++;
            }
        }
    }
}
