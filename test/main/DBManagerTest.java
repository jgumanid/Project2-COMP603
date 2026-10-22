/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package main;

import java.util.List;
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
public class DBManagerTest {
 
    private static final String playerA = "JUnitA_DB";
    private static final String playerB = "JUnitB_DB";
 
    private static DBManager db;
 
    @BeforeClass
    public static void openDatabase() throws Exception {
        // re-registers Derby in case another test class already shut it down
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").getDeclaredConstructor().newInstance();
        db = new DBManager();
    }
 
    @AfterClass
    public static void closeDatabase() {
        db.close();
    }
 
    @Before
    public void cleanBefore() {
        db.deletePlayer(playerA);
        db.deletePlayer(playerB);
    }
 
    @After
    public void cleanAfter() {
        db.deletePlayer(playerA);
        db.deletePlayer(playerB);
    }
 
    //tests for adding, duplicate prevention, and deletion
    @Test
    public void addPlayer() {
        assertFalse(db.playerExists(playerA));
        db.addPlayer(playerA);
        assertTrue(db.playerExists(playerA));
    }
    
    @Test
    public void addPlayerNotDuplicate() {
        db.addPlayer(playerA);
        db.addPlayer(playerA);
 
        int count = 0;
        for (String name : db.getAllPlayerNames()) {
            if (name.equals(playerA)) count++;
        }
        assertEquals(1, count);
    }
 
    @Test
    public void deletePlayer() {
        db.addPlayer(playerA);
        db.deletePlayer(playerA);
 
        assertFalse(db.playerExists(playerA));
        assertNull(db.getPlayerStats(playerA));
    }

    //test for the database after adding a new player they start with exactly 0 records
    @Test
    public void newPlayerStartsAtZero() {
        db.addPlayer(playerA);
        DBManager.PlayerStats s = db.getPlayerStats(playerA);
 
        assertNotNull(s);
        assertEquals(playerA, s.name);
        assertEquals(0, s.gamesPlayed);
        assertEquals(0, s.wins);
        assertEquals(0, s.losses);
        assertEquals(0, s.draws);
        assertEquals(0, s.winStreak);
    }
 
    //test for the execute command in the DB to reset to 0
    @Test
    public void resetPlayerStatsSetsEverythingBackToZero() {
        db.recordGame(playerA, playerB, playerA);
        db.resetPlayerStats(playerA);
 
        DBManager.PlayerStats s = db.getPlayerStats(playerA);
        assertEquals(0, s.gamesPlayed);
        assertEquals(0, s.wins);
        assertEquals(0, s.winStreak);
    }
 
    //tests to record the wins, losses if they are added to the database
    @Test
    public void recordGameWinnerAndLoser() {
        db.recordGame(playerA, playerB, playerA);   // A (black) wins
 
        DBManager.PlayerStats a = db.getPlayerStats(playerA);
        DBManager.PlayerStats b = db.getPlayerStats(playerB);
 
        assertEquals(1, a.gamesPlayed);
        assertEquals(1, a.wins);
        assertEquals(0, a.losses);
        assertEquals(1, a.winStreak);
 
        assertEquals(1, b.gamesPlayed);
        assertEquals(0, b.wins);
        assertEquals(1, b.losses);
        assertEquals(0, b.winStreak);
    }
 
    //tests the winstreak if it resets when a player loses
    @Test
    public void winStreakGainAndResetsOnLoss() {
        db.recordGame(playerA, playerB, playerA);
        db.recordGame(playerA, playerB, playerA);
        assertEquals(2, db.getPlayerStats(playerA).winStreak);
 
        db.recordGame(playerA, playerB, playerB);   // A loses
        assertEquals(0, db.getPlayerStats(playerA).winStreak);
        assertEquals(3, db.getPlayerStats(playerA).gamesPlayed);
    }
 
    //tests for the leaderboard if they follow based on the wins loss ratio
    @Test
    public void leaderboardOrdersByWins() {
        db.recordGame(playerA, playerB, playerA);
        db.recordGame(playerA, playerB, playerA);   // A: 2 wins
        db.recordGame(playerB, playerA, playerB);   // B: 1 win
 
        List<DBManager.PlayerStats> board = db.getLeaderboard(1000);
 
        int indexA = -1, indexB = -1;
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).name.equals(playerA)) indexA = i;
            if (board.get(i).name.equals(playerB)) indexB = i;
        }
 
        assertTrue("A should be on the leaderboard", indexA >= 0);
        assertTrue("B should be on the leaderboard", indexB >= 0);
        assertTrue("A (more wins) should rank above B", indexA < indexB);
    }
}