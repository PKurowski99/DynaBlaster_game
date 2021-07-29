/**
 * Klasa reprezentuje blok kamienia. Bohater nie może się przemieszczać po tym bloku. Możliwe jest jego zniszczenie za pomocą bomby.
 * Po zniszczeniu kamienia mogą się pojawić ukryte elementy takie jak bonus, dodatkowe życie lub portal na następny poziom.
 */
public class Stone extends Block {
    /**
     * czy jest ukryty portal
     */
    private boolean hiddenPortal;
    /**
     * czy jest ukryty bonus
     */
    private boolean hiddenCoin;
    /**
     * czy jest ukryte dodatkowe życie
     */
    private boolean hiddenHeart;

    public Stone(int x,int y, boolean portal, boolean heart, boolean coin){
        super(x,y);
        hiddenPortal = portal;
        hiddenCoin = coin;
        hiddenHeart = heart;
        imagePath = InputData.stoneImagePath;
    }

    /**
     * Metoda zwraca informację czy jest ukryty portal.
     * @return informacja o ukrytym portalu
     */
    public boolean getPortalInformation(){return hiddenPortal;}

    /**
     * Metoda zwraca informację czy jest ukryty bonus.
     * @return informacja o ukrytym bonusie
     */
    public boolean getCoinInformation(){return hiddenCoin;}

    /**
     * Metoda zwraca informację czy jest ukryte dodatkowe życie.
     * @return informacja o ukrytym dodatkowym życu
     */
    public boolean getHeartInformation(){return hiddenHeart;}
}
