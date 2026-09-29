/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Image;
import javax.swing.*;

/**
 *
 * @author GGPC
 */
public class Piece {
    private String color;
    private boolean king;
    private Image checkerImages;
    
    public Piece(String color, boolean king, String imagePath){
        this.color = color;
        this.king = king;
        this.checkerImages = new ImageIcon(imagePath).getImage();
    }
    
    public String getColor(){
        return color;
    }
    
    public boolean isKing(){
        return king;
    }
    
    public boolean isWhite(){
        return color.equals("white");
    }
    
    public Image getImage(){
        return checkerImages;
    }
    public void promote2King(){
        king = true;
    }
}
