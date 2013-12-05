/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

/**
 *
 * @author schuyler
 */
public class SaveLinker {
    
    private SaveNotifier notifier;
    private SaveModel model;
    
    public SaveLinker(SaveModel model) {
        
        notifier = new SaveNotifier(model);
        this.model = model;
        
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
    public SaveNotifier getNotifier() {
        return notifier;
    }
    
}
