/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.database.Database;
import server.database.Database.*;
import server.database.Images;
import shared.communication.*;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class API {
  
//    public static final String URLPREFIX = "localhost/";
//    public static final String URLPREFIX = "http://students.cs.byu.edu/~goodm4n/indexer/";
    
    public static class APIException extends Exception {
        public APIException(String message) {
            super(message);
        }
    }
    
    public API() {}
    
    /**
     * Queries the database for the user specified.
     * <p>
     * 
     * @param params shared.communication.ValidateUser_Param holding username and password.
     * @param password The User's password, for authentication.
     * 
     * @return shared.communication.ValidateUser_Result with results.
     */
    public static ValidateUser_Result validateUser(Database database, ValidateUser_Param params) {

        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.validateUser()");
        ValidateUser_Result result;
        ArrayList<User> users = new ArrayList<>();
        try {
            try {

                // Get the User with this username and password
                User user = new User();
                user.setUsername(params.username());
                user.setPassword(params.password());
                users.addAll((ArrayList) database.get(user));
                
                // Should return one or fewer results, because passwords are unique
                assert users.size() <= 1;
                
                if (!users.isEmpty() && users.size() <= 1) { // Then the user exists.
                    int onlyResult = 0;
                    user = users.get(onlyResult);
                    result = new ValidateUser_Result(true,
                                                   user.userId(),
                                                   user.firstName(),
                                                   user.lastName(),
                                                   user.indexedRecords());
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
    public static GetProjects_Result getProjects(Database database, GetProjects_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getProjects()");
        GetProjects_Result result;
        try {
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                
                // Get all Projects
                ArrayList<Project> projects = (ArrayList) database.get(new Project());
                
                // Add them to GetProjects_Result
                ArrayList<Integer> ids = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                for (Project project : projects) {
                    ids.add(new Integer(project.projectId()));
                    names.add(project.title());
                }
                result = new GetProjects_Result(ids, names);
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
    public static GetSampleImage_Result getSampleImage(Database database, GetSampleImage_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getSampleImage()");
        GetSampleImage_Result result;
        try {
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                // Get all Images from this Project
                Image tImage = new Image();
                tImage.setProjectId(params.projectId());
                ArrayList<Image> images = (ArrayList) database.get(tImage);
                
                // Get an unassigned Image
                String imagePath = null;
                int index = 0;
                while (imagePath == null && index < images.size()) {
                    Image currentImage = images.get(index);
                    if (currentImage.currentUser() == 0) {
                        imagePath = currentImage.path();
                    }
                    ++index;
                }
                if (imagePath == null) {
                    result = null;
                }
                else {
                    result = new GetSampleImage_Result(imagePath);
                }
            }
        } catch (DatabaseException | GetFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "getSampleImage request failed.", ex);
        }
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
    public static DownloadBatch_Result downloadBatch(Database database, DownloadBatch_Param params) {

        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.downloadBatch()");
        DownloadBatch_Result result;
        try {
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            Image hasImage = new Image();
            if (vResult == null || !vResult.validated()) {
                result = null;
            }
            else {
                
                // Check if this user already has an Image assigned
                hasImage.setCurrentUser(vResult.userId());
                ArrayList<Image> hasAssigned = (ArrayList) database.get(hasImage);
                Image image = null; // Image to be assigned to the User
                if (hasAssigned.size() > 0) { // If they do
                    if (hasAssigned.size() > 1) { // If they have more than one.
                        int userId = vResult.userId();
                        throw new DatabaseException(String.format( // There is a problem
                                "User should only have one Image assigned. User ID %d has %d.",
                                vResult.userId(), hasAssigned.size()));
                    }
                    else { // If they only have one
                        hasImage = hasAssigned.get(0);
//                        if (hasImage.projectId() == params.projectId()) { // And it is for this Project
//                            image = hasImage; // Return that Image
//                        }
//                        else { // Otherwise fail
                            throw new DatabaseException(String.format(
                                    "User ID %d already has an image %d assigned from project %d.",
                                    vResult.userId(), hasImage.imageId(), hasImage.projectId()));
//                        }
                    }
                }
                if (image == null) {

                    // Get all the images from this Project
                    Image tImage = new Image();
                    tImage.setProjectId(params.projectId());
                    ArrayList<Image> images = (ArrayList) database.get(tImage);
                    int index = 0;
                    while (image == null && index < images.size()) {
                        // Find an unassigned and uncompleted image
                        Image currentImage = images.get(index);
                        if (currentImage.currentUser() == 0) {
                            image = currentImage;
                        }
                        ++index;
                    }
                }
                // If there are none, fail
                if (image == null) {
                    result = null;
                }
                else { // Otherwise
                    
                    // Get the project
                    Project project = new Project();
                    project.setProjectId(params.projectId());
                    ArrayList<Project> projects = (ArrayList) database.get(project);

                    // projectId is unique, so should only return one project.
                    assert projects.size() == 1;
                    int firstProjectIndex = 0;

                    image.setCurrentUser(vResult.userId());
                    // Set the current user in the image to this user
                    database.update(image);

                    // Get all Fields from this project
                    Field tField = new Field();
                    tField.setProjectId(params.projectId());
                    ArrayList<Field> fields = (ArrayList) database.get(tField);
                    result = new DownloadBatch_Result(
                            projects.get(firstProjectIndex), image, fields);
                }
            }
        } catch (DatabaseException | GetFailedException | UpdateFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "downloadBatch request failed.", ex);
        }
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
    public static SubmitBatch_Result submitBatch(Database database, SubmitBatch_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.submitBatch()");
        SubmitBatch_Result result;
        try { // validate user
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // user validated
                
                // Get the Image
                Image tImage = new Image();
                tImage.setImageId(params.batchId());
                ArrayList<Image> images = (ArrayList) database.get(tImage);
                
                // Should only return one result, since image IDs are unique
                assert images.size() == 1;
                int firstImageIndex = 0;
                Image thisImage = images.get(firstImageIndex);
                if (thisImage.currentUser() != vResult.userId()) { // If the batch ID was wrong
                    throw new APIException(String.format(
                            "This batch does not belong to user ID %d", vResult.userId()));
                }
                
                // Get all Fields from this Image's project
                Field tField = new Field();
                tField.setProjectId(thisImage.projectId());
                ArrayList<Field> fields = (ArrayList) database.get(tField);
                
                // Get the Project for the number of records
                Project tProject = new Project();
                tProject.setProjectId(thisImage.projectId());
                ArrayList<Project> projects = (ArrayList) database.get(tProject);

                // Should only get one Project
                assert projects.size() == 1;
                int firstProjectIndex = 0;
                Project thisProject = projects.get(firstProjectIndex);
                
                // Split the records on ';' character
                String[] records = params.records().split(";");
                if (records.length != thisProject.recordCount()) {
                    throw new APIException(String.format(
                            "Invalid number of records given. %d given, %d required.",
                            records.length, thisProject.recordCount()));
                }
                for (int rowNumber = 0; rowNumber < records.length; ++rowNumber) {

                    // Split the record values on ',' character
                    String[] values = records[rowNumber].split(",", -1);
                    if (values.length != fields.size()) {
                        throw new APIException(String.format(
                                "Invalid number of values given. %d given, %d required.",
                                values.length, fields.size()));
                    }
                    for (int column = 1; column <= values.length; ++column) {
                        Field currentField = fields.get(column - 1);

                        // Insert the value for this record into the database
                        database.insert(new Record(params.batchId(), currentField.fieldId(), rowNumber, values[column - 1]));
                    }
                }

                // Update Current user for the image to -1 (complete Image)
                thisImage.setCurrentUser(Images.IMAGE_COMPLETED);
                database.update(thisImage);

                // Update the number of indexed records for this user
                User user = new User();
                user.setUserId(vResult.userId());
                user.setIndexedRecords(vResult.recordsIndexed() + records.length);
                database.update(user);
                result = new SubmitBatch_Result(true);
            }
        }
        catch (DatabaseException
               | InsertFailedException
               | GetFailedException
               | APIException
               | UpdateFailedException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "submitBatch request failed.", ex);
        }
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
    public static GetFields_Result getFields(Database database, GetFields_Param params) throws APIException {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        GetFields_Result result;
        try { // Validate User
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // User exists
                boolean useAll = false;
                int projectId;

                // If the project ID is a String (It is an Object in params)
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

                // Get Fields with the right project ID (or all Fields if
                // params.projectID is an empty String)
                Field tField = new Field();
                tField.setProjectId(projectId);
                ArrayList<Field> fields = (ArrayList) database.get(tField);

                // Get IDs and Titles out of the Fields
                ArrayList<Integer> fieldIds = new ArrayList<>();
                ArrayList<String> fieldTitles = new ArrayList<>();
                for (Field field : fields) {
                    fieldIds.add(new Integer(field.fieldId()));
                    fieldTitles.add(field.title());
                }
                if (fieldIds.size() != fieldTitles.size()) {
                    result = null;
                }
                else {
                    result = new GetFields_Result(projectId, fieldIds, fieldTitles);
                }
            }
        }
        catch (DatabaseException | GetFailedException | GetFields_Result.GetFields_ResultException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "getFields request failed.", ex);
        }
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
    public static Search_Result search(Database database, Search_Param params) {
        
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        Search_Result result;
        try {
            ValidateUser_Result vResult = validateUser(database, new ValidateUser_Param(params.username(), params.password()));
            if (vResult == null || !vResult.validated()) { // not validated
                result = null;
            }
            else { // User exists
                
                // Extract field IDs and convert to ArrayList<String>
                String[] fieldIdArray = params.fields().split(",", -1);
                ArrayList<String> fieldIdsAsStrings = new ArrayList<>(Arrays.asList(fieldIdArray));
                
                // Extract values and convert to ArrayList<String>
                String[] valueArray = params.values().split(",", -1);
                ArrayList<String> values = new ArrayList<>(Arrays.asList(valueArray));
                
                // Search database for the Records with values in values and
                // field IDs in fieldIdsAsStrings
                ArrayList<Record> records = (ArrayList) database.search(fieldIdsAsStrings, values);

                // Extract info for Search_Result
                Image currentImage; // current Image for the iteration
                ArrayList<Image> tImages; // result of get query inside for loop
                ArrayList<Integer> imageIds = new ArrayList<>(); // for Search_Result
                ArrayList<String> imagePaths = new ArrayList<>(); // for Search_Result
                ArrayList<Integer> rowNumbers = new ArrayList<>(); // for Search_Result
                ArrayList<Integer> fieldIds = new ArrayList<>(); // for Search_Result
                HashMap<Integer, Image> images = new HashMap<>(); // To limit # of database queries
                Image tImage = new Image(); // base Image object for running get queries
                tImage.setCurrentUser(Images.IMAGE_COMPLETED); // Only completed images are searchable
                for (Record record : records) {

                    // If we haven't already grabbed this Image from the database
                    if (!images.containsKey(record.imageId())) {
                        // Get this Image out of the database
                        tImage.setImageId(record.imageId());
                        tImages = (ArrayList) database.get(tImage);
                        
                        // Should only return one Image, because image IDs are unique
                        assert tImages.size() == 1;
                        int firstImageIndex = 0;
                        currentImage = tImages.get(firstImageIndex);
                        
                        // Add this image to the map
                        images.put(new Integer(record.imageId()), currentImage);
                    }
                    else { // If we have grabbed it already
                        // Take it from the HashMap
                        currentImage = images.get(record.imageId());
                    }
                    
                    // Grab info from the Image and Record for the Search_Result
                    imageIds.add(currentImage.imageId());
                    imagePaths.add(currentImage.path());
                    rowNumbers.add(record.rowNumber());
                    fieldIds.add(new Integer(record.fieldId()));
                }
                result = new Search_Result(imageIds, imagePaths, rowNumbers, fieldIds);
            }
        }
        catch (DatabaseException | GetFailedException | Search_Result.Search_ResultException ex) {
            result = null;
            Logger.getLogger(API.class.getName()).log(Level.WARNING, "search request failed.", ex);
        }
        Logger.getLogger(API.class.getName()).log(Level.FINE, "Entering API.getFields()");
        return result;
        
    }    
    
}
