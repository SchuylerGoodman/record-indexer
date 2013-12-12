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
import client.gui.model.save.AbstractSaveSubscriber;
import client.gui.model.save.SaveLinker;
import client.gui.model.save.settings.WindowSettings;
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
    
    public UtilityPanel(CommunicationLinker communicationLinker,
                        RecordLinker recordLinker, CellLinker cellLinker,
                        ImageLinker imageLinker, SaveLinker saveLinker) {
        
        super(JSplitPane.HORIZONTAL_SPLIT, true,
              new EntryPanel(recordLinker, cellLinker),
              new ExtrasPanel(communicationLinker, recordLinker, cellLinker, imageLinker));
        
        saveLinker.subscribe(saveSubscriber);
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
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public WindowSettings saveWindowSettings() {
            
            int entry = entryPanel.getSelectedIndex();
            int extra = extrasPanel.getSelectedIndex();
            return new WindowSettings(UtilityPanel.this.getDividerLocation(),
                                      new WindowSettings.EntryTab(entry),
                                      new WindowSettings.EntryTab(extra));
            
        }

        @Override
        public void setWindowSettings(WindowSettings settings) {
            
            UtilityPanel.this.setDividerLocation(settings.verticalDividerLocation());
            entryPanel.setSelectedIndex(settings.selectedEntryTab());
            extrasPanel.setSelectedIndex(settings.selectedExtrasTab());
            
        }
        
    };
    
}
