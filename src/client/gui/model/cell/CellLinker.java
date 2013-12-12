/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

import client.gui.model.communication.CommunicationLinker;
import client.gui.model.record.RecordLinker;
import client.gui.model.save.SaveLinker;

/**
 *
 * @author schuyler
 */
public class CellLinker {
    
    private CellNotifier notifier;
    private CellModel model;
    
    public CellLinker() {

        model = new CellModel();
        this.notifier = new CellNotifier(model);
        
    }
    
    /**
     * Sets up a connection between the CellModel to link to the RecordModel.
     * 
     * @param recordLinker the RecordLinker with the desired RecordModel.
     */
    public void link(CommunicationLinker communicationLinker,
                     RecordLinker recordLinker, SaveLinker saveLinker) {
        
        model.link(communicationLinker, recordLinker, saveLinker);
        
    }
    
    /**
     * Adds a subscriber to the CellModel.
     * 
     * @param subscriber the new subscriber to add to the CellModel.
     */
    public void subscribe(CellSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    /**
     * Get the CellNotifier that notifies the CellModel with subscribers that
     * have been subscribed through this CellLinker.
     * 
     * @return 
     */
    public CellNotifier getCellNotifier() {
        return notifier;
    }
    
}
