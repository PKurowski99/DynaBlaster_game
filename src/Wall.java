/**
 *  Klasa reprezentuje blok ściany, którego nie można zniszczyć
 */
public class Wall extends Block {
    public Wall(int x,int y){
        super(x,y);
        imagePath = InputData.wallImagePath;
    }
}
