package shared.communication;

import java.io.Serializable;

/**
 * Communication class for the downloadBatch API
 * 
 * @author schuyler
 */
public class DownloadBatch_Param extends RequestParam implements Serializable {

    private String username;
    private String password;
    private int projectId;
    
    public DownloadBatch_Param(String inUsername, String inPassword, int inId) {
        username = inUsername;
        password = inPassword;
        projectId = inId;
    }
    
    /**
     * Getter method for the stored username.
     * 
     * @return String username
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the stored password.
     * 
     * @return String password
     */
    public String password() {
        return password;
    }
    
    /**
     * Getter method for the stored project ID.
     * 
     * @return int project ID
     */
    public int projectId() {
        return projectId;
    }
    
}
