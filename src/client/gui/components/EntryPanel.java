/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import java.awt.Dimension;
import javax.swing.JTabbedPane;

/**
 * Tab Panel for the table and form entry panels.
 *
 * @author schuyler
 */
public class EntryPanel extends JTabbedPane {
    
    private static final Dimension minimumSize = new Dimension(Client.ORIG_WIDTH / 6, Client.ORIG_HEIGHT / 5);
    private static final Dimension preferredSize = new Dimension(Client.ORIG_WIDTH, Client.ORIG_HEIGHT / 3);
    
    private TableEntryPanel tableEntryPanel;
    private FormEntryPanel formEntryPanel;
    
    public EntryPanel() {
        
        super();
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {

        System.out.println(this.getSize().toString());
        tableEntryPanel = new TableEntryPanel();
        this.addTab("Table Entry", tableEntryPanel);
        
        formEntryPanel = new FormEntryPanel();
        this.addTab("Form Entry", formEntryPanel);
        
    }
    
}
