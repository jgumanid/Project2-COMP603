/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author GGPC
 */
public class Board extends JPanel {

    //initialise board image and buttons on  playable square
    public Image board;
    public Image playerUI;

    //adds 8x8 grid of  squares where each has a button to display pieces
    private PlayableSquares[][] squares = new PlayableSquares[8][8];
    private Handler handler;

    //false = black's turn
    //true = whites's turn
    private boolean whiteTurn = false;

    //to get the player names
    private String blackName = "Player 1";
    private String whiteName = "Player 2";

    //sets up the position of the board pieces buttons
    private static final int offsetX = 22;
    private static final int offsetY = 26;
    private static final int squareSize = 56;

    private CheckersGUI gui;

    private DBManager db;                  // new
    private String blackStats = "";        // new
    private String whiteStats = "";

    private GameTimer timer;

    private CapturedPiecesPanel capturedPanel;

    private String boardTheme = "Classic";
    private boolean highlightLegalMoves = true;
    private boolean confirmBeforeLeaving = true;

    //constructor
    public Board(CheckersGUI gui, DBManager db) {
        this.gui = gui;
        this.db = db;
        panelSetup();
    }
    
    public void panelSetup(){
        //button to go back to menu
        JButton exitButton = new JButton("Leave Game");
        exitButton.setBounds(525, 20, 90, 30);
        exitButton.addActionListener(e -> leaveGame());
        add(exitButton);

        //load image of the board and ui of player
        board = new ImageIcon("./resources/boards/board_plain_01.png").getImage();
        playerUI = new ImageIcon("./resources/playerui.png").getImage();
        setLayout(null);
        setBackground(Color.GRAY);
        // Panel displaying captured pieces on the side
        capturedPanel = new CapturedPiecesPanel();
        capturedPanel.setBounds(500, 120, 155, 350);
        add(capturedPanel);
        //sets the grid
        squares = new PlayableSquares[8][8];

        timer = new GameTimer(this);

        //aligns the button grid to the board image
        //scale each buttons size by 56x56
        //creates the buttons on all dark squares and make it playable
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                //if both row and column results in odd we add a button
                if ((row + col) % 2 == 1) {
                    PlayableSquares square = new PlayableSquares(row, col);
                    square.setBounds(offsetX + col * squareSize, offsetY + row * squareSize, squareSize, squareSize);
                    squares[row][col] = square;
                    add(square);
                }
            }
        }

        setupPieces();
        //movement
        handler = new Handler(this, db);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if (squares[row][col] != null) {
                    final int currentRow = row;
                    final int currentCol = col;
                    squares[row][col].addActionListener(e -> {
                        handler.squareClicked(currentRow, currentCol);
                    });
                }
            }
        }
        flipBoardOrientation();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.board, 0, 0, 500, 500, null);
        // player UI below the board, scaled to the board's width
        g.drawImage(this.playerUI, 0, 500, null);
        //text
        g.setColor(java.awt.Color.BLACK);
        g.setFont(new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 16));
        g.drawString(blackName, 80, 535);
        g.drawString(whiteName, 395, 535);

        g.setFont(new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 14));
        g.drawString(blackStats, 80, 560);
        g.drawString(whiteStats, 395, 560);

        g.setFont(new java.awt.Font("Comic Sans MS", java.awt.Font.BOLD, 18));
        g.setColor(java.awt.Color.WHITE);
        g.drawString("Time: " + timer.getFormattedTime(), 525, 80);
    }

    public PlayableSquares getSquare(int row, int col) {
        return squares[row][col];
    }

    public void setPlayerNames(String black, String white) {
        this.blackName = black;
        this.whiteName = white;
        refreshStats();
    }

    public String winnerPlayerName(boolean white) {
        return white ? whiteName : blackName;
    }

    public void checkGameResult() {
        handler.checkGameResult();
    }

    public void recordCompletedMove(boolean wasCapture) {
        handler.recordCompletedMove(wasCapture);
    }

    //positions every square button depending on whose turn it is
    private void flipBoardOrientation() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {

                    //black's turn: rotate so black is at the bottom
                    //white's turn: normal view with white at the bottom
                    int displayRow = whiteTurn ? row : 7 - row;
                    int displayCol = whiteTurn ? col : 7 - col;

                    squares[row][col].setBounds(offsetX + displayCol * squareSize, offsetY + displayRow * squareSize, squareSize, squareSize);
                }
            }
        }
        repaint();
    }

    private void setupPieces() {
        // clear everything first so this also works on a used board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {
                    squares[row][col].setPiece(null);
                }
            }
        }
        for (int row = 0; row <= 2; row++) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                squares[row][col].setPiece(new Piece("black", false, "./resources/black.png"));
            }
        }
        for (int row = 7; row >= 5; row--) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                squares[row][col].setPiece(new Piece("white", false, "./resources/white.png"));
            }
        }
    }

    public void resetGame() {
        resetCapturedPieces();
        setupPieces();
        setWhiteTurn(false);   // black starts, and this re-flips the board
        handler.reset();
        timer.start();
        refreshStats();
    }

    public void returnToMenu() {
        gui.showMenu();
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
        flipBoardOrientation();
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void refreshStats() {
        blackStats = formatStats(blackName);
        whiteStats = formatStats(whiteName);
        repaint();
    }

    private String formatStats(String name) {
        DBManager.PlayerStats s = db.getPlayerStats(name);
        if (s == null) {
            return "W: 0  L: 0  D: 0";
        }
        return "W: " + s.wins + "  L: " + s.losses + "  D: " + s.draws;
    }

    public void stopTimer() {
        timer.stop();
    }

    //Records a captured piece in the side panel
    public void recordCapturedPiece(Piece piece) {
        capturedPanel.addCapturedPiece(piece);
    }

    public void resetCapturedPieces() {
        capturedPanel.reset();
    }

    private static final Map<String, String> BOARD_FILES = Map.of(
            "Classic", "board_plain_01.png",
            "Dirty White", "board_plain_02.png",
            "Navy Blue", "board_plain_03.png",
            "Porcelain", "board_plain_04.png",
            "Wood", "board_plain_05.png"
    );

    public void applySettings(String theme, boolean highlight, boolean confirm) {

        boardTheme = theme;
        highlightLegalMoves = highlight;
        confirmBeforeLeaving = confirm;

        String file = BOARD_FILES.getOrDefault(theme, "board_plain_01.png");
        board = new ImageIcon("./resources/boards/" + file).getImage();

        repaint();
    }

    public String getBoardTheme() {
        return boardTheme;
    }

    public boolean isHighlightLegalMoves() {
        return highlightLegalMoves;
    }

    public boolean isConfirmBeforeLeaving() {
        return confirmBeforeLeaving;
    }

    private void leaveGame() {
        if (confirmBeforeLeaving) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to leave the game?",
                    "Leave Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        timer.stop();
        gui.showMenu();
    }
}
