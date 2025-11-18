package model;

public class sos_SimpleMode extends sos_Winning {
    private sos_Model.Player winner;
    private boolean gameEnd;
    private int player1Score;
    private int player2Score;

    public sos_SimpleMode(sos_Model model) {
        super(model);
        this.winner = null;
        this.gameEnd = false;
        this.player1Score = 0;
        this.player2Score = 0;
    }

    @Override
    public boolean isGameOver() {
        // Game is over if someone won or the board is full
        if (winner != null) {
            return true;
        }
        
        // Check if board is full
        sos_Model.Cell[][] board = model.getBoard();
        for (sos_Model.Cell[] row : board) {
            for (sos_Model.Cell cell : row) {
                if (cell == sos_Model.Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public sos_Model.Player getWinner() {
        return winner;
    }

    @Override
    public void handleSOSFormation(int sosCount, sos_Model.Player currentPlayer) {
        if (sosCount > 0) {
            if (currentPlayer == sos_Model.Player.Player1) {
                player1Score += sosCount;
            } else {
                player2Score += sosCount;
            }
            winner = currentPlayer;
            gameEnd = true;
        }
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    @Override
    public boolean shouldSwitchTurn(int sosCount) {
        // In simple mode, always switch turns unless game is won
        return !gameEnd;
    }

    public void reset() {
        winner = null;
        gameEnd = false;
    }
}