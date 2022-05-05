package seeBattle;

public class WreckedShip {

    //public DeckOfTheShip[] deckOfTheShip = new	DeckOfTheShip[4];
    public boolean shipIsWrecked;
    public ShipDirection shipDirection;
    boolean	upChecked, leftChecked, rightChecked, downChecked;
    public int firstShootedX,firstShootedY;

    void WreckedShip(){
        shipDirection = ShipDirection.UNKNOWN;
    }

}
