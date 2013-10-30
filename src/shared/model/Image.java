package shared.model;

import java.io.Serializable;
import server.database.Database;

/**
 * Model class to contain database information for an image in memory.
 * 
 * @author schuyler
 */
public class Image extends ModelClass implements Serializable {
    
    private int imageId;
    private String path;
    private String title;
    private int projectId;
    private int currentUser;
    
    public Image() {
        imageId = 0;
        path = null;
        title = null;
        projectId = 0;
        currentUser = 0;
    }
    
    public Image(String inPath, String inTitle, int inProjectId) {
        imageId = 0;
        path = inPath;
        title = inTitle;
        projectId = inProjectId;
        currentUser = 0;
    }
    
    public Image(String inPath, String inTitle, int inProjectId, int inCurrentUser) {
        imageId = 0;
        path = inPath;
        title = inTitle;
        projectId = inProjectId;
        currentUser = inCurrentUser;
    }
    
    public Image(int inImageId, String inPath, String inTitle, int inProjectId) {
        imageId = inImageId;
        path = inPath;
        title = inTitle;
        projectId = inProjectId;
        currentUser = 0;
    }
    
    public Image(int inImageId, String inPath, String inTitle, int inProjectId, int inCurrentUser) {
        imageId = inImageId;
        path = inPath;
        title = inTitle;
        projectId = inProjectId;
        currentUser = inCurrentUser;
    }
    
    /**
     * Getter method for the unique Image ID.
     * 
     * @return int image ID
     */
    public int imageId() {
        return imageId;
    }
    
    /**
     * Getter method for the String path to the Image
     * 
     * @return String path to this Image
     */
    public String path() {
        return path;
    }
    
    /**
     * Getter method for the title of this Image.
     * 
     * @return String image title
     */
    public String title() {
        return title;
    }
    
    /**
     * Getter method for the unique Project ID that this Image belongs to.
     * 
     * @return int project ID
     */
    public int projectId() {
        return projectId;
    }
    
    /**
     * Getter method for the user who is currently indexing this image.
     * 
     * @return int user ID
     */
    public int currentUser() {
        return currentUser;
    }
    
    /**
     * Setter method for the unique id of this image.
     * 
     * @param newId ID to set
     */
    public void setImageId(int newId) {
        imageId = newId;
    }
    
    /**
     * Setter method for the path to this image file on the server.
     * 
     * @param newPath String to set
     */
    public void setPath(String newPath) {
        path = newPath;
    }
    
    /**
     * Setter method for the title of this image.
     * 
     * @param newTitle Title to set
     */
    public void setTitle(String newTitle) {
        title = newTitle;
    }
    
    /**
     * Setter method for the project id to which this image belongs.
     * 
     * @param newId ID to set
     */
    public void setProjectId(int newId) {
        projectId = newId;
    }
    
    /**
     * Setter method for the currently indexing user.
     * 
     * @param newUserId ID to set
     */
    public void setCurrentUser(int newUserId) {
        currentUser = newUserId;
    }
    
    @Override
    public int hashCode() {
        int prime = 41;
        int prime1 = 31;
        int prime2 = 43;
        int prime3 = 59;
        int tIId = imageId;
        if (tIId == 0) {
            tIId = prime;
        }
        int tPath;
        if (path == null) {
            tPath = prime1;
        }
        else {
            tPath = path.length();
        }
        int tTitle;
        if (title == null) {
            tTitle = prime2;
        }
        else {
            tTitle = title.length();
        }
        int tPId = projectId;
        if (tPId == 0) {
            tPId = prime3;
        }
        int tCu = currentUser;
        if (tCu == 0) {
            tCu = prime1 * prime3;
        }
        
        int hash = tIId + tPath + tTitle + tPId + tCu;
        
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
        final Image other = (Image) obj;
        if (this.imageId != other.imageId) {
            return false;
        }
        if (!this.path.equals(other.path)) {
            return false;
        }
        if (!this.title.equals(other.title)) {
            return false;
        }
        if (this.projectId != other.projectId) {
            return false;
        }
        if (this.currentUser != other.currentUser) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canInsert() {
        if (path == null) {
            return false;
        }
        if (title == null) {
            return false;
        }
        if (projectId < 1) {
            return false;
        }
        return true;
    }

    @Override
    public String getTableName() {
        return Database.IMAGES;
    }
    
    
}
