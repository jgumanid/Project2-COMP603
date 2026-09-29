/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author arthurchan
 */
public class Movement {
    
    private Board board;
    
    public Movement(Board board) {
        this.board = board;
    }
    
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
    
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false;
        }
        
        //Get starting square
        PlayableSquares startSquare = board.getSquare(startRow, startCol);
        
        //Get end square
        PlayableSquares endSquare = board.getSquare(endRow, endCol);
        
        //Must be a square at the start
        if (startSquare.getPiece() == null) { 
            return false;
        }
        
        //Must be empty at the end
        if (endSquare.getPiece() != null) {
            return false;
        }
        
        Piece piece = startSquare.getPiece();
        
        //Calculate movement
        int rowDifference = endRow - startRow;
        int colDifference = Math.abs(endCol - startCol);
        
        //A normal move is exactly 1 row and one column
        if (Math.abs(rowDifference) != 1 || Math.abs(colDifference) != 1) {
            return false;
        }
        
        //White moves up
        if (piece.isWhite()) {
            return rowDifference == -1;
        }
        
        //Black moves downwards
        return rowDifference == 1;
    }
    
    public void movePiece(int startRow, int endRow, int startCol, int endCol) {
        if (!isValidMove(startRow, startCol, endRow, endCol)) {
            return;
        }
        
        PlayableSquares startSquare = board.getSquare(startRow, startCol);
        
        PlayableSquares endSquare = board.getSquare(endRow, endCol);
        
        //Get the piece
        Piece piece = startSquare.getPiece();
        
        //Remove it from the old square
        startSquare.setPiece(null);
        
        //Put it on the new square
        endSquare.setPiece(piece);
    }
}
