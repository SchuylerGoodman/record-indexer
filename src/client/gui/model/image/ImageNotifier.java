/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import java.awt.geom.Point2D;

/**
 *
 * @author schuyler
 */
public class ImageNotifier {
    
    private ImageModel model;
    
    public ImageNotifier(ImageModel model) {
        this.model = model;
    }
    
    /**
     * Notify the ImageModel to move the center of the visible window.
     * 
     * @param center the new center of the window, in world coordinates.
     */
    public void moveWindow(Point2D newCenter) {
        
    }
    
    /**
     * Notify the ImageModel that a zoom has occurred.
     * 
     * @param levels the change in zoom levels. Positive means zoom in, negative
     * means zoom out.
     */
    public void zoom(int levels) {
        
    }
    
    /**
     * Notify the ImageModel to invert the image.
     */
    public void invert() {
        
    }
    
    /**
     * Notify the ImageModel to toggle highlights on the cells.
     */
    public void toggleHighlights() {
        
    }

}
