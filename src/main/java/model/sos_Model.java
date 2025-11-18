package model;

public class sos_Model {
    public enum Mode { Simple, General}
    public enum Player {Player1, Player2}
    public enum Cell {EMPTY, S, O}

    private int size;
    private Mode mode;
    private Cell[][] board;
    private Player currentPlayer;
    private sos_Winning gameLogic;
    private java.util.ArrayList<SOSLine> sosLines;

    public sos_Model(int size, Mode mode){
        if(size < 3){
            throw new IllegalArgumentException("Board size must be greater than or eqaul to 3");
        }
        this.size = size;
        this.mode = mode;
        this.board = new Cell[size][size];
        this.currentPlayer = Player.Player1;
        
        // Initialize game logic based on mode
        if (mode == Mode.Simple) {
            this.gameLogic = new sos_SimpleMode(this);
        } else {
            this.gameLogic = new sos_GeneralMode(this);
        }

        //calling the function to create the board
        initialzeBoard();
        sosLines = new java.util.ArrayList<>();
    }

    public final void initialzeBoard(){
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
        if (mode == Mode.Simple) {
            gameLogic = new sos_SimpleMode(this);
        } else {
            gameLogic = new sos_GeneralMode(this);
        }
        sosLines = new java.util.ArrayList<>();
    }

    public boolean move(int row, int colmun, char letter){
        if(row < 0 || row >= size || colmun < 0 || colmun >= size){
            return false;
        }

        if(board[row][colmun] != Cell.EMPTY || gameLogic.isGameOver()){
            return false;
        }
        
        switch(letter) {
            case 'S':
                board[row][colmun] = Cell.S;
                break;
            case 'O':
                board[row][colmun] = Cell.O;
                break;
            default:
                return false;
        }
        
        // Store current player before checking SOS formations
        Player movePlayer = currentPlayer;
        
        // Check for SOS formations after the move
        int sosCount = checkSOSFormation(row, colmun, movePlayer);
        
        // Handle SOS formation in the appropriate game mode
        gameLogic.handleSOSFormation(sosCount, movePlayer);
        
        // Switch turns based on game mode rules
        if (gameLogic.shouldSwitchTurn(sosCount)) {
            currentPlayer = (currentPlayer == Player.Player1) ? Player.Player2 : Player.Player1;
        }
        
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
        return gameLogic.isGameOver();
    }

    public Player getWinner() {
        return gameLogic.getWinner();
    }

    public boolean isSimpleGameMode() {
        return mode == Mode.Simple;
    }

    public int getPlayer1Score() {
        return mode == Mode.Simple ? 
            ((sos_SimpleMode)gameLogic).getPlayer1Score() : 
            ((sos_GeneralMode)gameLogic).getPlayer1Score();
    }

    public int getPlayer2Score() {
        return mode == Mode.Simple ? 
            ((sos_SimpleMode)gameLogic).getPlayer2Score() : 
            ((sos_GeneralMode)gameLogic).getPlayer2Score();
    }

    public java.util.List<SOSLine> getSOSLines() {
        return new java.util.ArrayList<>(sosLines);
    }

    public int checkSOSFormation(int row, int col, Player movePlayer) {
        int count = 0;
        Cell currentCell = board[row][col];

        // Check if current cell is S or O and search for SOS formations accordingly
        if (currentCell == Cell.S) {
            // Check if S is at start of SOS
            count += checkSStartingFormations(row, col, movePlayer);
            // Check if S is at end of SOS
            count += checkSEndingFormations(row, col, movePlayer);
        } else if (currentCell == Cell.O) {
            // Check if O is in middle of SOS
            count += checkOMiddleFormations(row, col, movePlayer);
        }
        
        return count;
    }

    private int checkSStartingFormations(int row, int col, Player movePlayer) {
        int count = 0;
        
        // Check right
        if (col + 2 < size && 
            board[row][col + 1] == Cell.O && 
            board[row][col + 2] == Cell.S) {
            sosLines.add(new SOSLine(row, col, row, col + 2, movePlayer));
            count++;
        }

        // Check down
        if (row + 2 < size && 
            board[row + 1][col] == Cell.O && 
            board[row + 2][col] == Cell.S) {
            sosLines.add(new SOSLine(row, col, row + 2, col, movePlayer));
            count++;
        }

        // Check diagonal down-right
        if (row + 2 < size && col + 2 < size && 
            board[row + 1][col + 1] == Cell.O && 
            board[row + 2][col + 2] == Cell.S) {
            sosLines.add(new SOSLine(row, col, row + 2, col + 2, movePlayer));
            count++;
        }

        // Check diagonal down-left
        if (row + 2 < size && col >= 2 && 
            board[row + 1][col - 1] == Cell.O && 
            board[row + 2][col - 2] == Cell.S) {
            sosLines.add(new SOSLine(row, col, row + 2, col - 2, movePlayer));
            count++;
        }

        return count;
    }

    private int checkSEndingFormations(int row, int col, Player movePlayer) {
        int count = 0;

        // Check left
        if (col >= 2 && 
            board[row][col - 2] == Cell.S && 
            board[row][col - 1] == Cell.O) {
            sosLines.add(new SOSLine(row, col - 2, row, col, movePlayer));
            count++;
        }

        // Check up
        if (row >= 2 && 
            board[row - 2][col] == Cell.S && 
            board[row - 1][col] == Cell.O) {
            sosLines.add(new SOSLine(row - 2, col, row, col, movePlayer));
            count++;
        }

        // Check diagonal up-right
        if (row >= 2 && col + 2 < size && 
            board[row - 2][col + 2] == Cell.S && 
            board[row - 1][col + 1] == Cell.O) {
            sosLines.add(new SOSLine(row - 2, col + 2, row, col, movePlayer));
            count++;
        }

        // Check diagonal up-left
        if (row >= 2 && col >= 2 && 
            board[row - 2][col - 2] == Cell.S && 
            board[row - 1][col - 1] == Cell.O) {
            sosLines.add(new SOSLine(row - 2, col - 2, row, col, movePlayer));
            count++;
        }

        return count;
    }

    private int checkOMiddleFormations(int row, int col, Player movePlayer) {
        int count = 0;

        // Check horizontal
        if (col >= 1 && col + 1 < size && 
            board[row][col - 1] == Cell.S && 
            board[row][col + 1] == Cell.S) {
            sosLines.add(new SOSLine(row, col - 1, row, col + 1, movePlayer));
            count++;
        }

        // Check vertical
        if (row >= 1 && row + 1 < size && 
            board[row - 1][col] == Cell.S && 
            board[row + 1][col] == Cell.S) {
            sosLines.add(new SOSLine(row - 1, col, row + 1, col, movePlayer));
            count++;
        }

        // Check diagonal down-right to up-left
        if (row >= 1 && row + 1 < size && col >= 1 && col + 1 < size && 
            board[row - 1][col - 1] == Cell.S && 
            board[row + 1][col + 1] == Cell.S) {
            sosLines.add(new SOSLine(row - 1, col - 1, row + 1, col + 1, movePlayer));
            count++;
        }

        // Check diagonal up-right to down-left
        if (row >= 1 && row + 1 < size && col >= 1 && col + 1 < size && 
            board[row - 1][col + 1] == Cell.S && 
            board[row + 1][col - 1] == Cell.S) {
            sosLines.add(new SOSLine(row - 1, col + 1, row + 1, col - 1, movePlayer));
            count++;
        }

        return count;
    }
}

