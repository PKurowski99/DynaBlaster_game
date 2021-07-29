/**
 * Klasa reprezentuje blok portalu. Po wejściu na ten blok następuje przejście na kolejny poziom gry.
 */
public class Portal extends Block {
    public Portal(int x, int y){
        super(x,y);
        imagePath = InputData.portalImagePath;
    }
}
