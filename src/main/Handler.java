/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author arthurchan
 */
public class Handler {

    private Board board;
    private Movement movement;

    private PlayableSquares selectedSquare;

    //false black's turn
    //true white's turn
    private boolean gameOver = false;
    private int noCaptureMoves = 0;

    private MoveHighlight highlighter;
    private DBManager db;

    public Handler(Board board, DBManager db) {
        this.board = board;
        this.movement = new Movement(board);
        this.db = db;
        this.highlighter = new MoveHighlight(board, movement);

    }

    public void squareClicked(int row, int col) {
        if (gameOver) {
            return;
        }

        PlayableSquares clicked = board.getSquare(row, col);
        boolean whiteTurn = board.isWhiteTurn();

        //nothing selected yet: try to pick up a piece
        if (selectedSquare == null) {
            trySelectPiece(clicked, whiteTurn);
            return;
        }

        //clicked another of your own pieces: switch selection
        if (isOwnPiece(clicked, whiteTurn)) {
            selectPiece(clicked, whiteTurn);
            return;
        }

        //otherwise the click is a destination
        tryMove(clicked, whiteTurn);
    }

// ---------- selecting ----------
    private boolean isOwnPiece(PlayableSquares square, boolean whiteTurn) {
        Piece piece = square.getPiece();
        return piece != null && piece.isWhite() == whiteTurn;
    }

    private void trySelectPiece(PlayableSquares square, boolean whiteTurn) {
        if (square.getPiece() == null) {
            return;
        }
        if (!isOwnPiece(square, whiteTurn)) {
            System.out.println("Not your turn");
            return;
        }
        selectPiece(square, whiteTurn);
    }

    private void selectPiece(PlayableSquares square, boolean whiteTurn) {
        selectedSquare = square;
        highlighter.showMoves(square.getRow(), square.getCol(), movement.hasCapture(whiteTurn));
        System.out.println("Selected: " + square.getRow() + ", " + square.getCol());
    }

// ---------- moving ----------
    private void tryMove(PlayableSquares target, boolean whiteTurn) {
        int startRow = selectedSquare.getRow();
        int startCol = selectedSquare.getCol();
        int endRow = target.getRow();
        int endCol = target.getCol();

        boolean isCapture = Math.abs(endRow - startRow) == 2 && Math.abs(endCol - startCol) == 2;

        //must capture if one is available
        if (movement.hasCapture(whiteTurn) && !isCapture) {
            rejectMove(target, "You must capture");
            return;
        }

        if (!movement.isValidMove(startRow, startCol, endRow, endCol)) {
            rejectMove(target, "Invalid move");
            return;
        }

        executeMove(startRow, startCol, endRow, endCol, isCapture, whiteTurn);
    }

    private void rejectMove(PlayableSquares target, String message) {
        System.out.println(message);
        highlighter.clear();
        highlighter.showInvalid(target.getRow(), target.getCol());
        selectedSquare = null;
    }

    private void executeMove(int startRow, int startCol, int endRow, int endCol,
            boolean wasCapture, boolean whiteTurn) {
        movement.movePiece(startRow, startCol, endRow, endCol);
        promoteIfNeeded(endRow, endCol);
        System.out.println("Moved from: " + startRow + ", " + startCol + " to " + endRow + ", " + endCol);

        if (wasCapture && movement.hasCaptureFrom(endRow, endCol)) {
            continueCapture(endRow, endCol);
            return;
        }

        endTurn(whiteTurn, wasCapture);
    }

// ---------- results of a move ----------
//returns true if the piece was promoted by this move
    private boolean promoteIfNeeded(int row, int col) {
        PlayableSquares square = board.getSquare(row, col);
        Piece piece = square.getPiece();

        boolean reachedKingRow = piece.isWhite() ? row == 0 : row == 7;
        if (piece.isKing() || !reachedKingRow) {
            return false;
        }

        piece.promote2King();
        square.updateKingImage();
        System.out.println((piece.isWhite() ? "White" : "Black") + " piece promoted to king");
        return true;
    }

    private void continueCapture(int row, int col) {
        //same player keeps the turn and must keep jumping with this piece
        selectedSquare = board.getSquare(row, col);
        highlighter.showMoves(row, col, true);
        System.out.println("Another capture available!");
    }

    private void endTurn(boolean whiteTurn, boolean wasCapture) {
        board.setWhiteTurn(!whiteTurn);
        recordCompletedMove(wasCapture);   //checks for win or draw
        highlighter.clear();
        selectedSquare = null;
    }

    public void checkGameResult() {

        if (gameOver) {
            return;
        }

        boolean blackHasPieces = false;
        boolean whiteHasPieces = false;

        //Check whether either player has any pieces left
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                PlayableSquares square = board.getSquare(row, col);

                if (square == null) {
                    continue;
                }

                Piece piece = square.getPiece();

                if (piece != null) {
                    if (piece.isWhite()) {
                        whiteHasPieces = true;
                    } else {
                        blackHasPieces = true;
                    }
                }
            }
        }

        //Black has no pieces left
        if (!blackHasPieces) {
            String winner = board.winnerPlayerName(true);
            endGame("Game over! " + winner + " wins!", winner);
            return;
        }

//White has no pieces left
        if (!whiteHasPieces) {
            String winner = board.winnerPlayerName(false);
            endGame("Game over! " + winner + " wins!", winner);
            return;
        }

//Check whether the player can move
        boolean whiteTurn = board.isWhiteTurn();

        if (!movement.hasAnyMove(whiteTurn)) {
            String winner = board.winnerPlayerName(!whiteTurn);
            endGame("Game over! " + winner + " wins because their opponent has no legal moves!", winner);
            return;
        }

// Draw
        if (noCaptureMoves >= 80) {   // change back to 80 when done testing
            endGame("Draw! 80 turns has passed without a capture.", null);
        }
    }

    private void endGame(String message, String winnerName) {

        gameOver = true;
        highlighter.clear();
        selectedSquare = null;
        db.recordGame(board.getBlackName(), board.getWhiteName(), winnerName);

        Object[] options = {"Rematch", "Main Menu"};
        int choice = javax.swing.JOptionPane.showOptionDialog(
                board, message, "Game Over",
                javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            board.resetGame();
        } else if (choice == 1) {
            board.returnToMenu();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void recordCompletedMove(boolean wasCapture) {
        if (gameOver) {
            return;
        }
        if (wasCapture) {
            noCaptureMoves = 0;
        } else {
            noCaptureMoves++;
        }

        checkGameResult();
    }

    public void reset() {
        gameOver = false;
        noCaptureMoves = 0;
        selectedSquare = null;
        highlighter.clear();
    }
}
