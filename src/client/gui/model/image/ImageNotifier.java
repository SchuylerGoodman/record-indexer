/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import java.awt.Dimension;
import java.awt.Point;
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
    public void moveWindow(Point2D.Double newTopLeftRatio) {
        model.moveWindow(newTopLeftRatio);
    }
    
    /**
     * Notify the ImageModel that a zoom has occurred.
     * 
     * @param levels the change in zoom levels. Positive means zoom in, negative
     * means zoom out.
     */
    public void zoom(int levels) {
        model.zoom(levels);
    }
    
    /**
     * Notify the ImageModel to invert the image.
     */
    public void invert() {
        model.invert();
    }
    
    /**
     * Notify the ImageModel to toggle highlights on the cells.
     */
    public void toggleHighlights() {
        model.toggleHighlights();
    }
    
    /**
     * Notify the ImageModel to change the view dimension.
     * 
     * @param newViewDimension the new dimension for the view.
     */
    public void setView(Point2D.Double newViewRatio,
                        Point2D.Double newViewTopLeftRatio) {
        model.setView(newViewRatio, newViewTopLeftRatio);
    }
    
    /**
     * Notify the ImageModel to reset.
     */
    public void empty() {
        model.empty();
    }

}
