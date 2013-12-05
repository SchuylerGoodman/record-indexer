/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.communication.AbstractCommunicationSubscriber;
import client.gui.model.save.AbstractSaveSubscriber;
import client.gui.model.save.settings.ImageSettings;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import shared.communication.DownloadBatch_Result;

/**
 *
 * @author schuyler
 */
public class ImageModel {
    
    private ArrayList<ImageSubscriber> subscribers;
    
    private BufferedImage originalImage;
    private Point2D center;
    private boolean highlights;
    private int zoomLevel;
    private int zoomAmount;
    private double aspectRatio;
    
    public ImageModel() {
        subscribers = new ArrayList<>();
    }
    
    /**
     * Factory method for creating ImageLinkers that link to this ImageModel.
     * 
     * @return a new ImageLinker that allows components or classes to link to
     * this ImageModel.
     */
    public ImageLinker createImageLinker() {
        return new ImageLinker(this);
    }
    
    /**
     * Add a subscriber to the list of subscribers.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    protected void subscribe(ImageSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Change the zoom level and zoom amount and notify all subscribers.
     * 
     * @param levels the change in zoom level. Positive means zoom in and
     * negative means zoom out.
     */
    protected void zoom(int levels) {
        update();
    }
    
    /**
     * Invert the image and notify all subscribers.
     */
    protected void invert() {
        update();
    }
    
    /**
     * Toggle highlights and notify all subscribers.
     */
    protected void toggleHighlights() {
        highlights = !highlights;
        update();
    }
    
    protected void moveWindow(Point2D newCenter) {
        center = newCenter;
        update();
    }

    /**
     * Notify all subscribers of a change in the image settings.
     */
    private void update() {

    }
    
    private AbstractCommunicationSubscriber communicationSubscriber
                                    = new AbstractCommunicationSubscriber() {

        @Override
        public void setBatch(DownloadBatch_Result result) {
            // Set Image and stuff, then update.
        }

    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public ImageSettings saveImageSettings() {
            return null;
        }

        @Override
        public void setImageSettings(ImageSettings settings) {
            // update, then run update on subscribers.
        }

    };
    
}
