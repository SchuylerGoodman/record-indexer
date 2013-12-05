/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

/**
 *
 * @author schuyler
 */
public class RecordLinker {
    
    private RecordNotifier notifier;
    private RecordModel model;
    
    public RecordLinker(RecordModel model) {
        
        this.notifier = new RecordNotifier(model);
        this.model = model;
        
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
