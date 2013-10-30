package shared.model;

import server.database.Database;

/**
 * Model class to contain database information for a user in memory.
 * 
 * @author goodm4n
 */
public class User implements ModelClass {
    
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int indexedRecords;
    
    public User() {
        this.id = 0;
        this.username = null;
        this.firstName = null;
        this.lastName = null;
        this.password = null;
        this.email = null;
        this.indexedRecords = -1;
    }
    
    public User(int inId, String uName, String fName, String lName, String pWord, String inEmail, int numRecords) {
        this.id = inId;
        this.username = uName;
        this.firstName = fName;
        this.lastName = lName;
        this.password = pWord;
        this.email = inEmail;
        this.indexedRecords = numRecords;
    }
    
    public User(int inId, String uName, String fName, String lName, String pWord, String inEmail) {
        this.id = inId;
        this.username = uName;
        this.firstName = fName;
        this.lastName = lName;
        this.password = pWord;
        this.email = inEmail;
        this.indexedRecords = -1;
    }
    
    public User(String uName, String fName, String lName, String pWord, String inEmail, int numRecords) {
        this.id = 0;
        this.username = uName;
        this.firstName = fName;
        this.lastName = lName;
        this.password = pWord;
        this.email = inEmail;
        this.indexedRecords = numRecords;
    }
    
    public User(String uName, String fName, String lName, String pWord, String inEmail) {
        this.id = 0;
        this.username = uName;
        this.firstName = fName;
        this.lastName = lName;
        this.password = pWord;
        this.email = inEmail;
        this.indexedRecords = -1;
    }
    
    /**
     * Getter for the unique User ID.
     * 
     * @return int user ID
     */
    public int userId() {
        return this.id;
    }
    
    /**
     * Getter for the User's username
     * 
     * @return String username
     */
    public String username() {
        return this.username;
    }
    
    /**
     * Getter for the User's password
     * 
     * @return String password
     */
    public String password() {
        return this.password;
    }
    
    /**
     * Getter for the User's email
     * 
     * @return String email
     */
    public String email() {
        return this.email;
    }
    
    /**
     * Getter for the user's first name.
     * 
     * @return String representing the User's first name.
     */
    public String firstName() {
        return this.firstName;
    }
    
    /**
     * Getter for the user's last name.
     * 
     * @return String representing the User's last name.
     */
    public String lastName() {
        return this.lastName;
    }
    
    /**
     * Getter for the number of records already indexed by the User
     * 
     * @return int number of indexed records
     */
    public int indexedRecords() {
        return this.indexedRecords;
    }

    /**
     * Setter for the User's user ID
     * 
     * @param newId ID to set
     */
    public void setUserId(int newId) {
        this.id = newId;
    }
    
    /**
     * Setter for the User's username
     * 
     * @param newUsername username to set
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
    
    /**
     * Setter for the User's first name
     * 
     * @param newName first name to set
     */
    public void setFirstName(String newName) {
        this.firstName = newName;
    }
    
    /**
     * Setter for the User's last name
     * 
     * @param newName last name to set
     */
    public void setLastName(String newName) {
        this.lastName = newName;
    }
    
    /**
     * Setter for the User's password
     * 
     * @param newPassword password to set
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    
    /**
     * Setter for the User's email
     * 
     * @param newEmail email to set
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }
    
    /**
     * Setter for the number of indexed records
     * 
     * @param newNumber number to set
     */
    public void setIndexedRecords(int newNumber) {
        this.indexedRecords = newNumber;
    }
    
    @Override
    public int hashCode() {
        int prime = 37;
        int prime2 = 43;
        int tRec = this.indexedRecords;
        int tId = this.id;
        if (tId == 0) {
            tId = prime;
        }
        int hash = tId * username.length() * prime;
        hash ^= firstName.length() * prime2;
        hash *= lastName.length() * prime;
        hash ^= password.length() * prime2;
        hash *= email.length() * prime;
        if (tRec < 0) {
            tRec = prime2;
        }
        hash *= tRec;
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        User ob = (User) o;
        if (this.id != ob.userId()) {
            return false;
        }
        if (!this.username.equals(ob.username())
                || !this.firstName.equals(ob.firstName())
                || !this.lastName.equals(ob.lastName())
                || !this.password.equals(ob.password())
                || !this.email.equals(ob.email())
                || this.indexedRecords != ob.indexedRecords())
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean canInsert() {
        if (username == null) {
            return false;
        }
        if (firstName == null) {
            return false;
        }
        if (lastName == null) {
            return false;
        }
        if (password == null) {
            return false;
        }
        if (email == null) {
            return false;
        }
        return true;
    }

    @Override
    public String getTableName() {
        return Database.USERS;
    }
    
}
