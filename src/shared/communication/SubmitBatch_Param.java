package shared.communication;

import java.util.Collection;

/**
 * Communication class for the submitBatch API
 * 
 * @author schuyler
 */
public class SubmitBatch_Param {
    
    private String username;
    private String password;
    private int batchId;
    private String records;
    
    public SubmitBatch_Param(String inUsername, String inPassword, int inId, String inRecords) {
        username = inUsername;
        password = inPassword;
        batchId = inId;
        records = inRecords;
    }
    
    /**
     * Getter method for the username of the User submitting the Image.
     * 
     * @return String
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the password of the User submitting the Image.
     * 
     * @return String
     */
    public String password() {
        return password;
    }
    
    /**
     * Getter method for the ID of the Image being submitted.
     * 
     * @return int
     */
    public int batchId() {
        return batchId;
    }
    
    /**
     * Getter method for the records that need to be submitted to the database.
     * 
     * @return Collection of Record objects.
     */
    public String records() {
        return records;
    }
    
}
