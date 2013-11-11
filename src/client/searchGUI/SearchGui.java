/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI;

import client.Communicator;
import client.searchGUI.loginPanel.LoginPanel;
import client.searchGUI.searchPanel.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class SearchGui extends JFrame {

    public static int screenWidth;
    public static int screenHeight;
    public static int guiWidth;
    public static int guiHeight;
    public static Point center;
    
    private Communicator communicator;
    private GraphicsDevice gd;
    
    private JPanel mainPanel;
    
    private LoginPanel loginDialog;
    private SearchPanel searchPanel;
        
    private String serverHost;
    private int serverPort;
    
    private String username;
    private String password;
    
    private HashMap<Integer, HashMap<String, Integer>> fields;
    private HashMap<String, String> images;
    
    public SearchGui() {

        super("Search GUI");

        communicator = new Communicator();

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JMenuItem connectItem = new JMenuItem("Connect");
        connectItem.addActionListener(connectAction);
        fileMenu.add(connectItem);
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        this.createLoginComponents();
        
    }
    
    private void createLoginComponents() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight();
        center = new Point(screenWidth / 2, screenHeight / 2) {};
        
        guiWidth = screenWidth * 3 / 4;
        guiHeight = screenHeight * 3 / 4;
        
        // Set up frame in the center of the screen
        this.setPreferredSize(new Dimension(guiWidth, guiHeight));
        this.setLocation(center.x - guiWidth / 2, center.y - guiHeight / 2);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        searchPanel = new SearchPanel(projectContext, fieldContext, stringContext, resultContext, resultViewContext);
        mainPanel.add(searchPanel);
        
        connectAction.actionPerformed(null);
        
        this.add(mainPanel);
        
    }
    
    public void loggedIn(boolean success) {
        searchPanel.loggedIn(success);
    }
    
    public void projectSelected(Integer projectId) {
        searchPanel.projectSelected(projectId);
    }
    
    public Search_Param getSearchParameters() {
        return searchPanel.getSearchParameters(username, password);
    }
    
    public void searchCompleted(Search_Result result) {
        searchPanel.searchCompleted(result);
    }
    
    public void downloadImage(String path, Integer imageId) {
        searchPanel.downloadImage(path, imageId);
    }
    
    private LoginPanel.Context loginContext = new LoginPanel.Context() {

        @Override
        public ValidateUser_Result attemptLogin(String host, int port, ValidateUser_Param validateParams) {
            
            assert communicator != null;
            
            communicator.setHost(host);
            communicator.setPort(port);
            
            ValidateUser_Result validateResult = communicator.validateUser(validateParams);
            
            if (validateResult != null && validateResult.validated()) {
                serverHost = host;
                serverPort = port;
                username = validateParams.username();
                password = validateParams.password();

                loggedIn(true);
            }

            return validateResult;
            
        }
        
    };
    
    private ProjectPanel.Context projectContext = new ProjectPanel.Context() {

        @Override
        public GetProjects_Result getProjects() {
            
            assert communicator != null;
            
            GetProjects_Param params = new GetProjects_Param(username, password);
            
            GetProjects_Result result = communicator.getProjects(params);
            if (result == null) {
//                Logger.getLogger(SearchGui.class).log(Level.SEVERE, "Could not get projects from server.");
            }
            
            return result;
            
        }

        @Override
        public void projectSelected(Integer projectId) {
            SearchGui.this.projectSelected(projectId);
        }
        
    };
    
    private FieldPanel.Context fieldContext = new FieldPanel.Context() {

        @Override
        public GetFields_Result getFields(Integer projectId) {
            
            assert communicator != null;
            
            GetFields_Param params = new GetFields_Param(username, password, projectId);
            
            GetFields_Result result = communicator.getFields(params);
            if (result == null) {
//                Logger.getLogger(SearchGui.class).log(Level.SEVERE, "Could not get fields from server.");
            }
            
            return result;
            
        }
        
    };
    
    private StringPanel.Context stringContext = new StringPanel.Context() {

        @Override
        public void runSearch() {
            
            assert communicator != null;
            
            Search_Param params = getSearchParameters();
            
            Search_Result result = communicator.search(params);
            
            if (result != null) {
                searchCompleted(result);
//                System.out.println(result.imagePaths().toString());
            }
            
        }
        
    };
    
    private ResultPanel.Context resultContext = new ResultPanel.Context() {

        @Override
        public void triggerImageDownload(String path, Integer imageId) {
            SearchGui.this.downloadImage(path, imageId);
        }
    
    };
    
    private ResultViewPanel.Context resultViewContext = new ResultViewPanel.Context() {

        @Override
        public BufferedImage downloadImage(String path) {
            
            assert communicator != null;
            
            BufferedImage image = communicator.downloadImage(path);
            
            return image;
            
        }
    };
    
    private ActionListener connectAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (loginDialog == null) {
                loginDialog = new LoginPanel(loginContext);
            }
            
            loginDialog.showDialog(SearchGui.this);
            
        }
        
    };
    
    public static void main(String[] args) {
        SearchGui gui = new SearchGui();
        gui.setVisible(true);
        gui.pack();
    }
    
}
