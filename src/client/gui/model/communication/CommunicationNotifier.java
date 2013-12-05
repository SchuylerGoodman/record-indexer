/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class CommunicationNotifier {
    
    private CommunicationModel model;
    
    public CommunicationNotifier(CommunicationModel model) {
        this.model = model;
    }

    /**
     * Asks the CommunicationModel to validate the given username and password.
     * 
     * After hearing from the server, the CommunicationModel will send a
     * response to CommunicationSubscriber.login().
     * 
     * @param username Username to validate
     * @param password Password to validate
     */
    public void validateUser(String username, String password) {
    }
    
    /**
     * Asks the CommunicationModel for all projects available to this user.
     * 
     * @return the GetProjects_Result result of the request.
     */
    public GetProjects_Result getProjects() {
        return null;
    }

    /**
     * Asks the CommunicationModel for a sample image from a project.
     * 
     * @param projectId Id of the desired project
     * @return the GetSampleImage_Result result of the request.
     */
    public GetSampleImage_Result getSampleImage(int projectId) {
        return null;
    }
    
    /**
     * Asks the CommunicationModel to download a batch for a project.
     * 
     * After hearing from the server, the CommunicationModel will send a
     * response to CommunicationSubscriber.setBatch(DownloadBatch_Result result).
     * 
     * @param projectId Id of the desired project.
     */
    public void downloadBatch(int projectId) {
    }
    
    /**
     * Asks the CommunicationModel to submit a batch to the server.
     */
    public void submitBatch() {
    }

    /**
     * Asks the CommunicationModel to get the Fields for the current project
     * from the server.
     * 
     * After hearing from the server, the CommunicationModel will send a
     * response to CommunicationSubscriber.setFields(GetFields_Result result).
     */
    public void getFields() {
    }
    
}
