/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.database.Database.*;
import shared.communication.*;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class API {
    
    public static final String URLPREFIX = "http://students.cs.byu.edu/~goodm4n/indexer/";
    
    private Database database;
    
    public static class APIException extends Exception {
        public APIException(String message) {
            super(message);
        }
    }
    
    public API() throws APIException {
        try {
            database = new Database();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, "Error loading database.", ex);
            throw new APIException("Database failed to load.");
        }
    }
    
    /**
     * Queries the database for the user specified.
     * <p>
     * 
     * @param params shared.communication.ValidateUser_Param holding username and password.
     * @param password The User's password, for authentication.
     * 
     * @return shared.communication.ValidateUser_Result with results.
     */
    public ValidateUser_Result validateUser(ValidateUser_Param params) {

        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.validateUser()");
        ValidateUser_Result result;
        boolean success = false;
        try {
            try {
                database.startTransaction();
                User user = (User) database.get(Database.USERS, params.username(), params.password());
                if (user != null) { // Then the user exists.
                    result = new ValidateUser_Result(true,
                                                   user.userId(),
                                                   user.firstName(),
                                                   user.lastName(),
                                                   user.indexedRecords());
                    success = true;
                }
                else {
                    result = new ValidateUser_Result(false);
                }
            }
            catch (DatabaseException | GetFailedException ex) {
                result = null;
                Logger.getLogger(API.class.getName()).log(Level.WARNING, "validateUser request failed.", ex);
            }
        } catch (ValidateUser_Result.ValidateUser_ResultException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.validateUser()");
        return result;
        
    }
    
    /**
     * Query the database for the projects available to the current User.
     * 
     * @param params shared.communication.GetProjects_Param holding username and password.
     * 
     * @return shared.communication.GetProjects_Result with results
     */
    public GetProjects_Result getProjects(GetProjects_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getProjects()");
        GetProjects_Result result;
        boolean success = false;
        try {
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                database.startTransaction();
                ArrayList<Project> projects = (ArrayList) database.get(Database.PROJECTS);
                ArrayList<Integer> ids = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                for (Project project : projects) {
                    ids.add(new Integer(project.projectId()));
                    names.add(project.title());
                }
                result = new GetProjects_Result(ids, names);
                success = true;
            }
        }
        catch (DatabaseException | GetFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "getProjects request failed.", ex);
        }
        catch (GetProjects_Result.GetProjects_ResultException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.getProjects()");
        return result;
        
    }
     
    /**
     * Queries the database for a sample Image from the specified project.
     * 
     * @param params shared.communication.GetSampleImage_Param container for the parameters for this api.
     * 
     * @return shared.communication.GetSampleImage_Result with results.
     */
    public GetSampleImage_Result getSampleImage(GetSampleImage_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getSampleImage()");
        GetSampleImage_Result result;
        boolean success = false;
        try {
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                database.startTransaction();
                ArrayList<Image> images = (ArrayList) database.get(Database.IMAGES);
                URL imageURL = null;
                int index = 0;
                while (imageURL == null || index >= images.size() - 1) {
                    Image currentImage = images.get(index);
                    if (currentImage.projectId() == params.projectId()
                            && currentImage.currentUser() == 0) {
                        imageURL = currentImage.path();
                    }
                    ++index;
                }
                if (imageURL == null) {
                    result = null;
                }
                else {
                    result = new GetSampleImage_Result(imageURL);
                    success = true;
                }
            }
        } catch (DatabaseException | GetFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "getSampleImage request failed.", ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.getSampleImage()");
        return result;
        
    }
    
    /**
     * Queries the database to package and send a Project and its data to the 
     * HttpHandler.
     * <p>
     * Assigns this Image to the User.
     * 
     * @param params Communication class containing parameters for this API.
     * 
     * @return shared.communication.DownloadBatch_Result container for the data to be downloaded.
     * exist.
     */
    public DownloadBatch_Result downloadBatch(DownloadBatch_Param params) {

        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.downloadBatch()");
        DownloadBatch_Result result;
        boolean success = false;
        try {
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                database.startTransaction();
                // Get the project
                Project project = (Project) database.get(Database.PROJECTS, params.projectId());
                // Get all the images
                ArrayList<Image> images = (ArrayList) database.get(Database.IMAGES);
                Image image = null;
                int index = 0;
                while (image == null || index >= images.size() - 1) {
                    // Find an unassigned image from the project
                    Image currentImage = images.get(index);
                    if (currentImage.projectId() == params.projectId()
                            && currentImage.currentUser() == 0) {
                        image = currentImage;
                    }
                    ++index;
                }
                // If there is none, fail
                if (image == null) {
                    result = null;
                }
                else { // Otherwise
                    image.setCurrentUser(vResult.userId());
                    // Set the current user in the image to this user
                    database.update(image);
                    ArrayList<Field> allFields = (ArrayList) database.get(Database.FIELDS);
                    ArrayList<Field> fields = new ArrayList<>();
                    for (Field field : allFields) {
                        if (field.projectId() == params.projectId()) {
                            fields.add(field);
                        }
                    }
                    result = new DownloadBatch_Result(project, image, fields);
                    success = true;
                }
            }
        } catch (DatabaseException | GetFailedException | UpdateFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "downloadBatch request failed.", ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.downloadBatch()");
        return result;
        
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
    public SubmitBatch_Result submitBatch(SubmitBatch_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.submitBatch()");
        SubmitBatch_Result result;
        boolean success = false;
        try { // validate user
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // user validated
                database.startTransaction();
                // Get the Image
                Image image = (Image) database.get(Database.IMAGES, params.batchId());
                // Get all Fields
                ArrayList<Field> allFields = (ArrayList) database.get(Database.FIELDS);
                ArrayList<Field> fields = new ArrayList<>();
                // Get fields with the correct project ID
                for (Field field : allFields) {
                    if (field.projectId() == image.projectId()) {
                        fields.add(field);
                    }
                }
                // Split the records on ';' character
                
                String[] records = params.records().split(";");
                for (int rowNumber = 0; rowNumber < records.length; ++rowNumber) {
                    // Split the record values on ',' character
                    String[] values = records[rowNumber].split(",", -1);
                    for (int column = 1; column <= values.length; ++column) {
                        Field currentField = fields.get(column - 1);
                        // Insert the value for this record into the database
                        database.insert(new Record(params.batchId(), currentField.fieldId(), rowNumber, values[column - 1]));
                    }
                }
                // Update Current user for the image to 0 (unassign User)
                image.setCurrentUser(0);
                database.update(image);
                // Update the number of indexed records for this user
                User user = new User(vResult.userId(),
                                     null, null, null, null, null,
                                     vResult.recordsIndexed() + records.length);
                database.update(user);
                success = true;
                result = new SubmitBatch_Result(success);
            }
        }
        catch (DatabaseException
               | InsertFailedException
               | GetFailedException
               | UpdateFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "submitBatch request failed.", ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.submitBatch()");
        return result;
        
    }
    
    /**
     * Asks the database for the fields requested by the client.
     * 
     * @param params GetFields_Param container for the parameters for this API
     * 
     * @return GetFields_Result container for the result parameters for this API
     */
    public GetFields_Result getFields(GetFields_Param params) throws APIException {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        GetFields_Result result;
        boolean success = false;
        try { // Validate User
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // User exists
                database.startTransaction();
                boolean useAll = false;
                int projectId;
                // Get all fields
                ArrayList<Field> allFields = (ArrayList) database.get(Database.FIELDS);
                ArrayList<Integer> fieldIds = new ArrayList<>();
                ArrayList<String> fieldTitles = new ArrayList<>();
                // If the project ID is a String
                if (String.class == params.projectId().getClass()) {
                    String projectParam = (String) params.projectId();
                    if (projectParam.isEmpty()) { // And it is empty
                        useAll = true; // We want to return all fields
                        projectId = 0;
                    }
                    else { // And it is not empty, it is invalid.
                        APIException ex = new APIException("Project ID parameter is invalid.");
                        Logger.getLogger(API.class.getName()).log(Level.WARNING, "String Project ID must be empty.", ex);
                        throw ex;
                    }
                } // If the project ID is not a String or an Integer, it is invalid.
                else if (Integer.class != params.projectId().getClass()) {
                    APIException ex = new APIException("Project ID parameter is invalid.");
                    Logger.getLogger(API.class.getName()).log(Level.WARNING, "Project ID must be empty String or Integer", ex);
                    throw ex;
                }
                else { // If it is an Integer, get the int value.
                    projectId = ((Integer) params.projectId()).intValue();
                }
                // Get data out of either all Fields, if project ID is an empty String
                // or out of the fields that match the projectId
                for (Field field : allFields) {
                    if (useAll || field.projectId() == projectId) {
                        fieldIds.add(new Integer(field.fieldId()));
                        fieldTitles.add(field.title());
                    }
                }
                if (fieldIds.size() != fieldTitles.size()) {
                    result = null;
                }
                else {
                    result = new GetFields_Result(projectId, fieldIds, fieldTitles);
                    success = true;
                }
            }
        }
        catch (DatabaseException | GetFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "getFields request failed.", ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Exiting API.getFields()");
        return result;
        
    }
    
    /**
     * Sends a request to the database to search for matching records.
     * 
     * @param params Search_Param container for the parameters for this API.
     * 
     * @return Search_Result container for the results of this API.
     */
    public Search_Result search(Search_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        Search_Result result;
        boolean success = false;
        try {
            ValidateUser_Result vResult = validateUser(new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // User exists
                database.startTransaction();
                String[] fieldIdArray = params.fields().split(",", -1);
                ArrayList<String> fieldIdsAsStrings = new ArrayList<>(Arrays.asList(fieldIdArray));
                String[] valueArray = params.values().split(",", -1);
                ArrayList<String> values = new ArrayList<>(Arrays.asList(valueArray));
                ArrayList<Record> records = (ArrayList) database.search(fieldIdsAsStrings, values);

                ArrayList<Integer> imageIds = new ArrayList<>();
                ArrayList<URL> imageURLs = new ArrayList<>();
                ArrayList<Integer> rowNumbers = new ArrayList<>();
                ArrayList<Integer> fieldIds = new ArrayList<>();
                HashMap<Integer, Image> images = new HashMap<>();
                for (Record record : records) {
                    Image currentImage;
                    if (!images.containsKey(record.imageId())) {
                        currentImage = (Image) database.get(Database.IMAGES, record.imageId());
                        images.put(new Integer(record.imageId()), currentImage);
                    }
                    else {
                        currentImage = images.get(record.imageId());
                    }
                    imageIds.add(currentImage.imageId());
                    imageURLs.add(currentImage.path());
                    rowNumbers.add(record.rowNumber());
                    fieldIds.add(new Integer(record.fieldId()));
                }
                result = new Search_Result(imageIds, imageURLs, rowNumbers, fieldIds);
                success = true;
            }
        }
        catch (DatabaseException | GetFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "search request failed.", ex);
        }
        database.endTransaction(success);
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        return result;
        
    }    
}
