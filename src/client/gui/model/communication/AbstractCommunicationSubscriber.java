/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.communication;

import shared.communication.DownloadBatch_Result;
import shared.communication.GetFields_Result;
import shared.communication.ValidateUser_Result;

/**
 * Abstract class to simplify implementing a CommunicationSubscriber
 *
 * @author schuyler
 */
public abstract class AbstractCommunicationSubscriber implements CommunicationSubscriber {

    @Override
    public void login(ValidateUser_Result result) {
    }

    @Override
    public void setBatch(DownloadBatch_Result result) {
    }

    @Override
    public void setFields(GetFields_Result result) {
    }
    
}
