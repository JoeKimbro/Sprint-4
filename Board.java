package Sprint4;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Board {
    private JPanel gridPanel;
    private int gridSize;
    
    public Board(JPanel gridPanel, int gridSize) {
        this.gridPanel = gridPanel;
        this.gridSize = gridSize;
        createGrid();
    }

    public void createGrid() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize * gridSize; i++) {
            JButton button = new JButton("");
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setFocusable(false);
            gridPanel.add(button);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }
    
    public int getGridSize() {
        return gridSize;
    }
    
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void clearBoard() {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setText("");
            }
        }
    }
    
 
    public JButton[][] getButtonGrid() {
        Component[] components = gridPanel.getComponents();
        int gridSize = (int) Math.sqrt(components.length);
        JButton[][] grid = new JButton[gridSize][gridSize];
        
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = (JButton) components[i * gridSize + j];
            }
        }
        
        return grid;
    }
 
    public boolean isFull() {
        Component[] components = gridPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
