/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.cell.CellLinker;
import client.gui.model.record.RecordLinker;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    
    public EntryPanel(RecordLinker recordLinker, CellLinker cellLinker) {
        
        super();
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        
        createComponents(recordLinker, cellLinker);
        
        this.addChangeListener(changeListener);
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents(RecordLinker recordLinker, CellLinker cellLinker) {

        System.out.println(this.getSize().toString());
        tableEntryPanel = new TableEntryPanel(recordLinker, cellLinker);
        this.addTab("Table Entry", tableEntryPanel);
        
        formEntryPanel = new FormEntryPanel(recordLinker, cellLinker);
        this.addTab("Form Entry", formEntryPanel);
        
    }
    
    private ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (formEntryPanel.isVisible()) {
                formEntryPanel.setFocus();
            }
        }
        
    };
    
}
