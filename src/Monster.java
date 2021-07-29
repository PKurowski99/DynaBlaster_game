import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa reprezentująca potwora.
 */
public class Monster {
    /**
     * ID potwora
     */
    private int id;
    /**
     * pozycja startowa x potwora
     */
    private int xMapPos;
    /**
     * pozycja startowa y potwora
     */
    private int yMapPos;
    /**
     * pozycja x potwora w grze
     */
    private int xPos;
    /**
     * pozycja y potwora w grze
     */
    private int yPos;
    /**
     * prędkość poruszania się potwora
     */
    private static float velocity=InputData.monsterSpeed;
    /**
     * zmienna opisująca kierunek poruszania się potwora
     */
    private int direction = 1;
    /**
     * inforamcja czy potwór żyje
     */
    private boolean alive = true;
    /**
     * ścieżka do zdjęcia potwora
     */
    private String imagePath;

    public Monster( int height, int width, int id){
        this.id = id;
        xMapPos = width;
        yMapPos = height;
        imagePath = InputData.monsterImagePath;
    }
    /**
     * Metoda zwracająca przeskalowane zdjęcie potwora.
     * @param width docelowa szerokość zdjęcia w pikselach
     * @param height docelowa wysokość zdjęcia w pikselach
     * @return zdjęcie przeskalowane do podanych rozmiarów
     */
    public Image getResizedImage(int width, int height){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(this.imagePath));
        } catch (IOException ignored) {
        }
        assert img != null;
        return img.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING);
    }

    /**
     * Metoda zwraca współrzędną x położenia potwora w grze.
     * @return współrzędna x położenia potwora
     */
    public int getXPos(){return xPos;}
    /**
     * Metoda zwraca współrzędną y położenia potwora w grze.
     * @return współrzędna y położenia potwora
     */
    public int getYPos(){return yPos;}
    /**
     * Metoda zwraca startową współrzędną x położenia potwora na mapie.
     * @return startowa współrzędna x położenia potwora na mapie
     */
    public int getXMapPos(){return xMapPos;}
    /**
     * Metoda zwraca startową współrzędną y położenia potwora na mapie.
     * @return startowa współrzędna y położenia potwora na mapie
     */
    public int getYMapPos(){return yMapPos;}
    /**
     * Metoda zwraca informację czy potwór żyje.
     * @return czy potwór żyje
     */
    public boolean isAlive(){return alive;}

    /**
     * Metoda odpowiada za zabicie potwora.
     */
    public void killMonster(){
        alive = false;
        imagePath = InputData.voidImagePath;
    }

    /**
     * Metoda odpowiada za zmianę pozycji potwora w grze.
     * @param xPosition nowa współrzędna x położenia potwora
     * @param yPosition nowa współrzędna y położenia potwora
     */
    public void changePosition(int xPosition, int yPosition){
        xPos = xPosition;
        yPos = yPosition;
    }
    /**
     * Metoda odpowiada za zmianę współrzędnej y potwora w grze.
     * @param yPosition nowa współrzędna y położenia potwora
     */
    public void changeYPos(int yPosition){yPos = yPosition;}

    /**
     * Funkcja zwraca prędkość potwora.
     * @return prędkość potwora
     */
    public static float getVelocity(){return velocity;}

    /**
     * Funkcja odpowiada za zmianę prędkości potwora.
     * @param vel nowa prędkość potwora
     */
    public static void changeVelocity(float vel){
        velocity=vel;
    }

    /**
     * Metoda odpowiada za zmianę kierunku poruszania się potwora.
     */
    public void changeDirection(){direction=-direction;}

    /**
     * Metoda zwraca kierunek poruszania się potwora.
     * @return kierunek poruszania się potwora
     */
    public int getDirection(){return direction;}
}
