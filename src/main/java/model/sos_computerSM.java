package model;

/**
 * Simple Mode computer player
 * Focuses on winning moves or blocking opponent
 */
public class sos_computerSM extends sos_computer {
    
    public sos_computerSM(sos_Model model) {
        super(model);
    }
    
    /**
     * Finds the best move for Simple Mode
     * Prioritizes winning moves, then blocking opponent
     */
    @Override
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
        
        int[] blockMove = findOpponentWinningMove();
        if (blockMove != null) {
            return blockMove;
        }
        
        int[] bestMove = null;
        int bestScore = -1;
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (model.cellEmpty(row, col)) {
                    for (char letter : new char[]{'S', 'O'}) {
                        int score = evaluateMove(row, col, letter);
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[]{row, col, letter == 'S' ? 1 : 2};
                        }
                    }
                }
            }
        }
        
        if (bestMove == null) {
            bestMove = findRandomMove();
        }
        
        return bestMove;
    }
    /**
     * Checks if placing a letter at this position creates an SOS (winning move in Simple Mode)
     */
    public boolean isWinningMove(int row, int col, char letter) {
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell originalCell = board[row][col];
        
        // Temporarily place the letter
        board[row][col] = (letter == 'S') ? sos_Model.Cell.S : sos_Model.Cell.O;
        
        // Check if this creates an SOS
        int sosCount = countSOSAtPosition(row, col);
        
        // Restore original state
        board[row][col] = originalCell;
        
        return sosCount > 0;
    }
    
    /**
     * Checks if opponent can win on their next turn and returns blocking move
     */
    public int[] findOpponentWinningMove() {
        int boardSize = model.getBoard().length;
        
        // Check all possible opponent moves
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (model.cellEmpty(row, col)) {
                    // Try both letters for opponent
                    for (char letter : new char[]{'S', 'O'}) {
                        if (isWinningMove(row, col, letter)) {
                            // Block this winning move
                            return new int[]{row, col, letter == 'S' ? 1 : 2};
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Evaluates move for Simple Mode (win or block focus)
     */
    @Override
    public int evaluateMove(int row, int col, char letter) {
        int score = 0;
        
        // Prefer center positions
        int boardSize = model.getBoard().length;
        int centerRow = boardSize / 2;
        int centerCol = boardSize / 2;
        int distanceFromCenter = Math.abs(row - centerRow) + Math.abs(col - centerCol);
        score += (boardSize - distanceFromCenter) * 5;
        
        // Check setup potential (positions that could lead to SOS)
        score += checkSetupPotential(row, col, letter);
        
        // Add small random factor to avoid predictability (unless in test mode)
        if (!testMode) {
            score += (int)(Math.random() * 3);
        }
        
        return score;
    }
    
    /**
     * Checks potential for setting up future SOS formations
     */
    private int checkSetupPotential(int row, int col, char letter) {
        sos_Model.Cell[][] board = model.getBoard();
        int size = board.length;
        
        // Count nearby pieces that could form SOS
        int nearbyS = 0;
        int nearbyO = 0;
        
        // Check adjacent cells (within distance 2)
        for (int dr = -2; dr <= 2; dr++) {
            for (int dc = -2; dc <= 2; dc++) {
                if (dr == 0 && dc == 0) continue;
                
                int newRow = row + dr;
                int newCol = col + dc;
                
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                    if (board[newRow][newCol] == sos_Model.Cell.S) {
                        nearbyS++;
                    } else if (board[newRow][newCol] == sos_Model.Cell.O) {
                        nearbyO++;
                    }
                }
            }
        }
        
        // S is valuable near O, O is valuable near S
        if (letter == 'S') {
            return nearbyO * 3;
        } else { // letter == 'O'
            return nearbyS * 3;
        }
    }
    
}
