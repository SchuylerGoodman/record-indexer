package shared.communication;

/**
 * Communication class for the submitBatch API
 *
 * @author schuyler
 */
public class SubmitBatch_Result {
    
    private boolean success;
    
    public SubmitBatch_Result(boolean result) {
        success = result;
    }
    
    /**
     * Says if the submission was successful or not.
     * 
     * @return true if successful.
     * @return false if unsuccessful.
     */
    public boolean success() {
        return success;
    }
    
}
