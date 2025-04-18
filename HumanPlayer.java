package Sprint4;

import javax.swing.JButton;
import javax.swing.JRadioButton;

public class HumanPlayer extends Player {
    private String color;
    
    public HumanPlayer(String name, String color, JRadioButton sButton, JRadioButton oButton) {
        super(name, sButton, oButton);
        this.color = color;
        
        if (color.equals("Blue")) {
            setSelectedLetter('S');
        } else {
            setSelectedLetter('O');
        }
    }

    @Override
    public JButton makeAutomatedMove(Board board) {
        return null;
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

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return color;
    }
}