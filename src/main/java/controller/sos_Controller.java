package controller;

import model.sos_Model;
import model.sos_computerSM;
import model.sos_computerGM;
import model.sos_game_recorder;
import view.sos_View;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * Main controller for the SOS game
 * Handles all the interacshuns between the view and model
 */
public class sos_Controller {
    // Stores the game logic
    final private sos_Model model;
    // Stores the game view
    final private sos_View view;
    
    // Computer AI instances
    private sos_computerSM computerSM;
    private sos_computerGM computerGM;
    private Timer computerMoveTimer;
    
    // Game recording system
    private final sos_game_recorder gameRecorder;
    private boolean isRecording = false;

    /**
     * Creates new controller for the game
     * @param model the game logic
     * @param view the game view
     */
    public sos_Controller(sos_Model model, sos_View view){
        this.view = view;
        this.model = model;
        
        // Initialize computer AI
        this.computerSM = new sos_computerSM(model);
        this.computerGM = new sos_computerGM(model);
        
        // Initialize game recorder
        this.gameRecorder = new sos_game_recorder();

        initialzeGame();
        initializeListeners();
    }

    /**
     * Sets up the initial game state
     * Makes the board and sets everything up
     */
    private void initialzeGame(){
        // set default board size to 3x3
        model.setSize(3);
        view.setBoardSize(3);

        // start with simple mode
        model.mode(sos_Model.Mode.Simple);

        // make the game bord empty
        model.initialzeBoard();

        // create the visual board buttons
        createViewBoard(3);

        // show the empty board
        updateBoardDisplay();
    }

