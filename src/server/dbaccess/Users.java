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
    protected Collection<Object> validateUser(String username, String password) {
        return null;
    }
    
    /**
     * Creates a new user in the database.
     * 
     * @param newUser shared.model.User model class with data to insert.
     * @return shared.model.User with generated field ID.
     */
    protected shared.model.User insert(shared.model.User newUser) {
        return null;
    }

    /**
     * Updates a User in the database.
     * 
     * @param user with the updated information.
     */
    protected void update(shared.model.User user) {
    }
    
    /**
     * Gets Users from the database.
     * 
     * @param userIds Collection of user IDs whose information is being requested.
     * @return Collection of shared.model.User objs with the requested information.
     */
    protected Collection<shared.model.User> get(Collection<Integer> userIds) {
        return null;
    }

    /**
     * Deletes a User from the database.
     * 
     * @param deleteUser shared.model.User to delete from the database.
     */
    protected void delete(shared.model.User deleteUser) {
    }
    
}
