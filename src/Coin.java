/**
 * Klasa reprezentuje bonus dla gracza, który się pojawia po zniszczeniu niektórych bloków klasy Stone.
 */
public class Coin extends Block {
    /**
     * wartość bonusu punktowego
     */
    private static int value = InputData.pointsPerCoin;
    public Coin(int x, int y){
        super(x,y);
        imagePath = InputData.coinImagePath;
    }

    /**
     * Funkcja zwraca wartość bonusu punktowego.
     * @return wartość bonusu
     */
    public static int getValue(){
        return value;
    }
}
