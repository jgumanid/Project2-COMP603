/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;

/**
 *
 * @author GGPC
 */
public class MoveHighlight {

    private static final Color SELECTED_COLOR = new Color(0, 0, 255, 120); // blue
    private static final Color MOVE_COLOR = new Color(255, 255, 0, 120);   // yellow
    private static final Color CAPTURE_COLOR = new Color(0, 200, 0, 120);   // green
    private static final Color INVALID_COLOR = new Color(255, 0, 0, 140); // red

    private Board board;
    private Movement movement;

    public MoveHighlight(Board board, Movement movement) {
        this.board = board;
        this.movement = movement;
    }

    //highlights the selected piece, its legal moves and its legal captures
    public void showMoves(int row, int col, boolean mustCapture) {
        clear();

        PlayableSquares selected = board.getSquare(row, col);
        if (selected == null || selected.getPiece() == null) {
            return;
        }
        selected.setHighlight(SELECTED_COLOR);

        int[] directions = {-1, 1};

        for (int rowDir : directions) {
            for (int colDir : directions) {

                //capture (2 squares away)
                int capRow = row + 2 * rowDir;
                int capCol = col + 2 * colDir;
                if (movement.isValidMove(row, col, capRow, capCol)) {
                    board.getSquare(capRow, capCol).setHighlight(CAPTURE_COLOR);
                }

                //normal move (1 square away), skipped if a capture is forced
                if (!mustCapture) {
                    int moveRow = row + rowDir;
                    int moveCol = col + colDir;
                    if (movement.isValidMove(row, col, moveRow, moveCol)) {
                        board.getSquare(moveRow, moveCol).setHighlight(MOVE_COLOR);
                    }
                }
            }
        }
    }

    public void showInvalid(int row, int col) {
        PlayableSquares square = board.getSquare(row, col);
        if (square == null) {
            return;
        }

        square.setHighlight(INVALID_COLOR);

        //remove the red after 400ms (only if it's still red)
        javax.swing.Timer timer = new javax.swing.Timer(400, e -> {
            if (square.getHighlight() == INVALID_COLOR) {
                square.setHighlight(null);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    //removes all highlights from the board
    public void clear() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                PlayableSquares square = board.getSquare(row, col);
                if (square != null) {
                    square.setHighlight(null);
                }
            }
        }
    }
}
