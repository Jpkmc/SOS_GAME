package model;

/**
 * Computer AI strategy for Simple Mode
 * Focuses on forming one SOS to win the game
 */
public class sos_computerSM {
    private sos_Model model;
    
    public sos_computerSM(sos_Model model) {
        this.model = model;
    }
    
    /**
     * Finds the best move for Simple Mode
     * Prioritizes winning moves, then blocking opponent
     */
    public int[] findBestMove() {
        return null;
    }
    
    /**
     * Checks if this move wins the game immediately
     */
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
