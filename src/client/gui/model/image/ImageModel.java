/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.communication.*;
import client.gui.model.save.*;
import client.gui.model.save.settings.ImageSettings;
import client.gui.util.GuiImageManipulator;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import shared.communication.DownloadBatch_Result;

/**
 *
 * @author schuyler
 */
public class ImageModel {

    public static final double ZOOM_MULTIPLIER = -.01;
    
    private CommunicationNotifier communicationNotifier;
    
    private ArrayList<ImageSubscriber> subscribers;
    
    private String pathToImage;
    private BufferedImage originalImage;
    // Dimension and center of the image, in world coordinates.
    private Dimension imageDimension;
//    private Point imageTopLeft; // always (0,0)
    private boolean highlights;
    private boolean inverted;
    private int zoomLevel;
    private double zoomAmount;
    private double aspectRatio;
    
    // Ratio of the view to the image.
    private Point2D.Double viewRatio;
    // Ratio of the top left coordinates of the view to the dimensions of the image.
    private Point2D.Double viewTopLeftRatio;
    
    private ImageSettings settings;
    
    public ImageModel() {
        this(null, new Dimension(0, 0), 0, 0, true, false, 0);
    }
    
    public ImageModel(BufferedImage originalImage, Dimension originalDimension,
                      int zoomLevel, int zoomAmount, boolean highlights,
                      boolean inverted, double aspect) {
        
        subscribers = new ArrayList<>();      
        settings = null;
        init(originalImage, originalDimension, zoomLevel, zoomAmount, highlights,
             inverted, aspect);
        
    }
    
    private void init(BufferedImage originalImage, Dimension originalDimension,
                      int zoomLevel, int zoomAmount, boolean highlights,
                      boolean inverted, double aspect) {
        
        this.originalImage = originalImage;
        this.imageDimension = originalDimension;
        this.zoomLevel = zoomLevel;
        this.zoomAmount = zoomAmount;
        this.highlights = highlights;
        this.inverted = inverted;
        this.aspectRatio = aspect;
        
    }
    
    protected void link(CommunicationLinker communicationLinker, SaveLinker saveLinker) {
        
        communicationNotifier = communicationLinker.getCommunicationNotifier();
        communicationLinker.subscribe(communicationSubscriber);
        
        saveLinker.subscribe(saveSubscriber);
        
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
        
        int tZoom = zoomLevel + levels;
        int dLevels;
        if (tZoom > 80) {
            dLevels = 80 - zoomLevel;
            zoomLevel = 80;
        }
        else if (tZoom < -50) {
            dLevels = -50 - zoomLevel;
            zoomLevel = -50;
        }
        else {
            dLevels = levels;
            zoomLevel = tZoom;
        }
        zoomAmount = dLevels * ZOOM_MULTIPLIER;
        
        double tViewRatio = viewRatio.x / viewRatio.y;
        viewRatio.x += zoomAmount * tViewRatio;
        viewRatio.y += zoomAmount;
        
        viewTopLeftRatio.x += zoomAmount * tViewRatio / 2;
        viewTopLeftRatio.y += zoomAmount / 2;
        
        update();
        
    }
    
    /**
     * Invert the image and notify all subscribers.
     */
    protected void invert() {
        
        originalImage = GuiImageManipulator.invert(originalImage);
        inverted = !inverted;
        this.setSettings(originalImage, imageDimension, zoomLevel,
                         zoomAmount, highlights, inverted, aspectRatio,
                         pathToImage, viewRatio, viewTopLeftRatio);
        
        for (ImageSubscriber s : subscribers) {
            s.invert(originalImage);
        }
        
    }
    
    /**
     * Toggle highlights and notify all subscribers.
     */
    protected void toggleHighlights() {
        
        highlights = !highlights;
        update();
        
    }
    
