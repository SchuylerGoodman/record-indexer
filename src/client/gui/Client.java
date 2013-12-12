/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.Communicator;
import client.gui.components.DownloadBatchDialog;
import client.gui.components.LoginDialog;
import client.gui.components.MainPanel;
import client.gui.model.cell.*;
import client.gui.model.communication.*;
import client.gui.model.image.*;
import client.gui.model.record.*;
import client.gui.model.save.*;
import client.gui.model.save.settings.IndexerState;
import client.gui.model.save.settings.WindowSettings;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import shared.communication.DownloadBatch_Result;
;
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
    
    private CommunicationLinker communicationLinker;
    private RecordLinker recordLinker;
    private CellLinker cellLinker;
    private ImageLinker imageLinker;
    private SaveLinker saveLinker;
    
    private SaveNotifier saveNotifier;
    
    private JMenuBar menuBar;
    private MainPanel mainPanel;
    private JDialog loginDialog;
    private DownloadBatchDialog downloadBatchDialog;
    private JMenuItem downloadBatchItem;
    
    public Client(String host, int port) {
        
        super("Record Indexer");
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        communicator = new Communicator();
        communicator.initialize("HTTP", host, port);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        createLinkers();
        
        communicationLinker.subscribe(communicationSubscriber);
        
        saveNotifier = saveLinker.getSaveNotifier();
        saveLinker.subscribe(saveSubscriber);
        
        this.addWindowListener(clientWindowListener);
        
        createComponents();
        
    }
    
    private void createLinkers() {
        
        // Initialize linkers, models, and notifiers
        communicationLinker = new CommunicationLinker(communicator);
        recordLinker = new RecordLinker();
        cellLinker = new CellLinker();
        imageLinker = new ImageLinker();
        saveLinker = new SaveLinker();

        // Set up connections between models
        communicationLinker.link(imageLinker, recordLinker, saveLinker);
        recordLinker.link(communicationLinker, saveLinker);
        cellLinker.link(communicationLinker, recordLinker, saveLinker);
        imageLinker.link(communicationLinker, saveLinker);
        saveLinker.link(communicationLinker);
        
    }
    
    private void createComponents() {
        
        this.setVisible(false);
        
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
        downloadBatchItem = new JMenuItem("Download Batch");
        downloadBatchItem.addActionListener(downloadBatchAction);
        fileMenu.add(downloadBatchItem);
        
        // Add Logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(logoutAction);
        fileMenu.add(logoutItem);
        
        // Add Exit menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(exitAction);
        fileMenu.add(exitItem);
        
        mainPanel = new MainPanel(communicationLinker, recordLinker, cellLinker,
                                  imageLinker, saveLinker);
        
        mainPanel.setPreferredSize(this.getPreferredSize());

        this.add(mainPanel);
        this.pack();
        
        loginDialog = new LoginDialog(this, communicationLinker, exitAction, loginDialogCloseListener);

        downloadBatchDialog = new DownloadBatchDialog(this, communicationLinker);

        loginDialog.setVisible(true);
        
    }
    
    /**
     * Empties the contents of all child components without saving.
     */
    private void empty() {
        communicationLinker.getCommunicationNotifier().empty();
    }
    
    /**
     * Sets up a window closing event which is caught by the clientWindowListener
     * to prompt a save of all user data, if a user is logged in.
     */
    private void closeSafely() {
        
        WindowEvent wEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wEvent);
        
    }
    
    /**
     * Tells the SaveModel to save all current information.
     */
    private void save() {
        saveNotifier.save();
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber =
                                        new AbstractCommunicationSubscriber() {

        @Override
        public void setBatch(DownloadBatch_Result result) {
            if (result.equals(new DownloadBatch_Result())) {
                downloadBatchItem.setEnabled(true);
            }
            else {
                downloadBatchItem.setEnabled(false);
            }
        }
        
    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public WindowSettings saveWindowSettings() {
            
            Dimension thisSize = Client.this.getSize();
            Point thisLocation = Client.this.getLocation();
            return new WindowSettings(thisSize, thisLocation);
            
        }

        @Override
        public void setIndexerState(IndexerState state) {
            downloadBatchItem.setEnabled(false);
        }
        
        @Override
        public void setWindowSettings(WindowSettings settings) {
            setSize(settings.windowSize());
            setLocation(settings.windowLocation());
        }
        
    };
    
    private ActionListener downloadBatchAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            downloadBatchDialog.setVisible(true);
        }
        
    };

    private ActionListener logoutAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            save();
            empty();
            Client.this.setVisible(false);
            loginDialog.setVisible(true);
        }
        
    };
    
    private ActionListener exitAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            loginDialog.dispose();
            closeSafely();
        }
        
    };
    
    private WindowAdapter clientWindowListener = new WindowAdapter() {
        
        @Override
        public void windowClosing(WindowEvent e) {
            save();
        }
        
    };
    
    private WindowAdapter loginDialogCloseListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            closeSafely();
        }
        
    };
    
    public static void main(String[] args) {
        
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                Client client = new Client(host, port);
            }
        });

    }
    
}
