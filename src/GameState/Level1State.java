package GameState;

import java.awt.*;
import java.util.ArrayList;

import Entity.Collectable.Collectable;
import Entity.Enemy;
import Entity.Entity.Enemies.*;
import Entity.HUD;
import Entity.Player;
import Entity.Teleport;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.*;

public class Level1State extends GameState {

    private TileMap tileMap;
    private Background bg;

    private Player player;
    private ArrayList<Enemy> enemySluggers;
    private ArrayList<Enemy> enemyArachniks;
    private ArrayList<Enemy> enemyHatMonkeys;
    private ArrayList<Enemy> enemyShroomies;
    private ArrayList<BurningSlugger> burningS;
    private ArrayList<BurningArachnik> burningA;
    private ArrayList<BurningShroomie> burningSh;
    private ArrayList<BurningHatMonkey> burningH;
    private ArrayList<Explosion> explosions;
    private ArrayList<Collectable> coins;
    public static int endScore;


    private HUD hud;
    private Teleport teleport;

    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/TileSets/grasstileset1.gif");
        tileMap.loadMap("/Maps/map1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/grassbg1.gif", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);

        populateEnemies();
        populateCollectables();

        burningS = new ArrayList<BurningSlugger>();
        burningA = new ArrayList<BurningArachnik>();
        burningSh = new ArrayList<BurningShroomie>();
        burningH = new ArrayList<BurningHatMonkey>();
        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        // teleport
        teleport = new Teleport(tileMap);
        teleport.setPosition(3140, 190);
    }

    private void populateCollectables() {
        coins = new ArrayList<Collectable>();

        Collectable c;

        Point[] coinPoints = new Point[]{
                new Point(200, 200),
                new Point(375, 140),
                new Point(560, 55),
                new Point(1020, 170),
                new Point(1440, 100),
                new Point(1575, 170),
                new Point(1725, 170),
                new Point(1840, 170),
                new Point(2500, 130),
        };

        for (int i = 0; i < coinPoints.length; i++) {
            c = new Collectable(tileMap);
            c.setPosition(coinPoints[i].x, coinPoints[i].y);
            coins.add(c);
        }

    }

    private void populateEnemies() {
        enemySluggers = new ArrayList<Enemy>();

        //populate level with sluggers
        Slugger s;
        Point[] points = new Point[]{
                new Point(860, 200),
                new Point(1520, 200),
                new Point(1800, 200),
                new Point(2200, 200),
                new Point(2700, 200),
                new Point(2900, 200),
        };
        for (int i = 0; i < points.length; i++) {
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemySluggers.add(s);
        }

        //populate level with arachniks
        enemyArachniks = new ArrayList<Enemy>();

        Arachnik a;
        Point[] pointsA = new Point[]{
                new Point(860, 150),
                new Point(1020, 200),
                new Point(1420, 70),
                new Point(1920, 30),
        };
        for (int i = 0; i < pointsA.length; i++) {
            a = new Arachnik(tileMap);
            a.setPosition(pointsA[i].x, pointsA[i].y);
            enemyArachniks.add(a);
        }

        //populate level with shroomies
        enemyShroomies = new ArrayList<Enemy>();

        Shroomie sh;
        Point[] pointsC = new Point[]{
                new Point(150, 200),
                new Point(820, 200),
                new Point(1650, 200),
                new Point(2230, 200),
                new Point(2830, 200),
        };

        for (int i = 0; i < pointsC.length; i++) {
            sh = new Shroomie(tileMap);
            sh.setPosition(pointsC[i].x, pointsC[i].y);
            enemyShroomies.add(sh);
        }

        //populate level with hatmonkeys
        enemyHatMonkeys = new ArrayList<Enemy>();

        HatMonkey h;
        Point[] pointsB = new Point[]{
                new Point(200, 200),
                new Point(760, 150),
                new Point(1000, 50),
                new Point(2400, 200),
                new Point(2400, 110),
        };

        for (int i = 0; i < pointsB.length; i++) {
            h = new HatMonkey(tileMap);
            h.setPosition(pointsB[i].x, pointsB[i].y);
            enemyHatMonkeys.add(h);
        }
    }

    public void update() {
        //check keys
        handleInput();

        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());


        //set background
        bg.setPosition(tileMap.getx(), tileMap.gety());

        //attack enemies
        player.checkAttack(enemySluggers);
        player.checkAttack(enemyArachniks);
        player.checkAttack(enemyHatMonkeys);
        player.checkAttack(enemyShroomies);
        player.checkCoins(coins);

        //update all enemies
        for (int i = 0; i < enemySluggers.size(); i++) {
            Enemy es = enemySluggers.get(i);
            es.update();
            if (enemySluggers.get(i).isDead()) {
                enemySluggers.remove(i);
                i--;
                if (player.fireKill) {
                    burningS.add(new BurningSlugger(es.getx(), es.gety()));
                    player.fireKill = false;
                } else if (player.scratchKill) {
                    explosions.add(new Explosion(es.getx(), es.gety()));
                    player.scratchKill = false;
                }
            }
        }
        for (int j = 0; j < enemyArachniks.size(); j++) {
            Enemy ea = enemyArachniks.get(j);
            ea.update();
            if (enemyArachniks.get(j).isDead()) {
                enemyArachniks.remove(j);
                j--;
                if (player.fireKill) {
                    burningA.add(new BurningArachnik(ea.getx(), ea.gety()));
                    player.fireKill = false;
                } else if (player.scratchKill) {
                    explosions.add(new Explosion(ea.getx(), ea.gety()));
                    player.scratchKill = false;
                }
            }
        }
        for (int j = 0; j < enemyShroomies.size(); j++) {
            Enemy ea = enemyShroomies.get(j);
            ea.update();
            if (enemyShroomies.get(j).isDead()) {
                enemyShroomies.remove(j);
                j--;
                if (player.fireKill) {
                    burningSh.add(new BurningShroomie(ea.getx(), ea.gety()));
                    player.fireKill = false;
                } else if (player.scratchKill) {
                    explosions.add(new Explosion(ea.getx(), ea.gety()));
                    player.scratchKill = false;
                }
            }
        }
        for (int j = 0; j < enemyHatMonkeys.size(); j++) {
            Enemy ea = enemyHatMonkeys.get(j);
            ea.update();
            if (enemyHatMonkeys.get(j).isDead()) {
                enemyHatMonkeys.remove(j);
                j--;
                if (player.fireKill) {
                    burningH.add(new BurningHatMonkey(ea.getx(), ea.gety()));
                    player.fireKill = false;
                } else if (player.scratchKill) {
                    explosions.add(new Explosion(ea.getx(), ea.gety()));
                    player.scratchKill = false;
                }
            }
        }


        //update explosions
        for (int i = 0; i < burningS.size(); i++) {
            burningS.get(i).update();
            if (burningS.get(i).shouldRemove()) {
                burningS.remove(i);
                i--;
            }
        }
        for (int i = 0; i < burningA.size(); i++) {
            burningA.get(i).update();
            if (burningA.get(i).shouldRemove()) {
                burningA.remove(i);
                i--;
            }
        }
        for (int i = 0; i < burningSh.size(); i++) {
            burningSh.get(i).update();
            if (burningSh.get(i).shouldRemove()) {
                burningSh.remove(i);
                i--;
            }
        }
        for (int i = 0; i < burningH.size(); i++) {
            burningH.get(i).update();
            if (burningH.get(i).shouldRemove()) {
                burningH.remove(i);
                i--;
            }
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }

        //update coins
        for (int j = 0; j < coins.size(); j++) {
            Collectable c = coins.get(j);
            c.update();
            if (coins.get(j).intersects(player)) {
                coins.remove(j);
                j--;
            }
        }

        //if player is dead or enters portal
        if (player.getHealth() <= 0 || player.notOnScreen()) {
            endScore = player.getScore();
            player.isDead();
            gsm.setState(GameStateManager.GAMEOVERSTATE);
        }

        if (player.intersects(teleport)) {
            endScore = player.getScore();
            player.isDead();
            gsm.setState(GameStateManager.GAMEWINSTATE);
        }

        //update teleporting
        teleport.update();

    }

    public void draw(Graphics2D g) {

        //draw background
        bg.draw(g);

        //draw tilemap
        tileMap.draw(g);

        //draw player
        player.draw(g);


        //draw enemies
        for (int i = 0; i < enemySluggers.size(); i++) {
            enemySluggers.get(i).draw(g);
        }
        for (int i = 0; i < enemyArachniks.size(); i++) {
            enemyArachniks.get(i).draw(g);
        }
        for (int i = 0; i < enemyShroomies.size(); i++) {
            enemyShroomies.get(i).draw(g);
        }
        for (int i = 0; i < enemyHatMonkeys.size(); i++) {
            enemyHatMonkeys.get(i).draw(g);
        }

        //draw explosions
        for (int i = 0; i < burningS.size(); i++) {
            burningS.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            burningS.get(i).draw(g);
        }
        for (int i = 0; i < burningA.size(); i++) {
            burningA.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            burningA.get(i).draw(g);
        }
        for (int i = 0; i < burningSh.size(); i++) {
            burningSh.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            burningSh.get(i).draw(g);
        }
        for (int i = 0; i < burningH.size(); i++) {
            burningH.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            burningH.get(i).draw(g);
        }
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
            explosions.get(i).draw(g);
        }

        //draw coins
        for (int i = 0; i < coins.size(); i++) {
            coins.get(i).draw(g);
        }

        // draw teleport
        teleport.draw(g);

        //draw HUD
        hud.draw(g);
    }

    public void handleInput() {
        player.setUp(Keys.keyState[Keys.UP]);
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        player.setGliding(Keys.keyState[Keys.GLIDE]);
        player.setJumping(Keys.keyState[Keys.JUMP]);
        if (Keys.isPressed(Keys.SCRATCH)) player.setScratching();
        if (Keys.isPressed(Keys.FIREBALL)) player.setFiring();
    }
}
