/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.Communicator;
import client.gui.components.MainPanel;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author schuyler
 */
public class Client extends JFrame {
    
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Point ORIG_CENTER;
    public static int ORIG_WIDTH;
    public static int ORIG_HEIGHT;
    
    private Communicator communicator;
    private GraphicsDevice gd;
    
    private JMenuBar menuBar;
    private MainPanel mainPanel;
    
    public Client() {
        
        super("Record Indexer");
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        communicator = new Communicator();
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        // Initialize GUI dimensions
        SCREEN_WIDTH = gd.getDisplayMode().getWidth();
        SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
        ORIG_CENTER = new Point(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        ORIG_WIDTH = SCREEN_WIDTH * 3 / 4;
        ORIG_HEIGHT = SCREEN_HEIGHT * 3 / 4;
        
        // Set size and location of GUI
        this.setPreferredSize(new Dimension(ORIG_WIDTH, ORIG_HEIGHT));
        this.setLocation(ORIG_CENTER.x - ORIG_WIDTH / 2, ORIG_CENTER.y - ORIG_HEIGHT / 2);
        
        // Initialize menu bar
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        // Add file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        // Add Download batch menu item
        JMenuItem downloadBatchItem = new JMenuItem("Download Batch");
//        downloadBatchItem.addActionListener(downloadBatchAction);
        fileMenu.add(downloadBatchItem);
        
        // Add Logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout");
//        logoutItem.addActionListener(logoutAction);
        fileMenu.add(logoutItem);
        
        // Add Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
//        exitItem.addActionListener(exitAction);
        fileMenu.add(exitItem);
        
        mainPanel = new MainPanel();
        mainPanel.setPreferredSize(this.getPreferredSize());
        
        this.add(mainPanel);
        
    }
    
    public static void main(String[] args) {
        
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setVisible(true);
        client.pack();
        
    }
    
}
