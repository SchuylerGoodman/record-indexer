/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.communication.AbstractCommunicationSubscriber;
import client.gui.model.save.settings.*;
import java.nio.file.Path;
import java.util.ArrayList;
import shared.communication.ValidateUser_Result;

/**
 *
 * @author schuyler
 */
public class SaveModel {
    
    private ArrayList<SaveSubscriber> subscribers;
    
    private String username;
    private String password;
    
    public SaveModel() {
        subscribers = new ArrayList<>();
    }
    
    /**
     * Factory method for creating SaveLinkers linked to this CellModel.
     * 
     * @return a new SaveLinker that allows components or classes to link to
     * this CellModel.
     */
    public SaveLinker createSaveLinker() {
        return new SaveLinker(this);
    }
    
    /**
     * Add a subscriber to the subscriber list.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    protected void subscribe(SaveSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Generically saves all image settings, window settings, and the indexer
     * state for the current user.
     */
    protected void save() {
        
    }
    
    /**
     * Generically loads all image settings, window settings, and the indexer
     * state for the current user, if they exist.
     */
    private void load() {
        
    }
    
    /**
     * Save image settings to file specific to the current user.
     * 
     * @param settings the ImageSettings model for the image settings to save.
     */
    private void saveImageSettings(ImageSettings settings) {
        
    }
    
    /**
     * Save indexer state to file specific to the current user.
     * 
     * @param state the IndexerState model for the indexer state to save.
     */
    private void saveIndexerState(IndexerState state) {
        
    }
    
    /**
     * Save window settings to file specific to the current user.
     * 
     * @param settings the WindowSettings model for the window settings to save.
     */
    private void saveWindowSettings(WindowSettings settings) {
        
    }
    
    private void saveToFile(Path pathToFile, DataModel data) {
        
    }

    /**
     * Load image settings from file and notify all subscribers.
     * 
     * @return the ImageSettings model with all saved image settings for the
     * current user.
     */
    private ImageSettings loadImageSettings() {
        return null;
    }
    
    /**
     * Load indexer state from file and notify all subscribers.
     * 
     * @return the IndexerState model with all saved indexer state information
     * for the current user.
     */
    private IndexerState loadIndexerState() {
        return null;
    }
    
    /**
     * Load window settings from file and notify all subscribers.
     * 
     * @return the WindowSettings model with all the saved window settings for
     * the current user.
     */
    private WindowSettings loadWindowSettings() {
        return null;
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber = 
                                    new AbstractCommunicationSubscriber() {
        
        @Override
        public void login(ValidateUser_Result result) {
            // TODO Load all saved data from memory for this user, if it exists.
        }
        
    };
    
}
