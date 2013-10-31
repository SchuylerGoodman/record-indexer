package shared.communication;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Communication class for the search API
 * 
 * @author schuyler
 */
public class Search_Result extends RequestResult implements Serializable {
    
    public class Search_ResultException extends Exception {
        public Search_ResultException(String message) {
            super(message);
        }
    }
    
    private List<Integer> imageIds;
    private List<String> imagePaths;
    private List<Integer> rowNumbers;
    private List<Integer> fieldIds;
    
    public Search_Result(List<Integer> imageIds, List<String> imagePaths,
                         List<Integer> rowNumbers, List<Integer> fieldIds)
            throws Search_ResultException {
        
        if (imageIds.size() != imagePaths.size()
                && imagePaths.size() != rowNumbers.size()
                && rowNumbers.size() != fieldIds.size()) {
            throw new Search_ResultException("All input parameters must be lists of equal length.");
        }
        
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
    
    public String toString(String protocol, String host, int port)
            throws MalformedURLException {
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imageIds.size(); ++i) {
            sb.append(imageIds.get(i)).append("\n");
            URL imagePath = new URL(protocol, host, port, imagePaths.get(i));
            sb.append(imagePath.toString()).append("\n");
            sb.append(rowNumbers.get(i)).append("\n");
            sb.append(fieldIds.get(i)).append("\n");
        }
        return sb.toString();
    }
    
}
