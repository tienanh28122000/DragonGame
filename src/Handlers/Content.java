package Handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Content {
    public static BufferedImage[][] Slugger = load("/Sprites/Enemies/slugger.gif", 30, 30);
    public static BufferedImage[][] SluggerOnFire = load("/Sprites/Enemies/sluggeronfire.gif", 30, 30);
    public static BufferedImage[][] Arachnik = load("/Sprites/Enemies/arachnik.gif", 30, 30);
    public static BufferedImage[][] Explosion = load("/Sprites/Enemies/explosion.gif", 30, 30);
    public static BufferedImage[][] ArachnikOnFire = load("/Sprites/Enemies/arachnikonfire.png", 30, 30);
    public static BufferedImage[][] Shroomie = load("/Sprites/Enemies/shroomie.png", 30, 30);
    public static BufferedImage[][] ShroomieOnFire = load("/Sprites/Enemies/shroomiemelting.png", 30, 30);
    public static BufferedImage[][] HatMonkey = load("/Sprites/Enemies/hatmonkeymoving.png", 30, 30);
    public static BufferedImage[][] Coin = load("/Sprites/coin.png", 30, 30);
    public static BufferedImage[][] Portal = load("/Sprites/portal.gif", 40, 40);


    public static BufferedImage[][] load(String s, int w, int h) {
        BufferedImage[][] ret;
        try {
            BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
            int width = spritesheet.getWidth() / w;
            int height = spritesheet.getHeight() / h;
            ret = new BufferedImage[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
                }
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading graphics.");
            System.exit(0);
        }
        return null;
    }
}
