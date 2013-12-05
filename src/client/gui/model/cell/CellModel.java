/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

import client.gui.model.image.ImageNotifier;
import java.util.ArrayList;

/**
 *
 * @author schuyler
 */
public class CellModel {
    
    private ArrayList<CellSubscriber> subscribers;
    
    private ImageNotifier imageNotifier;
    
    private int currentRow;
    private int currentColumn;
    
    public CellModel(ImageNotifier imageNotifier) {
        
        this.imageNotifier = imageNotifier;
        subscribers = new ArrayList<>();
        
    }
    
    /**
     * Factory method for creating CellLinkers linked to this CellModel.
     * 
     * @return a new CellLinker that allows components or classes to link to
     * this CellModel.
     */
    public CellLinker createCellLinker() {
        return new CellLinker(this);
    }
    
    /**
     * Add a subscriber to the subscriber list.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    protected void subscribe(CellSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Notification receiver for cell selection.
     * 
     * Will send a notification to all subscribers.
     * 
     * @param row the row of the selected cell with origin at 0.
     * @param column the column of the selected cell with origin at 0.
     */
    protected void select(int row, int column) {
        
        this.currentRow = row;
        this.currentColumn = column;
        this.selected(row, column);
        
    }
    
    /**
     * Sends a notification to all subscribers.
     * 
     * @param row the row of the selected cell with origin at 0.
     * @param column the column of the selected cell with origin at 0.
     */
    private void selected(int row, int column) {
        
    }
    
}
