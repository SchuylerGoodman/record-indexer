package shared.communication;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import shared.model.Field;
import shared.model.Image;
import shared.model.Project;

/**
 * Communication class for the downloadBatch API
 * 
 * @author schuyler
 */
public class DownloadBatch_Result extends RequestResult implements Serializable {
    
    private int imageId;
    private int projectId;
    private String imagePath;
    private int firstYCoord;
    private int recordHeight;
    private int numRecords;
    private int numFields;
    private List<Field> fields;
    
    public DownloadBatch_Result(Project project, Image image, List<Field> inFields) {
        imageId = image.imageId();
        projectId = project.projectId();
        imagePath = image.path();
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
     * Getter method for the String path to the image file on the server.
     * 
     * @return Image Path
     */
    public String imagePath() {
        return imagePath;
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
    
    public String toString(String protocol, String host, int port)
            throws MalformedURLException {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.imageId).append("\n"); // Batch ID
        sb.append(this.projectId).append("\n"); // Project ID
        URL imageUrl = new URL(protocol, host, port, this.imagePath);
        sb.append(imageUrl).append("\n"); // Image URL
        sb.append(this.firstYCoord).append("\n"); // First Y Coordinate
        sb.append(this.recordHeight).append("\n"); // Record Height
        sb.append(this.numRecords).append("\n"); // Number of Records
        sb.append(this.numFields).append("\n"); // Number of Fields
        
        for (Field field : fields) { // For all returned Fields
            sb.append(field.fieldId()).append("\n"); // Field ID
            sb.append(field.columnNumber()).append("\n"); // Column Number
            sb.append(field.title()).append("\n"); // Title
            URL helpUrl = new URL(protocol, host, port, field.helpHtml());
            sb.append(helpUrl).append("\n"); // Help URL
            sb.append(field.xCoordinate()).append("\n"); // X Coordinate
            sb.append(field.width()).append("\n"); // Field Width
            if (!field.knownData().isEmpty() && field.knownData() != null) { // If known data URL exists for this Field
                URL dataUrl = new URL(protocol, host, port, field.knownData());
                sb.append(dataUrl.toString()).append("\n"); // Known Data URL
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        
        int hash;
        hash = this.firstYCoord + 13;
        hash += this.imageId + 31;
        hash += this.numFields + 47;
        hash += this.numRecords + 23;
        hash += this.projectId + 5;
        hash += this.recordHeight + 17;
        hash += this.fields.size() + 41;
        hash += this.imagePath.length() + 37;
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
        final DownloadBatch_Result other = (DownloadBatch_Result) obj;
        if (this.imageId != other.imageId) {
            return false;
        }
        if (this.projectId != other.projectId) {
            return false;
        }
        if (!Objects.equals(this.imagePath, other.imagePath)) {
            return false;
        }
        if (this.firstYCoord != other.firstYCoord) {
            return false;
        }
        if (this.recordHeight != other.recordHeight) {
            return false;
        }
        if (this.numRecords != other.numRecords) {
            return false;
        }
        if (this.numFields != other.numFields) {
            return false;
        }
        if (!Objects.deepEquals(this.fields, other.fields)) {
            return false;
        }
        return true;
    }
    
}