    /**
     * Updates what is shown on the board
     * Makes all the buttons show S, O, or nothing
     */
    private void updateBoardDisplay(){
        // get the current game state
        sos_Model.Cell[][] board = model.getBoard();
        // get the buttons to update
        JButton[][] boardButtons = view.getBoardButton();

        // only update if we have both arrays
        if(board != null && boardButtons != null){
            // Update board cells
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    switch(board[i][j]) {
                        case S:
                            boardButtons[i][j].setText("S");
                            break;
                        case O:
                            boardButtons[i][j].setText("O");
                            break;
                        default:
                            boardButtons[i][j].setText("");
                            break;
                    }
                }
            }
        }
    }
   
    /**
     * Makes the bord with buttuns
     * @param size how big the bord shud be
     */
    private void createViewBoard(int size){
        view.createBoard(size);
    }

    /**
     * Puts the click handelrs on the bord buttuns
     * Makes sure playrs can click the bord
     */
    private void attachBoardButtonListeners() {
        JButton[][] boardButtons = view.getBoardButton();
        for(int i = 0; i < boardButtons.length; i++) {
            for(int j = 0; j < boardButtons[i].length; j++) {
                final int row = i;
                final int column = j;
                // Remove any existing listeners to prevent duplicates
                for(ActionListener al : boardButtons[i][j].getActionListeners()) {
                    boardButtons[i][j].removeActionListener(al);
                }
                boardButtons[i][j].addActionListener(e -> handleCellClick(row, column));
            }
        }
    }

    /**
     * Sets up all the click handelrs
     * Makes buttons do stuff wen clicked
     */
    private void initializeListeners() {
        // make new game buttun work
        view.getNewGameButton().addActionListener(e -> handleNewGame());

        // make mode pikker work
        view.getRbSimple().addActionListener(e -> handleModeChange(sos_Model.Mode.Simple));
        view.getRbGeneral().addActionListener(e -> handleModeChange(sos_Model.Mode.General));

        // make bord buttuns work
        attachBoardButtonListeners();
        
        // Add recording control listeners
        view.getCbRecordGame().addActionListener(e -> handleRecordingToggle());
        view.getBtnSaveGame().addActionListener(e -> handleSaveGame());
        view.getBtnLoadGame().addActionListener(e -> handleLoadGame());
        

    }

    /**
     * Starts a new game with the chosen size
     * Resets everything and makes new board
     */
    private void handleNewGame() {
        try {
            // get what size the player wants
            int size = view.getBoardsize();
            if(size >= 3) {
                // make new board with that size
                model.setSize(size);
                model.resetGame();
                createViewBoard(size);
                attachBoardButtonListeners(); // add click handlers to new buttons
                updateBoardDisplay();
                // Clear any existing lines
                view.updateLines(model.getSOSLines());
                updateScores();
                // make sure the board looks right
                view.getBoardPanel().revalidate();
                view.getBoardPanel().repaint();
                
                // Reinitialize computer AI for new game
                computerSM = new sos_computerSM(model);
                computerGM = new sos_computerGM(model);
                
                // Start recording if checkbox is checked
                if (view.getCbRecordGame().isSelected() && !isRecording) {
                    gameRecorder.startRecording(model.getBoardSize(), model.getGameMode());
                    isRecording = true;
                    view.getBtnSaveGame().setEnabled(false);
                }
                
                // Start computer move if Player 1 is computer
                triggerComputerMoveIfNeeded();
            } else {
                // tell user they need bigger size
                JOptionPane.showMessageDialog(view, "Board size must be at least 3", "Invalid Size", JOptionPane.ERROR_MESSAGE);
                view.setBoardSize(5); // reset to default size
            }
        } catch (NumberFormatException e) {
            // tell user they need valid number
            JOptionPane.showMessageDialog(view, "Please enter a valid number for board size", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            view.setBoardSize(5); // reset to default size
        }
    }

    /**
     * Changes the game mode wen user clicks
     * @param newMode which mode they pikked
     */
    private void handleModeChange(sos_Model.Mode newMode) {
        model.setMode(newMode);
    }

    /**
     * Handles when player clicks a cell on board
     * Puts S or O based on what they picked
     * @param row which row they clicked
     * @param column which column they clicked
     */
    private void handleCellClick(int row, int column) {
        // Check if current player is computer - ignore human clicks
        sos_Model.Player currentPlayer = model.getCurrentPlayer();
        if ((currentPlayer == sos_Model.Player.Player1 && view.getCbPlayer1Computer().isSelected()) ||
            (currentPlayer == sos_Model.Player.Player2 && view.getCbPlayer2Computer().isSelected())) {
            return; // Ignore clicks when it's computer's turn
        }
        
        // only do something if cell is empty and game is not over
        if(model.cellEmpty(row, column) && !model.isGameOver()) {
            char letter;
            
            // get what letter they want (S or O)
            if(currentPlayer == sos_Model.Player.Player1) {
                letter = view.getRbPlayer1S().isSelected() ? 'S' : 'O';
            } else {
                letter = view.getRbPlayer2S().isSelected() ? 'S' : 'O';
            }

            // try to make the move
            if(model.move(row, column, letter)) {
                // Record the move if recording is enabled
                if (isRecording) {
                    int sosFormed = model.getSOSLines().size();
                    gameRecorder.recordMove(row, column, letter, model.getCurrentPlayer(), 
                                           sosFormed, model.isGameOver());
                }
                
                // Update the board display first
                updateBoardDisplay();
                // Force an update of the lines with latest state
                SwingUtilities.invokeLater(() -> {
                    view.updateLines(model.getSOSLines());
                    updateScores();
                    view.revalidate();
                    view.repaint();
                });
                
                if (model.isGameOver()) {
                    handleGameOver();
                } else {
                    // Check if next player is computer
                    triggerComputerMoveIfNeeded();
                }
            }
        }
    }

    private void updateScores() {
        view.updateScore(1, model.getPlayer1Score());
        view.updateScore(2, model.getPlayer2Score());
    }

    private void handleGameOver() {
        String message;
        sos_Model.Player winner = model.getWinner();
        
        if (model.isSimpleGameMode()) {
            // Simple game: first SOS wins
            if (winner != null) {
                message = (winner == sos_Model.Player.Player1) ? 
                    "Player 1 wins by forming SOS!" : 
                    "Player 2 wins by forming SOS!";
            } else {
                message = "Game is a draw - no SOS formed!";
            }
        } else {
            // General game: highest score wins
            int p1Score = model.getPlayer1Score();
            int p2Score = model.getPlayer2Score();
            
            if (p1Score > p2Score) {
                message = "Player 1 wins with score " + p1Score + "!";
            } else if (p2Score > p1Score) {
                message = "Player 2 wins with score " + p2Score + "!";
            } else {
                message = "It's a draw! Both players scored " + p1Score + " points.";
            }
        }
        
        // Stop recording when game ends
        if (isRecording) {
            gameRecorder.stopRecording();
            isRecording = false;
            view.getBtnSaveGame().setEnabled(true);
        }
        
        JOptionPane.showMessageDialog(view, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Checks if current player is computer and triggers their move
     */
    private void triggerComputerMoveIfNeeded() {
        sos_Model.Player currentPlayer = model.getCurrentPlayer();
        boolean isComputerTurn = (currentPlayer == sos_Model.Player.Player1 && view.getCbPlayer1Computer().isSelected()) ||
                                 (currentPlayer == sos_Model.Player.Player2 && view.getCbPlayer2Computer().isSelected());
        
        if (isComputerTurn && !model.isGameOver()) {
            // Add delay so user can see what's happening
            computerMoveTimer = new Timer(800, e -> {
                makeComputerMove();
                ((Timer)e.getSource()).stop();
            });
            computerMoveTimer.setRepeats(false);
            computerMoveTimer.start();
        }
    }
    
    /**
     * Makes the computer player move
     */
    private void makeComputerMove() {
        if (model.isGameOver()) {
            return;
        }
        
        // Get the appropriate computer AI based on game mode
        int[] move;
        if (model.isSimpleGameMode()) {
            move = computerSM.findBestMove();
        } else {
            move = computerGM.findBestMove();
        }
        
        if (move != null) {
            int row = move[0];
            int col = move[1];
            char letter = move[2] == 1 ? 'S' : 'O';
            
            // Make the move
            if (model.move(row, col, letter)) {
                // Record the move if recording is enabled
                if (isRecording) {
                    int sosFormed = model.getSOSLines().size();
                    gameRecorder.recordMove(row, col, letter, model.getCurrentPlayer(), 
                                           sosFormed, model.isGameOver());
                }
                
                // Update display
                updateBoardDisplay();
                SwingUtilities.invokeLater(() -> {
                    view.updateLines(model.getSOSLines());
                    updateScores();
                    view.revalidate();
                    view.repaint();
                });
                
                // Check if game is over
                if (model.isGameOver()) {
                    handleGameOver();
                } else {
                    // Check if next player is also computer
                    triggerComputerMoveIfNeeded();
                }
            }
        }
    }
    
    /**
     * Handles recording toggle checkbox
     */
    private void handleRecordingToggle() {
        boolean shouldRecord = view.getCbRecordGame().isSelected();
        
        if (shouldRecord && !isRecording) {
            // Start recording
            gameRecorder.startRecording(model.getBoardSize(), model.getGameMode());
            isRecording = true;
            view.getBtnSaveGame().setEnabled(false); // Disable save during recording
        } else if (!shouldRecord && isRecording) {
            // Stop recording
            gameRecorder.stopRecording();
            isRecording = false;
            view.getBtnSaveGame().setEnabled(true); // Enable save after recording
        }
    }
    
    /**
     * Handles saving the recorded game
     */
    private void handleSaveGame() {
        if (!gameRecorder.isRecording() && !gameRecorder.getMoves().isEmpty()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Game Recording");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            
            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filename = file.getAbsolutePath();
                if (!filename.endsWith(".txt")) {
                    filename += ".txt";
                }
                
                if (saveGameAsText(filename)) {
                    JOptionPane.showMessageDialog(view, "Game saved successfully!", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to save game.", "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(view, "No game recording to save.", "Nothing to Save", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Saves game in human-readable text format
     */
    private boolean saveGameAsText(String filename) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
            // Write simple game header
            writer.println("SOS Game - " + (model.isSimpleGameMode() ? "Simple" : "General") + " Mode");
            writer.println("Board: " + model.getBoardSize() + "x" + model.getBoardSize());
            writer.println("Score: Player1=" + model.getPlayer1Score() + ", Player2=" + model.getPlayer2Score());
            writer.println();
            
            // Write moves in simple format
            for (sos_game_recorder.GameMove move : gameRecorder.getMoves()) {
                String player = (move.player == sos_Model.Player.Player1) ? "P1" : "P2";
                String sos = move.sosFormed > 0 ? " +SOS" : "";
                writer.println(player + ": " + move.letter + " at (" + (move.row + 1) + "," + (move.col + 1) + ")" + sos);
            }
            
            // Simple winner line
            if (model.isSimpleGameMode()) {
                sos_Model.Player winner = model.getWinner();
                if (winner != null) {
                    writer.println("Winner: " + (winner == sos_Model.Player.Player1 ? "Player 1" : "Player 2"));
                } else {
                    writer.println("Result: Draw");
                }
            } else {
                int p1 = model.getPlayer1Score();
                int p2 = model.getPlayer2Score();
                if (p1 > p2) writer.println("Winner: Player 1");
                else if (p2 > p1) writer.println("Winner: Player 2");
                else writer.println("Result: Draw");
            }
            
            return true;
            
        } catch (java.io.IOException e) {
            System.err.println("Error saving game as text: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Handles loading and replaying a game from txt file
     */
    private void handleLoadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game Recording");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Use the sos_game_replay function to load moves
            java.util.List<sos_game_recorder.SimpleGameMove> moves = 
                sos_game_recorder.sos_game_replay(file.getAbsolutePath());
            
            if (moves != null && !moves.isEmpty()) {
                startGameReplay(moves);
            } else {
                JOptionPane.showMessageDialog(view, 
                    "Could not load game from file.\nMake sure it's a valid game recording.", 
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Replays the loaded game moves on the board
     */
    private void startGameReplay(java.util.List<sos_game_recorder.SimpleGameMove> moves) {
        // Reset game to clean state
        model.resetGame();
        updateBoardDisplay();
        view.updateLines(model.getSOSLines());
        updateScores();
        
        // Disable user interaction during replay
        JButton[][] buttons = view.getBoardButton();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
        
        // Create replay timer
        Timer replayTimer = new Timer(1500, null); // 1.5 second delay between moves
        final int[] currentMoveIndex = {0};
        
        replayTimer.addActionListener(e -> {
            if (currentMoveIndex[0] < moves.size()) {
                sos_game_recorder.SimpleGameMove move = moves.get(currentMoveIndex[0]);
                
                // Make the move on the model
                boolean moveSuccess = model.move(move.row, move.col, move.letter);
                
                if (moveSuccess) {
                    // Update display
                    updateBoardDisplay();
                    view.updateLines(model.getSOSLines());
                    updateScores();
                    
                    // Show move info
                    String playerName = (move.player == sos_Model.Player.Player1) ? "Player 1" : "Player 2";
                    String sosInfo = move.formedSOS ? " (SOS!)" : "";
                    view.setTitle("SOS Game - Replaying Move " + (currentMoveIndex[0] + 1) + 
                                 ": " + playerName + " places " + move.letter + sosInfo);
                }
                
                currentMoveIndex[0]++;
            } else {
                // Replay finished
                replayTimer.stop();
                
                // Re-enable buttons
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = 0; j < buttons[i].length; j++) {
                        buttons[i][j].setEnabled(true);
                    }
                }
                
                view.setTitle("SOS Board Game");
                JOptionPane.showMessageDialog(view, 
                    "Replay completed!\nClick 'New Game' to start a new game.", 
                    "Replay Finished", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Start replay
        JOptionPane.showMessageDialog(view, 
            "Starting replay of " + moves.size() + " moves.\n" +
            "Each move will be shown with a 1.5 second delay.", 
            "Replay Starting", JOptionPane.INFORMATION_MESSAGE);
        
        replayTimer.start();
    }
    
}
