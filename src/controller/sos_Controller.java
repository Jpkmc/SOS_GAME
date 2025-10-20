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
        //initialzeListners();

        
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
                        boardButtons[i][j].setText("null");
                    }
                }
            }
        }
    }
   
    private void createViewBoard(int size){
        view.createBoard(size);
    }
}
