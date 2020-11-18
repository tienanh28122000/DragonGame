package GameState;

import Entity.Player;
import Handlers.Keys;
import TileMap.Background;

import java.awt.*;

public class GameOverState extends GameState {
    private TileMap.Background bg;
    private Player player;
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public GameOverState(GameStateManager gsm) {

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
        g.drawString("Game Over", 80, 70);

        //draw return text
        g.setFont(font);
        g.drawString("Your score: " + Level1State.endScore, 65, 160);
        g.drawString("Press SPACEBAR to return to menu", 65, 180);
    }

    public void handleInput() {
        if (Keys.isPressed(Keys.UP)) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }
}
