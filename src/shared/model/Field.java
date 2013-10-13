package shared.model;

import java.net.URL;

/**
 * Model class to hold Field data in memory
 * 
 * @author schuyler
 */
public class Field {
    
    /**
     * Getter method for this Field's unique ID
     * 
     * @return Unique Field identifier
     */
    public int fieldId() {
        return 0;
    }
    
    /**
     * Getter method for the title of this Field (i.e. 'first name')
     * 
     * @return Title string of this Field
     */
    public String title() {
        return null;
    }
    
    /**
     * Getter method for the leftmost x coordinate of this Field on the Image.
     * 
     * @return x coordinate in pixels from the left edge of the Image
     */
    public int xCoordinate() {
        return 0;
    }
    
    /**
     * Getter method for the width of this field on the Image.
     * 
     * @return width in pixels of the field.
     */
    public int width() {
        return 0;
    }
    
    /**
     * Getter method for the URL path to the helper text for this field.
     * 
     * @return URL path to the Field's helper text.
     */
    public URL helpHtml() {
        return null;
    }
    
    /**
     * Getter method for the Project ID to which this Field belongs.
     * 
     * @return Unique Project Identifier
     */
    public int projectId() {
        return 0;
    }
    
    /**
     * Getter method for the column number.
     * 
     * @return Column number for the project.
     */
    public int columnNumber() {
        return 0;
    }
}
