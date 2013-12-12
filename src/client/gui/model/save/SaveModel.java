/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.communication.*;
import client.gui.model.save.settings.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.communication.ValidateUser_Result;

/**
 *
 * @author schuyler
 */
public class SaveModel {
    
    private ArrayList<SaveSubscriber> subscribers;
    
    private CommunicationNotifier communicationNotifier;
    
    private String username;
    
    public SaveModel() {
        subscribers = new ArrayList<>();
    }
    
    protected void link(CommunicationLinker communicationLinker) {
        
        communicationNotifier = communicationLinker.getCommunicationNotifier();
        communicationLinker.subscribe(communicationSubscriber);
        
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
        
        ImageSettings imageSettings = null;
        IndexerState indexerState = null;
        WindowSettings windowSettings = null;
        for (SaveSubscriber s : subscribers) {
            if (imageSettings == null) {
                imageSettings = s.saveImageSettings();
            }
            if (indexerState == null) {
                indexerState = s.saveIndexerState();
            }
            else {
                indexerState.combine(s.saveIndexerState());
            }
            if (windowSettings == null) {
                windowSettings = s.saveWindowSettings();
            }
            else {
                windowSettings.combine(s.saveWindowSettings());
            }
        }
        if (imageSettings == null) {
            indexerState = null;
        }
        this.saveImageSettings(imageSettings);
        this.saveIndexerState(indexerState);
        this.saveWindowSettings(windowSettings);
        
    }
    
    /**
     * Generically loads all image settings, window settings, and the indexer
     * state for the current user, if they exist.
     */
    private void load() {
        
        ImageSettings imageSettings = loadImageSettings();
        IndexerState indexerState = loadIndexerState();
        WindowSettings windowSettings = loadWindowSettings();
        
        for (SaveSubscriber s : subscribers) {
            if (windowSettings != null) {
                s.setWindowSettings(windowSettings);
            }
            if (imageSettings != null) {
                s.setImageSettings(imageSettings);
            }
            if (indexerState != null) {
                s.setIndexerState(indexerState);
            }
        }
        
    }
    
    /**
     * Save image settings to file specific to the current user.
     * 
     * @param settings the ImageSettings model for the image settings to save.
     */
    private void saveImageSettings(ImageSettings settings) {
        
        File file = new File("data" + File.separator + "image_settings_" + username + ".dat");
        saveToFile(file, settings);
        
    }
    
    /**
     * Save indexer state to file specific to the current user.
     * 
     * @param state the IndexerState model for the indexer state to save.
     */
    private void saveIndexerState(IndexerState state) {

        File file = new File("data" + File.separator + "indexer_state_" + username + ".dat");
        saveToFile(file, state);
        
    }
    
    /**
     * Save window settings to file specific to the current user.
     * 
     * @param settings the WindowSettings model for the window settings to save.
     */
    private void saveWindowSettings(WindowSettings settings) {
        
        File file = new File("data" + File.separator + "window_settings_" + username + ".dat");
        saveToFile(file, settings);
        
    }
    
    /**
     * Serializes and saves data to a file.
     * 
     * @param pathToFile the path to the saved file.
     * @param data the data to be saved.
     */
    private boolean saveToFile(File pathToFile, DataModel data) {

        boolean success = false;
        if (pathToFile != null && data != null) {
            ObjectOutputStream oos = null;
            try {
                if (!pathToFile.exists()) {
                    try {
                        pathToFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(SaveModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    if (!data.hasData()) {
                        pathToFile.delete();
                    }
                }
                oos = new ObjectOutputStream(new FileOutputStream(pathToFile));
                oos.writeObject(data);
                success = true;
            } catch (Exception ex) {
                Logger.getLogger(SaveModel.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (oos != null) oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SaveModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if (pathToFile != null && data == null) {
            if (pathToFile.exists()) {
                pathToFile.delete();
            }
        }
        return success;
        
    }
    
    /**
     * Loads and deserializes data from a file.
     * 
     * @param pathToFile the path to the file.
     * @return a DataModel model for the saved data.
     */
    private DataModel loadFromFile(File pathToFile) {
        
        DataModel dm = null;
        if (pathToFile != null) {
            ObjectInputStream ois = null;
            if (pathToFile.exists()) {
                try {
                    ois = new ObjectInputStream(new FileInputStream(pathToFile));
                    dm = (DataModel)ois.readObject();
                } catch (Exception ex) {
                    Logger.getLogger(SaveModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {
                    try {
                        if (ois != null) ois.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SaveModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return dm;
        
    }

    /**
     * Load image settings from file and notify all subscribers.
     * 
     * @return the ImageSettings model with all saved image settings for the
     * current user.
     */
    private ImageSettings loadImageSettings() {
        
        File file = new File("data" + File.separator + "image_settings_" + username + ".dat");
        return (ImageSettings)loadFromFile(file);
        
    }
    
    /**
     * Load indexer state from file and notify all subscribers.
     * 
     * @return the IndexerState model with all saved indexer state information
     * for the current user.
     */
    private IndexerState loadIndexerState() {
        
        File file = new File("data" + File.separator + "indexer_state_" + username + ".dat");
        return (IndexerState)loadFromFile(file);
        
    }
    
    /**
     * Load window settings from file and notify all subscribers.
     * 
     * @return the WindowSettings model with all the saved window settings for
     * the current user.
     */
    private WindowSettings loadWindowSettings() {
        
        File file = new File("data" + File.separator + "window_settings_" + username + ".dat");
        return (WindowSettings)loadFromFile(file);
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber = 
                                    new AbstractCommunicationSubscriber() {
        
        @Override
        public void login(ValidateUser_Result result) {
            if (result != null && result.validated()) {
                username = communicationNotifier.getUsername();
                load();
            }
        }
        
    };
    
}
