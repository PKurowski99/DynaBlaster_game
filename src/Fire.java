import java.util.TimerTask;

/**
 * Klasa reprezentuje blok ognia, który pojawia się po wybuchu bomby.
 */
public class Fire extends Block {
    /**
     * czy po zniknięciu pojawi się portal
     */
    private boolean portal;
    /**
     * czy po zniknięciu pojawi się serduszko
     */
    private boolean heart;
    /**
     * czy po zniknięciu pojawi się bonus
     */
    private boolean coin;
    /**
     * klatka w animacji
     */
    private int frame = 0;
    /**
     * stan timera - aktywne odliczanie lub pauza
     */
    private static boolean paused = false;
    /**
     * czas utrzymywania się ognia na mapie
     */
    private float timeToDisappear = InputData.fireTimeToDisappear;
    public Fire( int x, int y, boolean portal,boolean coin, boolean heart){
        super(x, y);
        startTimer();
        this.portal = portal;
        this.coin = coin;
        this.heart = heart;
        imagePath = InputData.fire1ImagePath;
    }

    /**
     * Metoda odlicza czas do zniknięcia ognia i odpowiada za animację.
     */
    private void startTimer(){
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(frame == 0){imagePath = InputData.fire1ImagePath;}
                else if(frame == 1){imagePath = InputData.fire2ImagePath;}
                else if(frame == 2){imagePath = InputData.fire3ImagePath;}
                else if(frame == 3){imagePath = InputData.fire4ImagePath;}
                else if(frame == 4){imagePath = InputData.fire5ImagePath;}
                else if(frame == 5){imagePath = InputData.fire6ImagePath;}
                if (timeToDisappear> 0) {
                    if (!paused) {
                        timeToDisappear -= 0.1;
                        frame += 1;
                    }
                }
                if (frame == InputData.fireFrames){frame = 0;}
            }
        };
        timer.scheduleAtFixedRate(task,InputData.fireFramesTickRate,InputData.fireFramesTickRate);
    }

    /**
     * Metoda zwraca czas po jakim ogień znika z planszy.
     * @return czas zaniku ognia
     */
    public float getTimeToDisappear(){
        return timeToDisappear;
    }

    /**
     * Metoda zwraca informację o tym czy pojawi się portal.
     * @return czy pojawi się portal
     */
    public boolean getPortalInformation(){
        return portal;
    }
    /**
     * Metoda zwraca informację o tym czy pojawi się bonus.
     * @return czy pojawi się bonus
     */
    public boolean getCoinInformation(){
        return coin;
    }
    /**
     * Metoda zwraca informację o tym czy pojawi się serduszko.
     * @return czy pojawi się serduszko
     */
    public boolean getHeartInformation(){
        return heart;
    }

    /**
     * Metoda zmienia stan timera.
     */
    public static void pause(){
        paused = !paused;
    }
}