/**
 * Klasa reprezentuje dodatkowe życie dla gracza, które się pojawia po zniszczeniu niektórych bloków klasy Stone.
 */
public class Heart extends Block {
    /**
     * wartość serduszka w punktach
     */
    private static int value = InputData.pointsPerHp;
    public Heart(int x, int y){
        super(x,y);
        imagePath = InputData.heartImagePath;
    }
    /**
     * Funkcja zwraca wartość serduszka w punktach.
     * @return wartość serduszka w punktach
     */
    public static int getValue(){
        return value;
    }
}