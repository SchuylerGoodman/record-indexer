/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Panel for the entering record info as a table.
 *
 * @author schuyler
 */
public class TableEntryPanel extends JPanel {
    
    private TableEntryModel tableEntryModel;
    private JTable entryTable;
    
    public TableEntryPanel() {
        
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
