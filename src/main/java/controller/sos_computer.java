package controller;

import model.sos_Model;

public class sos_computer {
    
    /**
     * Finds the best move for the computer player
     */
    public int[] findBestMove(sos_Model model) {
        return null;
    }
    
    /**
     * Evaluates how good a potential move is
     */
    public int evaluateMove(sos_Model model, int row, int col, char letter) {
        return 0;
    }
    
    /**
     * Counts how many SOS formations exist at a position
     */
    public int countSOSAtPosition(sos_Model model, int row, int col) {
        return 0;
    }
    
    /**
     * Checks if a move blocks opponent from scoring
     */
    public int checkBlockingPotential(sos_Model model, int row, int col, char letter) {
        return 0;
    }
    
    /**
     * Finds a random valid move as fallback
     */
    public int[] findRandomMove(sos_Model model) {
        return null;
    }
    
}
