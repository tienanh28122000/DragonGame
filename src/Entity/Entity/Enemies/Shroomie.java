package Entity.Entity.Enemies;

import Entity.Animation;
import Entity.Enemy;
import Entity.MapObject;
import Handlers.Content;
import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shroomie extends Enemy {
    private BufferedImage[] sprites;
    private int jumpHeight;
    private int maxJumpHeight;

    public Shroomie(TileMap tm) {
        super(tm);

        moveSpeed = 1.2;
        maxSpeed = 1.2;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.5; //-4.8
        stopJumpSpeed = 1.0;
        maxJumpHeight = (int)y + 200;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 5;
        damage = 3;

        //load sprites
        sprites = Content.Shroomie[0];
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        up = true;
    }

    private void getNextPosition() {
        //movement

        //jumping
        if (up) {
            down = false;
            dy = jumpStart;
            jumpHeight = gety();
        }

        //falling
        if (jumpHeight == maxJumpHeight) {
            up = false;
            down = true;
            dy += fallSpeed;

            if (dy > 0) up = false;
            if (dy < 0 && up) dy += stopJumpSpeed;

            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

    public void update() {
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //shroomie movement
        if(dy == 0) {
            up = !up;
            down = !down;
        }

        //System.out.println(jumpHeight);

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
