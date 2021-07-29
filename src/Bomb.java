import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
/**
 * Klasa ta pozwala na utworzenie bomby, która wybuchnie po określonym czasie.
 */
public class Bomb extends Block {
    /**
     * czas do wybuchu bomby
     */
    private int timeToExplode = InputData.bombTimeToExplode;
    /**
     * zmienna odpowiadająca za stan timera - aktywne odliczanie lub pauza
     */
    private static boolean paused = false;
    public Bomb( int x, int y){
                super(x, y);
                startTimer();
                imagePath = InputData.bombImagePath;
    }

    /**
     * Metoda zaczyna odliczanie do wybuchu bomby.
     */
    private void startTimer(){
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timeToExplode > 0) {
                    if (timeToExplode <= 2) imagePath = InputData.redBombImagePath;
                    if (!paused) {
                        timeToExplode -= 1;
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }

    /**
     *Metoda zwraca czas do wybuchu bomby.
     * @return czas do wybuchu bomby
     */
    public int getTimeToExplode(){
        return timeToExplode;
    }

    /**
     * Funkcja zmienia stan timera.
     */
    public static void pause(){
        paused=!paused;
    }
}