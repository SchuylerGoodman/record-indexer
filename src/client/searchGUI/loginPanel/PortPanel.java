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
public class PortPanel extends JPanel {
    
    private JTextField portField;
    
    public PortPanel() {
        
        super();
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        JLabel portLabel = new JLabel("Port:");
        portLabel.setPreferredSize(new Dimension(50, 30));
        
        portField = new JTextField(25);
        portField.setPreferredSize(new Dimension(325, 30));
        portField.setBackground(Color.white);
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(portLabel);
        this.add(portField);
        
    }
    
    /**
     * Gets the text stored in the text field.
     * 
     * @return String stored in the text field
     */
    public String getPort() {
        return this.portField.getText();
    }
    
}
