/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import client.Communicator;
import client.gui.model.image.ImageLinker;
import client.gui.model.image.ImageNotifier;
import client.gui.model.record.RecordLinker;
import client.gui.model.record.RecordNotifier;
import client.gui.model.save.SaveLinker;

/**
 *
 * @author schuyler
 */
public class CommunicationLinker {
    
    private CommunicationNotifier notifier;
    private CommunicationModel model;
    
    public CommunicationLinker(Communicator communicator) {
        
        model = new CommunicationModel(communicator);
        notifier = new CommunicationNotifier(model);
        
    }
    
    public void link(ImageLinker imageLinker, RecordLinker recordLinker,
                     SaveLinker saveLinker) {
        
        model.link(imageLinker, recordLinker, saveLinker);
        
    }
    
    /**
     * Subscribes the subscriber to the CommunicationModel.
     * 
     * @param subscriber the subscriber to subscribe.
     */
    public void subscribe(CommunicationSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    /**
     * Getter for the CommunicationNotifier linked to this CommunicationLinker.
     * 
     * @return the CommunicationNotifier that notifies all
     * CommunicationSubscribers subscribed to this CommunicationLinker.
     */
    public CommunicationNotifier getCommunicationNotifier() {
        return notifier;
    }
    
}
