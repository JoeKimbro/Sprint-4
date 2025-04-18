package Sprint4;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

public class SimpleGame implements GameType {
    private Board board;
    private SOSGameLogic gameLogic;
    private Player currentPlayer;
    private Player bluePlayer;
    private Player redPlayer;
    private JLabel turnLabel;
    private boolean gameEnded;
    private String gameOverMessage;
    
    public SimpleGame(Board board, Player bluePlayer, Player redPlayer, JLabel turnLabel) {
        this.board = board;
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
        this.turnLabel = turnLabel;
        this.currentPlayer = bluePlayer; 
        this.gameLogic = new SOSGameLogic(board);
        this.gameEnded = false;
        updateTurnLabel();
    }
    
    @Override
    public boolean processMove(JButton button) {
        if (gameEnded || !button.getText().isEmpty()) return false;
        
        button.setText(String.valueOf(currentPlayer.getSelectedLetter()));
        
        if (checkForSOS(button)) {
            gameEnded = true;
            gameOverMessage = currentPlayer + " wins!";
            return true;
        }
        
        switchPlayer();
        
        if (board.isFull()) {
            gameEnded = true;
            gameOverMessage = "Draw!";
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean checkForSOS(JButton button) {
        return gameLogic.checkForSOS(button);
    }
    
    @Override
    public boolean isGameOver() {
        return gameEnded || board.isFull();
    }
    
    @Override
    public String getGameOverMessage() {
        if (gameEnded) {
            return gameOverMessage;
        } else if (board.isFull()) {
            return "Game ended in a draw!";
        }
        return "";
    }
    
    @Override
    public void resetGame() {
        board.clearBoard();
        currentPlayer = bluePlayer;
        gameEnded = false;
        updateTurnLabel();
    }
    
    @Override
    public String getGameTypeName() {
        return "Simple Game";
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
        updateTurnLabel();
        
        System.out.println("Switched to: " + currentPlayer + 
                          " (Human: " + !(currentPlayer instanceof ComputerPlayer) + ")");
    }
    private void updateTurnLabel() {
        if (turnLabel != null) {
            turnLabel.setText("Current Turn: " + currentPlayer.toString() + 
                              " (" + currentPlayer.getSelectedLetter() + ")");
        }
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}