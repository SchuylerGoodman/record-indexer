/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.cell.CellLinker;
import client.gui.model.communication.CommunicationLinker;
import client.gui.model.image.ImageLinker;
import client.gui.model.record.RecordLinker;
import java.awt.Dimension;
import javax.swing.JTabbedPane;

/**
 *
 * @author schuyler
 */
class ExtrasPanel extends JTabbedPane {

    private static final Dimension minimumSize = new Dimension(Client.ORIG_WIDTH / 6, Client.ORIG_HEIGHT / 5);
    private static final Dimension preferredSize = new Dimension(Client.ORIG_WIDTH, Client.ORIG_HEIGHT / 3);
    
    private HelpPanel helpPanel;
    private NavigationPanel navigationPanel;
    
    public ExtrasPanel(CommunicationLinker communicationLinker,
                       RecordLinker recordLinker, CellLinker cellLinker,
                       ImageLinker imageLinker) {
        
        super();
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        
        createComponents(communicationLinker, recordLinker, cellLinker, imageLinker);
        
    }
    
    private void createComponents(CommunicationLinker communicationLinker,
                                  RecordLinker recordLinker, CellLinker cellLinker,
                                  ImageLinker imageLinker) {
        
        helpPanel = new HelpPanel(communicationLinker, recordLinker, cellLinker);
        this.addTab("Field Help", helpPanel);
        
        navigationPanel = new NavigationPanel();
        this.addTab("Image Navigation", navigationPanel);
        
    }
    
}
