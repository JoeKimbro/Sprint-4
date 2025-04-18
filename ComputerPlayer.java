package Sprint4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JRadioButton;

public class ComputerPlayer extends Player {
    private String color;
    private Random random;

    public ComputerPlayer(String name, String color, JRadioButton sButton, JRadioButton oButton) {
        super(name, sButton, oButton);
        this.color = color;
        this.random = new Random();
        this.selectedLetter = 'S'; 
        
        sButton.setEnabled(false);
        sButton.setVisible(false);
        oButton.setEnabled(false);
        oButton.setVisible(false);
    }

    @Override
    public char getSelectedLetter() {
        return selectedLetter;
    }

    @Override
    public void setSelectedLetter(char letter) {
        if (letter == 'S' || letter == 'O') {
            this.selectedLetter = letter;
        }
    }
    @Override
    public void makeMove(Board board, int row, int col) {
        JButton[][] grid = board.getButtonGrid();
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            JButton button = grid[row][col];
            if (button.getText().isEmpty()) {
                button.setText(String.valueOf(getSelectedLetter()));
            }
        }
    }

    @Override
    public JButton makeAutomatedMove(Board board) {
        JButton[][] grid = board.getButtonGrid();
        List<JButton> emptyButtons = getEmptyButtons(grid);
        
        if (emptyButtons.isEmpty()) return null;
        
        JButton move = findMoveToCompleteSOS(board, grid, 'S');
        if (move != null) {
            setSelectedLetter('S');
            return move;
        }
        
        move = findMoveToCompleteSOS(board, grid, 'O');
        if (move != null) {
            setSelectedLetter('O');
            return move;
        }
        
        move = findStrategicMove(grid);
        if (move != null) {
            setSelectedLetter(random.nextBoolean() ? 'S' : 'O'); // Randomly choose between S and O
            return move;
        }
        
        setSelectedLetter(random.nextBoolean() ? 'S' : 'O');
        return makeRandomMove(emptyButtons);
    }

    private JButton makeRandomMove(List<JButton> emptyButtons) {
        return emptyButtons.get(random.nextInt(emptyButtons.size()));
    }

    private JButton findStrategicMove(JButton[][] grid) {
        int gridSize = grid.length;
        
        int center = gridSize / 2;
        if (gridSize % 2 == 1 && grid[center][center].getText().isEmpty()) {
            return grid[center][center];
        }
        
        int[][] corners = {{0, 0}, {0, gridSize-1}, {gridSize-1, 0}, {gridSize-1, gridSize-1}};
        for (int[] corner : corners) {
            if (grid[corner[0]][corner[1]].getText().isEmpty()) {
                return grid[corner[0]][corner[1]];
            }
        }
        
        for (int i = 1; i < gridSize-1; i++) {
            if (grid[0][i].getText().isEmpty()) return grid[0][i];
            if (grid[i][0].getText().isEmpty()) return grid[i][0];
            if (grid[gridSize-1][i].getText().isEmpty()) return grid[gridSize-1][i];
            if (grid[i][gridSize-1].getText().isEmpty()) return grid[i][gridSize-1];
        }
        
        return null;
    }

    private List<JButton> getEmptyButtons(JButton[][] grid) {
        List<JButton> emptyButtons = new ArrayList<>();
        for (JButton[] row : grid) {
            for (JButton button : row) {
                if (button.getText().isEmpty()) {
                    emptyButtons.add(button);
                }
            }
        }
        return emptyButtons;
    }

  
    private JButton findMoveToCompleteSOS(Board board, JButton[][] grid, char letter) {
        SOSGameLogic gameLogic = new SOSGameLogic(board);
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                JButton button = grid[i][j];
                if (button.getText().isEmpty()) {
                    button.setText(String.valueOf(letter));
                    boolean formsSOS = gameLogic.checkForSOS(button);
                    button.setText("");
                    if (formsSOS) return button;
                }
            }
        }
        return null;
    }

   
    @Override
    public String toString() {
        return color;
    }
}