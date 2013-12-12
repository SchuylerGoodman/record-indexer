/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.cell.CellLinker;
import client.gui.model.communication.AbstractCommunicationSubscriber;
import client.gui.model.communication.CommunicationLinker;
import client.gui.model.image.ImageLinker;
import client.gui.model.record.RecordLinker;
import client.gui.model.save.*;
import client.gui.model.save.settings.ImageSettings;
import client.gui.model.save.settings.WindowSettings;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import shared.communication.DownloadBatch_Result;

/**
 * Main split pane for the image panel and the lower panel.
 *
 * @author schuyler
 */
public class MainPanel extends JPanel {
    
    private SaveNotifier saveNotifier;
    
    private ImageButtons buttonBar;
    private JSplitPane splitPane;
    private ImagePanel imagePanel;
    private UtilityPanel utilityPanel;
    
    public MainPanel(CommunicationLinker communicationLinker,
                     RecordLinker recordLinker,
                     CellLinker cellLinker,
                     ImageLinker imageLinker,
                     SaveLinker saveLinker) {
        
        super();

        communicationLinker.subscribe(communicationSubscriber);
        
        saveNotifier = saveLinker.getSaveNotifier();
        saveLinker.subscribe(saveSubscriber);
        
        createComponents(communicationLinker, recordLinker, cellLinker,
                         imageLinker, saveLinker);
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents(CommunicationLinker communicationLinker,
                                  RecordLinker recordLinker,
                                  CellLinker cellLinker,
                                  ImageLinker imageLinker,
                                  SaveLinker saveLinker) {
        
        this.setLayout(new BorderLayout());
        
        buttonBar = new ImageButtons(communicationLinker, imageLinker,
                                     saveLinker);
        this.add(buttonBar, BorderLayout.NORTH);
        
        imagePanel = new ImagePanel(recordLinker, cellLinker, imageLinker);
        utilityPanel = new UtilityPanel(communicationLinker, recordLinker,
                                        cellLinker, imageLinker, saveLinker);
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, imagePanel, utilityPanel);
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerLocation(Client.ORIG_HEIGHT / 2);
        
        this.add(splitPane, BorderLayout.CENTER);
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber = new AbstractCommunicationSubscriber() {
        
        @Override
        public void setBatch(DownloadBatch_Result result) {
            
            boolean enabled = false;
            if (result != null) {
                if (!result.equals(new DownloadBatch_Result())) {
                    enabled = true;
                }
            }
            buttonBar.enabled(enabled);
            
        }
        
    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public WindowSettings saveWindowSettings() {
            return new WindowSettings(splitPane.getDividerLocation());
        }

        @Override
        public void setWindowSettings(WindowSettings settings) {
            splitPane.setDividerLocation(settings.horizontalDividerLocation());
        }
        
        @Override
        public void setImageSettings(ImageSettings settings) {
            buttonBar.enabled(true);
        }
        
    };
    
}
