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
public class HostPanel extends JPanel {
    
    private JTextField hostField;
    
    public HostPanel() {
     
        super();

        createComponents();
        
    }
    
    private void createComponents() {
        
        JLabel hostLabel = new JLabel("Host:");
        hostLabel.setPreferredSize(new Dimension(50, 30));
        
        hostField = new JTextField(25);
        hostField.setPreferredSize(new Dimension(325, 30));
        hostField.setBackground(Color.white);
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(hostLabel);
        this.add(hostField);
        
    }
    
    /**
     * Gets the text stored in the text field.
     * 
     * @return String stored in the text field
     */
    public String getHost() {
        return this.hostField.getText();
    }
        
}
