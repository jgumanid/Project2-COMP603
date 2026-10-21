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
    private JButton statsButton;
    private CheckersGUI gui; //reference to the main panel
    public Image menu; //bg image for menu

    public Menu(CheckersGUI gui) {
        //load menu background image
        menu = new ImageIcon("./resources/menu_background.png").getImage();
        this.gui = gui; //window reference for action listeners
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //makethe components stack vertically in order
        setBackground(Color.DARK_GRAY);

        twoPlayerButton = new JButton("Start Game"); //start button
        statsButton = new JButton("Stats");
        settingsButton = new JButton("Settings"); //settings button
        exitButton = new JButton("Exit"); //exit button

        //sets the buttons at the center of the screen horizontally 
        twoPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue()); //space in panel grows or shrink depending of the number of buttons

        add(twoPlayerButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(statsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(settingsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(exitButton);

        add(Box.createVerticalGlue());

        // just placeholders
        twoPlayerButton.addActionListener(e -> {
            String player1 = askName("Player 1: Name?", "Player 1");
            if (player1 == null) {
                return;
            }
            String player2 = askName("Player 2: Name?", "Player 2");
            if (player2 == null) {
                return;
            }
            gui.startGame(player1, player2);
        });

        statsButton.addActionListener(e -> showStats());

        settingsButton.addActionListener(e
                -> System.out.println("Settings selected"));

        exitButton.addActionListener(e
                -> System.exit(0));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.menu, 0, 0, 650, 590, null);
    }

    private String askName(String message, String defaultName) {
        String name = JOptionPane.showInputDialog(this, message, "Enter Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            return null;
        }

        name = name.trim();
        return name.isEmpty() ? defaultName : name;
    }

    private void showStats() {
        java.util.List<DBManager.PlayerStats> stats = gui.getDb().getLeaderboard(50);

        String[] columns = {"Rank", "Player", "Played", "Wins", "Losses", "Draws", "Win Streak"};

        // cells are read-only
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int rank = 1;
        for (DBManager.PlayerStats s : stats) {
            model.addRow(new Object[]{rank++, s.name, s.gamesPlayed, s.wins, s.losses, s.draws, s.winStreak});
        }
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(480, 250));

        JOptionPane.showMessageDialog(this, scroll, "Player Stats", JOptionPane.PLAIN_MESSAGE);
    }
}
