/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.communication.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import shared.communication.ValidateUser_Result;

/**
 *
 * @author schuyler
 */
public class LoginDialog extends JDialog {
    
    private static final int DIALOG_WIDTH = 375;
    private static final int DIALOG_HEIGHT = 92;
    
    private CommunicationNotifier communicationNotifier;
    
    private JFrame parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    
    public LoginDialog(JFrame parent,
                       CommunicationLinker communicationLinker,
                       ActionListener exitAction,
                       WindowListener loginDialogCloseListener) {
        
        super(parent, "Login to Indexer", true);
        
        this.parent = parent;
        
        communicationNotifier = communicationLinker.getCommunicationNotifier();
        communicationLinker.subscribe(communicationSubscriber);

        createComponents(exitAction);
        
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setLocation(Client.SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
                         Client.SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2);
        this.addWindowListener(loginDialogCloseListener);
        this.pack();
        this.setResizable(false);
        
    }
    
    private void createComponents(ActionListener exitAction) {
        
        JPanel mainPanel = new JPanel();
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                
        JPanel username = new JPanel();
        username.setLayout(new BoxLayout(username, BoxLayout.X_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField();
        username.add(usernameLabel);
        username.add(usernameField);
        mainPanel.add(username);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();
        password.add(passwordLabel);
        password.add(Box.createRigidArea(new Dimension(2, 0)));
        password.add(passwordField);
        mainPanel.add(password);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        loginButton = new JButton("Login");
        loginButton.addActionListener(loginAction);
        buttons.add(Box.createRigidArea(new Dimension(50, 0)));
        buttons.add(loginButton);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAction);
        buttons.add(exitButton);
        buttons.add(Box.createRigidArea(new Dimension(50, 0)));
        mainPanel.add(buttons);
        
        this.add(mainPanel);
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber =
                                        new AbstractCommunicationSubscriber() {

        @Override
        public void login(ValidateUser_Result result) {
            // Process return for login request
            if (result != null && result.validated()) {
                usernameField.setText("");
                StringBuilder loginMessage = new StringBuilder();
                loginMessage.append("Welcome, ").append(result.firstName());
                loginMessage.append(" ").append(result.lastName()).append(".");
                loginMessage.append("\nYou have indexed ").append(result.recordsIndexed());
                loginMessage.append(" records.");
                JOptionPane.showMessageDialog(LoginDialog.this,
                                              loginMessage.toString(),
                                              "Welcome to Indexer", JOptionPane.PLAIN_MESSAGE);
                
                LoginDialog.this.setVisible(false);
                parent.setVisible(true);
            }
            else if (result != null && !result.validated()) {
                JOptionPane.showMessageDialog(LoginDialog.this,
                                              "Invalid username and/or password.",
                                              "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(LoginDialog.this,
                                              "Error connecting to server.",
                                              "Server Error", JOptionPane.ERROR_MESSAGE);
            }
            passwordField.setText("");
        }

    };
    
    private ActionListener loginAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Validate user if login is pressed
            communicationNotifier.validateUser(usernameField.getText(), new String(passwordField.getPassword()));
        }
        
    };
    
}
