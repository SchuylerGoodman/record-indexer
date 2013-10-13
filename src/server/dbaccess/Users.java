package server.dbaccess;

import java.util.Collection;

/**
 * Database Access class for the users table.
 * 
 * @author schuyler
 */
public class Users {
    
    /**
     * Queries the database for the user specified.
     * <p>
     * Will be packaged into a shared.communication.validateUser_Result 
     * object by the HttpHandler.
     * 
     * @param username
     * @param password
     * 
     * @return Collection of data to return to the client.
     * @return null if parameters did not match any User in the database.
     */
    public Collection<Object> validateUser(String username, String password) {
        return null;
    }
    
}
