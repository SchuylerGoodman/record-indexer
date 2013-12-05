/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.save.settings.*;

/**
 * Interface for a subscriber to the SaveModel.
 * 
 * Use if a component or class needs notifications from the Save model to save
 * or load saved information.
 *
 * @author schuyler
 */
public interface SaveSubscriber {
    
    // TODO - these should return the saved information in some type of object.
    // Not sure how just yet.
    
    /**
     * Process the request to save all image settings.
     * 
     * @return the ImageSettings model for the image settings to save.
     */
    public ImageSettings saveImageSettings();
    
    /**
     * Process the request to save the indexer state.
     * 
     * @return the IndexerState model for the indexer state to save.
     */
    public IndexerState saveIndexerState();
    
    /**
     * Process the request to save all window settings.
     * 
     * @return the WindowSettings model for the window settings to save.
     */
    public WindowSettings saveWindowSettings();
    
    /**
     * Process the saved image settings and set them.
     * 
     * @param settings the ImageSettings model for the image settings to set.
     */
    public void setImageSettings(ImageSettings settings);
    
    /**
     * Process the saved indexer state and set it.
     * 
     * @param indexerState the IndexerState model for the indexer state to set.
     */
    public void setIndexerState(IndexerState state);
    
    /**
     * Process the saved window state and set it.
     * 
     * @param windowSettings the WindowSettings model for the window settings to
     * set.
     */
    public void setWindowSettings(WindowSettings settings);
    
}
