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
public interface CommunicationSubscriber {
    
    /**
     * Process the user login.
     * 
     * @param result 
     */
    public void login(ValidateUser_Result result);
    
    /**
     * Process the downloaded batch.
     * 
     * @param result 
     */
    public void setBatch(DownloadBatch_Result result);
    
    /**
     * Process the project fields.
     * 
     * @param result 
     */
    public void setFields(GetFields_Result result);
    
}
