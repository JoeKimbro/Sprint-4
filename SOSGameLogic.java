package Sprint4;

import javax.swing.JButton;

public class SOSGameLogic {
    private Board board;
    
    public SOSGameLogic(Board board) {
        this.board = board;
    }
    public int checkForSOSCount(JButton changedButton) {
        JButton[][] gridButtons = board.getButtonGrid();
        int[] position = findButtonPosition(gridButtons, changedButton);
        
        if (position == null) return 0;
        
        int row = position[0];
        int col = position[1];
        int sosCount = 0;
        
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            if (checkSOSInDirection(gridButtons, row, col, dir[0], dir[1])) {
                sosCount++;
            }
        }
        
        return sosCount;
    }

    public boolean checkForSOS(JButton changedButton) {
        return checkForSOSCount(changedButton) > 0;
    }
    

    private int[] findButtonPosition(JButton[][] grid, JButton button) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == button) {
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }
    

    public boolean checkSOSInDirection(JButton[][] grid, int changedRow, int changedCol, int dRow, int dCol) {
        int gridSize = grid.length;
        
        int startRow = changedRow - 2*dRow;
        int startCol = changedCol - 2*dCol;
        
        if (isInBounds(startRow, startCol, gridSize) &&
            isInBounds(changedRow - dRow, changedCol - dCol, gridSize)) {
            
            String firstLetter = grid[startRow][startCol].getText();
            String secondLetter = grid[changedRow - dRow][changedCol - dCol].getText();
            String thirdLetter = grid[changedRow][changedCol].getText();
            
            if (firstLetter.equals("S") && 
                secondLetter.equals("O") && 
                thirdLetter.equals("S")) {
                return true;
            }
        }
        
        int leftRow = changedRow - dRow;
        int leftCol = changedCol - dCol;
        int rightRow = changedRow + dRow;
        int rightCol = changedCol + dCol;
        
        if (isInBounds(leftRow, leftCol, gridSize) &&
            isInBounds(rightRow, rightCol, gridSize)) {
            
            String leftLetter = grid[leftRow][leftCol].getText();
            String middleLetter = grid[changedRow][changedCol].getText();
            String rightLetter = grid[rightRow][rightCol].getText();
            
            if (leftLetter.equals("S") && 
                middleLetter.equals("O") && 
                rightLetter.equals("S")) {
                return true;
            }
        }
        
        return false;
    }
    

    private boolean isInBounds(int row, int col, int gridSize) {
        return row >= 0 && row < gridSize && col >= 0 && col < gridSize;
    }
}