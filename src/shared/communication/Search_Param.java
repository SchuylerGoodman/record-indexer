package shared.communication;

import java.io.Serializable;

/**
 * Communication class for the search API
 * 
 * @author schuyler
 */
public class Search_Param implements Serializable {
    
    private String username;
    private String password;
    private String fields;
    private String values;
    
    public Search_Param(String username, String password, String fields, String values) {
        this.username = username;
        this.password = password;
        this.fields = fields;
        this.values = values;
    }
    
    /**
     * Getter method for the User's username.
     * 
     * @return The username
     */
    public String username() {
        return username;
    }
    
    /**
     * Getter method for the User's password.
     * 
     * @return The password
     */
    public String password() {
        return password;
    }
    
    /**
     * Getter method for the field ids to search.
     * 
     * @return String with comma-separated field ids
     */
    public String fields() {
        return fields;
    }
    
    /**
     * Getter method for the strings to search for.
     * 
     * @return String comma-separated list of search strings.
     */
    public String values() {
        return values;
    }
    
}
