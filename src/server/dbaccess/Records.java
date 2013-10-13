/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dbaccess;

import java.util.Collection;

/**
 *
 * @author schuyler
 */
public class Records {
    
    /**
     * Gets all indexed records belonging to imageId from the database.
     * <p>
     * Preconditions:   User must actually exist.
     *                  Project must actually exist.
     * 
     * @param imageId Unique identifier whose data we want to send out.
     * @return Collection of shared.model.Record objects
     */
    protected Collection<shared.model.Record> downloadBatch(int imageId) {
        return null;
    }
    
    /**
     * Saves the indexed records to the database.
     * <p>
     * Preconditions:   User must actually exist.
     *                  imageId must point to an actual Image.
     * 
     * @param imageId Unique identifier for the Image whose records are being 
     * saved.
     * @param records Collection of Record objects to save.
     * @return true if successful
     * @return false if unsuccessful
     */
    protected boolean submitBatch(int imageId, Collection<shared.model.Record> records) {
        return false;
    }
    
}