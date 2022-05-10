package seeBattle;

import java.awt.*;

public class SeeBattleGame {

    Board board1;
    Board board2;
    public boolean fullRedraw;
    final private char[] TXT = {'a','b','c','d','e','f','g','h','i','j'};
    public GameState gameState = GameState.PLAYED;
    private int fieldStartX = 0;
    private int fieldStartY = 0;
    public boolean makingMove = false;

    public void setStartXY (int x, int y){
        fieldStartX = x;
        fieldStartY = y;
    }

    public void drowBoom (int startX, int startY, int width , Graphics2D gr2d){
        gr2d.setPaint(new Color(217, 21, 21));
        gr2d.setStroke(new BasicStroke(2));
        gr2d.drawLine(startX + 2, startY + 2, startX + width -2 , startY + width-2);
        gr2d.setStroke(new BasicStroke(2));
        gr2d.drawLine(startX + 2 , startY + width -2 , startX + width -2 , startY +2);
    }

    public void drowBoom2 (int startX, int startY, int width , Graphics2D gr2d){
        gr2d.setPaint(new Color(217, 21, 21));
        gr2d.setStroke(new BasicStroke(2));
        gr2d.fillOval(startX + width/3, startY + width/3, width/3, width/3);
    }

    public void drowShip (int startX, int startY, int stepOfGrid , Graphics2D gr2d){
        gr2d.setPaint(new Color(30, 147, 205));
        gr2d.setStroke(new BasicStroke(2));
        gr2d.drawRect(startX, startY, stepOfGrid, stepOfGrid );
    }

    public void drowField (int windowWidth, int windowHeight, int stepOfGrid , Graphics2D gr2d) {
        gr2d.setPaint(new Color(139, 233, 255, 255));

        for (int i = fieldStartX;i < windowWidth;i += stepOfGrid) {
            gr2d.drawLine(i, fieldStartY, i, windowHeight);
        }

        for (int i = fieldStartY;i < windowHeight;i += stepOfGrid) {
            gr2d.drawLine(fieldStartX, i, windowWidth, i);
        }
    }

