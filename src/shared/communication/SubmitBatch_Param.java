package shared.communication;

import java.util.Collection;

/**
 * Communication class for the submitBatch API
 * 
 * @author schuyler
 */
public class SubmitBatch_Param {
    
    /**
     * Getter method for the username of the User submitting the Image.
     * 
     * @return String
     */
    public String username() {
        return null;
    }
    
    /**
     * Getter method for the password of the User submitting the Image.
     * 
     * @return String
     */
    public String password() {
        return null;
    }
    
    /**
     * Getter method for the ID of the Image being submitted.
     * 
     * @return int
     */
    public int batchId() {
        return 0;
    }
    
    /**
     * Getter method for the records that need to be submitted to the database.
     * 
     * @return Collection of Record objects.
     */
    public Collection<shared.model.Record> recordsIndexed() {
        return null;
    }
    
}
