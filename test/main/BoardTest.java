/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package main;

import java.awt.Graphics;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author GGPC
 */
public class BoardTest {
 
    private static final String testBlack = "JUnitBlack_Board";
    private static final String testWhite = "JUnitWhite_Board";
 
    private static DBManager db;
    private Board board;
 
    @BeforeClass
    public static void openDatabase() {
        db = new DBManager();
    }
 
    @AfterClass
    public static void closeDatabase() {
        db.deletePlayer(testBlack);
        db.deletePlayer(testWhite);
        db.close();
    }
 
    @Before
    public void createBoard() {
        board = new Board(null, db);
    }
 
    @After
    public void stopTimer() {
        board.stopTimer();
    }
 
    
    //method to count pieces for each side
    private int countPieces(boolean white) {
        int count = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                PlayableSquares sq = board.getSquare(row, col);
                if (sq != null && sq.getPiece() != null && sq.getPiece().isWhite() == white) {
                    count++;
                }
            }
        }
        return count;
    }
 
    //game start setup pieces test
    //tests and checks the pieces if they are spawned on the dark squares
    //and keep light square empty. If theres pieces on light square test fails
    @Test
    public void onlyDarkSquaresExist() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 1) {
                    assertNotNull("dark square should exist", board.getSquare(row, col));
                } else {
                    assertNull("light square should be null", board.getSquare(row, col));
                }
            }
        }
    }
 
    //tests for turns
    //black goes first then next is white
    //if turns are swapped tests fails
    @Test
    public void blackGoesFirst() {
        assertFalse(board.isWhiteTurn());
    }
 
    @Test
    public void setWhiteTurnChangesTurn() {
        board.setWhiteTurn(true);
        assertTrue(board.isWhiteTurn());
 
        board.setWhiteTurn(false);
        assertFalse(board.isWhiteTurn());
    }
 
    //tests the player name's inputs
    //if theres an input it sets that name.
    //the test fails if it does update or set to default
 
    @Test
    public void setPlayerNamess() {
        board.setPlayerNames(testBlack, testWhite);
 
        assertEquals(testBlack, board.getBlackName());
        assertEquals(testWhite, board.getWhiteName());
    }
 
    //tests for the correct winner
    //the test fails if it announces wrong player
    @Test
    public void winnerPlayerNameReturnsToCorrectSide() {
        board.setPlayerNames(testBlack, testWhite);
 
        assertEquals(testWhite, board.winnerPlayerName(true));
        assertEquals(testBlack, board.winnerPlayerName(false));
    }
 
    //test for applying config to the game such as board theme
    //when user change board at setting
    //it will apply that board
    //test fails if image of board is not applied after the config
    @Test
    public void applySettingsStoresValues() {
        board.applySettings("Wood", false, false);
 
        assertEquals("Wood", board.getBoardTheme());
        assertFalse(board.isHighlightLegalMoves());
        assertFalse(board.isConfirmBeforeLeaving());
    }
 
    //test for resetting a game
    //either by leaving, remactch, or completing
    //checks if the resetGame method resets to start position of the game during these conditions
    //if reset fails test fails
    @Test
    public void resetGameRestoresStartingPosition() {
        // mess up the board: remove a black piece, a white piece, and switch turn
        board.getSquare(0, 1).setPiece(null);
        board.getSquare(7, 0).setPiece(null);
        board.setWhiteTurn(true);
 
        board.resetGame();
 
        assertEquals(12, countPieces(false));
        assertEquals(12, countPieces(true));
        assertFalse("black should start again", board.isWhiteTurn());
    }
 
    //test for recorded captured pieces during a game
    //test passes if nothing throws
    @Test
    public void recordAndResetCapturedPiecesDoNotThrow() {
        Piece captured = new Piece("white", false, "./resources/white.png");
 
        board.recordCapturedPiece(captured);
        board.resetCapturedPieces();
    }
}
 