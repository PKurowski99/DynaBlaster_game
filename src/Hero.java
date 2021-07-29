import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Reprezentuje bohaterów.
 */
public enum Hero{
    hero(InputData.heroHp,InputData.heroSpeed,InputData.heroFireRate,InputData.heroImagePath,InputData.heroDescription),
    heroine(InputData.heroineHp,InputData.heroineSpeed,InputData.heroineFireRate,InputData.heroineImagePath,InputData.heroineDescription);
    /**
     * zdrowie bohatera
     */
    private int hp;
    /**
     * prędkość bohatera
     */
    private int speed;
    /**
     * szybkość z jaką bohater może stawiać bomby
     */
    private int fireRate;
    /**
     * ścieżka do zdjęcia przedstawiającego bohatera
     */
    private String imagePath;
    /**
     * opis bohatera
     */
    private String heroDescription;

    Hero(int hp, int speed, int fireRate, String imagePath, String hd) {
        this.hp = hp;
        this.speed = speed;
        this.fireRate = fireRate;
        this.imagePath = imagePath;
        this.heroDescription = hd;
    }

    /**
     * Metoda zwracająca punkty życia bohatera.
     * @return punktu życia bohatera
     */
    public int getHp(){return this.hp;}

    /**
     * Metoda zwracająca prędkość bohatera.
     * @return prędkość bohatera
     */
    public int getSpeed(){return this.speed;}

    /**
     * Metoda zwracająca prędkość stawiania bomb bohatera.
     * @return prędkość stawiania bomb bohatera
     */
    public int getFireRate(){return this.fireRate;}

    /**
     * Metoda zwracająca ścieżkę do zdjęcia bohatera.
     * @return ścieżka do zdjęcia bohatera.
     */
    public String getImagePath(){return this.imagePath;}

    /**
     * Metoda zwracająca opis bohatera.
     * @return opis bohatera
     */
    public String getHeroDescription(){return  this.heroDescription;}

    /**
     * Metoda zwracająca przeskalowane zdjęcie bohatera.
     * @param width docelowa szerokość zdjęcia w pikselach
     * @param height docelowa wysokość zdjęcia w pikselach
     * @return zdjęcie przeskalowane do podanych rozmiarów.
     */
    public Image getResizedImage(int width, int height){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(this.imagePath));
        } catch (IOException ignored) {
        }
        return img.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING);
    }
    public static void reload(){
        Hero.hero.hp = InputData.heroHp;
        Hero.hero.speed = InputData.heroSpeed;
        Hero.hero.fireRate = InputData.heroFireRate;
        Hero.hero.heroDescription = InputData.heroDescription;
        Hero.hero.imagePath = InputData.heroImagePath;

        Hero.heroine.hp = InputData.heroineHp;
        Hero.heroine.speed = InputData.heroineSpeed;
        Hero.heroine.fireRate = InputData.heroineFireRate;
        Hero.heroine.heroDescription = InputData.heroineDescription;
        Hero.heroine.imagePath = InputData.heroineImagePath;
    }

}
