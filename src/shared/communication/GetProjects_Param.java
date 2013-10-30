package shared.communication;

import java.io.Serializable;

/**
 * Communication class for the getSampleImage API
 * 
 * @author schuyler
 */
public class GetProjects_Param implements Serializable {
    
    private String username;
    private String password;
    
    public GetProjects_Param(String inUsername, String inPassword) {
        username = inUsername;
        password = inPassword;
    }
    
    /**
     * Getter method for the username
     * 
     * @return String username.
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the password
     * 
     * @return String password.
     */
    public String password() {
        return password;
    }
}
