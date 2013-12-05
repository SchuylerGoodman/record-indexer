/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

/**
 *
 * @author schuyler
 */
public class CommunicationLinker {
    
    private CommunicationNotifier notifier;
    private CommunicationModel model;
    
    public CommunicationLinker(CommunicationModel model) {
        notifier = new CommunicationNotifier(model);
        this.model = model;
    }
    
    /**
     * Subscribes the subscriber to the CommunicationModel.
     * 
     * @param subscriber the subscriber to subscribe.
     */
    public void subscribe(CommunicationSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    public CommunicationNotifier getNotifier() {
        return notifier;
    }
    
}
