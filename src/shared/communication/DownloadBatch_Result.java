package shared.communication;

import java.net.URL;
import java.util.List;
import shared.model.Field;
import shared.model.Image;
import shared.model.Project;

/**
 * Communication class for the downloadBatch API
 * 
 * @author schuyler
 */
public class DownloadBatch_Result {
    
    private int imageId;
    private int projectId;
    private URL imageURL;
    private int firstYCoord;
    private int recordHeight;
    private int numRecords;
    private int numFields;
    private List<Field> fields;
    
    public DownloadBatch_Result(Project project, Image image, List<Field> inFields) {
        imageId = image.imageId();
        projectId = project.projectId();
        imageURL = image.path();
        firstYCoord = project.firstYCoord();
        recordHeight = project.fieldHeight();
        numRecords = project.recordCount();
        numFields = inFields.size();
        fields = inFields;
    }
    
    /**
     * Getter method for the Image ID
     * 
     * @return image ID
     */
    public int imageId() {
        return imageId;
    }
    
    /**
     * Getter method for the Project ID
     * 
     * @return project ID
     */
    public int projectId() {
        return projectId;
    }
    
    /**
     * Getter method for the URL path to the image file on the server.
     * 
     * @return Image URL
     */
    public URL imageURL() {
        return imageURL;
    }
    
    /**
     * Getter method for the first Y coordinate of the image.
     * 
     * @return Coordinate in pixels from the top
     */
    public int firstYCoord() {
        return firstYCoord;
    }
    
    /**
     * Getter method for the height of the record.
     * 
     * @return Height of the record in pixels
     */
    public int recordHeight() {
        return recordHeight;
    }
    
    /**
     * Getter method for the number of records (rows) in this image.
     * 
     * @return number of records
     */
    public int numRecords() {
        return numRecords;
    }
    
    /**
     * Getter method for the number of fields (columns) in this image.
     * 
     * @return number of fields
     */
    public int numFields() {
        return numFields;
    }

    /**
     * Getter method for all the fields associated with this image.
     * 
     * @return List of field objects
     */
    public List<Field> fields() {
        return fields;
    }
    
}
