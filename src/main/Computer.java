/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author GGPC
 */
public class Computer {
    private Board board;
    private Movement movement;
    private Random random = new Random();
    
    public Computer(Board board, Movement movement){
        this.board = board;
        this.movement = movement;
    }
    
    public void takeTurn() {
        boolean white = board.isWhiteTurn();
        List<int[]> moves = getAllMoves(white);

        if (moves.isEmpty()) {
            System.out.println("Computer has no moves");
            return;
        }

        //pick a random move: {startRow, startCol, endRow, endCol}
        int[] m = moves.get(random.nextInt(moves.size()));
        boolean capture = Math.abs(m[2] - m[0]) == 2;

        movement.movePiece(m[0], m[1], m[2], m[3]);
        promoteIfNeeded(m[2], m[3]);

        int row = m[2];
        int col = m[3];

        //keep jumping while more captures are available
        while (capture && movement.hasCaptureFrom(row, col)) {
            List<int[]> next = getMovesFrom(row, col, 2);
            int[] n = next.get(random.nextInt(next.size()));

            movement.movePiece(n[0], n[1], n[2], n[3]);
            row = n[2];
            col = n[3];
            promoteIfNeeded(row, col);
        }

        //end the computer's turn
        board.setWhiteTurn(!white);
    }
    private List<int[]> getAllMoves(boolean white) {
        int distance = movement.hasCapture(white) ? 2 : 1;
        List<int[]> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                PlayableSquares sq = board.getSquare(row, col);
                if (sq == null || sq.getPiece() == null) continue;
                if (sq.getPiece().isWhite() != white) continue;

                moves.addAll(getMovesFrom(row, col, distance));
            }
        }
        return moves;
    }

    //legal moves from one square, distance 1 = step, 2 = jump
    private List<int[]> getMovesFrom(int row, int col, int d) {
        List<int[]> moves = new ArrayList<>();
        int[] dirs = {-d, d};

        for (int dr : dirs) {
            for (int dc : dirs) {
                if (movement.isValidMove(row, col, row + dr, col + dc)) {
                    moves.add(new int[]{row, col, row + dr, col + dc});
                }
            }
        }
        return moves;
    }

    private void promoteIfNeeded(int row, int col) {
        Piece p = board.getSquare(row, col).getPiece();
        if (p == null || p.isKing()) return;

        if ((p.isWhite() && row == 0) || (!p.isWhite() && row == 7)) {
            p.promote2King();
            board.getSquare(row, col).updateKingImage();
        }
    }
}
