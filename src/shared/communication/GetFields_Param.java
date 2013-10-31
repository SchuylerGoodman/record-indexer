package shared.communication;

import java.io.Serializable;

/**
 * Communication class for the getFields API
 * 
 * @author schuyler
 */
public class GetFields_Param extends RequestParam implements Serializable {
    
    private String username;
    private String password;
    private Object projectId;
    
    public GetFields_Param(String username, String password, int projectId) {
        this.username = username;
        this.password = password;
        this.projectId = new Integer(projectId);
    }
    
    public GetFields_Param(String username, String password, String projectId) {
        this.username = username;
        this.password = password;
        this.projectId = projectId;
    }
    
    /**
     * Getter for the User's username.
     * 
     * @return The User's username
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter for the User's password.
     * 
     * @return The User's password.
     */
    public String password() {
        return password;
    }
    
    /**
     * Getter for the Project whose fields are being requested.
     * <p>
     * A returned Integer means to return the fields for that Project.
     * An empty String means to return the fields for all projects.
     * 
     * @return Integer representing the unique Project identifier
     * @return Empty string
     */
    public Object projectId() {
        return projectId;
    }
    
}
