/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author GGPC
 */
public class CheckersGUI extends JFrame {

    private JPanel mainPanel; //makes a main panel so we can change between two panels
    private Board board; //handles the board panel
    private Menu menu; //handles the menu panel
    private DBManager db; //handles databasemanager

    public CheckersGUI() {
        db = new DBManager();
        
        button();
        board = new Board(this, db); //creates the board
        menu = new Menu(this); //creates the menu

        mainPanel = new JPanel(new CardLayout()); //holds multiple screens, used to switch between panels.
        mainPanel.add(menu, "Menu");//panel for Menu
        mainPanel.add(board, "Board");//panel for board
        add(mainPanel);//put panel inside the window
        showMenu();//shows the menu panel first
    }

    public void showMenu() {
        //gets the panel layout manager to switch panel
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "Menu"); //show the card from mainPanel with the name Menu
    }

    public void showBoard() {
        //gets the panel layout manager to switch panel
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "Board"); //show the card from mainPanel with the name Board
    }

    public static void main(String[] args) {
        CheckersGUI cg = new CheckersGUI();
        cg.setVisible(true);
    }

    //returns database manager for other class
    public DBManager getDb() {
        return db;
    }

    //starts the game and reads the players names for the database
    public void startGame(String player1, String player2) {
        db.addPlayer(player1);
        db.addPlayer(player2);
        board.resetGame();
        board.setPlayerNames(player1, player2);
        showBoard();
    }
    
    public void button(){
        //window setup
        setSize(665, 623); // window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes program when close is clicked
        setLocationRelativeTo(null); //places the window at the center of the screen
        setTitle("Checkers"); //add the title Checkers on the window

        //closes database on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.close();
            }
        });
    }
}
