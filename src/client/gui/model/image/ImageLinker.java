/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.communication.CommunicationLinker;
import client.gui.model.save.SaveLinker;

/**
 *
 * @author schuyler
 */
public class ImageLinker {
    
    private ImageNotifier notifier;
    private ImageModel model;
    
    public ImageLinker() {

        model = new ImageModel();
        this.notifier = new ImageNotifier(model);
        
    }
    
    /**
     * Allows the ImageModel to link to the CommunicationModel and the SaveModel.
     * 
     * @param communicationLinker the CommunicationLinker with the desired
     * CommunicationModel.
     * @param saveLinker the SaveLinker with the desired SaveModel.
     */
    public void link(CommunicationLinker communicationLinker, SaveLinker saveLinker) {
        model.link(communicationLinker, saveLinker);
    }
    
    /**
     * Tells the ImageModel to add a subscriber to the subscriber list.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    public void subscribe(ImageSubscriber subscriber) {
        model.subscribe(subscriber);
    }
    
    /**
     * Getter for an ImageNotifier linked to the same ImageModel as the
     * subscribers subscribed to this ImageLinker.
     * 
     * @return the ImageNotifier for notifying all subscribers for this
     * ImageLinker.
     */
    public ImageNotifier getImageNotifier() {
        return notifier;
    }
    
}
