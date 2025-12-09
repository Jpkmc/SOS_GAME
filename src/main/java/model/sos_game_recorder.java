package model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles recording game moves to a file and replaying them
 */
public class sos_game_recorder {
    private List<GameMove> moves;
    private boolean isRecording;
    private String gameMetadata;
    
    public sos_game_recorder() {
        this.moves = new ArrayList<>();
        this.isRecording = false;
    }
    
    /**
     * Starts recording a new game
     */
    public void startRecording(int boardSize, sos_Model.Mode mode) {
        moves.clear();
        isRecording = true;
        
        // Create metadata header
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        gameMetadata = String.format("GAME_START|%s|%d|%s|%s", 
            formatter.format(now), boardSize, mode.toString(), "1.0");
    }
    
    /**
     * Stops recording the current game
     */
    public void stopRecording() {
        isRecording = false;
    }
    
    /**
     * Records a move made during the game
     */
    public void recordMove(int row, int col, char letter, sos_Model.Player player, 
                          int sosFormed, boolean gameEnded) {
        if (!isRecording) return;
        
        GameMove move = new GameMove(row, col, letter, player, sosFormed, gameEnded, moves.size() + 1);
        moves.add(move);
    }
    
    /**
     * Saves the recorded game to a file
     */
    public boolean saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write metadata
            writer.println(gameMetadata);
            
            // Write moves
            for (GameMove move : moves) {
                writer.println(move.toString());
            }
            
            // Write game end marker
            writer.println("GAME_END");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads a game from file for replay
     */
    public GameReplay loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<GameMove> loadedMoves = new ArrayList<>();
            String metadataLine = reader.readLine();
            
            if (metadataLine == null || !metadataLine.startsWith("GAME_START")) {
                throw new IOException("Invalid game file format");
            }
            
            // Parse metadata
            String[] metaParts = metadataLine.split("\\|");
            if (metaParts.length < 4) {
                throw new IOException("Invalid metadata format");
            }
            
            int boardSize = Integer.parseInt(metaParts[2]);
            sos_Model.Mode mode = sos_Model.Mode.valueOf(metaParts[3]);
            
