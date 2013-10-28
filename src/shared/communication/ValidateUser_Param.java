package shared.communication;

/**
 * Communication class for the validateUser API
 * 
 * @author schuyler
 */
public class ValidateUser_Param {
    
    private String username;
    private String password;
    
    public ValidateUser_Param(String inUsername, String inPassword) {
        username = inUsername;
        password = inPassword;
    }
    
    /**
     * Getter method for the username.
     * 
     * @return String containing username and password for the user
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the User password.
     * 
     * @return String User password.
     */
    public String password() {
        return password;
    }
}
