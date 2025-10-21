package model;

public class sos_Model {
    public enum Mode { Simple, General}
    public enum Player {Player1, Player2}
    public enum Cell {EMPTY, S, O}

    private int size;
    private Mode mode;
    private Cell[][] board;
    private Player currentPlayer;
    //Adding Variable for later development 
    //private boolean gameEnd add for future devlopment 
    //private Player Winner
    //Private int player1Score
    //private int player2Score

    

    public sos_Model(int size, Mode mode){
        if(size < 3){
            throw new IllegalArgumentException("Board size must be greater than or eqaul to 3");
        }
        this.size = size;
        this.mode = mode;
        this.board =  new Cell[size][size];
        this.currentPlayer = Player.Player1;

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
    }


    public boolean move(int row, int colmun, char letter){
        if(row < 0 || row >= size || colmun < 0 || colmun >= size){
            return false;
        }

        if(board[row][colmun] != Cell.EMPTY){
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

   




}
