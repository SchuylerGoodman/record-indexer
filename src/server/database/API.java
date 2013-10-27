/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import java.net.URL;
import java.util.Collection;

/**
 *
 * @author schuyler
 */
public class API {
    
    public static final String URLPREFIX = "http://students.cs.byu.edu/~goodm4n/indexer/";
    
    /**
     * Queries the database for the user specified.
     * <p>
     * Will be packaged into a shared.communication.validateUser_Result 
     * object by the HttpHandler.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * 
     * @return Collection of data to return to the client.
     * @return null if parameters did not match any User in the database.
     */
    public Collection<Object> validateUser(String username, String password) {
        return null;
    }
    
    /**
     * Query the database for the projects available to the current User.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * 
     * @return Collection of data to return to the client.
     * @return null if the User does not exist.
     */
    public Collection<Collection<Object>> getProjects(String username, String password) {
        return null;
    }
     
    /**
     * Queries the database for a sample Image from the project specified in imageParams.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * 
     * @param projectId Unique identifier for a Project in the database.
     * @return URL path to the sample image for later download.
     */
    public URL getSampleImage(String username, String password, int projectId) {
        return null;
    }
    
    /**
     * Queries the database to package and send a Project and its data to the 
     * HttpHandler.
     * <p>
     * Assigns this Image to the User.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * @param projectId Unique identifier for the Project to download.
     * @return shared.model.Project The container for the Project, Batch, Field, 
     * and Record data to be downloaded, unless the user or project ID does not 
     * exist.
     */
    public shared.model.Project downloadBatch(String username, String password, int projectId) {
        return null;
    }
    
    /**
     * Attempts to submit the records for an Image to the database.
     * <p>
     * If successful, the User will be un-assigned from this Image
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * @param imageId Unique identifier for the Image in the database.
     * @param records Collection of shared.model.Record objects to be submitted.
     * @return true if submission is successful
     * @return false if submission is unsuccessful
     */
    public boolean submitBatch(String username, String password, int imageId, Collection<shared.model.Record> records) {
        return false;
    }
    
    /**
     * Asks the database for the fields requested by the client.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * @param project An Object that is either an Integer with a Project ID, or 
     * an empty string signifying to return the fields from all projects.
     * @return A Collection of Field objects.
     */
    public Collection<shared.model.Field> getFields(String username, String password, Object project) {
        return null;
    }
    
    /**
     * Sends a request to the database to search for matching fields.
     * 
     * @param username The User's username, for authentication.
     * @param password The User's password, for authentication.
     * @param fields Comma-separated string with field IDs to search through.
     * @param values Comma-separated string with search values.
     * @return  Collection of a Collection of Objects.
     *      Each inner Collection contains:
     *          Integer batchId
     *          URL imageURL
     *          Integer recordNumber
     *          Integer fieldId
     */
    public Collection<Collection<Object>> search(String username, String password, String fields, String values) {
        return null;
    }    
}
