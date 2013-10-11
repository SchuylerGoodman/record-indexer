/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author goodm4n
 */
public interface Client {
    
    /**
     * Uses a shared.communicate.Communication object to request user from the database.
     * @param userName
     * @param userPassword
     * @return
     * @throws shared.model.User.InvalidUserInfoException 
     */
    public shared.model.User loadUser(String userName, String userPassword) throws shared.model.User.InvalidUserInfoException;
    
    
}
