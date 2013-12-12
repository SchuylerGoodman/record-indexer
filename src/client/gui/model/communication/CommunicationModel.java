/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import client.Communicator;
import client.gui.model.image.ImageLinker;
import client.gui.model.image.ImageNotifier;
import client.gui.model.record.RecordLinker;
import client.gui.model.record.RecordNotifier;
import client.gui.model.save.AbstractSaveSubscriber;
import client.gui.model.save.SaveLinker;
import client.gui.model.save.settings.IndexerState;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class CommunicationModel {

    private Communicator communicator;
    
    private ArrayList<CommunicationSubscriber> subscribers;
    
    private ImageNotifier imageNotifier;
    private RecordNotifier recordNotifier;
    
    private String username;
    private String password;
    private int projectId;
    private int imageId;
    private int recordHeight;
    private int firstYCoordinate;
    
    public CommunicationModel(Communicator communicator) {
        
        this.communicator = communicator;
        subscribers = new ArrayList<>();
        
    }
    
    protected void link(ImageLinker imageLinker, RecordLinker recordLinker,
                        SaveLinker saveLinker) {
        
        this.imageNotifier = imageLinker.getImageNotifier();
        this.recordNotifier = recordLinker.getRecordNotifier();
        saveLinker.subscribe(saveSubscriber);
        
    }

    /**
     * Adds a subscriber to the subscriber list.
     * 
     * @param newSubscriber the subscriber to receive notifications.
     */
    protected void subscribe(CommunicationSubscriber newSubscriber) {
        subscribers.add(newSubscriber);
    }
    
    /**
     * Asks the server to validate the given username and password.
     * 
     * Sends a response through CommunicationSubscriber.login().
     * 
     * @param username the username to validate.
     * @param password the password to validate.
     */
    protected void validateUser(String username, String password) {
        
        ValidateUser_Param param = new ValidateUser_Param(username, password);
        ValidateUser_Result result = communicator.validateUser(param);
        
        this.username = username;
        this.password = password;
        this.login(result);
        
    }
    
    /**
     * Asks the server for all projects available to the user.
     * 
     * @return the GetProjects_Result result from the server.
     */
    protected GetProjects_Result getProjects() {
        
        GetProjects_Param param = new GetProjects_Param(username, password);
        GetProjects_Result result = communicator.getProjects(param);
        
        return result;
        
    }
    
    /**
     * Asks the server for a sample image.
     * 
     * @param projectId the Id of the desired project.
     * @return the GetSampleImage_Result result from the server.
     */
    protected GetSampleImage_Result getSampleImage(int projectId) {
        
        GetSampleImage_Param param = new GetSampleImage_Param(username, password, projectId);
        GetSampleImage_Result result = communicator.getSampleImage(param);
        
        return result;
        
    }
    
    /**
     * Asks the server for a new batch.
     * 
     * Sends a response through CommunicationSubscriber.setBatch().
     * 
     * @param projectId the Id of the desired project.
     */
    protected void downloadBatch(int projectId) {
        
        DownloadBatch_Param param = new DownloadBatch_Param(username, password, projectId);
        DownloadBatch_Result result = communicator.downloadBatch(param);
        
        if (result != null) {
            this.projectId = result.projectId();
            this.imageId = result.imageId();
            this.recordHeight = result.recordHeight();
            this.firstYCoordinate = result.firstYCoord();
            this.setBatches(result);
        }
        
    }
    
    protected void empty() {
        this.blankAll();
    }
    
    /**
     * Submits the current batch to the server.
     */
    protected void submitBatch() {
        
        SubmitBatch_Param param = new SubmitBatch_Param(username, password,
                                                        imageId,
                                    recordNotifier.getRecordsForSubmission());
        
        SubmitBatch_Result result = communicator.submitBatch(param);
        
        if (result != null && result.success()) {
            this.projectId = 0;
            this.imageId = 0;
            this.blankAll();
        }
                                                            
    }
    
    /**
     * Asks the server for the fields associated with the current project.
     * 
     * Sends a result through CommunicationSubscriber.setFields().
     */
    protected void getFields() {
        
        GetFields_Param param = new GetFields_Param(username, password, projectId);
        GetFields_Result result = communicator.getFields(param);
        
        if (result != null) {
            this.setFields(result);
        }
        
    }
    
    /**
     * Downloads an image from the server.
     * 
     * @param path the path to the image on the server.
     * @return the requested image as a BufferedImage.
     */
    protected BufferedImage downloadImage(String path) {
        
        return communicator.downloadImage(path);
        
    }
    
    protected String downloadHtml(String path) {
        return communicator.downloadHtml(path);
    }
    
    protected URL pathToURL(String path) {
        return communicator.pathToURL(path);
    }
    
    protected String getUsername() {
        return username;
    }

    /**
     * Tells all subscribers to process a login.
     * 
     * @param result the ValidateUser_Result result from the server.
     */
    private void login(ValidateUser_Result result) {
        
        int e = 1;
        for (CommunicationSubscriber s : subscribers) {
            s.login(result);
        }
        int i = 1;
    }

    /**
     * Tells all subscribers to process the downloaded batch.
     * 
     * @param result the DownloadBatch_Result result from the server. If null,
     * clear all information to present a blank screen.
     */
    private void setBatches(DownloadBatch_Result result) {
        
        for (CommunicationSubscriber s : subscribers) {
            s.setBatch(result);
        }
        
    }

    /**
     * Tells all subscribers to process the project fields.
     * 
     * @param result the GetFields_Result result from the server.
     */
    private void setFields(GetFields_Result result) {
        
        for (CommunicationSubscriber s : subscribers) {
            s.setFields(result);
        }
        
    }
    
    private void blankAll() {
        
        username = null;
        password = null;
        projectId = 0;
        imageId = 0;
        recordHeight = 0;
        firstYCoordinate = 0;
        this.setBatches(new DownloadBatch_Result());
        
    }
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public IndexerState saveIndexerState() {
            return new IndexerState(projectId, imageId, recordHeight, firstYCoordinate);
        }

        @Override
        public void setIndexerState(IndexerState state) {
            projectId = state.projectId();
            imageId = state.imageId();
            recordHeight = state.recordHeight();
            firstYCoordinate = state.firstYCoordinate();
        }
        
    };
    
}
