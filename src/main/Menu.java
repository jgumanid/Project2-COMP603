/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author GGPC
 */
public class Menu extends JPanel {
    
    private JButton twoPlayerButton; //button for two player mode
    private JButton settingsButton; //button for settings
    private JButton exitButton; //exit button
    private CheckersGUI gui; //reference to the main panel
    public Image menu; //bg image for menu

    public Menu(CheckersGUI gui) {
        //load menu background image
        menu = new ImageIcon("./resources/menu_background.png").getImage();
        this.gui = gui; //window reference for action listeners
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //makethe components stack vertically in order
        setBackground(Color.DARK_GRAY); //color of button


        twoPlayerButton = new JButton("Start Game"); //start button
        settingsButton = new JButton("Settings"); //settings button
        exitButton = new JButton("Exit"); //exit button
        
        //sets the buttons at the center of the screen horizontally 
        twoPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue()); //space in panel grows or shrink depending of the number of buttons

        add(twoPlayerButton); 
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(exitButton);

        add(Box.createVerticalGlue());

        // just placeholders
        twoPlayerButton.addActionListener(e -> {
            String player1 = askName("Player 1: Name?",  "Player 1");
            if(player1 == null) return;
            String player2 = askName("Player 2: Name?",  "Player 2");
            if(player2==null) return;
            gui.startGame(player1, player2);
        });

        settingsButton.addActionListener(e
                -> System.out.println("Settings selected"));

        exitButton.addActionListener(e
                -> System.exit(0));
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.menu, 0, 0, 715, 600, null);
    }
    
    private String askName(String message, String defaultName){
        String name = JOptionPane.showInputDialog(this, message, "Enter Name", JOptionPane.QUESTION_MESSAGE);
        if(name == null){
            return null;
        }
        
        name = name.trim();
        return name.isEmpty() ? defaultName : name;
    }
}