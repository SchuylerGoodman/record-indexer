package client;

/**
 * Client Communicator class with a connection to the server.
 * 
 * @author goodm4n
 */
public class ToServer {
    
    /**
     * Queries the database to validate the input shared.communication.ValidateUser_Param object.
     * 
     * @param userParams a shared.communication.ValidateUser_Param container for
     * the User's username and password.
     * @return shared.communication.ValidateUser_Result container class for a 
     * User matching the input parameters, or null if the parameters did not match a user in the database.
     */
    public shared.communication.ValidateUser_Result validateUser(shared.communication.ValidateUser_Param userParams) {
        return null;
    }
    
    /**
     * Queries the database to find the projects available to this User.
     * 
     * @param userParams shared.communication.GetProjects_Param container for
     * the User's username and password.
     * @return shared.communication.GetProjects_Result container class for an
     * array of available Projects.
     */
    public shared.communication.GetProjects_Result getProjects(shared.communication.GetProjects_Param userParams) {
        return null;
    }
    
    /**
     * Queries the database for the URL for a sample image of the given Project.
     * 
     * @param imageParams shared.communication.getSampleImage_Param container for
     * the User's username, password, and the requested Project ID.
     * @return shared.communication.getSampleImage_Result container class
     * for a URL pointing to the sample image or null if the parameters do not 
     * match anything in the database.
     */
    public shared.communication.GetSampleImage_Result getSampleImage(shared.communication.GetSampleImage_Param imageParams) {
        return null;
    }
    
    /**
     * Sends a request to the server to download the Project, Batch, Fields, 
     * and known records associated with the parameters in batchParams.
     * <p>
     * Assigns this Image to the User.
     * 
     * @param batchParams shared.communication.DownloadBatch_Param container for
     * the User's username and password, and a Project ID.
     * @return shared.communication.DownloadBatch_Result container for a Project
     * object containing all the useful infos, or null if the batchParams did 
     * not match anything in the database.
     */
    public shared.communication.DownloadBatch_Result downloadBatch(shared.communication.DownloadBatch_Param batchParams) {
        return null;
    }

    /**
     * Attempts to submit the indexed records for the current Image to the database.
     * <p>
     * If successful, the User is un-assigned from this Image.
     * 
     * @param submitParams shared.communication.SubmitBatch_Param container for 
     * the User's username and password, the Image ID, and a collection of 
     * Record objects.
     * @return shared.communication.SubmitBatch_Result container which says if 
     * the submission was successful or not.
     */
    public shared.communication.SubmitBatch_Result submitBatch(shared.communication.SubmitBatch_Param submitParams) {
        return null;
    }
    
    /**
     * Gets a collection of fields from the database.
     * 
     * @param fieldParams shared.communication.GetFields_Param container for 
     * the User's username and password, and an Object specifying from which 
     * Project to get the fields.
     * @return shared.communication.GetFields_Result container for the Collection 
     * of returned Field objects.
     */
    public shared.communication.GetFields_Result getFields(shared.communication.GetFields_Param fieldParams) {
        return null;
    }
    
    /**
     * Searches the database for fields.
     * 
     * @param searchParams shared.communication.Search_Param container for 
     * the User's username and password, a comma-separated list of field ids, 
     * and a comma-separated list of search strings.
     * @return shared.communication.Search_Result container for a Collection 
     * of a Collection of Objects.
     *      Each inner Collection contains:
     *          Integer batchId
     *          URL imageURL
     *          Integer recordNumber
     *          Integer fieldId
     */
    public shared.communication.Search_Result search(shared.communication.Search_Param searchParams) {
        return null;
    }
    
}
