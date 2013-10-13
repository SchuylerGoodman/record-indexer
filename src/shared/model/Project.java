package shared.model;

/**
 * Model class to contain database information for an indexing Project in memory.
 * 
 * @author Schuyler Goodman
 */
public class Project {

    /**
     * Getter method for the Project ID.
     * 
     * @return int Unique ID of this Project.
     */
    public int projectId() {
        return 0;
    }
    
    /**
     * Getter method for the title of this Project.
     * 
     * @return String Project title.
     */
    public String title() {
        return null;
    }
    
    /**
     * Getter method for the number of Fields in this Project.
     * 
     * @return int Field (column) number
     */
    public int numberOfFields() {
        return 0;
    }
    
    /**
     * Getter method for the top-left y coordinate of the Images in this Project.
     * 
     * @return int top-left y coordinate
     */
    public int firstYCoord() {
        return 0;
    }
    
    /**
     * Getter method for the height of each Field in this Project (all Fields are the same height).
     * 
     * @return int height of each field
     */
    public int fieldHeight() {
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