/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author GGPC
 */
public class GameTimer implements Runnable{

    private final Board board;
    private volatile boolean running = false;
    private volatile long startTime;
    private volatile long elapsedMillis = 0;
    private Thread thread;

    public GameTimer(Board board) {
        this.board = board;
    }

    //resets to 00:00 and starts counting
    public synchronized void start() {
        stop();
        elapsedMillis = 0;
        startTime = System.currentTimeMillis();
        running = true;
        thread = new Thread(this, "GameTimer");
        thread.setDaemon(true);   // won't keep the program alive after the window closes
        thread.start();
        board.repaint();
    }

    //freezes the time where it is
    public synchronized void stop() {
        if (running) {
            elapsedMillis = System.currentTimeMillis() - startTime;
            running = false;
        }
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(250);   // checks often so the display never lags a full second
            } catch (InterruptedException e) {
                return;
            }
            if (running) {
                board.repaint();
            }
        }
    }
    
    //this is to display the time in a minute : second format
    public String getFormattedTime() {
        long millis = running ? System.currentTimeMillis() - startTime : elapsedMillis;
        long totalSeconds = millis / 1000;
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
    
}
