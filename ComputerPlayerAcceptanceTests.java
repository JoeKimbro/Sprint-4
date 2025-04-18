package Sprint4;

import static org.junit.Assert.*;
import javax.swing.*;
import org.junit.Before;
import org.junit.Test;

public class ComputerPlayerAcceptanceTests {
    private Board board;
    private ComputerPlayer blueComputer;
    private ComputerPlayer redComputer;
    private JRadioButton sButton;
    private JRadioButton oButton;
    private SimpleGame simpleGame;
    private GeneralGame generalGame;
    private SOSGameLogic gameLogic;

    @Before
    public void setUp() {
        board = new Board(new JPanel(), 3);
        sButton = new JRadioButton("S");
        oButton = new JRadioButton("O");
        blueComputer = new ComputerPlayer("Blue Computer", "Blue", sButton, oButton);
        redComputer = new ComputerPlayer("Red Computer", "Red", sButton, oButton);
        simpleGame = new SimpleGame(board, blueComputer, redComputer, new JLabel());
        generalGame = new GeneralGame(board, blueComputer, redComputer, new JLabel(), new JLabel());
        gameLogic = new SOSGameLogic(board);
    }

    // AC 8.1 <Computer Placement>
    @Test
    public void testComputerPlacesSOrO() {
        JButton move = blueComputer.makeAutomatedMove(board);
        assertNotNull(move);
        
        // Make the move through the game
        simpleGame.processMove(move);
        String text = move.getText();
        assertTrue(text.equals("S") || text.equals("O"));
    }

    // AC 8.1 <SOS FORMED> 
    @Test
    public void testComputerCanFormSOS() {
        JButton[][] grid = board.getButtonGrid();
        
        // Setup partial SOS
        grid[0][0].setText("S");
        grid[0][1].setText("O");
        
        // Computer should complete the SOS
        JButton move = blueComputer.makeAutomatedMove(board);
        assertEquals(grid[0][2], move);
    }


    // AC 9.1 <Computer Vs. Computer>
    @Test
    public void testComputerVsComputerGameCompletion() {
        // Simulate full game between two computers
        while (!board.isFull()) {
            Player currentPlayer = simpleGame.getCurrentPlayer();
            ComputerPlayer currentComputer = (currentPlayer == blueComputer) ? blueComputer : redComputer;
            JButton move = currentComputer.makeAutomatedMove(board);
            if (move != null) {
                simpleGame.processMove(move);
            }
        }
        assertTrue(board.isFull());
    }

    // AC 8.1 <Computer Wins in Simple Game>
  
    
    // AC 8.1 <Computer as blue player>
    @Test
    public void testComputerAsBluePlayerFirstMove() {
        JButton firstMove = blueComputer.makeAutomatedMove(board);
        assertNotNull(firstMove);
        
        boolean isStrategic = firstMove == board.getButtonGrid()[1][1] || // center
                            firstMove == board.getButtonGrid()[0][0] || // corner
                            firstMove == board.getButtonGrid()[0][2] || // corner
                            firstMove == board.getButtonGrid()[2][0] || // corner
                            firstMove == board.getButtonGrid()[2][2];   // corner
        assertTrue(isStrategic);
    }

    // AC 8.1 <Computer Simple game>
    @Test
    public void testSimpleGameCompletion() {
        boolean gameOver = false;
        while (!gameOver && !board.isFull()) {
            Player current = simpleGame.getCurrentPlayer();
            ComputerPlayer computer = (current == blueComputer) ? blueComputer : redComputer;
            JButton move = computer.makeAutomatedMove(board);
            if (move != null) {
                gameOver = simpleGame.processMove(move);
            }
        }
        assertTrue(gameOver || board.isFull());
    }

    // AC 8.1 <invalid move>
    @Test
    public void testComputerDoesntMakeInvalidMove() {
        JButton[][] grid = board.getButtonGrid();
        grid[0][0].setText("S"); // Occupied
        
        JButton move = blueComputer.makeAutomatedMove(board);
        assertNotEquals(grid[0][0], move);
    }

    // AC 8.1 <Computer Draw>
    @Test
    public void testComputerHandlesFullBoard() {
        JButton[][] grid = board.getButtonGrid();
        
        // Fill the board
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].setText("S");
            }
        }
        
        assertNull(blueComputer.makeAutomatedMove(board));
    }

    // Helper methods
    private int getRow(JButton button) {
        // Implement based on your button position tracking
        // Example if buttons are named "row,col":
        String name = button.getName();
        return Integer.parseInt(name.split(",")[0]);
    }
    
    private int getCol(JButton button) {
        String name = button.getName();
        return Integer.parseInt(name.split(",")[1]);
    }
}