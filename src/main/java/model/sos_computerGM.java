package model;

/**
 * General Mode computer player
 * Focuses on maximizing multiple SOS formations
 */
public class sos_computerGM extends sos_computer {
    
    public sos_computerGM(sos_Model model) {
        super(model);
    }
    
    /**
     * Finds the best move for General Mode
     * Prioritizes moves that create multiple SOS formations
     */
    @Override
    public int[] findBestMove() {
        int boardSize = model.getBoard().length;
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
     * Evaluates move with General Mode scoring
     */
    @Override
    protected int evaluateMove(int row, int col, char letter) {
        int score = 0;
        
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell originalCell = board[row][col];
        board[row][col] = (letter == 'S') ? sos_Model.Cell.S : sos_Model.Cell.O;
        
        int sosCount = countSOSAtPosition(row, col);
        
        board[row][col] = originalCell;
        
        if (sosCount > 0) {
            score = 150 * sosCount;
            score += checkChainPotential(row, col) * 20;
        } else {
            score += checkBlockingPotential(row, col, letter);
            score += checkSetupPotential(row, col, letter);
            
            int boardSize = board.length;
            int centerDist = Math.abs(row - boardSize/2) + Math.abs(col - boardSize/2);
            score += (boardSize - centerDist) * 3;
            
            // Add randomness only when not in test mode
            if (!testMode) {
                score += (int)(Math.random() * 5);
            }
        }
        
        return score;
    }
    
    /**
     * Checks for chain opportunities (multiple SOS in sequence)
     */
    public int checkChainPotential(int row, int col) {
        int chainScore = 0;
        sos_Model.Cell[][] board = model.getBoard();
        int size = board.length;
        
        int[][] directions = {{0,1}, {1,0}, {1,1}, {1,-1}};
        
        for (int[] dir : directions) {
            int adjacentS = 0;
            int adjacentO = 0;
            
            for (int dist = 1; dist <= 2; dist++) {
                int newRow = row + dir[0] * dist;
                int newCol = col + dir[1] * dist; 
                
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                    if (board[newRow][newCol] == sos_Model.Cell.S) adjacentS++;
                    if (board[newRow][newCol] == sos_Model.Cell.O) adjacentO++;
                }
                
                newRow = row - dir[0] * dist;
                newCol = col - dir[1] * dist;
                
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                    if (board[newRow][newCol] == sos_Model.Cell.S) adjacentS++;
                    if (board[newRow][newCol] == sos_Model.Cell.O) adjacentO++;
                }
            }
            
            chainScore += (adjacentS + adjacentO);
        }
        
        return chainScore;
    }
    
    /**
     * Analyzes board for strategic position control
     */
    public int analyzePositionControl() {
        sos_Model.Cell[][] board = model.getBoard();
        int size = board.length;
        
        int myCells = 0;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != sos_Model.Cell.EMPTY) {
                    myCells++;
                }
            }
        }
        
        return (myCells * 100) / Math.max(1, size * size);
    }
    
    private int checkBlockingPotential(int row, int col, char letter) {
        int blockScore = 0;
        
        char opponentLetter = (letter == 'S') ? 'O' : 'S';
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell originalCell = board[row][col];
        
        board[row][col] = (opponentLetter == 'S') ? sos_Model.Cell.S : sos_Model.Cell.O;
        int opponentPotential = countSOSAtPosition(row, col);
        board[row][col] = originalCell;
        
        if (opponentPotential > 0) {
            blockScore = 80 * opponentPotential;
        }
        
        return blockScore;
    }
    
    private int checkSetupPotential(int row, int col, char letter) {
        sos_Model.Cell[][] board = model.getBoard();
        int size = board.length;
        
        int nearbyEmpty = 0;
        int nearbySO = 0;
        
        for (int dr = -2; dr <= 2; dr++) {
            for (int dc = -2; dc <= 2; dc++) {
                if (dr == 0 && dc == 0) continue;
                
                int newRow = row + dr;
                int newCol = col + dc;
                
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                    if (board[newRow][newCol] == sos_Model.Cell.EMPTY) {
                        nearbyEmpty++;
                    } else if (board[newRow][newCol] == sos_Model.Cell.S || 
                               board[newRow][newCol] == sos_Model.Cell.O) {
                        nearbySO++;
                    }
                }
            }
        }
        
        return nearbyEmpty * 2 + nearbySO * 3;
    }
    
}
