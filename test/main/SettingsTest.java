/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

package main;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettingsTest {

    private Settings settings;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Starting Settings tests");
    }
    
    @AfterClass
    public static void tearDownClass() {
            System.out.println("Settings tests Completed");
    }
    
    @Before
    public void setUp() {
        settings = new Settings(null, null);
    }
    
    @After
    public void tearDown() {
        settings = null;
    }

    //Check that all settings have correct default settings
    @Test
    public void testDefaultSettings() {
        assertEquals("Classic", settings.getSelectedBoardTheme());
        assertTrue(settings.isHighlightedMovesSelected());
        assertTrue(settings.isConfirmLeaveSelected());
    }

    // Check that all 5 board themes are available
    @Test
    public void testAvailableBoardThemes() {
        String[] expected = {
            "Classic", "Dirty White", "Navy Blue",
            "Porcelain", "Wood"
        };

        assertArrayEquals(expected, settings.getAvailableThemes());
    }

    //Check that both options can be disabled
    @Test
    public void testDisableSettings() {
        settings.loadSettings(null, false, false);

        assertFalse(settings.isHighlightedMovesSelected());
        assertFalse(settings.isConfirmLeaveSelected());
    }

    //Check that both options can be enabled
    @Test
    public void testEnableSettings() {
        settings.loadSettings(null, true, true);

        assertTrue(settings.isHighlightedMovesSelected());
        assertTrue(settings.isConfirmLeaveSelected());
    }

    //Check that mixed combinations of options work
    @Test
    public void testMixedSettings() {
        settings.loadSettings(null, false, true);

        assertFalse(settings.isHighlightedMovesSelected());
        assertTrue(settings.isConfirmLeaveSelected());

        settings.loadSettings(null, true, false);

        assertTrue(settings.isHighlightedMovesSelected());
        assertFalse(settings.isConfirmLeaveSelected());
    }
    
    //Check that settings can be changed multiple times
    @Test
    public void testRepeatedSettingChanges() {
        settings.loadSettings(null, false, false);
        settings.loadSettings(null, true, true);
        settings.loadSettings(null, false, true);
        
        assertFalse(settings.isHighlightedMovesSelected());
        assertTrue(settings.isConfirmLeaveSelected());
    }
}
