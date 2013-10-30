package shared.communication;

import java.util.*;

/**
 * Communication class for the search API
 * 
 * @author schuyler
 */
public class Search_Result {
    
    private List<Integer> imageIds;
    private List<String> imagePaths;
    private List<Integer> rowNumbers;
    private List<Integer> fieldIds;
    
    public Search_Result(List<Integer> imageIds, List<String> imagePaths,
                         List<Integer> rowNumbers, List<Integer> fieldIds) {
        
        this.imageIds = imageIds;
        this.imagePaths = imagePaths;
        this.rowNumbers = rowNumbers;
        this.fieldIds = fieldIds;
    }
    
    /**
     * Getter method for the image IDs.
     * Correspond 1 to 1 with all other members of this class.
     * 
     * @return List of image IDs
     */
    public List<Integer> imageIds() {
        return imageIds;
    }
    
    /**
     * Getter method for the image URLs.
     * Correspond 1 to 1 with all other members of this class.
     * 
     * @return List of image URLs
     */
    public List<String> imagePaths() {
        return imagePaths;
    }

    /**
     * Getter method for the row numbers.
     * Correspond 1 to 1 with all other members of this class.
     * 
     * @return List of row numbers
     */
    public List<Integer> rowNumbers() {
        return rowNumbers;
    }
    
    /**
     * Getter method for the field IDs.
     * Correspond 1 to 1 with all other members of this class.
     * 
     * @return List of field IDs
     */
    public List<Integer> fieldIds() {
        return fieldIds;
    }
    
}
