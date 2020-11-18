package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class Teleport extends MapObject {
    private BufferedImage[] sprites;

    public Teleport(TileMap tm) {
        super(tm);
        facingRight = true;
        width = height = 40;
        cwidth = 20;
        cheight = 40;

        //load sprites
        sprites = Content.Portal[0];
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(1);
    }

    public void update() {
        setPosition(x, y);

        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();

        super.draw(g);
    }
}
