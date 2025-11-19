package test;

import model.sos_Model;
import model.sos_computer;
import model.sos_computerSM;
import model.sos_computerGM;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Sprint 4 Test Cases: Computer Player Functionality
 * Tests acceptance criteria AC 8.1, 8.2 and AC 9.1,9.2 from Sprint Document
 */
public class sosGameTest3 {

    private sos_Model simpleGame;
    private sos_Model generalGame;
    private sos_computerSM computerSM;
    private sos_computerGM computerGM;

    @Before
    public void setUp() {
        simpleGame = new sos_Model(3, sos_Model.Mode.Simple);
        generalGame = new sos_Model(5, sos_Model.Mode.General);

        computerSM = new sos_computerSM(simpleGame);
        computerGM = new sos_computerGM(generalGame);
    }

    // AC 8.2 - Computer can make valid move (Simple)
    @Test
    public void testComputerMakesValidMoveSimpleMode() {
        int[] move = computerSM.findBestMove();
        assertNotNull(move);
        assertEquals(3, move.length);
        assertTrue(simpleGame.cellEmpty(move[0], move[1]));
    }

    // AC 8.2 - Computer can make valid move (General)
    @Test
    public void testComputerMakesValidMoveGeneralMode() {
        int[] move = computerGM.findBestMove();
        assertNotNull(move);
        assertEquals(3, move.length);
        assertTrue(generalGame.cellEmpty(move[0], move[1]));
    }

    // AC 9.1 - Computer detects and executes winning move (Simple)
    @Test
    public void testComputerDetectsWinningMoveSimpleMode() {
        // Create S _ S pattern → must place O in center
        simpleGame.getBoard()[0][0] = sos_Model.Cell.S;
        simpleGame.getBoard()[0][2] = sos_Model.Cell.S;

        int[] move = computerSM.findBestMove();
        
        assertEquals(0, move[0]);
        assertEquals(1, move[1]);
        assertEquals("Computer must place O to complete SOS", 2, move[2]);
    }

    // AC 9.2 - Computer blocks human winning move (Simple)
    @Test
    public void testComputerBlocksOpponentWinSimpleMode() {
        // Human threatens SOS: row 1 → O _ O
        simpleGame.getBoard()[1][0] = sos_Model.Cell.O;
        simpleGame.getBoard()[1][2] = sos_Model.Cell.O;

        int[] move = computerSM.findBestMove();

        assertEquals(1, move[0]);
        assertEquals(1, move[1]);
    }

    // AC 8.2 - Computer makes reasonable move when no win/block possible
    @Test
    public void testComputerMakesStrategicMove() {
        simpleGame.getBoard()[0][0] = sos_Model.Cell.S;
        simpleGame.getBoard()[2][2] = sos_Model.Cell.O;

        int[] move = computerSM.findBestMove();
        assertNotNull(move);
        assertTrue(simpleGame.cellEmpty(move[0], move[1]));
    }

    // AC 9.1 - General Mode: Computer maximizes SOS creation
    @Test
    public void testComputerMaximizesSOSInGeneralMode() {
        // Pattern that allows an SOS with center move
        generalGame.getBoard()[2][0] = sos_Model.Cell.S;
        generalGame.getBoard()[2][2] = sos_Model.Cell.S;

        int[] move = computerGM.findBestMove();
        assertNotNull(move);
        
        // Should select middle position in row 2
        assertEquals(2, move[0]);
        assertEquals(1, move[1]);
    }

    // AC 8.2 - Computer handles board nearly full
    @Test
    public void testComputerHandlesFullBoard() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (!(r == 1 && c == 1))
                    simpleGame.getBoard()[r][c] = sos_Model.Cell.S;

        int[] move = computerSM.findBestMove();
        assertEquals(1, move[0]);
        assertEquals(1, move[1]);
    }

    // AC 8.2 - Computer move format correct
    @Test
    public void testComputerMoveFormat() {
        int[] move = computerSM.findBestMove();
        assertNotNull(move);
        assertEquals(3, move.length);
        assertTrue(move[2] == 1 || move[2] == 2);
    }

    // AC 9.2 - Computer prefers center in neutral state
    @Test
    public void testComputerPrefersCenter() {
        int[] move = computerSM.findBestMove();
        assertEquals(1, move[0]);
        assertEquals(1, move[1]);
    }

    // AC 8 & 9 - AI uses class hierarchy correctly
    @Test
    public void testComputerClassHierarchy() {
        assertTrue(computerSM instanceof sos_computer);
        assertTrue(computerGM instanceof sos_computer);
    }

    // AC 8.2 - Computer avoids occupied cells
    @Test
    public void testComputerAvoidsOccupiedCells() {
        simpleGame.getBoard()[0][0] = sos_Model.Cell.S;

        int[] move = computerSM.findBestMove();
        assertTrue(simpleGame.cellEmpty(move[0], move[1]));
    }

    // AC 9.1 - Simple Mode computer recognizes win
    @Test
    public void testSimpleModeComputerRecognizesWin() {
        simpleGame.getBoard()[0][0] = sos_Model.Cell.S;
        simpleGame.getBoard()[0][2] = sos_Model.Cell.S;

        int[] move = computerSM.findBestMove();
        assertEquals(0, move[0]);
        assertEquals(1, move[1]);
        assertEquals(2, move[2]); // O
    }

    // AC 9.1 - General Mode: Computer continues after scoring
    @Test
    public void testGeneralModeComputerContinuesAfterSOS() {
        generalGame.getBoard()[0][0] = sos_Model.Cell.S;
        generalGame.getBoard()[0][2] = sos_Model.Cell.S;

        int[] firstMove = computerGM.findBestMove();
        char letter = (firstMove[2] == 1) ? 'S' : 'O';
        generalGame.move(firstMove[0], firstMove[1], letter);

        int[] secondMove = computerGM.findBestMove();
        assertNotNull(secondMove);
    }

    // AC 8.2 & 9 - Computer handles larger boards
    @Test
    public void testComputerHandlesLargeBoard() {
        sos_Model largeGame = new sos_Model(8, sos_Model.Mode.General);
        sos_computerGM largeAI = new sos_computerGM(largeGame);

        int[] move = largeAI.findBestMove();
        assertNotNull(move);
    }

    // AC 8.2 - Computer vs computer full game
    @Test
    public void testComputerVsComputerGame() {
        sos_computerSM c1 = new sos_computerSM(simpleGame);
        sos_computerSM c2 = new sos_computerSM(simpleGame);

        int moves = 0;
        while (!simpleGame.isGameOver() && moves < 9) {
            sos_computerSM current = 
                (simpleGame.getCurrentPlayer() == sos_Model.Player.Player1) ? c1 : c2;

            int[] move = current.findBestMove();
            assertNotNull(move);

            char letter = (move[2] == 1) ? 'S' : 'O';
            assertTrue(simpleGame.move(move[0], move[1], letter));

            moves++;
        }

        assertTrue(simpleGame.isGameOver());
    }
}
