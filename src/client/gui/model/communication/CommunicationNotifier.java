/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import java.awt.image.BufferedImage;
import java.net.URL;
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
        model.validateUser(username, password);
    }
    
    /**
     * Asks the CommunicationModel for all projects available to this user.
     * 
     * @return the GetProjects_Result result of the request.
     */
    public GetProjects_Result getProjects() {
        return model.getProjects();
    }

    /**
     * Asks the CommunicationModel for a sample image from a project.
     * 
     * @param projectId Id of the desired project
     * @return the GetSampleImage_Result result of the request.
     */
    public GetSampleImage_Result getSampleImage(int projectId) {
        return model.getSampleImage(projectId);
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
        model.downloadBatch(projectId);
    }

    /**
     * Asks the CommunicationModel to submit a batch to the server.
     */
    public void submitBatch() {
        model.submitBatch();
    }

    /**
     * Asks the CommunicationModel to get the Fields for the current project
     * from the server.
     * 
     * After hearing from the server, the CommunicationModel will send a
     * response to CommunicationSubscriber.setFields(GetFields_Result result).
     */
    public void getFields() {
        model.getFields();
    }
    
    public BufferedImage downloadImage(String path) {
        return model.downloadImage(path);
    }
    
    public String downloadHtml(String path) {
        return model.downloadHtml(path);
    }
    
    /**
     * Asks the CommunicationModel to reset.
     */
    public void empty() {
        model.empty();
    }
    
    public URL pathToURL(String path) {
        return model.pathToURL(path);
    }
    
    public String getUsername() {
        return model.getUsername();
    }
    
}
