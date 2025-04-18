package Sprint4;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameController {
    private Board board;
    private GameType currentGame;
    private Player bluePlayer;
    private Player redPlayer;
    private JPanel gridPanel;
    private JTextField gridSizeField;
    private JLabel turnLabel;
    private JLabel scoreLabel;
    
    private boolean isSimpleGame = true;
    private boolean isGameInProgress = false;
    private Timer computerMoveTimer;
    
    private boolean blueIsComputer = false;
    private boolean redIsComputer = false;
    
    public GameController(JPanel gridPanel, JTextField gridSizeField, JLabel turnLabel, JLabel scoreLabel,
                          JRadioButton blueS, JRadioButton blueO, JRadioButton redS, JRadioButton redO,
                          JComboBox<String> bluePlayerType, JComboBox<String> redPlayerType) {
        this.gridPanel = gridPanel;
        this.gridSizeField = gridSizeField;
        this.turnLabel = turnLabel;
        this.scoreLabel = scoreLabel;
        
        int defaultGridSize = 3;
        this.board = new Board(gridPanel, defaultGridSize);
        
        this.bluePlayer = new HumanPlayer("Blue Player", "Blue", blueS, blueO);
        this.redPlayer = new HumanPlayer("Red Player", "Red", redS, redO);
        
        blueS.setSelected(true);
        redO.setSelected(true);
        
        this.currentGame = new SimpleGame(board, bluePlayer, redPlayer, turnLabel);
        
        addPlayerSelectionListeners(blueS, blueO, redS, redO);
        
        addPlayerTypeListeners(bluePlayerType, redPlayerType, blueS, blueO, redS, redO);
        
        addButtonListeners();
        
        computerMoveTimer = new Timer(500, e -> {
            makeComputerMove();
        });
        computerMoveTimer.setRepeats(false);
    }
    
    private void addPlayerTypeListeners(JComboBox<String> bluePlayerType, JComboBox<String> redPlayerType,
                                       JRadioButton blueS, JRadioButton blueO, JRadioButton redS, JRadioButton redO) {
        bluePlayerType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) bluePlayerType.getSelectedItem();
                blueIsComputer = !selectedType.equals("Human");
                
                updatePlayers(blueS, blueO, redS, redO);
            }
        });
        
        redPlayerType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) redPlayerType.getSelectedItem();
                redIsComputer = !selectedType.equals("Human");
                
                updatePlayers(blueS, blueO, redS, redO);
            }
        });
    }

    private void updatePlayers(JRadioButton blueS, JRadioButton blueO, JRadioButton redS, JRadioButton redO) {
        // Store current letter selections
        char blueSelectedLetter = bluePlayer.getSelectedLetter();
        char redSelectedLetter = redPlayer.getSelectedLetter();
        
        if (blueIsComputer) {
            bluePlayer = new ComputerPlayer("Blue Computer", "Blue", blueS, blueO);
        } else {
            bluePlayer = new HumanPlayer("Blue Player", "Blue", blueS, blueO);
        }
        bluePlayer.setSelectedLetter(blueSelectedLetter);
        
        if (redIsComputer) {
            redPlayer = new ComputerPlayer("Red Computer", "Red", redS, redO);
        } else {
            redPlayer = new HumanPlayer("Red Player", "Red", redS, redO);
        }
        redPlayer.setSelectedLetter(redSelectedLetter);
        
        createGameInstance();
        
        if (isGameInProgress) {
            checkForComputerTurn();
        }
    }

    private void addPlayerSelectionListeners(JRadioButton blueS, JRadioButton blueO, 
                                           JRadioButton redS, JRadioButton redO) {
        ActionListener blueListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTurnLabel();
            }
        };
        
        ActionListener redListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTurnLabel();
            }
        };
        
        blueS.addActionListener(blueListener);
        blueO.addActionListener(blueListener);
        redS.addActionListener(redListener);
        redO.addActionListener(redListener);
    }
    

    private void updateTurnLabel() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            String playerColor = (currentPlayer == bluePlayer) ? "Blue" : "Red";
            char playerLetter = currentPlayer.getSelectedLetter();
            String playerType = (currentPlayer instanceof ComputerPlayer) ? " (Computer)" : "";
            
            turnLabel.setText("Current Turn: " + playerColor + playerType + " (" + playerLetter + ")");
        }
    }
    
    public void makeMove(JButton button) {
        if (!isGameInProgress) {
            isGameInProgress = true;
        }
        
        if (button.getText().isEmpty()) {
            boolean gameOver = currentGame.processMove(button);
            
            if (gameOver) {
                showGameOverMessage();
                isGameInProgress = false;
            } else {
                updateTurnLabel();
                checkForComputerTurn();
            }
        }
    }
    
    private void makeComputerMove() {
        if (!isGameInProgress || computerMoveTimer.isRunning()) return;

        Player currentPlayer = getCurrentPlayer();
        if (!(currentPlayer instanceof ComputerPlayer)) return;

        computerMoveTimer.stop(); 
        
        Timer moveTimer = new Timer(300, e -> {
            ComputerPlayer computerPlayer = (ComputerPlayer) currentPlayer;
            JButton selectedButton = computerPlayer.makeAutomatedMove(board);
            
            if (selectedButton != null) {
                boolean gameOver = currentGame.processMove(selectedButton);
                
                if (gameOver) {
                    showGameOverMessage();
                    isGameInProgress = false;
                    return;
                }
                
                updateTurnLabel();
                
                if (currentGame instanceof GeneralGame) {
                    GeneralGame generalGame = (GeneralGame) currentGame;
                    if (generalGame.wasSosFormedLastTurn() && 
                        getCurrentPlayer() instanceof ComputerPlayer) {
                        makeComputerMove(); 
                        return;
                    }
                }
                
                if (getCurrentPlayer() instanceof ComputerPlayer) {
                    Timer nextMoveTimer = new Timer(500, ev -> makeComputerMove());
                    nextMoveTimer.setRepeats(false);
                    nextMoveTimer.start();
                } else {
                    enableHumanInput(true);
                }
            }
        });
        moveTimer.setRepeats(false);
        moveTimer.start();
    }

    private void enableHumanInput(boolean enable) {
        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(enable);
            }
        }
    }
    
    private void checkForComputerTurn() {
        if (computerMoveTimer.isRunning()) {
            computerMoveTimer.stop();
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer instanceof ComputerPlayer && isGameInProgress) {
            computerMoveTimer.start();
        }
    }

    public void resetGame() {
        if (currentGame != null) {
            currentGame.resetGame();
            addButtonListeners();
            updateTurnLabel();
            isGameInProgress = true; 
            
            if (computerMoveTimer.isRunning()) {
                computerMoveTimer.stop();
            }
            
            Timer startupDelay = new Timer(500, e -> {
                Player currentPlayer = getCurrentPlayer();
                if (currentPlayer instanceof ComputerPlayer) {
                    makeComputerMove();
                }
            });
            startupDelay.setRepeats(false);
            startupDelay.start();
        }
    }
    

    private Player getCurrentPlayer() {
        if (currentGame instanceof SimpleGame) {
            return ((SimpleGame) currentGame).getCurrentPlayer();
        } else if (currentGame instanceof GeneralGame) {
            return ((GeneralGame) currentGame).getCurrentPlayer();
        }
        return null;
    }

    public void createNewGameBoard() {
        try {
            int gridSize = Integer.parseInt(gridSizeField.getText());
            
            if (gridSize > 16) {
                JOptionPane.showMessageDialog(gridPanel,
                    "Please enter a grid size between 3 and 16.\nThe grid size has been set to 16.",
                    "Invalid Grid Size", JOptionPane.WARNING_MESSAGE);
                gridSize = 16;
            } else if (gridSize < 3) {
                JOptionPane.showMessageDialog(gridPanel,
                    "Please enter a grid size between 3 and 16.\nThe grid size has been set to 3.",
                    "Invalid Grid Size", JOptionPane.WARNING_MESSAGE);
                gridSize = 3;
            }
            
            board = new Board(gridPanel, gridSize);
            
            createGameInstance();
            
            addButtonListeners();
            
            updateTurnLabel();
            
            isGameInProgress = true;
            
            if (blueIsComputer) {
                Timer startupDelay = new Timer(500, e -> makeComputerMove());
                startupDelay.setRepeats(false);
                startupDelay.start();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gridPanel, "Please enter a valid integer.");
        }
    }
    
    public void setGameMode(boolean isSimpleGame) {
        if (this.isSimpleGame != isSimpleGame) {
            this.isSimpleGame = isSimpleGame;
            createGameInstance();
            addButtonListeners();
            updateTurnLabel();
            isGameInProgress = false;
        }
    }
    
 
    private void createGameInstance() {
        if (isSimpleGame) {
            currentGame = new SimpleGame(board, bluePlayer, redPlayer, turnLabel);
            scoreLabel.setVisible(false);
        } else {
            currentGame = new GeneralGame(board, bluePlayer, redPlayer, turnLabel, scoreLabel);
            scoreLabel.setVisible(true);
        }
    }
    

    private void addButtonListeners() {
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                
                // Remove existing listeners
                for (ActionListener listener : button.getActionListeners()) {
                    button.removeActionListener(listener);
                }
                
                // Add new listener
                button.addActionListener(e -> {
                    Player current = getCurrentPlayer();
                    if (!(current instanceof ComputerPlayer)) {
                        makeMove(button);
                    }
                });
            }
        }
    }
    
    private void showGameOverMessage() {
        JOptionPane.showMessageDialog(gridPanel, currentGame.getGameOverMessage());
        resetGame();
    }
}