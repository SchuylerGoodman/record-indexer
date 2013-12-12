/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.save.settings.ImageSettings;
import java.awt.image.BufferedImage;

/**
 *
 * @author schuyler
 */
public interface ImageSubscriber {
    
    /**
     * Initialize all subscribers with initial settings.
     * 
     * @param settings Initial known image settings.
     */
    public void init(ImageSettings settings);
    
    /**
     * Process an update to the image settings.
     * 
     * @param settings the ImageSettings model for the new image settings.
     */
    public void update(ImageSettings settings);
    
    /**
     * Process the inversion of an image.
     * 
     * @param invertedImage the inverted image.
     */
    public void invert(BufferedImage invertedImage);

}
