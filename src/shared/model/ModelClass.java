/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author schuyler
 */
public interface ModelClass {
    
    /**
     * Checks if the model class contains all the requisite information to be
     * able to insert it into the database.
     * 
     * @return true if insertable, otherwise false
     */
    public boolean canInsert();
    
    /**
     * Returns the String representation of the name of the database table for
     * this model class.
     * i.e. User.getName() returns "USER"
     * 
     * @return Name of the database table for this model class
     */
    public String getTableName();
    
}
