package controller;

import model.sos_Model;
import view.sos_View;
import javax.swing.*;
import java.awt.event.*;



/**
 * Main controller for the SOS game
 * Handles all the interacshuns between the view and model
 */
public class sos_Controller {
    // Stores the game logic
    private sos_Model model;
    // Stores the game view
    private sos_View view;

    /**
     * Creates new controller for the game
     * @param model the game logic
     * @param view the game view
     */
    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;

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
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    if(board[i][j] == sos_Model.Cell.S){
                        boardButtons[i][j].setText("S");
                    }
                    else if(board[i][j] == sos_Model.Cell.O){
                        boardButtons[i][j].setText("O");
                    }
                    else{
                        boardButtons[i][j].setText("");
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
                // make sure the board looks rite
                view.getBoardPanel().revalidate();
                view.getBoardPanel().repaint();
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
        // only do sumthing if cell is empty
        if(model.cellEmpty(row, column)) {
            // see whos turn it is
            sos_Model.Player currentPlayer = model.getCurrentPlayer();
            char letter;
            
            // get wut letter they want (S or O)
            if(currentPlayer == sos_Model.Player.Player1) {
                letter = view.getRbPlayer1S().isSelected() ? 'S' : 'O';
            } else {
                letter = view.getRbPlayer2S().isSelected() ? 'S' : 'O';
            }

            // try to make the move
            if(model.move(row, column, letter)) {
                updateBoardDisplay();
            }
        }
    }
}
