package Sprint4;

import javax.swing.JButton;
import javax.swing.JLabel;

public class GeneralGame implements GameType {
    private Board board;
    private SOSGameLogic gameLogic;
    private Player currentPlayer;
    private Player bluePlayer;
    private Player redPlayer;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    private boolean sosFormedLastTurn;
    
    public GeneralGame(Board board, Player bluePlayer, Player redPlayer, JLabel turnLabel, JLabel scoreLabel) {
        this.board = board;
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
        this.turnLabel = turnLabel;
        this.scoreLabel = scoreLabel;
        this.currentPlayer = bluePlayer; 
        this.gameLogic = new SOSGameLogic(board);
        this.sosFormedLastTurn = false;
        
        updateTurnLabel();
        updateScoreLabel();
    }
    
    @Override
    public boolean processMove(JButton button) {
        if (!button.getText().isEmpty()) return false;
        
        button.setText(String.valueOf(currentPlayer.getSelectedLetter()));
        int sosCount = gameLogic.checkForSOSCount(button);
        
        if (sosCount > 0) {
            currentPlayer.incrementSOSCount(sosCount);
            updateScoreLabel();
            sosFormedLastTurn = true;
        } else {
            sosFormedLastTurn = false;
            switchPlayer(); 
        }
        
        return board.isFull();
    }
    public boolean wasSosFormedLastTurn() {
        return sosFormedLastTurn;
    }
    
    @Override
    public boolean checkForSOS(JButton button) {
        return gameLogic.checkForSOS(button);
    }
    
    @Override
    public boolean isGameOver() {
        return board.isFull();
    }
    
    @Override
    public String getGameOverMessage() {
        if (board.isFull()) {
            int blueScore = bluePlayer.getSOSCount();
            int redScore = redPlayer.getSOSCount();
            
            if (blueScore > redScore) {
                return "Blue player wins with " + blueScore + " SOSs!";
            } else if (redScore > blueScore) {
                return "Red player wins with " + redScore + " SOSs!";
            } else {
                return "Game ended in a draw!";
            }
        }
        return "";
    }
    
    @Override
    public void resetGame() {
        board.clearBoard();
        currentPlayer = bluePlayer;
        bluePlayer.resetSOSCount();
        redPlayer.resetSOSCount();
        sosFormedLastTurn = false;
        updateTurnLabel();
        updateScoreLabel();
    }
    
    @Override
    public String getGameTypeName() {
        return "General Game";
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
    
    private void updateScoreLabel() {
        if (scoreLabel != null) {
            scoreLabel.setText("Blue SOS: " + bluePlayer.getSOSCount() + 
                               " | Red SOS: " + redPlayer.getSOSCount());
        }
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}