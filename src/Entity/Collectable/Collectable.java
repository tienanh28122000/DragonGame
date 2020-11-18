package Entity.Collectable;

import Entity.Animation;
import Entity.MapObject;
import GameState.Level1State;
import Handlers.Content;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Collectable extends MapObject {
    private BufferedImage[] sprites;
    private int amount;

    public Collectable(TileMap tm) {
        super(tm);

        width = 15;
        height = 15;
        cwidth = 15;
        cheight = 15;

        //load sprites
        sprites = Content.Coin[0];
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);
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
