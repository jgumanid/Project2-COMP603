/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;

/**
 *
 * @author GGPC
 */
public class CheckersGUI extends JFrame{
    public CheckersGUI(){
        //window setup
        Board board = new Board();
        this.add(board);
        this.setSize(730, 640);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Checkers");
    }
    
    public static void main(String[] args) {
        CheckersGUI cg =  new CheckersGUI();
        cg.setVisible(true);
    }
}
