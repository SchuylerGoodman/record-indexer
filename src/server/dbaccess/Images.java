/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dbaccess;

import java.net.URL;

/**
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
}
