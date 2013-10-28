package shared.communication;

/**
 * Communication class for the getSampleImage API
 * 
 * @author schuyler
 */
public class GetSampleImage_Param {
    
    private String username;
    private String password;
    private int projectId;
    
    public GetSampleImage_Param(String inUsername, String inPassword, int inId) {
        username = inUsername;
        password = inPassword;
        projectId = inId;
    }
    
    /**
     * Getter method for the username.
     * 
     * @return String username
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the password.
     * 
     * @return String password
     */
    public String password() {
        return password;
    }
    
    /**
     * Getter method for the Project ID
     * 
     * @return int Project ID
     */
    public int projectId() {
        return projectId;
    }
    
}
