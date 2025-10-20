package model;

public class sos_Model {
    public enum Mode { Simple, General}
    public enum Player {Player1, Player2}

    private int size;
    private Mode mode;
    private char board[][];
    private Player currentPlayer;


    public void SOS_Main(int size, Mode mode){
        if(size < 3){
            throw new IllegalArgumentException("Board size must be greater than or eqaul to 3");
        }
        this.size = size;
        this.mode = mode;
        this.board = new char[size][size];
        this.currentPlayer = Player.Player1;
    }

    public int getSize(){
        return size;
    }
    public Mode mode(){
        return mode;
    }
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


}
