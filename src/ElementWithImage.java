import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa pozwala na tworzenie obiektów zawierających obraz.
 */
public class ElementWithImage {
    /**
     * ścieżka do obrazu
     */
    protected String imagePath;

    /**
     * Metoda zwraca przeskalowany obraz do odpowiedniego rozmiaru.
     * @param width docelowa szerokość obrazu w pixelach
     * @param height docelowa wysokość obrazu w pixelach
     * @return przeskalowany obraz
     */
    protected Image getResizedImage(int width, int height){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(this.imagePath));
        } catch (IOException ignored) {
        }
        return img.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING);
    }
}
