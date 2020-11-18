package Entity.Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Slugger extends Enemy{

    private BufferedImage[] sprites;

    public Slugger(TileMap tm) {
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 10;
        damage = 1;

        //load sprites
        sprites = Content.Slugger[0];
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    private void  getNextPosition() {
        //movement
        if(left) dx = -moveSpeed;
        else if(right) dx = moveSpeed;
        else dx = 0;
        if(falling) {
            dy += fallSpeed;
            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

    public void update() {

        //update position
        getNextPosition();
        checkTileMapCollision();

        //slugger turns around if it hits a wall or is about to fall off a cliff
        calculateCorners(x, ydest + 1);
        if(!bottomLeft) {
            left = false;
            right = facingRight = true;
        }
        if(!bottomRight) {
            left = true;
            right = facingRight = false;
        }

        setPosition(xtemp, ytemp);

        if(dx == 0) {
            left = !left;
            right = !right;
            facingRight = !facingRight;
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
        //if (notOnScreen()) return;

        setMapPosition();

        super.draw(g);
    }
}
