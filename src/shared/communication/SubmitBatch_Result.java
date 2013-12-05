package shared.communication;

import java.io.Serializable;

/**
 * Communication class for the submitBatch API
 *
 * @author schuyler
 */
public class SubmitBatch_Result extends RequestResult implements Serializable {
    
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
    
    @Override
    public String toString() {
        if (success) {
            return "TRUE\n";
        }
        else {
            return "FAILED\n";
        }
    }
    
    @Override
    public int hashCode() {
        if (success) {
            return 1;
        }
        else {
            return 2;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubmitBatch_Result other = (SubmitBatch_Result) obj;
        if (this.success != other.success) {
            return false;
        }
        return true;
    }
    
}
