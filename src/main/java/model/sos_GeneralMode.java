package model;

public class sos_GeneralMode extends sos_Winning {
    private int player1Score;
    private int player2Score;

    public sos_GeneralMode(sos_Model model) {
        super(model);
        this.player1Score = 0;
        this.player2Score = 0;
    }

    @Override
    public boolean isGameOver() {
        // In general mode, game is only over when board is full
        sos_Model.Cell[][] board = model.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == sos_Model.Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public sos_Model.Player getWinner() {
        if (!isGameOver()) {
            return null;
        }

        if (player1Score > player2Score) {
            return sos_Model.Player.Player1;
        } else if (player2Score > player1Score) {
            return sos_Model.Player.Player2;
        } else {
            return null; // Draw
        }
    }

    @Override
    public void handleSOSFormation(int sosCount, sos_Model.Player currentPlayer) {
        if (sosCount > 0) {
            if (currentPlayer == sos_Model.Player.Player1) {
                player1Score += sosCount;
            } else {
                player2Score += sosCount;
            }
        }
    }

    @Override
    public boolean shouldSwitchTurn(int sosCount) {
        // In general mode, only switch turns if no SOS was formed
        return sosCount == 0;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void reset() {
        player1Score = 0;
        player2Score = 0;
    }
}