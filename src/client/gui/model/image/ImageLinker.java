/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

/**
 *
 * @author schuyler
 */
public class ImageLinker {
    
    private ImageNotifier notifier;
    private ImageModel model;
    
    public ImageLinker(ImageModel model) {
        
        this.notifier = new ImageNotifier(model);
        this.model = model;
        
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
