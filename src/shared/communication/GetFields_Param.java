/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.communication;

/**
 *
 * @author schuyler
 */
public class GetFields_Param {
    
    /**
     * Getter for the User's username.
     * 
     * @return The User's username
     */
    public String username() {
        return null;
    }
    
    /**
     * Getter for the User's password.
     * 
     * @return The User's password.
     */
    public String password() {
        return null;
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
    public Object project() {
        return null;
    }
    
}
