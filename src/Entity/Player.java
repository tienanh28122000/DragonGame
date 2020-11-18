package Entity;

import Entity.Collectable.Collectable;
import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends MapObject {

    //player stuff
    private int health;
    private int maxHealth;
    private int coinAmount;
    private int score;
    private int fire;
    private int maxFire;
    private boolean teleporting;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    //fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;
    public boolean fireKill;  //if killed enemy with fireball

    //scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;
    public boolean scratchKill; //if killed enemy by scratching

    //gliding
    private boolean gliding;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {2, 8, 1, 2, 4, 2, 5};

    //animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;

    public Player(TileMap tm) {
        super(tm);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6; 
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        coinAmount = 0;

        facingRight = true;

        health = maxHealth = 10;
        fire = maxFire = 1000;

        fireCost = 250;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 3;
        scratchRange = 50;

        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.gif"));
            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < 7; i++) {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    if (i != 6) {
                        bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                    } else {
                        bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
                    }
                }
                sprites.add(bi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(100);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public int getCoinAmount() {
        return coinAmount;
    }

    public boolean getTeleporting() {
        return teleporting;
    }

    public void setTeleporting() {
        teleporting = true;
    }

    public void setFiring() {
        firing = true;
    }

    public void setScratching() {
        scratching = true;
    }

    public void setGliding(boolean b) {
        gliding = b;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {

        for (int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);
            // scratch attack
            if (scratching) {
                if (facingRight) {
                    if (e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(scratchDamage);
                    }
                } else {
                    if (e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2) {
                        e.hit(scratchDamage);
                    }
                }
                if (e.isDead()) {
                    scratchKill = true;
                    score += 300;
                }
            }
            //fireballs
            for (int j = 0; j < fireBalls.size(); j++) {
                if (fireBalls.get(j).intersects(e)) {
                    e.hit(fireBallDamage);
                    if (e.isDead()) {
                        fireKill = true;
                        score += 200;
                    }
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            //check for enemy collision
            if (intersects(e)) {
                hit(e.getDamage());
            }
        }
    }

    public void checkCoins(ArrayList<Collectable> coins) {
        for (int j = 0; j < coins.size(); j++) {
            if (intersects(coins.get(j))) {
                coinAmount++;
                score += 100;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void hit(int damage) {
        if (flinching) return;
        health -= damage;
        score -= 100;
        if (score < 0) score = 0;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public boolean isDead() {
        return dead;
    }

    private void getNextPosition() {
        //movement
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        } else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) {
                dx = maxSpeed;
            }
        } else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) {
                    dx = 0;
                }
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        //cannot move while attacking, except in air
        if ((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
            dx = 0;
        }

        //jumping
        if (jumping && !falling) {
            dy = jumpStart;
            falling = true;
        }

        //if(!falling) return;

        //falling
        if (falling) {
            if (dy > 0 && gliding) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;

            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

    public void update() {

        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //check if attack has stopped
        if (currentAction == SCRATCHING) {
            if (animation.hasPlayedOnce()) scratching = false;
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        //fireball attack
        fire += 1;
        if (fire > maxFire) fire = maxFire;
        if (firing && currentAction != FIREBALL) {
            if (fire > fireCost) {
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        if (firing) scratching = false;

        //update fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }

        //check done flinching
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) flinching = false;
        }

        // set animation
        if (scratching) {
            if (currentAction != SCRATCHING) {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
            }
        } else if (firing) {
            if (currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }

        } else if (dy > 0) {
            if (gliding) {
                if (currentAction != GLIDING) {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            } else if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        } else if (dy < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(100);
                width = 30;
            }
        }

        animation.update();

        //set direction
        if (currentAction != SCRATCHING && currentAction != FIREBALL) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
    }

    public void draw(Graphics2D g) {
        setMapPosition();

        //draw fireballs
        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw(g);
        }

        //draw player
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }

        }

        super.draw(g);
    }
}