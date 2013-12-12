/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

import client.gui.model.communication.CommunicationLinker;
import client.gui.model.communication.CommunicationNotifier;
import client.gui.model.communication.CommunicationSubscriber;
import client.gui.model.save.SaveLinker;
import client.gui.model.save.SaveNotifier;
import client.gui.model.save.SaveSubscriber;

/**
 *
 * @author schuyler
 */
public class RecordLinker {
    
    private RecordNotifier notifier;
    private RecordModel model;
    
    public RecordLinker() {

        model = new RecordModel();
        this.notifier = new RecordNotifier(model);
        
    }
    
    /**
     * Sets up a link between the RecordModel, the CommunicationModel, and the
     * SaveModel.
     * 
     * @param communicationLinker the CommunicationLinker with the desired
     * CommunicationModel.
     * @param saveLinker the SaveLinker with the desired SaveModel.
     */
    public void link(CommunicationLinker communicationLinker,
                     SaveLinker saveLinker) {
        model.link(communicationLinker, saveLinker);
    }
    
    /**
     * Tells the RecordModel to add a subscriber to the list.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    public void subscribe(RecordSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    /**
     * Getter for the notifier for the RecordModel connected to this
     * RecordLinker.
     * 
     * @return the RecordNotifier for notifying all subscribers to this
     * RecordLinker.
     */
    public RecordNotifier getRecordNotifier() {
        return notifier;
    }
    
}