    protected void moveWindow(Point2D.Double newTopLeftRatio) {
        
        if (imageDimension != null && viewRatio != null) {
            Dimension viewSize = new Dimension((int)(viewRatio.getX() * imageDimension.getWidth()),
                                               (int)(viewRatio.getY() * imageDimension.getHeight()));
            Point topLeft = new Point((int)(newTopLeftRatio.getX() * imageDimension.getWidth()),
                                      (int)(newTopLeftRatio.getY() * imageDimension.getHeight()));
            topLeft = GuiImageManipulator.translateWithBounds(topLeft, imageDimension, viewSize);
            newTopLeftRatio.setLocation(topLeft.getX() / imageDimension.getWidth(),
                                        topLeft.getY() / imageDimension.getHeight());       
            
            viewTopLeftRatio = newTopLeftRatio;
            update();
        }
        
    }
    
    protected void setView(Point2D.Double newViewRatio,
                           Point2D.Double newViewTopLeftRatio) {
        
        viewRatio = newViewRatio;
        viewTopLeftRatio = newViewTopLeftRatio;
        update();
        
    }
    
    protected void empty() {
        
        originalImage = null;
        highlights = false;
        update();
        
    }
    
    private void initSubscribers() {
        
        this.setSettings(originalImage, imageDimension, zoomLevel,
                         zoomAmount, highlights, inverted, aspectRatio,
                         pathToImage, viewRatio, viewTopLeftRatio);
        
        for (ImageSubscriber s : subscribers) {
            s.init(settings);
        }

    }

    /**
     * Notify all subscribers of a change in the image settings.
     */
    private void update() {
        
        this.setSettings(originalImage, imageDimension, zoomLevel,
                         zoomAmount, highlights, inverted, aspectRatio,
                         pathToImage, viewRatio, viewTopLeftRatio);
        
        for (ImageSubscriber s : subscribers) {
            s.update(settings);
        }

    }
    
    private void setSettings(BufferedImage originalImage, Dimension imageDimension,
                             int zoomLevel, double zoomAmount, boolean highlights,
                             boolean inverted, double aspect, String pathToImage,
                             Point2D.Double viewRatio, Point2D.Double viewTopLeftRatio) {
        
        if (settings == null) {
            settings = new ImageSettings(originalImage, imageDimension, zoomLevel,
                                         zoomAmount, highlights, inverted, aspect,
                                         pathToImage, viewRatio, viewTopLeftRatio);
        }
        
        settings.set(originalImage, imageDimension, zoomLevel,
                     zoomAmount, highlights, inverted, aspect,
                     pathToImage, viewRatio, viewTopLeftRatio);
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber
                                    = new AbstractCommunicationSubscriber() {

        @Override
        public void setBatch(DownloadBatch_Result result) {

            pathToImage = result.imagePath();
            BufferedImage image = communicationNotifier.downloadImage(result.imagePath());
            Dimension imageD;
            double aspect;
            if (image != null) {
                imageD = new Dimension(image.getWidth(), image.getHeight());
                aspect = imageD.getWidth() / imageD.getHeight();
            }
            else {
                imageD = new Dimension(0, 0);
                aspect = 0.0d;
            }
            
            init(image, imageD, 0, 0, true, false, aspect);
            
            initSubscribers();
            
        }

    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {

        @Override
        public ImageSettings saveImageSettings() {
            if (settings == null || settings.originalImage() == null) {
                return null;
            }
            return settings;
        }

        @Override
        public void setImageSettings(ImageSettings settings) {
            
            ImageModel.this.settings = settings;
            
            originalImage = communicationNotifier.downloadImage(settings.pathToImage());
            imageDimension = settings.originalDimension();
            zoomLevel = settings.zoomLevel();
            zoomAmount = settings.zoomAmount();
            highlights = settings.highlights();
            inverted = settings.inverted();
            aspectRatio = settings.aspectRatio();
            pathToImage = settings.pathToImage();
            viewRatio = settings.viewRatio();
            viewTopLeftRatio = settings.viewTopLeftRatio();
            
            update();
            
            if (inverted) {
                invert();
            }
            
        }

    };
    
}
