/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

/**
 *
 * @author schuyler
 */
public class CellLinker {
    
    private CellNotifier notifier;
    private CellModel model;
    
    public CellLinker(CellModel model) {
        
        this.notifier = new CellNotifier(model);
        this.model = model;
        
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
