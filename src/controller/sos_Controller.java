package controller;

import model.sos_Model;
import view.sos_View;
import javax.swing.*;
import java.awt.event.*;



public class sos_Controller {
    private sos_Model model;
    private sos_View view;

    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;

        initialzeGame();
        initializeListeners();
    }

    private void initialzeGame(){
        // the default of the baord to 3
        model.setSize(3);
        view.setBoardSize(3);

        model.mode(sos_Model.Mode.Simple);

        model.initialzeBoard();

        createViewBoard(3);

        updateBoardDisplay();
        
    }

    private void updateBoardDisplay(){
        sos_Model.Cell[][] board = model.getBoard();
        JButton[][] boardButtons = view.getBoardButton();

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
   
    private void createViewBoard(int size){
        view.createBoard(size);
    }

    private void attachBoardButtonListeners() {
        JButton[][] boardButtons = view.getBoardButton();
        for(int i = 0; i < boardButtons.length; i++) {
            for(int j = 0; j < boardButtons[i].length; j++) {
                final int row = i;
                final int col = j;
                // Remove any existing listeners to prevent duplicates
                for(ActionListener al : boardButtons[i][j].getActionListeners()) {
                    boardButtons[i][j].removeActionListener(al);
                }
                boardButtons[i][j].addActionListener(e -> handleCellClick(row, col));
            }
        }
    }

    private void initializeListeners() {
        // Add listener for new game button
        view.getNewGameButton().addActionListener(e -> handleNewGame());

        // Add listeners for mode selection
        view.getRbSimple().addActionListener(e -> handleModeChange(sos_Model.Mode.Simple));
        view.getRbGeneral().addActionListener(e -> handleModeChange(sos_Model.Mode.General));

        // Add listeners for board buttons
        attachBoardButtonListeners();
    }

    private void handleNewGame() {
        try {
            int size = view.getBoardsize();
            if(size >= 3) {
                model.setSize(size);
                model.resetGame();
                createViewBoard(size);
                attachBoardButtonListeners(); // Re-attach listeners to new buttons
                updateBoardDisplay();
                view.getBoardPanel().revalidate();
                view.getBoardPanel().repaint();
            } else {
                JOptionPane.showMessageDialog(view, "Board size must be at least 3", "Invalid Size", JOptionPane.ERROR_MESSAGE);
                view.setBoardSize(5); // Reset to default size
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Please enter a valid number for board size", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            view.setBoardSize(5); // Reset to default size
        }
    }

    private void handleModeChange(sos_Model.Mode newMode) {
        model.setMode(newMode);
    }

    private void handleCellClick(int row, int col) {
        if(model.cellEmpty(row, col)) {
            sos_Model.Player currentPlayer = model.getCurrentPlayer();
            char letter;
            
            if(currentPlayer == sos_Model.Player.Player1) {
                letter = view.getRbPlayer1S().isSelected() ? 'S' : 'O';
            } else {
                letter = view.getRbPlayer2S().isSelected() ? 'S' : 'O';
            }

            if(model.move(row, col, letter)) {
                updateBoardDisplay();
            }
        }
    }
}
