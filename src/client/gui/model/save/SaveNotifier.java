/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.save.settings.*;

/**
 *
 * @author schuyler
 */
public class SaveNotifier {
    
    private SaveModel model;
    
    public SaveNotifier(SaveModel model) {
        this.model = model;
    }
    
    /**
     * Tells the saveModel to save all indexer GUI settings.
     */
    public void save() {
        model.save();
    }
    
    /**
     * Tells the SaveModel to save all image settings to a file.
     * 
     * @param settings the ImageSettings model for the image settings to save.
     */
    public void saveImageSettings(ImageSettings settings) {
        
    }

    /**
     * Tells the SaveModel to save the indexer state to a file.
     * 
     * @param state the IndexerState model for the indexer state to save.
     */
    public void saveIndexerState(IndexerState state) {
        
    }
    
    /**
     * Tells the SaveModel to save the window settings to a file.
     * 
     * @param settings the WindowSettings model for the window settings to save.
     */
    public void saveWindowSettings(WindowSettings settings) {
        
    }
    
}
