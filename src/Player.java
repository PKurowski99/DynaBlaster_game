import java.io.Serializable;
import java.util.TimerTask;

/**
 * Klasa opisuję gracza.
 */
public class Player implements Serializable {
    /**
     * nazwa gracza
     */
    private String nickname;
    /**
     * zdobyte punkty
     */
    private int scores;
    /**
     * zdrowie bohatera
     */
    private int heroHp;
    /**
     * wybrany przez gracza bohater
     */
    private Hero chosenHero = Hero.hero;
    /**
     * Zmienna opisująca kiedy gracz będzie mógł postawić bombę.
     */
    private int timeToPutBomb = 0;

    public Player(String name){
        nickname = name;
        scores = 0;
        heroHp = chosenHero.getHp();
        startTimer();
    }

    public Player(String name, int score){
        nickname = name;
        scores = score;
        heroHp = chosenHero.getHp();
    }

    /**
     * Metoda zmienia liczbę punktów uzyskanych przez gracza.
     * @param result punkty
     */
    public void setScores(int result){
        scores = result;
    }

    /**
     * Metoda zwraca liczbę punktów uzyskanych przez gracza.
     * @return punkty uzyskane przez gracza
     */
    public int getScores(){
        return scores;
    }

    /**
     * Metoda zwraca nazwę gracza.
     * @return nazwa gracza
     */
    public String getNickname() {return nickname; }

    /**
     * Metoda odpowiada za wybranie przez gracza bohatera.
     * @param x wybrany bohater
     */
    public void setChosenHero(Hero x){
        chosenHero = x;
        heroHp = x.getHp();
    }

    /**
     * Metoda zmniejsza liczbę żyć gracza o 1 (min 0).
     */
    public void reduceHp(){
        if(heroHp>0){heroHp -= 1;}
    }

    /**
     * Metoda zwiększa liczbę żyć gracza o 1 (max 5).
     */
    public void addHp(){
        if(heroHp<5){heroHp += 1;}
    }

    /**
     * Metoda zwraca liczbę żyć gracza.
     * @return liczba żyć gracza
     */
    public int getHeroHp(){return heroHp;}

    /**
     * Metoda odpowiada za postawienie przez gracza bomby.
     */
    public void putBomb(){
        timeToPutBomb = InputData.normalFireRate - 2*chosenHero.getFireRate();
    }

    /**
     * Metoda zwraca czas po którym gracz może postawić bombę.
     * @return czas do możliwego postawienia bomby.
     */
    public int getTimeToPutBomb(){return timeToPutBomb;}

    /**
     * Metoda odpowiada za zmniejszanie się czasu do możliwego postawienia bomby wraz z upływem czasu.
     */
    private void startTimer(){
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timeToPutBomb> 0) {
                    timeToPutBomb -= 1;
                }
            }
        };
        timer.scheduleAtFixedRate(task,InputData.defaultTickRate,InputData.defaultTickRate);
    }

    /**
     * Metoda zwraca wybranego przez gracza bohatera.
     * @return wybrany przez gracza bohater
     */
    public Hero getChosenHero(){return chosenHero;}

}
