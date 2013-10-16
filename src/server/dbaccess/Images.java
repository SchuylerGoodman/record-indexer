package server.dbaccess;

import java.net.URL;
import java.util.Collection;

/**
 * Database Access class for the images table.
 * 
 * @author schuyler
 */
public class Images {
    
    /**
     * Queries the database for a sample Image from the project specified in imageParams.
     * <p>
     * Preconditions:   Username and password belong to a User in the database.
     *                  Project ID refers to a project available to the User.
     * 
     * @param username
     * @param password
     * @param projectId unique identifier for the project from which to extract 
     * a sample Image
     * @return URL pointing to the sample Image for later download
     * @return null if User does not exist, or no project has projectId
     
     */
    protected URL getSampleImage(String username, String password, int projectId) {
        return null;
    }
    
    /**
     * Asks the database to find the first Image belonging to the specified 
     * Project with no assigned User.
     * <p>
     * Assigns this Image to the User.
     * Preconditions:   User must actually exist.
     *                  Project must actually exist.
     * 
     * @param projectId Unique identifier for the project from which to 
     * download an Image.
     * @return shared.model.Image containing the path to this Image, etc.
     * @return null if User does not exist, or no project has projectId
     
     */
    protected shared.model.Image downloadBatch(int projectId) {
        return null;
    }

    /**
     * Sets the currentUser field in the images table at imageId to null.
     * 
     * @param imageId Unique identifier for the image being unassigned.
     */
    protected void unassignUser(int imageId) {
    }
    
    /**
     * Searches the Images table for the path to an Image.
     * 
     * @param imageId Unique ID of the Image to search for.
     * @return URL path to the Image on the server.
     */
    protected URL search(int imageId) {
        return null;
    }
    
    /**
     * Inserts an new Image into the database.
     * 
     * @param newImage shared.model.Image model class to insert into the database.
     * @return shared.model.Image with the generated image ID
     */
    protected shared.model.Image insert(shared.model.Image newImage) {
        return null;
    }

    /**
     * Updates an Image in the database.
     * 
     * @param image shared.model.Image to update.
     */
    protected void update(shared.model.Image image) {
    }
    
    /**
     * Gets data on the requested images from the database.
     * 
     * @param imageIds Collection of image IDs whose data we want.
     * @return Collection of shared.model.Image objs with the requested info.
     */
    protected Collection<shared.model.Image> get(Collection<Integer> imageIds) {
        return null;
    }

    /**
     * Deletes an image from the database.
     * 
     * @param deleteImage Image to delete from the database.
     */
    protected void delete(shared.model.Image deleteImage) {
    }
}
