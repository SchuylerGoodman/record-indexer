/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.loginPanel;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author schuyler
 */
public class PasswordPanel extends JPanel {
    
    private JTextField passwordField;
    
    public PasswordPanel() {
        
        super();
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new Dimension(90, 30));
        
        passwordField = new JPasswordField(25);
        passwordField.setPreferredSize(new Dimension(195, 30));
        passwordField.setBackground(Color.white);
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(passwordLabel);
        this.add(passwordField);
        
    }
    
    /**
     * Gets the text stored in the text field.
     * 
     * @return String stored in the text field
     */
    public String getPassword() {
        return this.passwordField.getText();
    }
    
}
