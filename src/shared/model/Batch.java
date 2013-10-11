/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.util.Set;

/**
 *
 * @author schuyler
 */
public interface Batch {
    
    /**
     * Loads the Batch with database key batch_id from the database
     * 
     * @param batch_id unique key for the batch to load
     */
    public void loadBatch(int batchId);
    
    public Set<Field> getFields();
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object o);
    
}
