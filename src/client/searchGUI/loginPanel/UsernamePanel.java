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
import javax.swing.JTextField;

/**
 *
 * @author schuyler
 */
public class UsernamePanel extends JPanel {
    
    private JTextField usernameField;
    
    public UsernamePanel() {
        
        super();
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setPreferredSize(new Dimension(90, 30));
        
        usernameField = new JTextField(25);
        usernameField.setPreferredSize(new Dimension(195, 30));
        usernameField.setBackground(Color.white);
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(usernameLabel);
        this.add(usernameField);
        
    }
    
    /**
     * Gets the text stored in the text field.
     * 
     * @return String stored in the text field
     */
    public String getUsername() {
        return this.usernameField.getText();
    }
    
}
