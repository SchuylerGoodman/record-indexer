package shared.communication;

import java.util.List;

/**
 * Communication class for the getFields API
 * 
 * @author schuyler
 */
public class GetFields_Result {
    
    private int projectId;
    private List<Integer> fieldIds;
    private List<String> fieldTitles;
    
    public GetFields_Result(int projectId, List<Integer> fieldIds, List<String> fieldTitles) {
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
    
}
