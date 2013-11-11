/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author schuyler
 */
public class ErrorLabel extends JLabel {
    
    public ErrorLabel() {
        this(null);
    }
    
    public ErrorLabel(String message) {
        
        super(message);
        
        this.setForeground(Color.red);
        
    }
    
    public void setErrorText(String message) {
        this.setText(message);
    }
    
}
