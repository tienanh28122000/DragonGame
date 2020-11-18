package Entity;

import GameState.Level1State;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;
    private Level1State level1State;
    private BufferedImage image;
    private BufferedImage image2;
    private Font font;

    public HUD(Player p) {
        player = p;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/hudleft.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/HUD/hudright1.png"));
            font = new Font("Arial", Font.PLAIN, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, 0, 10, null);
        //g.drawImage(image2, 566, 10, null);
        g.drawImage(image2, 246, 10, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
        g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 45);
        g.drawString(String.valueOf(player.getCoinAmount()), 260, 25);
        g.drawString(String.valueOf(player.getScore()), 260, 45);
    }
}
