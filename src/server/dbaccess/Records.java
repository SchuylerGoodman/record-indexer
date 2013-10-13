package server.dbaccess;

import java.util.Collection;

/**
 * Database Access class for the records table.
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
    
    /**
     * Searches the records table for the records with the search values.
     * <p>
     * Precondition: User must exist.
     * 
     * @param fields Comma-separated string with field IDs to search through.
     * @param values Comma-separated string with search values.
     * @return Collection of a Collection of objects:
     *      Each inner Collection contains:
     *          Integer imageId
     *          Integer recordNumber (the row number in the image)
     *          Integer fieldId (ID of the Field to which the record belongs.
     */
    protected Collection<Object> search(String fields, String values) {
        return null;
    }
    
}