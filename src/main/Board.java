/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

/**
 *
 * @author GGPC
 */
public class Board extends JPanel {

    //initialise board image and buttons on  playable square
    public Image board;

    //adds 8x8 grid of  squares where each has a button to display pieces
    private PlayableSquares[][] squares = new PlayableSquares[8][8];

    private Movement movement; //handles movement logic
    private PlayableSquares selectedSquare;//square clicked by a player

    //false = black's turn
    //true = whites's turn
    private boolean whiteTurn = false;

    private String blackName = "Player 1";
    private String whiteName = "Player 2";

    //constructor
    public Board() {

        //load image of the board
        board = new ImageIcon("./resources/boards/board_plain_01.png").getImage();
        setLayout(null);

        //sets the grid
        squares = new PlayableSquares[8][8];

        //aligns the button grid to the board image
        int offsetX = 26;
        int offsetY = 26;
        //scale each buttons size by 56x56
        int squareSize = 56;

        //creates the buttons on all dark squares and make it playable
        for (int row = 0; row < 8; row++) { // goes through all squares in 8 rows
            for (int col = 0; col < 8; col++) { //goes through each  of the 8 columns in that row

                //if both row and column results in odd we add a button
                if ((row + col) % 2 == 1) {

                    //creates a button in current sqaure
                    PlayableSquares square = new PlayableSquares(row, col);

                    //sets the size and where the buttons are placed aligning with the board.
                    square.setBounds(offsetX + col * squareSize, offsetY + row * squareSize, squareSize, squareSize);

                    squares[row][col] = square;//stores button in the grid
                    add(square);//add the button
                }
            }
        }

        //black piece
        for (int row = 0; row <= 2; row++) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                //Add black pieces (moved inside to make them all separate pieces (was only running once before))
                Piece blackPiece = new Piece("black", false, "./resources/black.png");
                squares[row][col].setPiece(blackPiece);
            }
        }

        //white piece
        for (int row = 7; row >= 5; row--) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                //Add white pieces (moved inside to make them all separate pieces (was only running once before))
                Piece whitePiece = new Piece("white", false, "./resources/white.png");
                squares[row][col].setPiece(whitePiece);
            }
        }

        //movement
        movement = new Movement(this);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if (squares[row][col] != null) {

                    final int currentRow = row;
                    final int currentCol = col;

                    squares[row][col].addActionListener(e -> {
                        squareClicked(currentRow, currentCol);
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

        g.setColor(java.awt.Color.BLACK);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        g.drawString("Black: " + blackName, 520, 110);
        g.drawString("White: " + whiteName, 520, 400);
    }

    public PlayableSquares getSquare(int row, int col) {
        return squares[row][col];
    }

    public void squareClicked(int row, int col) {

        PlayableSquares clickedSquare = squares[row][col];

        //No piece has been selected yet
        if (selectedSquare == null) {

            if (clickedSquare.getPiece() != null) {

                //Check if the piece belongs to the current player
                if (clickedSquare.getPiece().isWhite() != whiteTurn) {
                    System.out.println("Not your turn");
                    return;
                }

                selectedSquare = clickedSquare;

                System.out.println("Selected: " + row + ", " + col);
            }

            return;
        }

        //A piece is already selected
        int startRow = selectedSquare.getRow();
        int startCol = selectedSquare.getCol();

        //Check if player has a capture available
        boolean captureAvailable = movement.hasCapture(whiteTurn);

        //Check if this move is a capture
        boolean isCapture = Math.abs(row - startRow) == 2 && Math.abs(col - startCol) == 2;

        //Must capture if there is one available
        if (captureAvailable && !isCapture) {
            System.out.println("You must capture");
            selectedSquare = null;
            return;
        }

        //Try to move the piece
        if (movement.isValidMove(startRow, startCol, row, col)) {

            //Check if this move is a capture
            boolean wasCapture = Math.abs(row - startRow) == 2;

            //Move piece
            movement.movePiece(startRow, startCol, row, col);

            System.out.println("Moved from: " + startRow + ", " + startCol + " to " + row + ", " + col);

            //If it was a capture, check for another capture
            if (wasCapture && movement.hasCaptureFrom(row, col)) {

                //Keep the same player's turn
                selectedSquare = squares[row][col];

                System.out.println("Another capture available!");

                return;
            }

            //No more captures so end turn
            whiteTurn = !whiteTurn;
            repaint();
            flipBoardOrientation();
        } else {
            System.out.println("Invalid move");
        }

        //Clear selection
        selectedSquare = null;
    }

    public void setPlayerNames(String black, String white) {
        this.blackName = black;
        this.whiteName = white;
        repaint();
    }

    private String currentPlayerName() {
        return whiteTurn ? whiteName : blackName;
    }

    //positions every square button depending on whose turn it is
    private void flipBoardOrientation() {
        int offsetX = 26;
        int offsetY = 26;
        int squareSize = 56;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {

                    //black's turn: rotate so black is at the bottom
                    //white's turn: normal view with white at the bottom
                    int displayRow = whiteTurn ? row : 7 - row;
                    int displayCol = whiteTurn ? col : 7 - col;

                    squares[row][col].setBounds(
                            offsetX + displayCol * squareSize,
                            offsetY + displayRow * squareSize,
                            squareSize, squareSize);
                }
            }
        }
        repaint();
    }
}
