package Sprint4;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class Player {
    protected String name;
    protected char selectedLetter; 
    private int sosCount;
    private JRadioButton sButton;
    private JRadioButton oButton;

    public Player(String name, JRadioButton sButton, JRadioButton oButton) {
        this.name = name;
        this.sButton = sButton;
        this.oButton = oButton;
        this.selectedLetter = sButton.isSelected() ? 'S' : 'O';
        this.sosCount = 0;
        addRadioButtonListeners();
    }
    
    public JButton makeAutomatedMove(Board board) {
        return null; 
    }

    protected void setSelectedLetterDirect(char letter) {
        if (letter == 'S' || letter == 'O') {
            this.selectedLetter = letter;
        }
    }

    private void addRadioButtonListeners() {
        for (ActionListener al : sButton.getActionListeners()) {
            sButton.removeActionListener(al);
        }
        for (ActionListener al : oButton.getActionListeners()) {
            oButton.removeActionListener(al);
        }
        
        sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedLetter = 'S';
            }
        });
        
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedLetter = 'O';
            }
        });
    }
    
    public String getName() {
        return name;
    }
    
    

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char letter) {
        if (letter == 'S' || letter == 'O') {
            this.selectedLetter = letter;
            if (!(this instanceof ComputerPlayer)) {
                if (letter == 'S') {
                    sButton.setSelected(true);
                } else {
                    oButton.setSelected(true);
                }
            }
        }
    }
    
    public void incrementSOSCount(int count) {
        sosCount += count;
    }

    public void incrementSOSCount() {
        incrementSOSCount(1);
    }
    public int getSOSCount() {
        return sosCount;
    }
    
    public void resetSOSCount() {
        sosCount = 0;
    }
    
    public JRadioButton getSButton() {
        return sButton;
    }
    
    public JRadioButton getOButton() {
        return oButton;
    }
    
    public void initializeButtons() {
        if (selectedLetter == 'S') {
            sButton.setSelected(true);
        } else {
            oButton.setSelected(true);
        }
    }
    public abstract void makeMove(Board board, int row, int col);
}