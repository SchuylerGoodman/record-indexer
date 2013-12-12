/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

/**
 *
 * @author schuyler
 */
public abstract class AbstractRecordSubscriber implements RecordSubscriber {

    @Override
    public void recordChanged(int row, int column, String newValue) {
    }
    
    @Override
    public void setRecords() {
    }
    
    @Override
    public void empty() {
    }
    
}
