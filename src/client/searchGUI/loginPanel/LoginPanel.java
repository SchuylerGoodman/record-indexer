/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.loginPanel;

import client.searchGUI.ErrorLabel;
import client.searchGUI.SearchGui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class LoginPanel extends JPanel {
    
    public static final int PANEL_WIDTH = 400;
    public static final int PANEL_HEIGHT = 200;
    
    private JLabel errorLabel;
    private HostPanel hostPanel;
    private PortPanel portPanel;
    private UsernamePanel userPanel;
    private PasswordPanel passPanel;
    private JButton loginButton;
    
    private JDialog loginDialog;
    private boolean loginSuccess;
    
    public interface Context {
        
        public ValidateUser_Result attemptLogin(String host, int port, ValidateUser_Param params);
        
    }
    
    private Context context;
    
    public LoginPanel(Context context) {
        
        super();
        
        this.context = context;
        
        createComponents();

    }
    
    private void createComponents() {

        this.setLayout(new BorderLayout());

        errorLabel = new ErrorLabel();
        this.add(errorLabel, BorderLayout.NORTH);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        hostPanel = new HostPanel();
        inputPanel.add(hostPanel);
        portPanel = new PortPanel();
        inputPanel.add(portPanel);
        userPanel = new UsernamePanel();
        inputPanel.add(userPanel);
        passPanel = new PasswordPanel();
        inputPanel.add(passPanel);
        this.add(inputPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        loginButton.addActionListener(loginListener);
        buttonPanel.add(loginButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loginDialog.setVisible(false);
            }
            
        });
        buttonPanel.add(cancelButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
    }
    
    public void showDialog(SearchGui parent) {

        assert parent != null;
        
        if (loginDialog == null) {
            loginDialog = new JDialog(parent, true);
            loginDialog.add(this);
            loginDialog.getRootPane().setDefaultButton(loginButton);
            loginDialog.pack();
            loginDialog.setLocation(SearchGui.center.x - loginDialog.getWidth() / 2,
                                    SearchGui.center.y - loginDialog.getHeight() / 2);
        }
        
        loginDialog.setTitle("Login");
        loginDialog.setVisible(true);
        
    }
    
    private ActionListener loginListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            String host = hostPanel.getHost();
            String portString = portPanel.getPort();
            String username = userPanel.getUsername();
            String password = passPanel.getPassword();
            
            // Validate inputs
            String errorMessage = validateForm(host, portString, username, password);
            if (errorMessage == null) {
                
                int port = Integer.parseInt(portString); // Convert port to int
                ValidateUser_Param params = new ValidateUser_Param(username, password);

                ValidateUser_Result result = context.attemptLogin(host, port, params);

                if (result == null) {
                    errorLabel.setText("Error validating user: Check Host or Port number.");
                }
                else if (!result.validated()) {
                    errorLabel.setText("Invalid Username or Password.");
                }
                else {
                    errorLabel.setText(null);
                    LoginPanel.this.loginSuccess = true;
                    LoginPanel.this.loginDialog.setVisible(false);
                }
            }
            else {
                errorLabel.setText(errorMessage);
            }
            
        }
        
        private String validateForm(String host, String port, String username, String password) {
            
            if (host == null || host.isEmpty()) {
                return "No server hostname entered";
            }
            
            if (port == null || port.isEmpty()) {
                return "No port number entered.";
            }
            try {
                Integer.parseInt(port);
            }
            catch (NumberFormatException ex) {
                return "Port number must be an integer.";
            }
            
            if (username == null || username.isEmpty()) {
                return "No username entered";
            }
            
            if (password == null || password.isEmpty()) {
                return "No password entered.";
            }
            
            return null;

        }
        
    };
    
}
