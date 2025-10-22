package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import model.sos_Model;

public class SosGameTest {
    private sos_Model game;

    @Before
    public void setUp() {
        game = new sos_Model(3, sos_Model.Mode.Simple);
    }

    @Test
    public void testInitialGameState() {
        assertEquals(sos_Model.Player.Player1, game.getCurrentPlayer());
        
        // Test initial board is empty
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(sos_Model.Cell.EMPTY, game.getCell(i, j));
            }
        }
    }

    @Test
    public void testValidMove() {
        assertTrue(game.move(0, 0, 'S'));
        assertEquals(sos_Model.Cell.S, game.getCell(0, 0));
        
        assertTrue(game.move(1, 1, 'O'));
        assertEquals(sos_Model.Cell.O, game.getCell(1, 1));
    }

    @Test
    public void testInvalidMove() {
        // Test move outside board
        assertFalse(game.move(-1, 0, 'S'));
        assertFalse(game.move(3, 0, 'S'));
        
        // Test move on occupied cell
        game.move(0, 0, 'S');
        assertFalse(game.move(0, 0, 'O'));
    }

    @Test
    public void testGameReset() {
        game.move(0, 0, 'S');
        game.move(1, 1, 'O');
        
        game.resetGame();
        
        // Test board is empty after reset
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                assertEquals(sos_Model.Cell.EMPTY, game.getCell(i, j));
            }
        }
        assertEquals(sos_Model.Player.Player1, game.getCurrentPlayer());
    }

    @Test
    public void testBoardSize() {
        sos_Model largeGame = new sos_Model(8, sos_Model.Mode.Simple);
        assertEquals(sos_Model.Cell.EMPTY, largeGame.getCell(7, 7));
        assertEquals(sos_Model.Cell.EMPTY, largeGame.getCell(0, 7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBoardSize() {
        new sos_Model(2, sos_Model.Mode.Simple);
    }
}
