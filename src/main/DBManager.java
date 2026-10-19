/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GGPC
 */
public class DBManager {

    private static final String url = "jdbc:derby:CheckersDB;create=true";
    private static final String table = "playerTable";

    private Connection connection;

    //player stats for the database
    public static class PlayerStats {

        public final String name;
        public final int gamesPlayed, wins, losses, draws, winStreak;

        //creates player's statistics
        public PlayerStats(String name, int gamesPlayed, int wins, int losses, int draws, int winStreak) {
            this.name = name;
            this.gamesPlayed = gamesPlayed;
            this.wins = wins;
            this.losses = losses;
            this.draws = draws;
            this.winStreak = winStreak;
        }

        @Override
        public String toString() {
            return name + " | P:" + gamesPlayed + " W:" + wins + " L:" + losses + " D:" + draws + " Streak:" + winStreak;
        }
    }
    
    //opens databbase connection and make sure table exists
    public DBManager() {
        connect();
        createTable();
    }

    //opens a connection to the embedded DB and creates if not exists yet
    private void connect() {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println(url + " connected...");
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    //creates player table if did not exist.
    //if already existed, stats are preserved
    //includes player id, name, played games, wins, loss, draw, and streak
    public void createTable() {
        if (connection == null) {
            return;
        }
        try {
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, table.toUpperCase(), null)) {
                if (rs.next()) {
                    return; // already exists, keep stats
                }
            }
            try (Statement st = connection.createStatement()) {
                st.executeUpdate("CREATE TABLE " + table + " ("
                        + "ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "PLAYER_NAME VARCHAR(50) NOT NULL UNIQUE, "
                        + "GAMES_PLAYED INT DEFAULT 0, "
                        + "WINS INT DEFAULT 0, "
                        + "LOSSES INT DEFAULT 0, "
                        + "DRAWS INT DEFAULT 0, "
                        + "WIN_STREAK INT DEFAULT 0)");
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    //checks if whether a player with the given name is already in the database
    public boolean playerExists(String name) {
        if (connection == null) return false;
        String sql = "SELECT 1 FROM " + table + " WHERE PLAYER_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }
    
    //if there are no existing player. it add it in the database with stats of 0
    public void addPlayer(String name) {
        if (connection == null || playerExists(name)) return;
        String sql = "INSERT INTO " + table + " (PLAYER_NAME) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
    
    //deletes player and thier stats from the database
    public void deletePlayer(String name) {
        if (connection == null) return;
        String sql = "DELETE FROM " + table + " WHERE PLAYER_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
    
    //resets playe stats back to 0;
    public void resetPlayerStats(String name) {
        if (connection == null) return;
        String sql = "UPDATE " + table + " SET GAMES_PLAYED = 0, WINS = 0, " + "LOSSES = 0, DRAWS = 0, WIN_STREAK = 0 WHERE PLAYER_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
    
    //records the outcome of the finished game so either players stats are updated
    //gets name from the input during start
    public void recordGame(String blackName, String whiteName, String winner) {
        if (connection == null) return;
        try {
            connection.setAutoCommit(false);
            updatePlayer(blackName, winner == null ? Result.DRAW : winner.equals(blackName) ? Result.WIN : Result.LOSS);
            updatePlayer(whiteName, winner == null ? Result.DRAW : winner.equals(whiteName) ? Result.WIN : Result.LOSS);
            connection.commit();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ignored) { }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) { }
        }
    }
    
    //outcomes of a game of a player
    private enum Result { WIN, LOSS, DRAW }

    
    //updates payer stats in a result of a single game
    private void updatePlayer(String name, Result result) throws SQLException {
        addPlayer(name);
        String sql;
        switch (result) {
            case WIN:
                sql = "UPDATE " + table + " SET GAMES_PLAYED = GAMES_PLAYED + 1, " + "WINS = WINS + 1, WIN_STREAK = WIN_STREAK + 1 WHERE PLAYER_NAME = ?";
                break;
            case LOSS:
                sql = "UPDATE " + table + " SET GAMES_PLAYED = GAMES_PLAYED + 1, " + "LOSSES = LOSSES + 1, WIN_STREAK = 0 WHERE PLAYER_NAME = ?";
                break;
            default:
                sql = "UPDATE " + table + " SET GAMES_PLAYED = GAMES_PLAYED + 1, " + "DRAWS = DRAWS + 1, WIN_STREAK = 0 WHERE PLAYER_NAME = ?";
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }
    
    //retrieve the stats of a player;
    public PlayerStats getPlayerStats(String name) {
        if (connection == null) return null;
        String sql = "SELECT PLAYER_NAME, GAMES_PLAYED, WINS, LOSSES, DRAWS, WIN_STREAK " + "FROM " + table + " WHERE PLAYER_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? readStats(rs) : null;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    // returns top players ordered by wins, then current streak
    public List<PlayerStats> getLeaderboard(int limit) {
        List<PlayerStats> list = new ArrayList<>();
        if (connection == null) return list;
        String sql = "SELECT PLAYER_NAME, GAMES_PLAYED, WINS, LOSSES, DRAWS, WIN_STREAK " + "FROM " + table + " ORDER BY WINS DESC, WIN_STREAK DESC " + "FETCH FIRST " + limit + " ROWS ONLY";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(readStats(rs));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return list;
    }

    //returns the name of all players stored in the database;
    public List<String> getAllPlayerNames() {
        List<String> names = new ArrayList<>();
        if (connection == null) {
            return names;
        }
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT PLAYER_NAME FROM " + table + " ORDER BY PLAYER_NAME")) {
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return names;
    }

    //converts the current row of a Result set into player stats object and must be
    //positioned in a valid row
    private PlayerStats readStats(ResultSet rs) throws SQLException {
        return new PlayerStats(rs.getString("PLAYER_NAME"), rs.getInt("GAMES_PLAYED"),
                rs.getInt("WINS"), rs.getInt("LOSSES"), rs.getInt("DRAWS"),
                rs.getInt("WIN_STREAK"));
    }
    
    //closes the database and shut down embedded db. Calls it when application exits
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) { }
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            // Derby always throws on shutdown; XJ015 means it shut down normally
            if (!"XJ015".equals(ex.getSQLState())) {
                System.err.println("Derby shutdown: " + ex.getMessage());
            }
        }
    }
}
