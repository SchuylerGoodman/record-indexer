package shared.communication;

import java.io.Serializable;
import java.util.*;

/**
 * Communication class for the getProjects API
 * 
 * @author schuyler
 */
public class GetProjects_Result implements Serializable {
    
    private List<Integer> ids;
    private List<String> names;
    
    public static class GetProjects_ResultException extends Exception {
        public GetProjects_ResultException(String message) {
            super(message);
        }
    }
    
    public GetProjects_Result() {
        ids = new ArrayList<>();
        names = new ArrayList<>();
    }
    
    public GetProjects_Result(List<Integer> inIds, List<String> inNames) throws GetProjects_ResultException {
        
        if (inIds.size() != inNames.size()) {
            throw new GetProjects_ResultException("Size of inputs must be the same.");
        }
        ids = inIds;
        names = inNames;
    }
    
    /**
     * Getter for the array of Projects available to the User.
     * 
     * @return Object[][] - Each inner array contains a [Integer, String] => [Project ID, Project name] pair.
     */
    public List<Integer> getProjectIds() {
        return ids;
    }
    
    public List<String> getProjectNames() {
        return names;
    }
    
}
