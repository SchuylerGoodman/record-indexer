package shared.model;

/**
 * Model class to contain database information for a user in memory.
 * 
 * @author goodm4n
 */
public class User {
    
    /**
     * Getter for the unique User ID.
     * 
     * @return int user ID
     */
    public int userId() {
        return 0;
    }
    
    /**
     * Getter for the User's username
     * 
     * @return String username
     */
    public String username() {
        return null;
    }
    
    /**
     * Getter for the User's email
     * 
     * @return String email
     */
    public String email() {
        return null;
    }
    
    /**
     * Getter for the user's first name.
     * 
     * @return String representing the User's first name.
     */
    public String firstName() {
        return null;
    }
    
    /**
     * Getter for the user's last name.
     * 
     * @return String representing the User's last name.
     */
    public String lastName() {
        return null;
    }
    
    /**
     * Getter for the number of records already indexed by the User
     * 
     * @return int number of indexed records
     */
    public int indexedRecords() {
        return 0;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(Object o) {
        return false;
    }
}
