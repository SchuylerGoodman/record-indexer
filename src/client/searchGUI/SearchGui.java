/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI;

import client.searchGUI.searchPanel.children.*;
import client.Communicator;
import client.searchGUI.loginPanel.LoginPanel;
import client.searchGUI.searchPanel.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
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
        
    private String username;
    private String password;
    
    public SearchGui() {

        super("Search GUI");
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        this.createComponents(true);
        
    }
    
    /**
     * Create the components for the SearchGui
     * 
     * @param logIn True if the login dialog is desired upon starting
     */
    private void createComponents(boolean logIn) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        communicator = new Communicator();

        // Create Menu
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        // Add Login button
        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.addActionListener(loginAction);
        fileMenu.add(loginItem);
        
        // Add logout button
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(logoutAction);
        fileMenu.add(logoutItem);
        
        // Initialize window size based on display
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight();
        center = new Point(screenWidth / 2, screenHeight / 2) {};
        guiWidth = screenWidth * 3 / 4;
        guiHeight = screenHeight * 3 / 4;
        
        // Set up frame in the center of the screen
        this.setPreferredSize(new Dimension(guiWidth, guiHeight));
        this.setLocation(center.x - guiWidth / 2, center.y - guiHeight / 2);
        
        // Create main container panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create search GUI panel and pass in contexts
        searchPanel = new SearchPanel(projectContext, fieldContext, stringContext, resultContext, resultViewContext);
        mainPanel.add(searchPanel);
        
        // Open login dialog if desired
        if (logIn) {
            loginAction.actionPerformed(null);
        }
        
        // Add the main panel to the frame
        this.add(mainPanel);
        
    }
    
    /**
     * Notification function that tells the ProjectPanel that a login has
     * occurred and, if it was successful, to download the projects.
     * 
     * @param success If the login was successful
     */
    public void loggedIn(boolean success) {
        searchPanel.loggedIn(success);
    }
    
    /**
     * Notification function that tells the FieldPanel that a project has been
     * selected and to download the fields for that project.
     * 
     * @param projectId ID of the project that was selected
     */
    public void projectSelected(Integer projectId) {
        searchPanel.projectSelected(projectId);
    }
    
    /**
     * Traverses the component tree down to the StringPanel to extract the
     * Search_Param object to send a Search request to the server.
     * 
     * @return Search_Param with the information to request from the server
     */
    public Search_Param getSearchParameters() {
        return searchPanel.getSearchParameters(username, password);
    }
    
    /**
     * Notifies the ResultPanel that a search has been completed and that it can
     * now extract and display the results.
     * 
     * @param result Search_Result object with the results of the Search request
     */
    public void searchCompleted(Search_Result result) {
        searchPanel.searchCompleted(result);
    }
    
    /**
     * Notifies the ResultViewPanel that an image has been selected and that
     * it can now download the image data from the server.
     * 
     * @param path The relative path to the image on the server
     * @param imageId The ID of the image being downloaded (for caching purposes)
     */
    public void downloadImage(String path, Integer imageId) {
        searchPanel.downloadImage(path, imageId);
    }
    
    /**
     * Context used by the LoginPanel to log in to the server.
     */
    private LoginPanel.Context loginContext = new LoginPanel.Context() {

        @Override
        public ValidateUser_Result attemptLogin(String host, int port, ValidateUser_Param validateParams) {
            
            assert communicator != null;
            
            communicator.setHost(host);
            communicator.setPort(port);
            
            ValidateUser_Result validateResult = communicator.validateUser(validateParams);
            
            if (validateResult != null && validateResult.validated()) {
                username = validateParams.username();
                password = validateParams.password();

                loggedIn(true);
            }

            return validateResult;
            
        }
        
    };
    
    /**
     * Context used by the ProjectPanel to get the list of projects from the server.
     */
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
    
    /**
     * Context used by the FieldPanel to get the fields for a specific project.
     */
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
    
    /**
     * Context used by the StringPanel to send a search request to the server.
     * The result is passed down to the ResultPanel
     */
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
    
    /**
     * Context used by the ResultPanel to tell the ResultViewPanel to download
     * an image file.
     */
    private ResultPanel.Context resultContext = new ResultPanel.Context() {

        @Override
        public void triggerImageDownload(String path, Integer imageId) {
            SearchGui.this.downloadImage(path, imageId);
        }
    
    };
    
    /**
     * Context used by the ResultViewPanel to download a BufferedImage from the server.
     */
    private ResultViewPanel.Context resultViewContext = new ResultViewPanel.Context() {

        @Override
        public BufferedImage downloadImage(String path) {
            
            assert communicator != null;
            
            BufferedImage image = communicator.downloadImage(path);
            
            return image;
            
        }
    };
    
    /**
     * Listener that opens a dialog to log in.
     */
    private ActionListener loginAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (loginDialog == null) {
                loginDialog = new LoginPanel(loginContext);
            }
            
            loginDialog.showDialog(SearchGui.this);
            
        }
        
    };
    
    /**
     * Listener that exits the program when the Logout menu item is pushed.
     */
    private ActionListener logoutAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            SearchGui.this.dispose();
        }
        
    };
    
    public static void main(String[] args) {
        SearchGui gui = new SearchGui();
        gui.setVisible(true);
        gui.pack();
    }
    
}
