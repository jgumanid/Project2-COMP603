/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;


/**
 *
 * @author arthurchan
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CapturedPiecesPanel extends JPanel {

    private ArrayList<Piece> capturedBlack = new ArrayList<>();
    private ArrayList<Piece> capturedWhite = new ArrayList<>();

    public CapturedPiecesPanel() {
        setBackground(new Color(55, 55, 55));
    }

    // Adds the captured piece to the correct list
    public void addCapturedPiece(Piece piece) {
        if (piece == null) {
            return;
        }

        if (piece.isWhite()) {
            capturedWhite.add(piece);
        } else {
            capturedBlack.add(piece);
        }

        repaint();
    }

    // Clears the panel when a new game starts
    public void reset() {
        capturedBlack.clear();
        capturedWhite.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Captured Pieces", 10, 25);

        // Black pieces that have been captured
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Black: " + capturedBlack.size(), 10, 55);

        drawPieces(g, capturedBlack, 10, 65);

        // White pieces that have been captured
        g.drawString("White: " + capturedWhite.size(), 10, 205);

        drawPieces(g, capturedWhite, 10, 215);
    }

    // Draws the captured pieces in rows
    
private void drawPieces(Graphics g, ArrayList<Piece> pieces,
                        int startX, int startY) {
    int pieceSize = 28;
    int gap = 7;
    int columns = 4;

    for (int i = 0; i < pieces.size(); i++) {
        Piece piece = pieces.get(i);

        int row = i / columns;
        int col = i % columns;

        int x = startX + col * (pieceSize + gap);
        int y = startY + row * (pieceSize + gap);

        String imagePath;

        if (piece.isKing()) {
            imagePath = piece.isWhite()
                    ? "./resources/whiteking.png"
                    : "./resources/blackking.png";
        } else {
            imagePath = piece.isWhite()
                    ? "./resources/white.png"
                    : "./resources/black.png";
        }

        ImageIcon icon = new ImageIcon(imagePath);

        if (icon.getIconWidth() == -1) {
            System.out.println("Image failed to load: " + imagePath);
            continue;
        }

        g.drawImage(
            icon.getImage(),
            x, y,
            pieceSize, pieceSize,
            this
        );
    }
}

}
