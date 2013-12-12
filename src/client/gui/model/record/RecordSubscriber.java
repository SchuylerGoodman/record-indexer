/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

/**
 *
 * @author schuyler
 */
public interface RecordSubscriber {
    
    /**
     * Process the change of a record.
     * 
     * @param row the row of the changed record.
     * @param column the column of the changed record.
     * @param newValue the new value of the changed record.
     */
    public void recordChanged(int row, int column, String newValue);
    
    /**
     * Process the change of all records.
     */
    public void setRecords();
    
    /**
     * Empty all subscribers.
     */
    public void empty();
    
}
