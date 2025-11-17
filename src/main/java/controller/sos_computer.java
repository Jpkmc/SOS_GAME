package controller;

import model.sos_Model;
import java.util.ArrayList;
import java.util.List;

public class sos_computer {
    
    /**
     * Finds the best move for the computer player
     */
    public int[] findBestMove(sos_Model model) {
        int boardSize = model.getBoard().length;
        int[] bestMove = null;
        int bestScore = -1;
        
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (model.cellEmpty(row, col)) {
                    for (char letter : new char[]{'S', 'O'}) {
                        int score = evaluateMove(model, row, col, letter);
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new int[]{row, col, letter == 'S' ? 1 : 2};
                        }
                    }
                }
            }
        }
        
        if (bestMove == null) {
            bestMove = findRandomMove(model);
        }
        
        return bestMove;
    }
    
    /**
     * Evaluates how good a potential move is
     */
    public int evaluateMove(sos_Model model, int row, int col, char letter) {
        int score = 0;
        
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell originalCell = board[row][col];
        board[row][col] = (letter == 'S') ? sos_Model.Cell.S : sos_Model.Cell.O;
        
        int sosCount = countSOSAtPosition(model, row, col);
        
        board[row][col] = originalCell;
        
        if (sosCount > 0) {
            score = 100 * sosCount;
        } else {
            score += checkBlockingPotential(model, row, col, letter);
            
            int boardSize = board.length;
            int centerDist = Math.abs(row - boardSize/2) + Math.abs(col - boardSize/2);
            score += (boardSize - centerDist) * 2;
            
            score += (int)(Math.random() * 5);
        }
        
        return score;
    }
    
    /**
     * Counts how many SOS formations exist at a position
     */
    public int countSOSAtPosition(sos_Model model, int row, int col) {
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
     * Checks if a move blocks opponent from scoring
     */
    public int checkBlockingPotential(sos_Model model, int row, int col, char letter) {
        int blockScore = 0;
        
        char opponentLetter = (letter == 'S') ? 'O' : 'S';
        sos_Model.Cell[][] board = model.getBoard();
        sos_Model.Cell originalCell = board[row][col];
        
        board[row][col] = (opponentLetter == 'S') ? sos_Model.Cell.S : sos_Model.Cell.O;
        int opponentPotential = countSOSAtPosition(model, row, col);
        board[row][col] = originalCell;
        
        if (opponentPotential > 0) {
            blockScore = 50 * opponentPotential;
        }
        
        return blockScore;
    }
    
    /**
     * Finds a random valid move as fallback
     */
    public int[] findRandomMove(sos_Model model) {
        sos_Model.Cell[][] board = model.getBoard();
        int boardSize = board.length;
        List<int[]> emptyCells = new ArrayList<>();
        
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
