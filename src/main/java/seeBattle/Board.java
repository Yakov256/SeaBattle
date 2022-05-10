package seeBattle;

import java.util.Random;

class Board {

    private boolean shipVisible = true;
    private int decksLeft = 0;
    public Cell[][] board = new Cell[10][10];
    WreckedShip wreckedShip = new WreckedShip();

    public boolean isShipVisible() {
        return shipVisible;
    }

    public void setShipVisible(boolean shipVisible) {
        this.shipVisible = shipVisible;
    }

    public int decksLeft() {
        return decksLeft;
    }

    Board() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) board[i][j] = new Cell();
        }
    }

    private int checkXorY(int xOrY) {
        if (xOrY < 0) return 0;
        if (xOrY > 9) return 9;
        return xOrY;
    }

    private DeckOfTheShip checkAndSetDestroyed (int x, int y){
        DeckOfTheShip deckOfTheShip = new DeckOfTheShip();
        deckOfTheShip.x = x;
        deckOfTheShip.y = y;
        deckOfTheShip.deck = true;
        deckOfTheShip.destroyed = board[x][y].isShot();
        return deckOfTheShip;
    }

    private DeckOfTheShip[] buildShipArray(int x, int y) {

        DeckOfTheShip[] shipArray = new DeckOfTheShip[4];
        int shipCurrentPosition = 0;

        for (int i = 0; i < 4; i++) shipArray[i] = new DeckOfTheShip();

        //гор. прав.
        for (int i = 0; (i < 4) && (x + i < 10); i++) {
            if (board[x + i][y].isShip()) {
                shipArray[shipCurrentPosition] = checkAndSetDestroyed (x + i, y);
                shipCurrentPosition++;
            } else break;
        }

        //гор. лев.
        for (int i = 1; (i < 4) && (x - i >= 0); i++) {
            if (board[x - i][y].isShip()) {
                shipArray[shipCurrentPosition] = checkAndSetDestroyed (x - i, y);
                shipCurrentPosition++;
            } else break;
        }

        //вер. прав.
        for (int i = 1; (i < 4) && (y + i < 10); i++) {
            if (board[x][y + i].isShip()) {
                shipArray[shipCurrentPosition] = checkAndSetDestroyed (x,y + i);
                shipCurrentPosition++;
            } else break;
        }

        //вер. лев.
        for (int i = 1; (i < 4) && (y - i >= 0); i++) {
            if (board[x][y - i].isShip()) {
                shipArray[shipCurrentPosition] = checkAndSetDestroyed (x,y - i);
                shipCurrentPosition++;
            } else break;
        }

        return shipArray;
    }

    private void markDestroyedShip(DeckOfTheShip[] shipArray) {

        for (int n = 0; n < 4; n++) {
            if (shipArray[n].destroyed & shipArray[n].deck) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        board[checkXorY(shipArray[n].x + i)][checkXorY(shipArray[n].y + j)].setShot(true);
                    }
                }
            }
        }
    }

    private boolean NeighborhoodsCellsIsFree(int x, int y) {
        boolean cellsFree = true;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (board[checkXorY(x + i)][checkXorY(y + j)].isShip()) cellsFree = false;
            }
        }
        return cellsFree;
    }

    private boolean tryToSetShipOnBoard(int decks, int poputokRasstanovki) {

        boolean shipsSetupSuccessfull = true;
        boolean posebleSetShip = true;
        boolean setShipGorizontal = true;
        Random random = new Random();
        int shipStartX = 0;
        int shipStartY = 0;

        for (int i = 0; i < poputokRasstanovki; i++) {

            posebleSetShip = true;
            shipStartX = random.nextInt(11 - decks);
            shipStartY = random.nextInt(11 - decks);

            if (random.nextInt(2) > 0) setShipGorizontal = false;
            else setShipGorizontal = true;

            for (int j = 0; j < decks; j++) {
                if (setShipGorizontal) {
                    if (!NeighborhoodsCellsIsFree((shipStartX + j), shipStartY)) posebleSetShip = false; //горизонталь
                } else {
                    if (!NeighborhoodsCellsIsFree(shipStartX, (shipStartY + j))) posebleSetShip = false; //вертикаль
                }
            }

            if (posebleSetShip) i = poputokRasstanovki;

        }

        if (posebleSetShip) {
            for (int j = 0; j < decks; j++) {
                if (setShipGorizontal) board[shipStartX + j][shipStartY].setShip(true);//горизонталь
                else board[shipStartX][shipStartY + j].setShip(true); //вертикаль
            }
        } else shipsSetupSuccessfull = false;

        if (shipsSetupSuccessfull) decksLeft += decks;

        return shipsSetupSuccessfull;
    }


    private boolean setShipOnBoard(int decks, int kolichectvoKorabley, int poputokRasstanovki) {

        boolean shipsSetupSuccessfull = true;
        boolean shipSet;

        for (int i = 0; i < kolichectvoKorabley; i++) {
            shipSet = tryToSetShipOnBoard(decks, poputokRasstanovki);
            if (!shipSet) {
                shipsSetupSuccessfull = false;
            }
        }

        return shipsSetupSuccessfull;
    }

    public void setShipsOnBoard() {
        decksLeft = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) board[i][j] = new Cell();
        }

        if (!setShipOnBoard(4, 1, 100))
            System.out.println("Не удалось разместить четырех палубный корабль!");
        if (!setShipOnBoard(3, 2, 100))
            System.out.println("Не удалось разместить трех палубные корабли!");
        if (!setShipOnBoard(2, 3, 100))
            System.out.println("Не удалось разместить двух палубные корабли!");
        if (!setShipOnBoard(1, 4, 100))
            System.out.println("Не удалось разместить одно палубные корабли!");
    }

    private void setWreckedShipDirectionChecked(int direction, boolean isCheked){
        switch (direction) {
            case (0):
                wreckedShip.upChecked = isCheked; //UP
                break;
            case (1):
                wreckedShip.downChecked = isCheked; //down
                break;
            case (2):
                wreckedShip.leftChecked = isCheked; //Left
                break;
            case (3):
                wreckedShip.rightChecked = isCheked; //Right
                break;
        }
    }

    boolean finishOffTheShip() {
        Random random = new Random();
        boolean deckCutted = false;
        int direction;
        boolean shotIsDone = false, wholeShipDestroyed = false;
        int maxRange, truingToShoot = 0;
        int dX =0, dY=0;

        while (!shotIsDone && truingToShoot < 25) {
            truingToShoot++;
            maxRange = 0;
            direction = random.nextInt(4);
            boolean continueCheck = true;

            switch (direction) {
                case (0):
                    dX = 0; dY = -1; //UP
                    if (wreckedShip.upChecked) continueCheck = false;
                    break;
                case (1):
                    dX = 0; dY = 1; //down
                    if (wreckedShip.downChecked) continueCheck = false;
                    break;
                case (2):
                    dX = -1; dY = 0; //Left
                    if (wreckedShip.leftChecked) continueCheck = false;
                    break;
                case (3):
                    dX = 1; dY = 0;  //Right
                    if (wreckedShip.rightChecked) continueCheck = false;
                    break;
            }

            shotIsDone = false;
            if (continueCheck) {
                for (int i = 1; (i < 4) && (wreckedShip.firstShootedX + i*dX >= 0) && (wreckedShip.firstShootedY + i*dY >= 0); i++) {
                    if (board[checkXorY(wreckedShip.firstShootedX + i*dX)][checkXorY(wreckedShip.firstShootedY + i*dY)].isShip()) {
                        if(direction == 0 || direction == 1) {
                            wreckedShip.leftChecked = true;
                            wreckedShip.rightChecked = true;
                        } else {
                            wreckedShip.upChecked = true;
                            wreckedShip.downChecked = true;
                        }

                    } else {
                        setWreckedShipDirectionChecked(direction, true);
                        if (board[wreckedShip.firstShootedX + i*dX][wreckedShip.firstShootedY + i*dY].isShot()) break;
                    }

                    if (!board[checkXorY(wreckedShip.firstShootedX+ i*dX)][checkXorY(wreckedShip.firstShootedY  + i*dY)].isShot()) {
                        wholeShipDestroyed = shootXY(wreckedShip.firstShootedX + i*dX, wreckedShip.firstShootedY + i*dY);
                        shotIsDone = true;
                        maxRange = i;
                        if (board[wreckedShip.firstShootedX + i*dX][wreckedShip.firstShootedY  + i*dY].isShip()) deckCutted = true;
                        i = 4;
                    }
                }
                if (maxRange > 2 && !shotIsDone) {

                    setWreckedShipDirectionChecked(direction, true);
                }
            }
        }

        // Нужна проверка не добили ли корабль
        if (wholeShipDestroyed) {
            wreckedShip.shipIsWrecked = false;
            wreckedShip.upChecked = false;
            wreckedShip.downChecked = false;
            wreckedShip.leftChecked = false;
            wreckedShip.rightChecked = false;
        }
        /*  //debug info
        System.out.println("----  Добиваем корабль  Направление:" + napravlenie);
        System.out.println("shotIsDone         - " + shotIsDone);
        System.out.println("wholeShipDestroyed - " + wholeShipDestroyed);
        System.out.println("wreckedShip.upChecked    - " + wreckedShip.upChecked);
        System.out.println("wreckedShip.downChecked  - " + wreckedShip.downChecked);
        System.out.println("wreckedShip.leftChecked  - " + wreckedShip.leftChecked);
        System.out.println("wreckedShip.rightChecked - " + wreckedShip.rightChecked);
        */

        return deckCutted;
    }

    public void cpuMakeShoot() {
        Random random = new Random();
        int x, y;

        if (wreckedShip.shipIsWrecked) {
            if (finishOffTheShip()) cpuMakeShoot();
        } else {
            for (int i = 0; i < 100; i++) { //Количество попыток для случайного выстрела
                x = random.nextInt(10);
                y = random.nextInt(10);
                if (!board[x][y].isShot()) {
                    i = 100;
                    this.shootXY(x, y);
                    if (this.board[x][y].isShip()) cpuMakeShoot();
                }
            }
        }
    }

    public boolean shootXY(int x, int y) {

        DeckOfTheShip[] shipArray;
        boolean wholeShipDestroyed = true;

        if (board[checkXorY(x)][checkXorY(y)].isShip() & !board[checkXorY(x)][checkXorY(y)].isShot()) {
            //Первое попадание в палубу корабля, анализируем не подбили ли корабль
            decksLeft--;

            board[checkXorY(x)][checkXorY(y)].setShot(true);
            shipArray = buildShipArray(checkXorY(x), checkXorY(y));

            for (int i = 0; i < 4; i++) {
                if (shipArray[i].deck & !shipArray[i].destroyed) wholeShipDestroyed = false;
            }

            if (wholeShipDestroyed) {
                markDestroyedShip(shipArray);
            } else {
                if (!wreckedShip.shipIsWrecked) {
                    wreckedShip.shipIsWrecked = true;
                    wreckedShip.firstShootedX = x;
                    wreckedShip.firstShootedY = y;
                }
            }
        } else {
            board[checkXorY(x)][checkXorY(y)].setShot(true);
            wholeShipDestroyed = false;
        }

        return wholeShipDestroyed;
    }

}