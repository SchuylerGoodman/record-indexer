package shared.model;

import java.io.Serializable;
import server.database.Database;

/**
 * Model class to hold Field data in memory
 * 
 * @author schuyler
 */
public class Field extends ModelClass implements Serializable {
    
    private int fieldId;
    private String title;
    private int xCoordinate;
    private int width;
    private String helpHtml;
    private int columnNumber;
    private int projectId;
    private String knownData;
    
    public Field() {
        fieldId = 0;
        title = null;
        xCoordinate = -1;
        width = -1;
        helpHtml = null;
        columnNumber = 0;
        projectId = 0;
        knownData = null;
    }
    
    public Field(String inTitle, int inXCoordinate, int inWidth, String inHelpHtml,
            int inColumnNumber, int inProjectId) {
        
        fieldId = 0;
        title = inTitle;
        xCoordinate = inXCoordinate;
        width = inWidth;
        helpHtml = inHelpHtml;
        columnNumber = inColumnNumber;
        projectId = inProjectId;
        knownData = null;
    }
    
    public Field(String inTitle, int inXCoordinate, int inWidth, String inHelpHtml,
            int inColumnNumber, int inProjectId, String inKnownData) {
        
        fieldId = 0;
        title = inTitle;
        xCoordinate = inXCoordinate;
        width = inWidth;
        helpHtml = inHelpHtml;
        columnNumber = inColumnNumber;
        projectId = inProjectId;
        knownData = inKnownData;
    }
    
    public Field(int inFieldId, String inTitle, int inXCoordinate, int inWidth,
            String inHelpHtml, int inColumnNumber, int inProjectId) {
        
        fieldId = inFieldId;
        title = inTitle;
        xCoordinate = inXCoordinate;
        width = inWidth;
        helpHtml = inHelpHtml;
        columnNumber = inColumnNumber;
        projectId = inProjectId;
        knownData = null;
    }
    
    public Field(int inFieldId, String inTitle, int inXCoordinate, int inWidth,
            String inHelpHtml, int inColumnNumber, int inProjectId, String inKnownData) {
        
        fieldId = inFieldId;
        title = inTitle;
        xCoordinate = inXCoordinate;
        width = inWidth;
        helpHtml = inHelpHtml;
        columnNumber = inColumnNumber;
        projectId = inProjectId;
        knownData = inKnownData;
    }
    
    /**
     * Getter method for this Field's unique ID
     * 
     * @return Unique Field identifier
     */
    public int fieldId() {
        return fieldId;
    }
    
    /**
     * Getter method for the title of this Field (i.e. 'first name')
     * 
     * @return Title string of this Field
     */
    public String title() {
        return title;
    }
    
    /**
     * Getter method for the leftmost x coordinate of this Field on the Image.
     * 
     * @return x coordinate in pixels from the left edge of the Image
     */
    public int xCoordinate() {
        return xCoordinate;
    }
    
    /**
     * Getter method for the width of this field on the Image.
     * 
     * @return width in pixels of the field.
     */
    public int width() {
        return width;
    }
    
    /**
     * Getter method for the String path to the helper text for this field.
     * 
     * @return String path to the Field's helper text.
     */
    public String helpHtml() {
        return helpHtml;
    }
    
    /**
     * Getter method for the column number.
     * 
     * @return Column number for the project.
     */
    public int columnNumber() {
        return columnNumber;
    }
    
    /**
     * Getter method for the Project ID to which this Field belongs.
     * 
     * @return Unique Project Identifier
     */
    public int projectId() {
        return projectId;
    }
    
    /**
     * Getter method for the String containing the known data for this field.
     * 
     * @return String path to the known data .txt file on the server
     */
    public String knownData() {
        return knownData;
    }
    
    /**
     * Setter for the ID of this field.
     * 
     * @param newId ID to set
     */
    public void setFieldId(int newId) {
        fieldId = newId;
    }
    
    /**
     * Setter for the title of this field.
     * 
     * @param newTitle Title to set
     */
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    
    /**
     * Setter for the leftmost x coordinate of this field.
     * 
     * @param newCoordinate Coordinate (in pixels) to set
     */
    public void setXCoordinate(int newCoordinate) {
        xCoordinate = newCoordinate;
    }
    
    /**
     * Setter for the width of this field.
     * 
     * @param newWidth Width (in pixels) to set
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }
    
    /**
     * Setter for the location of the help text on the server.
     * 
     * @param newUrl String to set
     */
    public void setHelpHtml(String newUrl) {
        helpHtml = newUrl;
    }
    
    /**
     * Setter for the number of the column in the project where this field is located.
     * 
     * @param newNumber Number to set
     */
    public void setColumnNumber(int newNumber) {
        columnNumber = newNumber;
    }
    
    /**
     * Setter for the project ID this field belongs to.
     * 
     * @param newId ID to set
     */
    public void setProjectId(int newId) {
        projectId = newId;
    }
    
    /**
     * Setter for the String path of the known data .txt file for this field.
     * 
     * @param newUrl String to set
     */
    public void setKnownData(String newUrl) {
        knownData = newUrl;
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
        int prime1 = 43;
        int prime2 = 59;
        int tFieldId = fieldId;
        
        int tTitle;
        if (title == null) {
            tTitle = prime1;
        }
        else {
            tTitle = title.length();
        }
        
        int tXCoord = xCoordinate;
        int tWidth = width;
        
        int tHelp;
        if (helpHtml == null) {
            tHelp = prime2;
        }
        else {
            tHelp = helpHtml.length();
        }
        
        int tCol = columnNumber;
        int tProjectId = projectId;
        
        if (tFieldId < 1) {
            tFieldId = prime2;
        }
        if (tXCoord <= 0) {
            tXCoord = prime1;
        }
        if (tWidth <= 0) {
            tWidth = prime;
        }
        if (tCol < 1) {
            tCol = prime1;
        }
        if (tProjectId < 1) {
            tProjectId = prime2;
        }
        int tKData;
        if (knownData == null) {
            tKData = knownData.length();
        }
        else {
            tKData = prime;
        }
        
        int hash = tFieldId * prime;
        hash *= tTitle ^ prime2;
        hash += tXCoord * prime;
        hash *= tWidth ^ prime1;
        hash += tHelp * prime;
        hash *= tCol ^ prime2;
        hash += tProjectId * prime1;
        hash *= tKData ^ prime2;
        
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Field other = (Field) obj;
        if (this.fieldId != other.fieldId) {
            return false;
        }
        if (!this.title.equals(other.title)) {
            return false;
        }
        if (this.xCoordinate != other.xCoordinate) {
            return false;
        }
        if (this.width != other.width) {
            return false;
        }
        if (!this.helpHtml.equals(other.helpHtml)) {
            return false;
        }
        if (this.columnNumber != other.columnNumber) {
            return false;
        }
        if (this.projectId != other.projectId) {
            return false;
        }
        if (!this.knownData.equals(other.knownData)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canInsert() {
        if (title == null) {
            return false;
        }
        if (xCoordinate < 0) {
            return false;
        }
        if (width < 0) {
            return false;
        }
        if (helpHtml == null) {
            return false;
        }
        if (columnNumber < 1) {
            return false;
        }
        if (projectId < 1) {
            return false;
        }
        return true;
    }

    @Override
    public String getTableName() {
        return Database.FIELDS;
    }
    
}
