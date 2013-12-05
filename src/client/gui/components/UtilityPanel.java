/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import java.awt.Dimension;
import javax.swing.JSplitPane;

/**
 * Split Pane for the bottom portion of the gui.
 *
 * @author schuyler
 */
public class UtilityPanel extends JSplitPane {
    
    private static final Dimension minimumSize = new Dimension(Client.ORIG_WIDTH / 6, Client.ORIG_HEIGHT / 5);
    private static final Dimension preferredSize = new Dimension(Client.ORIG_WIDTH, Client.ORIG_HEIGHT / 3);
    
    private EntryPanel entryPanel;
    private ExtrasPanel extrasPanel;
    
    public UtilityPanel() {
        
        super(JSplitPane.HORIZONTAL_SPLIT, true, new EntryPanel(), new ExtrasPanel());
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
        this.setResizeWeight(0.0);
        
        entryPanel = (EntryPanel) this.getLeftComponent();
        extrasPanel = (ExtrasPanel) this.getRightComponent();
        
        this.setDividerLocation(Client.ORIG_WIDTH / 2);
        
    }
    
}
