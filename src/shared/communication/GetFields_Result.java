package shared.communication;

import java.io.Serializable;
import java.util.List;

/**
 * Communication class for the getFields API
 * 
 * @author schuyler
 */
public class GetFields_Result extends RequestResult implements Serializable {

    public class GetFields_ResultException extends Exception {
        public GetFields_ResultException(String message) {
            super(message);
        }
    }
    
    private int projectId;
    private List<Integer> fieldIds;
    private List<String> fieldTitles;
    
    public GetFields_Result(int projectId, List<Integer> fieldIds, List<String> fieldTitles)
            throws GetFields_ResultException {
        
        if (fieldIds.size() != fieldTitles.size()) {
            throw new GetFields_ResultException("Field IDs list and Field Titles list must be the same size.");
        }
        this.projectId = projectId;
        this.fieldIds = fieldIds;
        this.fieldTitles = fieldTitles;
    }
    
    /**
     * Getter method for the project ID
     * 
     * @return Project ID to which the fields belong.
     */
    public int projectId() {
        return projectId;
    }
    
    /**
     * Getter method for the field IDs.
     * Correspond 1 to 1 with the field title;
     * 
     * @return Field IDs of the fields belonging to the requested project
     */
    public List<Integer> fieldIds() {
        return fieldIds;
    }
    
    /**
     * Getter method for the field titles.
     * Correspond 1 to 1 with the field IDs
     * 
     * @return Field Titles of the fields belonging to the requested project
     */
    public List<String> fieldTitles() {
        return fieldTitles;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldIds.size(); ++i) {
            sb.append(this.projectId).append("\n");
            sb.append(this.fieldIds.get(i)).append("\n");
            sb.append(this.fieldTitles.get(i)).append("\n");
        }
        return sb.toString();
    }
    
}
