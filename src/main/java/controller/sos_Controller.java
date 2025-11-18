package controller;

import model.sos_Model;
import model.sos_computerSM;
import model.sos_computerGM;
import view.sos_View;
import javax.swing.*;
import java.awt.event.*;



/**
 * Main controller for the SOS game
 * Handles all the interacshuns between the view and model
 */
public class sos_Controller {
    // Stores the game logic
    final private sos_Model model;
    // Stores the game view
    final private sos_View view;
    
    // Computer AI instances
    private sos_computerSM computerSM;
    private sos_computerGM computerGM;
    private Timer computerMoveTimer;

    /**
     * Creates new controller for the game
     * @param model the game logic
     * @param view the game view
     */
    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;
        
        // Initialize computer AI
        this.computerSM = new sos_computerSM(model);
        this.computerGM = new sos_computerGM(model);

        initialzeGame();
        initializeListeners();
    }

    /**
     * Sets up the initial game state
     * Makes the board and sets everything up
     */
    private void initialzeGame(){
        // set default board size to 3x3
        model.setSize(3);
        view.setBoardSize(3);

        // start with simple mode
        model.mode(sos_Model.Mode.Simple);

        // make the game bord empty
        model.initialzeBoard();

        // create the visual board buttons
        createViewBoard(3);

        // show the empty board
        updateBoardDisplay();
    }

    /**
     * Updates what is shown on the board
     * Makes all the buttons show S, O, or nothing
     */
    private void updateBoardDisplay(){
        // get the current game state
        sos_Model.Cell[][] board = model.getBoard();
        // get the buttons to update
        JButton[][] boardButtons = view.getBoardButton();

        // only update if we have both arrays
        if(board != null && boardButtons != null){
            // Update board cells
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    switch(board[i][j]) {
                        case S:
                            boardButtons[i][j].setText("S");
                            break;
                        case O:
                            boardButtons[i][j].setText("O");
                            break;
                        default:
                            boardButtons[i][j].setText("");
                            break;
                    }
                }
            }
        }
    }
   
    /**
     * Makes the bord with buttuns
     * @param size how big the bord shud be
     */
    private void createViewBoard(int size){
        view.createBoard(size);
    }

    /**
     * Puts the click handelrs on the bord buttuns
     * Makes sure playrs can click the bord
     */
    private void attachBoardButtonListeners() {
        JButton[][] boardButtons = view.getBoardButton();
        for(int i = 0; i < boardButtons.length; i++) {
            for(int j = 0; j < boardButtons[i].length; j++) {
                final int row = i;
                final int column = j;
                // Remove any existing listeners to prevent duplicates
                for(ActionListener al : boardButtons[i][j].getActionListeners()) {
                    boardButtons[i][j].removeActionListener(al);
                }
                boardButtons[i][j].addActionListener(e -> handleCellClick(row, column));
            }
        }
    }

    /**
     * Sets up all the click handelrs
     * Makes buttons do stuff wen clicked
     */
    private void initializeListeners() {
        // make new game buttun work
        view.getNewGameButton().addActionListener(e -> handleNewGame());

        // make mode pikker work
        view.getRbSimple().addActionListener(e -> handleModeChange(sos_Model.Mode.Simple));
        view.getRbGeneral().addActionListener(e -> handleModeChange(sos_Model.Mode.General));

        // make bord buttuns work
        attachBoardButtonListeners();
    }

    /**
     * Starts a new game with the chosen size
     * Resets everything and makes new board
     */
    private void handleNewGame() {
        try {
            // get what size the player wants
            int size = view.getBoardsize();
            if(size >= 3) {
                // make new board with that size
                model.setSize(size);
                model.resetGame();
                createViewBoard(size);
                attachBoardButtonListeners(); // add click handlers to new buttons
                updateBoardDisplay();
                // Clear any existing lines
                view.updateLines(model.getSOSLines());
                updateScores();
                // make sure the board looks right
                view.getBoardPanel().revalidate();
                view.getBoardPanel().repaint();
                
                // Reinitialize computer AI for new game
                computerSM = new sos_computerSM(model);
                computerGM = new sos_computerGM(model);
                
                // Start computer move if Player 1 is computer
                triggerComputerMoveIfNeeded();
            } else {
                // tell user they need bigger size
                JOptionPane.showMessageDialog(view, "Board size must be at least 3", "Invalid Size", JOptionPane.ERROR_MESSAGE);
                view.setBoardSize(5); // reset to default size
            }
        } catch (NumberFormatException e) {
            // tell user they need valid number
            JOptionPane.showMessageDialog(view, "Please enter a valid number for board size", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            view.setBoardSize(5); // reset to default size
        }
    }

    /**
     * Changes the game mode wen user clicks
     * @param newMode which mode they pikked
     */
    private void handleModeChange(sos_Model.Mode newMode) {
        model.setMode(newMode);
    }

    /**
     * Handles when player clicks a cell on board
     * Puts S or O based on what they picked
     * @param row which row they clicked
     * @param column which column they clicked
     */
    private void handleCellClick(int row, int column) {
        // Check if current player is computer - ignore human clicks
        sos_Model.Player currentPlayer = model.getCurrentPlayer();
        if ((currentPlayer == sos_Model.Player.Player1 && view.getCbPlayer1Computer().isSelected()) ||
            (currentPlayer == sos_Model.Player.Player2 && view.getCbPlayer2Computer().isSelected())) {
            return; // Ignore clicks when it's computer's turn
        }
        
        // only do something if cell is empty and game is not over
        if(model.cellEmpty(row, column) && !model.isGameOver()) {
            char letter;
            
            // get what letter they want (S or O)
            if(currentPlayer == sos_Model.Player.Player1) {
                letter = view.getRbPlayer1S().isSelected() ? 'S' : 'O';
            } else {
                letter = view.getRbPlayer2S().isSelected() ? 'S' : 'O';
            }

            // try to make the move
            if(model.move(row, column, letter)) {
                // Update the board display first
                updateBoardDisplay();
                // Force an update of the lines with latest state
                SwingUtilities.invokeLater(() -> {
                    view.updateLines(model.getSOSLines());
                    updateScores();
                    view.revalidate();
                    view.repaint();
                });
                
                if (model.isGameOver()) {
                    handleGameOver();
                } else {
                    // Check if next player is computer
                    triggerComputerMoveIfNeeded();
                }
            }
        }
    }

    private void updateScores() {
        view.updateScore(1, model.getPlayer1Score());
        view.updateScore(2, model.getPlayer2Score());
    }

    private void handleGameOver() {
        String message;
        sos_Model.Player winner = model.getWinner();
        
        if (model.isSimpleGameMode()) {
            // Simple game: first SOS wins
            if (winner != null) {
                message = (winner == sos_Model.Player.Player1) ? 
                    "Player 1 wins by forming SOS!" : 
                    "Player 2 wins by forming SOS!";
            } else {
                message = "Game is a draw - no SOS formed!";
            }
        } else {
            // General game: highest score wins
            int p1Score = model.getPlayer1Score();
            int p2Score = model.getPlayer2Score();
            
            if (p1Score > p2Score) {
                message = "Player 1 wins with score " + p1Score + "!";
            } else if (p2Score > p1Score) {
                message = "Player 2 wins with score " + p2Score + "!";
            } else {
                message = "It's a draw! Both players scored " + p1Score + " points.";
            }
        }
        
        JOptionPane.showMessageDialog(view, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Checks if current player is computer and triggers their move
     */
    private void triggerComputerMoveIfNeeded() {
        sos_Model.Player currentPlayer = model.getCurrentPlayer();
        boolean isComputerTurn = (currentPlayer == sos_Model.Player.Player1 && view.getCbPlayer1Computer().isSelected()) ||
                                 (currentPlayer == sos_Model.Player.Player2 && view.getCbPlayer2Computer().isSelected());
        
        if (isComputerTurn && !model.isGameOver()) {
            // Add delay so user can see what's happening
            computerMoveTimer = new Timer(800, e -> {
                makeComputerMove();
                ((Timer)e.getSource()).stop();
            });
            computerMoveTimer.setRepeats(false);
            computerMoveTimer.start();
        }
    }
    
    /**
     * Makes the computer player move
     */
    private void makeComputerMove() {
        if (model.isGameOver()) {
            return;
        }
        
        // Get the appropriate computer AI based on game mode
        int[] move;
        if (model.isSimpleGameMode()) {
            move = computerSM.findBestMove();
        } else {
            move = computerGM.findBestMove();
        }
        
        if (move != null) {
            int row = move[0];
            int col = move[1];
            char letter = move[2] == 1 ? 'S' : 'O';
            
            // Make the move
            if (model.move(row, col, letter)) {
                // Update display
                updateBoardDisplay();
                SwingUtilities.invokeLater(() -> {
                    view.updateLines(model.getSOSLines());
                    updateScores();
                    view.revalidate();
                    view.repaint();
                });
                
                // Check if game is over
                if (model.isGameOver()) {
                    handleGameOver();
                } else {
                    // Check if next player is also computer
                    triggerComputerMoveIfNeeded();
                }
            }
        }
    }
}
