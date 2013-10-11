/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.communicate;

/**
 *
 * @author goodm4n
 */
public interface Communication {
    
    /**
     * Sends an HTTP request to the server to get a User object.
     * 
     * @param userName name of the User
     * @param passWord password of the User
     * @return User - a fully loaded User object
     */
    public shared.model.User loadUser(String userName, String passWord);
    
    
}
