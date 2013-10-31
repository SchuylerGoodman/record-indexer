package shared.communication;

import java.io.Serializable;
import java.util.*;

/**
 * Communication class for the getProjects API
 * 
 * @author schuyler
 */
public class GetProjects_Result extends RequestResult implements Serializable {
    
    private List<Integer> ids;
    private List<String> names;
    
    public static class GetProjects_ResultException extends Exception {
        public GetProjects_ResultException(String message) {
            super(message);
        }
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); ++i) {
            sb.append(ids.get(i)).append("\n");
            sb.append(names.get(i)).append("\n");
        }
        return sb.toString();
    }
    
}
