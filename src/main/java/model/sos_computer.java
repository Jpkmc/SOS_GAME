package model;

/**
 * Abstract base class for computer players
 * Defines common interface and shared functionality for AI opponents
 */
public abstract class sos_computer {
    protected final sos_Model model;
    
    public sos_computer(sos_Model model) {
        this.model = model;
    }
    
    /**
     * Finds the best move for the computer player
     * Must be implemented by subclasses for mode-specific strategies
     */
    public abstract int[] findBestMove();
    
    /**
     * Evaluates how good a potential move is
     */
    protected abstract int evaluateMove(int row, int col, char letter);
    
    /**
     * Counts how many SOS formations exist at a position
     */
    protected int countSOSAtPosition(int row, int col) {
        int count = 0;
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell cell = board[row][col];
        int size = board.length;
        
        if (cell == sos_Model.Cell.S) {
            if (col + 2 < size && board[row][col + 1] == sos_Model.Cell.O && 
                board[row][col + 2] == sos_Model.Cell.S) count++;
            
            if (row + 2 < size && board[row + 1][col] == sos_Model.Cell.O && 
                board[row + 2][col] == sos_Model.Cell.S) count++;
            
            if (row + 2 < size && col + 2 < size && 
                board[row + 1][col + 1] == sos_Model.Cell.O && 
                board[row + 2][col + 2] == sos_Model.Cell.S) count++;
            
            if (row + 2 < size && col >= 2 && 
                board[row + 1][col - 1] == sos_Model.Cell.O && 
                board[row + 2][col - 2] == sos_Model.Cell.S) count++;
            
            if (col >= 2 && board[row][col - 2] == sos_Model.Cell.S && 
                board[row][col - 1] == sos_Model.Cell.O) count++;
            
            if (row >= 2 && board[row - 2][col] == sos_Model.Cell.S && 
                board[row - 1][col] == sos_Model.Cell.O) count++;
            
            if (row >= 2 && col + 2 < size && 
                board[row - 2][col + 2] == sos_Model.Cell.S && 
                board[row - 1][col + 1] == sos_Model.Cell.O) count++;
            
            if (row >= 2 && col >= 2 && 
                board[row - 2][col - 2] == sos_Model.Cell.S && 
                board[row - 1][col - 1] == sos_Model.Cell.O) count++;
        } else if (cell == sos_Model.Cell.O) {
            if (col >= 1 && col + 1 < size && 
                board[row][col - 1] == sos_Model.Cell.S && 
                board[row][col + 1] == sos_Model.Cell.S) count++;
            
            if (row >= 1 && row + 1 < size && 
                board[row - 1][col] == sos_Model.Cell.S && 
                board[row + 1][col] == sos_Model.Cell.S) count++;
            
            if (row >= 1 && row + 1 < size && col >= 1 && col + 1 < size && 
                board[row - 1][col - 1] == sos_Model.Cell.S && 
                board[row + 1][col + 1] == sos_Model.Cell.S) count++;
            
            if (row >= 1 && row + 1 < size && col >= 1 && col + 1 < size && 
                board[row - 1][col + 1] == sos_Model.Cell.S && 
                board[row + 1][col - 1] == sos_Model.Cell.S) count++;
        }
        
        return count;
    }
    
    /**
     * Finds a random valid move as fallback
     */
    protected int[] findRandomMove() {
        sos_Model.Cell[][] board = model.getBoard();
        int boardSize = board.length;
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (model.cellEmpty(row, col)) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }
        
        if (!emptyCells.isEmpty()) {
            int[] cell = emptyCells.get((int)(Math.random() * emptyCells.size()));
            int letter = Math.random() < 0.5 ? 1 : 2;
            return new int[]{cell[0], cell[1], letter};
        }
        
        return null;
    }
}
