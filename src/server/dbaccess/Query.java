/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dbaccess;

/**
 *
 * @author goodm4n
 */
public interface Query {
    
    public static class DataNotFoundException extends Exception {}
    
    /**
     * Queries the database for the user specified.
     * 
     * @param userName First access argument for the User with password userPassword.
     * @param userPassword Second access argument for the User with username userName.
     * @return shared.model.User with user's first and last name and email.
     * @throws shared.model.User.InvalidUserInfoException 
     */
    public shared.model.User loadUser(String userName, String userPassword) throws DataNotFoundException;
    
    

}
