/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.communication.CommunicationLinker;

/**
 *
 * @author schuyler
 */
public class SaveLinker {
    
    private SaveNotifier notifier;
    private SaveModel model;
    
    public SaveLinker() {

        model = new SaveModel();
        notifier = new SaveNotifier(model);
        
    }

    /**
     * Sets up a connection between the SaveModel and the CommunicationModel.
     * 
     * @param communicationLinker the CommunicationLinker with the desired
     * CommunicationModel.
     */
    public void link(CommunicationLinker communicationLinker) {
        model.link(communicationLinker);
    }
    
    /**
     * Subscribes the subscriber to the SaveModel.
     * 
     * @param subscriber the subscriber to subscribe.
     */
    public void subscribe(SaveSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    /**
     * Getter for the SaveNotifier to notify the SaveModel to save.
     * 
     * @return the SaveNotifier linked to the SaveModel for the subscribers
     * given to this SaveLinker.
     */
    public SaveNotifier getSaveNotifier() {
        return notifier;
    }
    
}
