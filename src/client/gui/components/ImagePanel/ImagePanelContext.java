/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components.ImagePanel;

import java.awt.image.BufferedImage;

/**
 *
 * @author schuyler
 */
public interface ImagePanelContext {
    
    /**
     * Downloads the image from the server.
     * 
     * @return BufferedImage object with the raw image data
     */
    public BufferedImage downloadImage();
    
    /**
     * Tells the rest of the GUI to zoom in.
     * 
     * @param zoomLevels How many levels to zoom in.
     */
    public void zoomIn(int zoomLevels);
    
    /**
     * Tells the rest of the GUI to zoom out.
     * 
     * @param zoomLevels How many levels to zoom in.
     */
    public void zoomOut(int zoomLevels);
    
    /**
     * Tells the rest of the GUI invert the colors in the image.
     * 
     * @param zoomLevels How many levels to zoom in.
     */
    public void invert();
    
    public void toggleHighlights();
    
}
