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
    
        //Make sure the move is in the board
        if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
            return false;
        }
        
        //Get starting square
        PlayableSquares startSquare = board.getSquare(startRow, startCol);
        
        //Get end square
        PlayableSquares endSquare = board.getSquare(endRow, endCol);
        
        //Make sure both squares exist
        if (startSquare == null || endSquare == null) {
            return false;
        }
        
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
        if (Math.abs(rowDifference) == 1 && Math.abs(colDifference) == 1) {
        
            //King can move forwards AND backwards
            if (piece.isKing()) {
                return true;
            }
            
            //White moves up
            if (piece.isWhite()) {
                return rowDifference == -1;
            }
        
        //Black moves downwards
        return rowDifference == 1;
        }   
        
        //Capture move is exactly 2 rows and 2 columns
        if (Math.abs(rowDifference) == 2 && Math.abs(colDifference) == 2) {
            
            //Find the square between the start and end
            int middleRow = (startRow + endRow) / 2;
            int middleCol = (startCol + endCol) / 2;
            
            PlayableSquares middleSquare = board.getSquare(middleRow, middleCol);
            
            if (middleSquare == null) {
                return false;
            }
            
            Piece middlePiece = middleSquare.getPiece();
            
            //There must be a piece in the middle
            if (middlePiece == null) {
                return false;
            }
            
            //Middle piece must belong to opponent
            if (piece.isWhite() == middlePiece.isWhite()) {
                return false;
            }
            
            //King piece can capture both forwards and backwards
            if (piece.isKing()) {
                return true;
            }
            
            //White captures upwards
            if (piece.isWhite()) {
                return rowDifference == -2;
            }
            
            //Black captures downwards
            return rowDifference == 2;
        }
        
        return false;
    }
    
    public boolean hasCapture(boolean white) {
        
        //Check every row
        for (int row = 0; row < 8; row++) {
            
            //Check every column
            for (int col = 0; col < 8; col++) {
                
                PlayableSquares square = board.getSquare(row, col);
                
                //Skip if there are no playable square
                if (square == null) {
                    continue;
                }
                
                Piece piece = square.getPiece();
                
                //Check if this is the current player's piece
                if (piece != null && piece.isWhite() == white) {
                    
                    //Check capture down-right
                    if (isValidMove(row, col, row + 2, col + 2)) {
                        return true;
                    }
                    
                    //Check capture down-left
                    if (isValidMove(row, col, row + 2, col - 2)) {
                        return true;
                    }
                    
                    //Check capture top-right
                    if (isValidMove(row, col, row - 2, col + 2)) {
                        return true;
                    }
                    
                    //Check capture top-left
                    if (isValidMove(row, col, row - 2, col - 2)) {
                        return true;
                    }
                }
            }
        }
        //No capture was found
        return false;
    }
    
    public boolean hasCaptureFrom(int row, int col) {
        
        PlayableSquares square = board.getSquare(row, col);
        
        if (square == null || square.getPiece() == null) {
            return false;
        }
        
        //Check all four possible capture directions (down-right, down-left, top-right, top-left)
        
        if (isValidMove(row, col, row + 2, col + 2)) {
            return true;
        }
        
        if (isValidMove(row, col, row + 2, col - 2)) {
            return true;
        }
        
        if (isValidMove(row, col, row - 2, col + 2)) {
            return true;
        }
        
        if (isValidMove(row, col, row - 2, col - 2)) {
            return true;
        }
        
        return false;
    }
    
    
    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        
        if (!isValidMove(startRow, startCol, endRow, endCol)) {
            return;
        }
        
        PlayableSquares startSquare = board.getSquare(startRow, startCol);
        
        PlayableSquares endSquare = board.getSquare(endRow, endCol);
        
        //Get the piece
        Piece piece = startSquare.getPiece();
        
        //Check if this is a capture
        if (Math.abs(endRow - startRow) == 2) {
            
            //Find the captured piece
            int middleRow = (startRow + endRow) / 2;
            int middleCol = (startCol + endCol) / 2;
            
            PlayableSquares middleSquare = board.getSquare(middleRow, middleCol);
            
            middleSquare.setPiece(null);
        }
        
        //Remove it from the old square
        startSquare.setPiece(null);
        
        //Put it on the new square
        endSquare.setPiece(piece);
    }
}
