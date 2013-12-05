/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import client.Communicator;
import client.gui.model.image.ImageNotifier;
import client.gui.model.record.RecordNotifier;
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
    
    public CommunicationModel(ImageNotifier imageNotifier,
                              RecordNotifier recordNotifier) {
        
        this.imageNotifier = imageNotifier;
        this.recordNotifier = recordNotifier;
        subscribers = new ArrayList<>();
        
    }
    
    /**
     * Factory method for creating CommunicationLinkers linked to this
     * CommunicationModel.
     * 
     * @return a new CommunicationLinker that allows components and classes to
     * link to this CommunicationModel.
     */
    public CommunicationLinker createCommunicationLinker() {
        return new CommunicationLinker(this);
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
    }
    
    /**
     * Asks the server for all projects available to the user.
     * 
     * @return the GetProjects_Result result from the server.
     */
    protected GetProjects_Result getProjects() {
        return null;
    }
    
    /**
     * Asks the server for a sample image.
     * 
     * @param projectId the Id of the desired project.
     * @return the GetSampleImage_Result result from the server.
     */
    protected GetSampleImage_Result getSampleImage(int projectId) {
        return null;
    }
    
    /**
     * Asks the server for a new batch.
     * 
     * Sends a response through CommunicationSubscriber.setBatch().
     * 
     * @param projectId the Id of the desired project.
     */
    protected void downloadBatch(int projectId) {
    }
    
    /**
     * Submits the current batch to the server.
     */
    protected void submitBatch() {
    }
    
    /**
     * Asks the server for the fields associated with the current project.
     * 
     * Sends a result through CommunicationSubscriber.setFields().
     */
    protected void getFields() {
    }

    /**
     * Tells all subscribers to process a login.
     * 
     * @param result the ValidateUser_Result result from the server.
     */
    private void login(ValidateUser_Result result) {
    }

    /**
     * Tells all subscribers to process the downloaded batch.
     * 
     * @param result the DownloadBatch_Result result from the server.
     */
    private void setBatches(DownloadBatch_Result result) {
    }

    /**
     * Tells all subscribers to process the project fields.
     * 
     * @param result the GetFields_Result result from the server.
     */
    private void setFields(GetFields_Result result) {
    }

}
