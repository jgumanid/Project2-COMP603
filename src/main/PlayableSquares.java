/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author GGPC
 */
public class PlayableSquares extends JButton {

    private int row;
    private int col;
    private Piece piece;
    private Color highlight; // null = no highlight

    public PlayableSquares(int row, int col) {
        this.row = row;
        this.col = col;
        setOpaque(true);
        setContentAreaFilled(false);
        setBorderPainted(true);
        setFocusPainted(false);

        //test click for buttons (added in board)
        /*
        addActionListener(e -> {
            System.out.println("Clicked square: " + row + ", " + col);
        }); 
         */
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

        if (piece != null) {
            ImageIcon icon = new ImageIcon(piece.getImage());

            // resize piece image to fit inside 56x56 button
            Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(image));

        } else {
            setIcon(null);
        }

        repaint();
    }

    public void updateKingImage() {

        if (piece == null) {
            return;
        }

        ImageIcon kingIcon;

        if (piece.isWhite()) {
            kingIcon = new ImageIcon("./resources/whiteking.png");
        } else {
            kingIcon = new ImageIcon("./resources/blackking.png");
        }

        Image image = kingIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(image));

        repaint();
    }

    public Color getHighlight() {      // NEW
        return highlight;
    }

    public void setHighlight(Color color) {
        this.highlight = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //draw the colour first so the piece icon appears on top
        if (highlight != null) {
            g.setColor(highlight);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}
