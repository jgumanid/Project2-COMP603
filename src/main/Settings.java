/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author arthurchan
 */
public class Settings extends JPanel {

    private final CheckersGUI gui;
    private final Board bp;

    private JComboBox<String> boardTheme;
    private JCheckBox highlightMoves;
    private JCheckBox confirmLeave;

    public Settings(CheckersGUI gui, Board bp) {
        this.gui = gui;
        this.bp = bp;

        buttonSetup();
    }
    
    private void buttonSetup(){
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(new Color(45, 45, 45));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel themeLabel = new JLabel("Board Appearance");
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        boardTheme = new JComboBox<>(
                new String[]{"Classic", "Dirty White", "Navy Blue", "Porcelain", "Wood"});
        boardTheme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        boardTheme.setAlignmentX(Component.LEFT_ALIGNMENT);

        boardTheme.addActionListener(e -> {
            if (gui != null) {
                String theme = (String) boardTheme.getSelectedItem();
                gui.applySettings(theme, highlightMoves.isSelected(), confirmLeave.isSelected());
            }
        });
        
        highlightMoves = new JCheckBox("Highlight legal moves");
        highlightMoves.setSelected(true);
        highlightMoves.setForeground(Color.WHITE);
        highlightMoves.setBackground(new Color(45, 45, 45));
        highlightMoves.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmLeave = new JCheckBox("Confirm before leaving a game");
        confirmLeave.setSelected(true);
        confirmLeave.setForeground(Color.WHITE);
        confirmLeave.setBackground(new Color(45, 45, 45));
        confirmLeave.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = new JButton("Save Settings");
        JButton backButton = new JButton("Back to Menu");

        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        settingsPanel.add(title);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        settingsPanel.add(themeLabel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        settingsPanel.add(boardTheme);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        settingsPanel.add(highlightMoves);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        settingsPanel.add(confirmLeave);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        settingsPanel.add(saveButton);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        settingsPanel.add(backButton);

        add(settingsPanel);

        saveButton.addActionListener(e -> saveSettings());

        backButton.addActionListener(e -> gui.showMenu());
    }

    private void saveSettings() {
        String theme = (String) boardTheme.getSelectedItem();

        boolean highlight = highlightMoves.isSelected();
        boolean confirm = confirmLeave.isSelected();

        gui.applySettings(theme, highlight, confirm);

        JOptionPane.showMessageDialog(
                this,
                "Settings saved successfully!",
                "Settings",
                JOptionPane.INFORMATION_MESSAGE
        );

        gui.showMenu();
    }

    public void loadSettings(String theme, boolean highlight, boolean confirm) {
       
        highlightMoves.setSelected(highlight);
        confirmLeave.setSelected(confirm);
        boardTheme.setSelectedItem(theme);
    }
    
    String getSelectedBoardTheme() {
        return (String) boardTheme.getSelectedItem();
    }
    
    boolean isHighlightedMovesSelected() {
        return highlightMoves.isSelected();
    }
    
    boolean isConfirmLeaveSelected() {
        return confirmLeave.isSelected();
    }
    
    String[] getAvailableThemes() {
        String[] themes = new String[boardTheme.getItemCount()];
        
        for (int i = 0; i < boardTheme.getItemCount(); i++) {
            themes[i] = boardTheme.getItemAt(i);
        }
        
        return themes;
    }
}
