package shared.communication;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    
    private List<Integer> projectIds;
    private List<Integer> fieldIds;
    private List<String> fieldTitles;
    
    public GetFields_Result(List<Integer>projectIds, List<Integer> fieldIds, List<String> fieldTitles)
            throws GetFields_ResultException {
        
        if (projectIds.size() != fieldIds.size()
                && fieldIds.size() != fieldTitles.size()) {
            throw new GetFields_ResultException("Field IDs list and Field Titles list must be the same size.");
        }
        this.projectIds = projectIds;
        this.fieldIds = fieldIds;
        this.fieldTitles = fieldTitles;
    }
    
    /**
     * Getter method for the project ID
     * 
     * @return Project ID to which the fields belong.
     */
    public List<Integer> projectIds() {
        return projectIds;
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldIds.size(); ++i) {
            sb.append(this.projectIds.get(i)).append("\n");
            sb.append(this.fieldIds.get(i)).append("\n");
            sb.append(this.fieldTitles.get(i)).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        
        int hash = projectIds.size() + 41;
        hash += fieldIds.size() + 37;
        hash += fieldTitles.size() + 31;
        return hash;
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GetFields_Result other = (GetFields_Result) obj;
        if (!Objects.deepEquals(this.projectIds, other.projectIds)) {
            return false;
        }
        if (!Objects.deepEquals(this.fieldIds, other.fieldIds)) {
            return false;
        }
        if (!Objects.deepEquals(this.fieldTitles, other.fieldTitles)) {
            return false;
        }
        return true;
    }
    
}
