package model;

public class sos_Model {
    public enum Mode { Simple, General}
    public enum Player {Player1, Player2}
    public enum Cell {EMPTY, S, O}

    private int size;
    private Mode mode;
    private Cell[][] board;
    private Player currentPlayer;
    private boolean gameEnd;
    private Player winner;
    private int player1Score;
    private int player2Score;

    

    public sos_Model(int size, Mode mode){
        if(size < 3){
            throw new IllegalArgumentException("Board size must be greater than or eqaul to 3");
        }
        this.size = size;
        this.mode = mode;
        this.board =  new Cell[size][size];
        this.currentPlayer = Player.Player1;
        this.gameEnd = false;
        this.winner = null;
        this.player1Score = 0;
        this.player2Score = 0;

        //calling the function to create the board
        initialzeBoard();
    }

    public void initialzeBoard(){
        board = new Cell[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = Cell.EMPTY;
            }
        }
    }

    public void resetGame(){
        initialzeBoard();
        currentPlayer = Player.Player1;
        gameEnd = false;
        winner = null;
        player1Score = 0;
        player2Score = 0;
    }


    public boolean move(int row, int colmun, char letter){
        if(row < 0 || row >= size || colmun < 0 || colmun >= size){
            return false;
        }

        if(board[row][colmun] != Cell.EMPTY || gameEnd){
            return false;
        }
        
        if(letter == 'S'){
            board[row][colmun] = Cell.S;
        }
        else if (letter == 'O'){
            board[row][colmun] = Cell.O;
        }
        else{
            return false;
        }
        
        // Check for SOS formations after the move
        checkSOSFormation(row, colmun);
        
        // Switch players after a successful move
        currentPlayer = (currentPlayer == Player.Player1) ? Player.Player2 : Player.Player1;
        return true;
    }

     public boolean cellEmpty(int row, int colmun){
        if(row < 0 || row >= size || colmun < 0 || colmun >= size){
            return false;
        }
        return board[row][colmun] == Cell.EMPTY;
    }

    public Cell getCell(int row, int colmun){
        if(row < 0 || row >= size || colmun < 0 || colmun >= size){
            return Cell.EMPTY;
        }
        return board[row][colmun];
    }


    public void setSize(int size){
        if(size < 3) {
            throw new IllegalArgumentException("Board size must be greater than or equal to 3");
        }
        if (this.size != size) {
            this.size = size;
            initialzeBoard();
            currentPlayer = Player.Player1;
        }
    }

    public Mode mode(Mode simple){
        return mode;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Cell[][] getBoard(){
        return board;
    }

    public boolean isGameOver() {
        if (mode == Mode.Simple && winner != null) {
            return true;
        }
        
        // Check if board is full
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        if (!isGameOver()) {
            return null;
        }
        
        if (mode == Mode.Simple) {
            return winner;
        } else {
            // For general game mode
            if (player1Score > player2Score) {
                return Player.Player1;
            } else if (player2Score > player1Score) {
                return Player.Player2;
            } else {
                return null; // Draw
            }
        }
    }

    public boolean isSimpleGameMode() {
        return mode == Mode.Simple;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    private boolean checkSOS(int row, int col) {
        // Check horizontal SOS
        if (col <= size - 3 && 
            board[row][col] == Cell.S &&
            board[row][col + 1] == Cell.O &&
            board[row][col + 2] == Cell.S) {
            return true;
        }
        
        // Check vertical SOS
        if (row <= size - 3 &&
            board[row][col] == Cell.S &&
            board[row + 1][col] == Cell.O &&
            board[row + 2][col] == Cell.S) {
            return true;
        }
        
        // Check diagonal down-right
        if (row <= size - 3 && col <= size - 3 &&
            board[row][col] == Cell.S &&
            board[row + 1][col + 1] == Cell.O &&
            board[row + 2][col + 2] == Cell.S) {
            return true;
        }
        
        // Check diagonal down-left
        if (row <= size - 3 && col >= 2 &&
            board[row][col] == Cell.S &&
            board[row + 1][col - 1] == Cell.O &&
            board[row + 2][col - 2] == Cell.S) {
            return true;
        }
        
        return false;
    }

    public int checkSOSFormation(int row, int col) {
        int count = 0;
        
        // Check for SOS formations in all directions
        for (int i = Math.max(0, row - 2); i <= Math.min(size - 3, row); i++) {
            for (int j = Math.max(0, col - 2); j <= Math.min(size - 3, col); j++) {
                if (checkSOS(i, j)) {
                    count++;
                    if (currentPlayer == Player.Player1) {
                        player1Score++;
                        if (mode == Mode.Simple) {
                            winner = Player.Player1;
                            gameEnd = true;
                        }
                    } else {
                        player2Score++;
                        if (mode == Mode.Simple) {
                            winner = Player.Player2;
                            gameEnd = true;
                        }
                    }
                }
            }
        }
        
        return count;
    }
}
