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
    private PlayableSquares[][] squares = new PlayableSquares[8][8];
    
    //constructor
    public Board() {
        
        //image
        board = new ImageIcon("./resources/boards/board_plain_01.png").getImage();
        setLayout(null);
        
        //sets the grid
        squares = new PlayableSquares[8][8];
        
        //aligns the button grid to the board image
        int offsetX = 26;
        int offsetY = 26;
        //scale each buttons by 56x56
        int squareSize = 56;
        
        //loop to make the grid of buttons
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if ((row + col) % 2 == 1) {

                    PlayableSquares square = new PlayableSquares(row, col);

                    square.setBounds(offsetX + col * squareSize, offsetY + row * squareSize, squareSize, squareSize);

                    squares[row][col] = square;
                    add(square);
                }
            }
        }

        // Add some pieces
        Piece blackPiece = new Piece("black" , false, "./resources/black.png");

        Piece whitePiece = new Piece("white", false, "./resources/white.png");
        
        //black piece
        for (int row = 0; row <= 2; row++) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                squares[row][col].setPiece(blackPiece);
            }
        }
        
        //white piece
        for (int row = 7; row >= 5; row--) {
            for (int col = (row % 2 == 0 ? 1 : 0); col < 8; col += 2) {
                squares[row][col].setPiece(whitePiece);
            }
        }

        
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.board, 0, 0, 500, 500, null);
    }
}