    public void showBoardsGUI(int stepOfGrid,int windowWidth, int windowHeight ,Graphics2D gr2d) {

        /* Debug info
        if (!makingMove) System.out.println("Redrawing All " + nuberOfDrowing);
        else System.out.println("Redrawing only ships " + nuberOfDrowing);
        */

        //Полностью перерисовываем поле, только когда процедура перерисовки была вызванна не ходом пользователя
        if (!makingMove || fullRedraw) {
            fullRedraw = false;
            //System.out.println("Full redraw");
            gr2d.setBackground(Color.WHITE);
            gr2d.clearRect(fieldStartX, fieldStartY, windowWidth - fieldStartX, windowHeight - fieldStartY);

            drowField(windowWidth, windowHeight, stepOfGrid, gr2d);
            gr2d.setPaint(new Color(13, 128, 180));
            gr2d.setStroke(new BasicStroke(2));
            gr2d.drawRect(fieldStartX + stepOfGrid * 2, fieldStartY + stepOfGrid * 2, stepOfGrid * 10, stepOfGrid * 10);
            gr2d.drawRect(fieldStartX + stepOfGrid * 14, fieldStartY + stepOfGrid * 2, stepOfGrid * 10, stepOfGrid * 10);

            //Подписи координат
            gr2d.setFont(new Font("Verdana", Font.PLAIN, stepOfGrid / 6 * 4));
            for (int i = 0;i < 10;i++) {
                gr2d.drawString(String.valueOf(TXT[i]), fieldStartX + stepOfGrid * (2 + i) + stepOfGrid / 3, fieldStartY + stepOfGrid * 2 - stepOfGrid / 4);
                gr2d.drawString(String.valueOf(TXT[i]), fieldStartX + stepOfGrid * (14 + i) + stepOfGrid / 3, fieldStartY + stepOfGrid * 2 - stepOfGrid / 4);

                if ((i + 1) < 10) {
                    gr2d.drawString(String.valueOf(i + 1), fieldStartX + stepOfGrid + stepOfGrid / 3, fieldStartY + stepOfGrid * (2 + i) + stepOfGrid / 5 * 4);
                    gr2d.drawString(String.valueOf(i + 1), fieldStartX + stepOfGrid * 13 + stepOfGrid / 3, fieldStartY + stepOfGrid * (2 + i) + stepOfGrid / 5 * 4);
                } else {
                    gr2d.drawString(String.valueOf(i + 1), fieldStartX + stepOfGrid + stepOfGrid / 11, fieldStartY + stepOfGrid * (2 + i) + stepOfGrid / 5 * 4);
                    gr2d.drawString(String.valueOf(i + 1), fieldStartX + stepOfGrid * 13 + stepOfGrid / 11, fieldStartY + stepOfGrid * (2 + i) + stepOfGrid / 5 * 4);
                }
            }
        }

        gr2d.setBackground(Color.WHITE);
        //gr2d.clearRect(fieldStartX + stepOfGrid*3+1, fieldStartY + stepOfGrid*13+1, stepOfGrid*20-3, stepOfGrid-1);
        gr2d.clearRect(fieldStartX + stepOfGrid*3+1, fieldStartY + stepOfGrid*13+1, stepOfGrid*16-3, stepOfGrid-1);
        gr2d.setPaint(new Color(13, 128, 180));
        gr2d.setFont(new Font("Verdana", Font.BOLD, stepOfGrid/7*4));

        gr2d.clearRect(fieldStartX + stepOfGrid*19+1, fieldStartY + stepOfGrid * 13+1, stepOfGrid*4-2, stepOfGrid-2);
        gr2d.drawString(" New Game", stepOfGrid*19+13, windowHeight - stepOfGrid*3/2+7);
        gr2d.drawRect(fieldStartX + stepOfGrid*19, fieldStartY + stepOfGrid * 13, stepOfGrid*4, stepOfGrid);

        if (gameState.equals(GameState.PLAYED)){
            gr2d.drawString("Deck left:  User - " + board1.decksLeft() + "  CPU - " + board2.decksLeft(), stepOfGrid*3+13, windowHeight - stepOfGrid*3/2+7);
        } else if (gameState.equals(GameState.USERWIN)){
            gr2d.drawString("You WIN !!!", stepOfGrid*3+13, windowHeight - stepOfGrid*3/2+7);
        } else if (gameState.equals(GameState.CPUWIN)){
            gr2d.drawString("CPUWIN !!!", stepOfGrid*3+13, windowHeight - stepOfGrid*3/2+7);
            board2.setShipVisible(true);
        }

///////////////////

        //board1
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board1.board[j][i].isShot()){
                    if (board1.board[j][i].isShip()) drowBoom (fieldStartX + (j+2)*stepOfGrid, fieldStartY + (i+2)*stepOfGrid, stepOfGrid ,  gr2d);
                    else drowBoom2 (fieldStartX + (j+2)*stepOfGrid, fieldStartY + (i+2)*stepOfGrid, stepOfGrid ,  gr2d);
                }
                if (board1.board[j][i].isShip()){
                    drowShip (fieldStartX + (j+2)*stepOfGrid, fieldStartY + (i+2)*stepOfGrid, stepOfGrid ,  gr2d);
                }
            }
        }

        //board2
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board2.board[j][i].isShot()){
                    if (board2.board[j][i].isShip()) drowBoom (fieldStartX + (j+14)*stepOfGrid, fieldStartY + (i+2)*stepOfGrid, stepOfGrid ,  gr2d);
                    else drowBoom2 (fieldStartX + (j+14)*stepOfGrid, fieldStartY + (i+2)*stepOfGrid, stepOfGrid ,  gr2d);
                }
                if (board2.isShipVisible() || board2.board[j][i].isShot()) {
                    if (board2.board[j][i].isShip()) {
                        drowShip(fieldStartX + (j + 14) * stepOfGrid, fieldStartY + (i + 2) * stepOfGrid, stepOfGrid, gr2d);
                    }
                }
            }
        }

    }

    public void userMoveGUI(int mouseX, int mouseY,int stepOfGrid) {

        if (gameState.equals(GameState.PLAYED)) {
            int x, y;
            x = mouseX - fieldStartX - stepOfGrid * 14;
            y = mouseY - fieldStartY - stepOfGrid * 2;

            /*
            if (((mouseX - fieldStartX - stepOfGrid * 19) > 0 && (mouseX - fieldStartX - stepOfGrid * 19) < stepOfGrid*4)
                    && ((mouseY - fieldStartY - stepOfGrid * 13) > 0 && (mouseY - fieldStartY - stepOfGrid * 13) < stepOfGrid))
                SeeBattleNewGame();
             */

            if (10 > x / stepOfGrid && x > 0 && 10 > y / stepOfGrid && y > 0) {

                if (!board2.board[x / stepOfGrid][y / stepOfGrid].isShot()) {
                    board2.shootXY(x / stepOfGrid, y / stepOfGrid);

                    if (!board2.board[x / stepOfGrid][y / stepOfGrid].isShip()) board1.cpuMakeShoot();
                    if (board1.decksLeft() == 0) gameState = GameState.CPUWIN;
                    if (board2.decksLeft() == 0) gameState = GameState.USERWIN;
                }
            }

        }
    }

    public void SeeBattleNewGame(){

        GameState gameState = GameState.PLAYED;
        board1 = new Board();
        board1.setShipsOnBoard();
        //for (int i = 0; i < 15; i++) { board1.cpuMakeShoot(); }

        board2 = new Board();
        board2.setShipsOnBoard();
        board2.setShipVisible(false);
        //for (int i = 0; i < 15; i++) {board2.cpuMakeShoot(); }

        fullRedraw = true;

    }

    public SeeBattleGame() {
        SeeBattleNewGame();
    }

}

