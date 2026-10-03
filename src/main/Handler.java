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
    private boolean whiteTurn = false;
    
    public Handler(Board board) {
        this.board = board;
        this.movement = new Movement(board);
    }
    
    public void squareClicked(int row, int col) {
        
        PlayableSquares clickedSquare = board.getSquare(row, col);
        
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
            
            Piece movedPiece = board.getSquare(row, col).getPiece();
            
            if (movedPiece.isWhite() && row == 0) {
                
                movedPiece.promote2King();
                
                board.getSquare(row, col).updateKingImage();
                
                System.out.println("White piece promoted to king");
            }
            
            if (!movedPiece.isWhite() && row == 7) {
                
                movedPiece.promote2King();
                
                board.getSquare(row, col).updateKingImage();
                
                System.out.println("Black piece promoted to king");
            }
            
            System.out.println("Moved from: " + startRow + ", " + startCol + " to " + row + ", " + col);
            
            //If it was a capture, check for another capture
            if (wasCapture && movement.hasCaptureFrom(row, col)) {
                
                //Keep the same player's turn
                selectedSquare = board.getSquare(row, col);
                
                System.out.println("Another capture available!");
                
                return;
            }
            
            //No more captures so end turn
            whiteTurn = !whiteTurn;
        }
        else
        {
            System.out.println("Invalid move");
        }
        
        //Clear selection
        selectedSquare = null;
    }

}
