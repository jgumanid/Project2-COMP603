/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package main;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arthurchan
 */
public class HandlerTest {
    
    private Board board;
    private Handler handler;
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("Starting Handler Tests");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Handler Tests Completed");
    }
    
    @Before
    public void setUp() {
        board = new Board(null, null);
        handler = new Handler(board, null);
    }
    
    @After
    public void tearDown() {
        handler = null;
        board = null;
    }

    // Newly created game should not be over
    @Test
    public void testInitialGameOverStatus() {
        assertFalse("A new game should not be over", handler.isGameOver());
    }
    
    //The game should still run after being reset
    @Test
    public void testResetKeepsGameActive() {
        handler.reset();
        
        assertFalse("The game should remain active after reset", handler.isGameOver());
    }
    
    //A non-capture move should be recorded successfully
    @Test
    public void testRecordNonCaptureMove() {
        handler.recordCompletedMove(false);
        
        assertFalse("The game should remain active", handler.isGameOver());
    }
    
    //Capture move should be recorded successfully
    @Test 
    public void testRecordCaptureMove() {
        handler.recordCompletedMove(true);
        
        assertFalse("The game should remain active", handler.isGameOver());
    }
    
    //Resetting more than once should be safe
    @Test
    public void testRepeatedReset() {
        handler.reset();
        handler.reset();
        
        assertFalse("Repeated resets should not end the game", handler.isGameOver());
    }
    
    //Clicking a square should be safe
    @Test
    public void testSquareClicked() {
        System.out.println("squareClicked");
        int row = 0;
        int col = 1;
        handler.squareClicked(row, col);

        assertFalse(handler.isGameOver());
    }

    /**
     * Test of checkGameResult method, of class Handler.
     */
    @Test
    public void testCheckGameResult() {
        System.out.println("checkGameResult");
        handler.checkGameResult();
        assertFalse(handler.isGameOver());
    }

    /**
     * Test of isGameOver method, of class Handler.
     */
    @Test
    public void testIsGameOver() {
        System.out.println("isGameOver");
        boolean expResult = false;
        boolean result = handler.isGameOver();
        assertEquals(expResult, result);
        assertFalse(handler.isGameOver());
    }

    /**
     * Test of recordCompletedMove method, of class Handler.
     */
    @Test
    public void testRecordCompletedMove() {
        System.out.println("recordCompletedMove");
        boolean wasCapture = false;
        handler.recordCompletedMove(wasCapture);
        assertFalse(handler.isGameOver());
    }

    /**
     * Test of reset method, of class Handler.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        handler.reset();
        assertFalse(handler.isGameOver());
    }
    
}
