package test;

import model.sos_Model;
import model.SOSLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class sosGameTest2 {
    private sos_Model simpleGame;
    private sos_Model generalGame;

    @Before
    public void setUp() {
        simpleGame = new sos_Model(3, sos_Model.Mode.Simple);
        generalGame = new sos_Model(3, sos_Model.Mode.General);
    }

    // User Story 1 Tests
    @Test
    public void testValidBoardSize() {
        // Input: size=5. Expected: Board created successfully
        sos_Model game = new sos_Model(5, sos_Model.Mode.Simple);
        assertEquals("Board should be 5x5", 5, game.getBoard().length);
        assertEquals("Board rows should be 5", 5, game.getBoard()[0].length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoardSize() {
        // Input: size=2. Expected: Exception thrown
        new sos_Model(2, sos_Model.Mode.Simple);
    }

    // User Story 2 Tests
    @Test
    public void testGameModeSelection() {
        // Input: Select General mode. Expected: General logic initialized
        assertFalse("General game should not be simple mode", generalGame.isSimpleGameMode());
    }

    @Test
    public void testDefaultGameMode() {
        // Input: No selection. Expected: Defaults to Simple mode
        sos_Model defaultGame = new sos_Model(3, sos_Model.Mode.Simple);
        assertTrue("Default should be simple mode", defaultGame.isSimpleGameMode());
    }

    // User Story 3 Tests
    @Test
    public void testNewGameReset() {
        // After moves, call reset. Expected: Empty board, Player1 turn, scores reset
        
        // Make some moves first
        simpleGame.move(0, 0, 'S');
        simpleGame.move(0, 1, 'O');
        
        // Reset the game
        simpleGame.resetGame();
        
        // Check if board is empty
        sos_Model.Cell[][] board = simpleGame.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                assertEquals("All cells should be empty after reset", 
                            sos_Model.Cell.EMPTY, board[i][j]);
            }
        }
        
        // Check if player 1 starts
        assertEquals("Player 1 should start after reset", 
                    sos_Model.Player.Player1, simpleGame.getCurrentPlayer());
        
        // Check if scores are reset
        assertEquals("Player 1 score should be 0 after reset", 0, simpleGame.getPlayer1Score());
        assertEquals("Player 2 score should be 0 after reset", 0, simpleGame.getPlayer2Score());
    }

    // User Story 4 Tests
    @Test
    public void testValidSimpleMove() {
        // Input: (0,0,'S'). Expected: Cell contains S, turn switches
        boolean moveResult = simpleGame.move(0, 0, 'S');
        assertTrue("Move should be successful", moveResult);
        assertEquals("Cell should contain S", sos_Model.Cell.S, simpleGame.getCell(0, 0));
        assertEquals("Turn should switch to Player2", 
                    sos_Model.Player.Player2, simpleGame.getCurrentPlayer());
    }

    @Test
    public void testOccupiedCellMove() {
        // Input: occupied cell. Expected: move returns false
        
        // First move should succeed
        assertTrue("First move should succeed", simpleGame.move(0, 0, 'S'));
        
        // Second move to same cell should fail
        assertFalse("Move to occupied cell should fail", simpleGame.move(0, 0, 'O'));
    }

    // User Story 5 Tests
    @Test
    public void testSimpleGameWin() {
        // Create SOS sequence. Expected: Game ends, winner declared
        
        // Create SOS: S-O-S horizontally
        simpleGame.move(0, 0, 'S'); // Player1
        simpleGame.move(1, 0, 'S'); // Player2 - dummy move
        simpleGame.move(0, 1, 'O'); // Player1  
        simpleGame.move(1, 1, 'O'); // Player2 - dummy move
        simpleGame.move(0, 2, 'S'); // Player1 - This should create SOS and win
        
        assertTrue("Game should be over", simpleGame.isGameOver());
        assertEquals("Player1 should be winner", 
                    sos_Model.Player.Player1, simpleGame.getWinner());
    }

    @Test
    public void testSimpleGameDraw() {
        // Fill board without SOS. Expected: Draw declared
    
        simpleGame.move(0, 0, 'S'); // P1
        simpleGame.move(0, 1, 'O'); // P2
        simpleGame.move(0, 2, 'O'); // P1
        simpleGame.move(1, 0, 'O'); // P2
        simpleGame.move(1, 1, 'S'); // P1
        simpleGame.move(1, 2, 'S'); // P2
        simpleGame.move(2, 0, 'O'); // P1
        simpleGame.move(2, 1, 'O'); // P2
        simpleGame.move(2, 2, 'O'); // P1 - Final move
        
        assertTrue("Game should be over when board full", simpleGame.isGameOver());
        assertNull("Should be draw (no winner)", simpleGame.getWinner());
    }

    // User Story 6 Tests
    @Test
    public void testGeneralMoveNoSOS() {
        // Input: move without SOS. Expected: Turn switches
        boolean moveResult = generalGame.move(0, 0, 'S');
        assertTrue("Move should be successful", moveResult);
        assertEquals("Turn should switch when no SOS", 
                    sos_Model.Player.Player2, generalGame.getCurrentPlayer());
    }

    @Test
    public void testGeneralMoveWithSOS() {
        // Input: move creating SOS. Expected: Points awarded, extra turn
        
        // Setup for SOS: S-O-S horizontally
        // Player1: S at (0,0)
        generalGame.move(0, 0, 'S');
        // Player2: dummy move at (1,0) - avoid creating SOS
        generalGame.move(1, 0, 'O');
        // Player1: O at (0,1) - setup for SOS
        generalGame.move(0, 1, 'O');
        // Player2: dummy move at (1,1) - avoid creating SOS  
        generalGame.move(1, 1, 'S');
        // Player1: S at (0,2) - This should create SOS and give extra turn
        generalGame.move(0, 2, 'S');
        
        // Check if Player1 got points and still has turn
        assertTrue("Player1 should have score > 0", generalGame.getPlayer1Score() > 0);
        assertEquals("Player1 should have extra turn", 
                    sos_Model.Player.Player1, generalGame.getCurrentPlayer());
    }

    @Test
    public void testGeneralOccupiedCell() {
        // Input: occupied cell in general. Expected: Move rejected
        
        // First move should succeed
        assertTrue("First move should succeed", generalGame.move(0, 0, 'S'));
        
        // Second move to same cell should fail
        assertFalse("Move to occupied cell should fail", generalGame.move(0, 0, 'O'));
    }

    // User Story 7 Tests
    @Test
    public void testGeneralGameWin() {
        // Fill board with different scores. Expected: Higher score wins
        
        // Use a smaller board for easier control - 2x2 won't work, so use 3x3
        // but carefully control moves to ensure Player1 wins
        
        // Reset to start fresh
        generalGame.resetGame();
        
        // Setup moves to ensure Player1 gets more points
        // Player1 creates one SOS
        generalGame.move(0, 0, 'S'); // P1
        generalGame.move(1, 0, 'O'); // P2 - dummy
        generalGame.move(0, 1, 'O'); // P1
        generalGame.move(1, 1, 'S'); // P2 - dummy  
        generalGame.move(0, 2, 'S'); // P1 - creates SOS (gets point + extra turn)
        
        // Player1 continues with extra turn (no SOS)
        generalGame.move(1, 2, 'O'); // P1 - no SOS, turn switches
        
        // Fill remaining cells without creating more SOS for Player2
        generalGame.move(2, 0, 'S'); // P2
        generalGame.move(2, 1, 'O'); // P1
        generalGame.move(2, 2, 'S'); // P2 - final move
        
        // Now manually check the scores and winner
        int player1Score = generalGame.getPlayer1Score();
        int player2Score = generalGame.getPlayer2Score();
        
        // Since we created one SOS for Player1 and none for Player2, Player1 should win
        assertTrue("Player1 should have higher score", player1Score > player2Score);
        assertEquals("Player1 should be winner", 
                    sos_Model.Player.Player1, generalGame.getWinner());
    }

    @Test
    public void testGeneralGameDraw() {
        // Fill board with equal scores. Expected: Draw declared
        
        // Reset to start fresh
        generalGame.resetGame();
        
        // Setup moves to give equal scores to both players
        // This is tricky but we can create scenarios where both get same number of SOS
        
        // Fill board in a way that gives equal opportunities
        // Pattern that might create equal SOS counts
        generalGame.move(0, 0, 'S'); // P1
        generalGame.move(0, 1, 'O'); // P2
        generalGame.move(0, 2, 'S'); // P1 - creates SOS (P1: 1 point)
        
        // Player1 continues (extra turn)
        generalGame.move(1, 0, 'O'); // P1 - no SOS, turn switches
        
        generalGame.move(1, 1, 'S'); // P2
        generalGame.move(1, 2, 'O'); // P1
        generalGame.move(2, 0, 'S'); // P2 - creates SOS (P2: 1 point)
        
        // Player2 continues (extra turn)  
        generalGame.move(2, 1, 'O'); // P2 - no SOS, turn switches
        
        generalGame.move(2, 2, 'O'); // P1 - final move, no additional SOS
        
        // Both players should have 1 point each
        int player1Score = generalGame.getPlayer1Score();
        int player2Score = generalGame.getPlayer2Score();
        
        assertEquals("Both players should have equal scores", player1Score, player2Score);
        assertNull("Should be draw when scores equal", generalGame.getWinner());
    }

    // Additional Tests from 4.3
    @Test
    public void testInitialGameState() {
        // Initialize 3x3 board, both modes
        assertEquals("Initial board should be empty", 
                    sos_Model.Cell.EMPTY, simpleGame.getCell(0, 0));
        assertEquals("Player1 should start", 
                    sos_Model.Player.Player1, simpleGame.getCurrentPlayer());
        assertEquals("Initial Player1 score should be 0", 0, simpleGame.getPlayer1Score());
        assertEquals("Initial Player2 score should be 0", 0, simpleGame.getPlayer2Score());
    }

    @Test
    public void testVariousBoardSizes() {
        // Test different board sizes (3,5,8) work correctly
        int[] sizes = {3, 5, 8};
        
        for (int size : sizes) {
            sos_Model game = new sos_Model(size, sos_Model.Mode.Simple);
            assertEquals("Board should be correct size", size, game.getBoard().length);
            assertEquals("Board rows should be correct size", size, game.getBoard()[0].length);
            
            // Test that moves work on this board size
            assertTrue("Should be able to make move", game.move(0, 0, 'S'));
            assertEquals("Cell should contain S", sos_Model.Cell.S, game.getCell(0, 0));
        }
    }

    @Test
    public void testSOSAllDirections() {
        // Test SOS in all directions: horizontal, vertical, diagonal
        
        // Test horizontal SOS
        simpleGame.move(0, 0, 'S');
        simpleGame.move(1, 0, 'S'); // dummy
        simpleGame.move(0, 1, 'O');
        simpleGame.move(1, 1, 'O'); // dummy
        simpleGame.move(0, 2, 'S');
        
        // Game should be over with SOS detected
        assertTrue("Horizontal SOS should end game", simpleGame.isGameOver());
        
        // Reset for vertical test
        simpleGame.resetGame();
        simpleGame.move(0, 0, 'S');
        simpleGame.move(0, 1, 'S'); // dummy
        simpleGame.move(1, 0, 'O');
        simpleGame.move(0, 2, 'O'); // dummy
        simpleGame.move(2, 0, 'S');
        
        assertTrue("Vertical SOS should end game", simpleGame.isGameOver());
    }

    @Test
    public void testSOSLineDetection() {
        // Test that SOS lines are properly detected and stored
        simpleGame.move(0, 0, 'S');
        simpleGame.move(1, 0, 'S'); // dummy
        simpleGame.move(0, 1, 'O');
        simpleGame.move(1, 1, 'O'); // dummy
        simpleGame.move(0, 2, 'S');
        
        java.util.List<SOSLine> lines = simpleGame.getSOSLines();
        assertFalse("SOS lines should be detected", lines.isEmpty());
        
        // Check that we have at least one SOS line
        assertTrue("Should have at least one SOS line", lines.size() > 0);
    }
}