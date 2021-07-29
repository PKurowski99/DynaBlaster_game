/**
 * Klasa Block jest klasą podstawową, po której dziedziczą wszystkie bloki, z których zbudowana jest mapa gry.
 */
public class Block extends ElementWithImage {
    /**
     * współrzędna x położenia bloku na mapie gry
     */
    protected int xPosition = 0;
    /**
     * współrzędna y położenia bloku na mapie gry
     */
    protected int yPosition = 0;

    public Block(int x, int y){
        setPosition(x,y);
    }

    /**
     * Metoda pozwala na ustawienie współrzędnych bloku.
     * @param x odpowiada za składową x położenia bloku
     * @param y odpowiada za składową y położenia bloku
     */
    public void setPosition(int x,int y){
        xPosition = x;
        yPosition = y;
    }
}
