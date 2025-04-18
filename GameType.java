package Sprint4;

import javax.swing.JButton;


public interface GameType {

    boolean processMove(JButton button);

    boolean checkForSOS(JButton button);

    boolean isGameOver();

    String getGameOverMessage();

    void resetGame();

    String getGameTypeName();
}