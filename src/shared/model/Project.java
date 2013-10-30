package shared.model;

import java.io.Serializable;
import server.database.Database;

/**
 * Model class to contain database information for an indexing Project in memory.
 * 
 * @author Schuyler Goodman
 */
public class Project extends ModelClass implements Serializable {

    private int projectId;
    private String title;
    private int recordCount;
    private int firstYCoord;
    private int fieldHeight;
    
    public Project() {
        projectId = 0;
        title = null;
        recordCount = -1;
        firstYCoord = -1;
        fieldHeight = -1;
    }
    
    public Project(String inTitle, int inRecordCount, int inFirstYCoord, int inFieldHeight) {
        projectId = 0;
        title = inTitle;
        recordCount = inRecordCount;
        firstYCoord = inFirstYCoord;
        fieldHeight = inFieldHeight;
    }
    
    public Project(int inId, String inTitle, int inRecordCount, int inFirstYCoord, int inFieldHeight) {
        projectId = inId;
        title = inTitle;
        recordCount = inRecordCount;
        firstYCoord = inFirstYCoord;
        fieldHeight = inFieldHeight;
    }
    
    /**
     * Getter method for the Project ID.
     * 
     * @return int Unique ID of this Project.
     */
    public int projectId() {
        return projectId;
    }
    
    /**
     * Getter method for the title of this Project.
     * 
     * @return String Project title.
     */
    public String title() {
        return title;
    }
    
    /**
     * Getter method for the number of Fields in this Project.
     * 
     * @return int Field (column) number
     */
    public int recordCount() {
        return recordCount;
    }
    
    /**
     * Getter method for the top-left y coordinate of the Images in this Project.
     * 
     * @return int top-left y coordinate
     */
    public int firstYCoord() {
        return firstYCoord;
    }
    
    /**
     * Getter method for the height of each Field in this Project (all Fields are the same height).
     * 
     * @return int height of each field
     */
    public int fieldHeight() {
        return fieldHeight;
    }
    
    /**
     * Setter for the project ID
     * 
     * @param newId ID to set
     */
    public void setProjectId(int newId) {
        projectId = newId;
    }
    
    /**
     * Setter for the project title
     * 
     * @param newTitle Title to set
     */
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    
    /**
     * Setter for the number of fields in this project
     * 
     * @param newCount Number of fields
     */
    public void setRecordCount(int newCount) {
        recordCount = newCount;
    }
    
    /**
     * Setter for the top left of the images in the project.
     * 
     * @param newCoord Coordinate to set (in pixels from the top)
     */
    public void setFirstYCoord(int newCoord) {
        firstYCoord = newCoord;
    }
    
    /**
     * Setter for the height of the fields in the project.
     * 
     * @param newHeight Height (in pixels) to set.
     */
    public void setFieldHeight(int newHeight) {
        fieldHeight = newHeight;
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
        int prime2 = 47;
        int tId = this.projectId;
        int tFCount = this.recordCount;
        int tYCoord = this.firstYCoord;
        int tFHeight = this.fieldHeight;
        int tTitle;
        if (tId == 0) {
            tId = prime;
        }
        if (tFCount < 0) {
            tFCount = prime2;
        }
        if (tYCoord < 0) {
            tYCoord = prime ^ prime2;
        }
        if (tFHeight < 0) {
            tFHeight = prime * prime2;
        }
        if (this.title == null) {
            tTitle = prime;
        }
        else {
            tTitle = this.title.length();
        }
        int hash = tId * prime2;
        hash *= tFCount ^ prime;
        hash += tYCoord * prime2;
        hash *= tFHeight ^ prime;
        hash += tTitle * prime2;
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Project pr = (Project) o;
        if (pr.projectId != this.projectId) {
            return false;
        }
        if (pr.recordCount != this.recordCount
                || pr.fieldHeight != this.fieldHeight
                || pr.firstYCoord != this.firstYCoord
                || !pr.title.equals(this.title)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canInsert() {
        if (title == null) {
            return false;
        }
        if (recordCount < 1) {
            return false;
        }
        if (firstYCoord < 0) {
            return false;
        }
        if (fieldHeight < 0) {
            return false;
        }
        return true;
    }

    @Override
    public String getTableName() {
        return Database.PROJECTS;
    }
    
}
