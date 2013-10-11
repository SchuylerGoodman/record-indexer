/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author goodm4n
 */
public interface Field<T extends Object> {
    
    /**
     * Loads the field keyed by field_id from the database into this object.
     * 
     * @param field_id 
     */
    public void loadField(int fieldId);
    
    /**
     * Getter for the internal value of the field.
     * 
     * @return the value from the database as a template object, or null if it 
     * does not exist.
     */
    public T getValue();
    
    /**
     * Getter for the name of the field for its project.
     * 
     * @return String containing Field name
     */
    public String getName();
    
    /**
     * Getter for the column that the field is in for its project.
     * 
     * @return the column number
     */
    public int getColumn();
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object o);
    
}
