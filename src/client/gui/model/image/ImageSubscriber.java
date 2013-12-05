/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.save.settings.ImageSettings;

/**
 *
 * @author schuyler
 */
public interface ImageSubscriber {
    
    /**
     * Process an update to the image settings.
     * 
     * @param settings the ImageSettings model for the new image settings.
     */
    public void update(ImageSettings settings);
    
}
