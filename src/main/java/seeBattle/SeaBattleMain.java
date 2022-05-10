package seeBattle;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 Исправил - Косяк - иногда подбивает 2 палубы корабля за одно нажатие мыши.
 20210409 - Ускорил рисование окна, убрал в условие отрисовку поля.
 20210409 - Добавил возможность ходить повторно, после попадания в корабль, для игрока и компьютера.

 Добавить вывод окна о выигрыше/проигрыше import javax.swing.JOptionPane;
**/

public class SeaBattleMain extends JFrame {

    //final int STEP_OF_GRID = 30;
    final int stepOfGrid = 30;
    final int fieldStartX = 8;
    final int fieldStartY = 31;//31
    final int WINDOW_WIDTH = stepOfGrid * 26;
    final int WINDOW_HEIGHT = stepOfGrid * 16;

    //private JToolBar jtb = new JToolBar("my toolbar");

    public SeeBattleGame sb = new SeeBattleGame();

    public SeaBattleMain( ) {
        super("SeaBattle");
    }

    public void SeaBattleStart( ) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);

        sb.setStartXY (fieldStartX, fieldStartY); //Устанавливаем смещение игрового поля

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    if (((mouseX - fieldStartX - stepOfGrid * 19) > 0
                            && (mouseX - fieldStartX - stepOfGrid * 19) < stepOfGrid*4)
                            && ((mouseY - fieldStartY - stepOfGrid * 13) > 0
                            && (mouseY - fieldStartY - stepOfGrid * 13) < stepOfGrid)) {
                        sb.SeeBattleNewGame();
                        sb.fullRedraw = true;
                        sb.gameState = GameState.PLAYED;
                    }

                    sb.userMoveGUI(mouseX,mouseY, stepOfGrid);
                }
                //if (e.getButton() == MouseEvent.BUTTON3) game.pressRightButton(coord);
                //if (e.getButton() == MouseEvent.BUTTON2) game.start();
                sb.makingMove = true;
                repaint();
            }
        });

    }

    @Override
    public void paint(Graphics g) {
        //public void paintComponent (Graphics g) {

        Graphics2D gr2d = (Graphics2D) g;
        //gr2d.setBackground(Color.WHITE);

        /*
        if (!runOnce) {
            JMenuBar jtm = createMenuBar();
            //add(jtm);
            add(jtm, BorderLayout.NORTH);
            setJMenuBar(jtm);
            runOnce = true;
        }
        */
        sb.showBoardsGUI(stepOfGrid, WINDOW_WIDTH, WINDOW_HEIGHT,gr2d);
        sb.makingMove = false;
    }

//    public static void main(String[] args) {
//        SeaBattleMain sb = new SeaBattleMain();
//        //sb = new SeeBattle();
//    }
}
