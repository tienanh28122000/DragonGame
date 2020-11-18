package Entity.Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Arachnik extends Enemy {
    private BufferedImage[] sprites;

    public Arachnik(TileMap tm) {
        super(tm);

        moveSpeed = 1.2;
        maxSpeed = 1.2;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 5;
        damage = 2;

        //load sprites
        sprites = Content.Arachnik[0];
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        up = true;
    }

    private void  getNextPosition() {
        //movement
        if(up) dy = -moveSpeed;
        else if(down) dy = moveSpeed;
        else dy = 0;
    }

    public void update() {
        //update position
        getNextPosition();
        checkTileMapCollision();

        //arcahanik movement
        calculateCorners(x, ydest + 1);
        if(!topLeft && topRight) {
            up = false;
        }
        if(!bottomLeft && bottomRight) {
            up = true;
        }

        setPosition(xtemp, ytemp);

        if(dy == 0) {
            up = !up;
            down = !down;
        }

        //check flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 400) {
                flinching = false;
            }
        }

        //update animation
        animation.update();
    }

    public void draw(Graphics2D g) {

        setMapPosition();

        super.draw(g);
    }
}
