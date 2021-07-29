/**
 * Klasa reprezentująca blok powietrza na mapie. Przez ten blok gracz może swobodnie przechodzić oraz stawiać na nim bomby.
 * Nie jest możliwe zniszczenie tego bloku.
 */
public class Air extends Block {
    public Air(int x, int y){
        super(x,y);
        imagePath = InputData.airImagePath;
    }
}
