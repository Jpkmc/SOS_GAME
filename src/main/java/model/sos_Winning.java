package model;

public abstract class sos_Winning {
    protected sos_Model model;

    public sos_Winning(sos_Model model) {
        this.model = model;
    }

    // Method to check if the game is over
    public abstract boolean isGameOver();

    // Method to determine the winner
    public abstract sos_Model.Player getWinner();

    // Method to handle SOS formation
    public abstract void handleSOSFormation(int sosCount, sos_Model.Player currentPlayer);

    // Method to handle turn switching after a move
    public abstract boolean shouldSwitchTurn(int sosCount);
}