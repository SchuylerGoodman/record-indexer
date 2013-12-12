/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save.settings;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author schuyler
 */
public class ImageSettings implements DataModel {
    
    private transient BufferedImage originalImage;
    private Dimension originalDimension;
    private int zoomLevel;
    private double zoomAmount;
    private boolean highlights;
    private double aspectRatio;
    private String pathToImage;
    
    private Point2D.Double viewRatio;
    private Point2D.Double viewTopLeftRatio;
    
    public ImageSettings(BufferedImage originalImage, Dimension originalDimension,
                         int zoomLevel, double zoomAmount, boolean highlights,
                         double aspect, String pathToImage,
                         Point2D.Double viewRatio, Point2D.Double viewTopLeftRatio) {
        
        this.set(originalImage, originalDimension, zoomLevel, zoomAmount,
                 highlights, aspect, pathToImage, viewRatio, viewTopLeftRatio);
        
    }
    
    public final void set(BufferedImage originalImage, Dimension originalDimension,
                         int zoomLevel, double zoomAmount, boolean highlights,
                         double aspect, String pathToImage,
                         Point2D.Double viewRatio, Point2D.Double viewTopLeftRatio) {
        
        this.originalImage = originalImage;
        this.originalDimension = originalDimension;
        this.zoomLevel = zoomLevel;
        this.zoomAmount = zoomAmount;
        this.highlights = highlights;
        this.aspectRatio = aspect;
        this.pathToImage = pathToImage;
        this.viewRatio = viewRatio;
        this.viewTopLeftRatio = viewTopLeftRatio;
        
    }
    
    public BufferedImage originalImage() {
        return this.originalImage;
    }
    
    public Dimension originalDimension() {
        return this.originalDimension;
    }
    
    public int zoomLevel() {
        return this.zoomLevel;
    }
    
    public double zoomAmount() {
        return this.zoomAmount;
    }
    
    public boolean highlights() {
        return this.highlights;
    }
    
    public double aspectRatio() {
        return this.aspectRatio;
    }
    
    public String pathToImage() {
        return this.pathToImage;
    }
    
    public Point2D.Double viewRatio() {
        return this.viewRatio;
    }
    
    public Point2D.Double viewTopLeftRatio() {
        return this.viewTopLeftRatio;
    }
    
    public void combine(ImageSettings otherSettings) {
        // Do nothing, retain first image settings
    }
    
}