            // Read moves
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("GAME_END")) break;
                
                GameMove move = GameMove.fromString(line);
                if (move != null) {
                    loadedMoves.add(move);
                }
            }
            
            return new GameReplay(boardSize, mode, loadedMoves, metaParts[1]);
            
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Returns whether currently recording
     */
    public boolean isRecording() {
        return isRecording;
    }
    
    /**
     * Returns the current moves list (for debugging/display)
     */
    public List<GameMove> getMoves() {
        return new ArrayList<>(moves);
    }
    
    /**
     * Replays a game from a simple .txt file
     * @param filename Path to the .txt file containing game moves
     * @return List of moves that were parsed from the file, or null if error
     */
    public static List<SimpleGameMove> sos_game_replay(String filename) {
        List<SimpleGameMove> replayMoves = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int moveNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip header lines and empty lines
                if (line.isEmpty() || 
                    line.startsWith("SOS Game") || 
                    line.startsWith("Board:") || 
                    line.startsWith("Score:") || 
                    line.startsWith("Winner:") || 
                    line.startsWith("Result:")) {
                    continue;
                }
                
                // Parse move lines like "P1: S at (1,2)" or "P2: O at (2,3) +SOS"
                if (line.contains(": ") && line.contains(" at (")) {
                    SimpleGameMove move = parseSimpleMove(line, moveNumber);
                    if (move != null) {
                        replayMoves.add(move);
                        moveNumber++;
                    }
                }
            }
            
            return replayMoves.isEmpty() ? null : replayMoves;
            
        } catch (IOException e) {
            System.err.println("Error reading replay file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to parse a single move line from txt file
     */
    private static SimpleGameMove parseSimpleMove(String line, int moveNumber) {
        try {
            // Parse format: "P1: S at (1,2)" or "P2: O at (2,3) +SOS"
            String[] parts = line.split(": ");
            if (parts.length != 2) return null;
            
            String playerStr = parts[0].trim();
            String moveStr = parts[1].trim();
            
            // Parse player (P1 or P2)
            sos_Model.Player player;
            if (playerStr.equals("P1")) {
                player = sos_Model.Player.Player1;
            } else if (playerStr.equals("P2")) {
                player = sos_Model.Player.Player2;
            } else {
                return null;
            }
            
            // Parse letter and position: "S at (1,2)" or "O at (2,3) +SOS"
            String[] moveParts = moveStr.split(" at \\(");
            if (moveParts.length != 2) return null;
            
            char letter = moveParts[0].trim().charAt(0);
            
            // Parse coordinates: "1,2)" or "2,3) +SOS"
            String coordStr = moveParts[1];
            boolean formedSOS = coordStr.contains("+SOS");
            
            // Remove +SOS and closing parenthesis
            coordStr = coordStr.replace(") +SOS", "").replace(")", "");
            String[] coords = coordStr.split(",");
            
            if (coords.length != 2) return null;
            
            int row = Integer.parseInt(coords[0].trim()) - 1; // Convert to 0-based
            int col = Integer.parseInt(coords[1].trim()) - 1; // Convert to 0-based
            
            return new SimpleGameMove(moveNumber, player, letter, row, col, formedSOS);
            
        } catch (Exception e) {
            System.err.println("Error parsing move: " + line + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Represents a single move in the game
     */
    public static class GameMove {
        public final int row;
        public final int col;
        public final char letter;
        public final sos_Model.Player player;
        public final int sosFormed;
        public final boolean gameEnded;
        public final int moveNumber;
        
        public GameMove(int row, int col, char letter, sos_Model.Player player, 
                       int sosFormed, boolean gameEnded, int moveNumber) {
            this.row = row;
            this.col = col;
            this.letter = letter;
            this.player = player;
            this.sosFormed = sosFormed;
            this.gameEnded = gameEnded;
            this.moveNumber = moveNumber;
        }
        
        @Override
        public String toString() {
            return String.format("MOVE|%d|%d|%d|%c|%s|%d|%b", 
                moveNumber, row, col, letter, player.toString(), sosFormed, gameEnded);
        }
        
        public static GameMove fromString(String line) {
            try {
                String[] parts = line.split("\\|");
                if (parts.length != 7 || !parts[0].equals("MOVE")) {
                    return null;
                }
                
                int moveNumber = Integer.parseInt(parts[1]);
                int row = Integer.parseInt(parts[2]);
                int col = Integer.parseInt(parts[3]);
                char letter = parts[4].charAt(0);
                sos_Model.Player player = sos_Model.Player.valueOf(parts[5]);
                int sosFormed = Integer.parseInt(parts[6]);
                boolean gameEnded = Boolean.parseBoolean(parts[7]);
                
                return new GameMove(row, col, letter, player, sosFormed, gameEnded, moveNumber);
                
            } catch (Exception e) {
                return null;
            }
        }
    }
    
    /**
     * Simple move class for replay from txt files
     */
    public static class SimpleGameMove {
        public final int moveNumber;
        public final sos_Model.Player player;
        public final char letter;
        public final int row;
        public final int col;
        public final boolean formedSOS;
        
        public SimpleGameMove(int moveNumber, sos_Model.Player player, char letter, 
                             int row, int col, boolean formedSOS) {
            this.moveNumber = moveNumber;
            this.player = player;
            this.letter = letter;
            this.row = row;
            this.col = col;
            this.formedSOS = formedSOS;
        }
        
        @Override
        public String toString() {
            String playerStr = (player == sos_Model.Player.Player1) ? "P1" : "P2";
            String sosStr = formedSOS ? " +SOS" : "";
            return String.format("%s: %c at (%d,%d)%s", 
                playerStr, letter, row + 1, col + 1, sosStr);
        }
    }
    
    /**
     * Container for loaded game data
     */
    public static class GameReplay {
        public final int boardSize;
        public final sos_Model.Mode mode;
        public final List<GameMove> moves;
        public final String timestamp;
        
        public GameReplay(int boardSize, sos_Model.Mode mode, List<GameMove> moves, String timestamp) {
            this.boardSize = boardSize;
            this.mode = mode;
            this.moves = moves;
            this.timestamp = timestamp;
        }
    }
}