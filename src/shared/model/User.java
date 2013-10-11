/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author goodm4n
 */
public interface User {
    
    public static class InvalidUserInfoException extends Exception {}
    
    /**
     * Loads a User from the database.
     * If userName and userPassword do not match, throws an InvalidUserIdException.
     * 
     * @param userName name of the user account with password userPassword
     * @param userPassword password for the user account that matches userName
     * @throws shared.model.User.InvalidUserInfoException 
     */
    public User loadUser(String userName, String userPassword) throws InvalidUserInfoException;
    
    /**
     * Loads User settings like current project, etc from the local save file.
     */
    public void loadUserSettings();
    
    /**
     * Saves User settings to the local save file.
     */
    public void saveUserSettings();
    
    /**
     * Getter for the user's first name.
     * 
     * @return String representing the User's first name.
     */
    public String fName();
    
    /**
     * Getter for the user's last name.
     * 
     * @return String representing the User's last name.
     */
    public String lName();
    
    /**
     * Getter for the User's current Project.
     * 
     * @return Project
     */
    public Project currentProject();
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object o);
}
