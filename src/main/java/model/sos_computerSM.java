package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Computer AI strategy for Simple Mode
 * Focuses on forming one SOS to win the game
 */
public class sos_computerSM {
    final private sos_Model model;
    
    public sos_computerSM(sos_Model model) {
        this.model = model;
    }
    
    /**
     * Finds the best move for Simple Mode
     * Prioritizes winning moves, then blocking opponent
     */
    public int[] findBestMove() {
        int boardSize = model.getBoard().length;
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (model.cellEmpty(row, col)) {
                    for (char letter : new char[]{'S', 'O'}) {
                        if (isWinningMove(row, col, letter)) {
                            return new int[]{row, col, letter == 'S' ? 1 : 2};
                        }
                    }
                }
            }
        }
        return null;
    }
    public boolean isWinningMove(int row, int col, char letter) {
        return false;
    }
    
    /**
     * Checks if opponent can win on their next turn
     */
    public int[] findOpponentWinningMove() {
        return null;
    }
    
    /**
     * Evaluates move for Simple Mode (win or block focus)
     */
    public int evaluateMove(int row, int col, char letter) {
        return 0;
    }
      
}
