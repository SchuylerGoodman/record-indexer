/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Panel for entering recorded info as a form.
 *
 * @author schuyler
 */
class FormEntryPanel extends JPanel {
    
    private JList recordNumbers;
    private JPanel entryForm;
    
    public FormEntryPanel() {
        
        super();
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
        System.out.println(this.getSize().toString());
        
    }
    
}
