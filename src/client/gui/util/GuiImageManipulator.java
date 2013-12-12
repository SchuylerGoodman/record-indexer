/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author schuyler
 */
public class GuiImageManipulator {
    
    /**
     * Keeps an image within the bounds of a window, given that the window has
     * top-left coordinates at (0, 0)
     * 
     * @param newTopLeftCoords Coordinates that need to be corrected.
     * @param imageDimension Dimensions of the image being translated.
     * @param windowDimension Dimensions of the window displaying the image.
     * @return Point with the corrected coordinates.
     */
    public static Point translateWithBounds(Point newTopLeftCoords, Dimension imageDimension, Dimension windowDimension) {
        
        double xLeft = 0;
        double yTop = 0;
        boolean imageWidthLarger = false;
        boolean imageHeightLarger = false;
        
        if (imageDimension.getWidth() > windowDimension.getWidth()) {
            imageWidthLarger = true;
        }
        if (imageDimension.getHeight() > windowDimension.getHeight()) {
            imageHeightLarger = true;
        }

        double newX = newTopLeftCoords.getX();
        double newY = newTopLeftCoords.getY();
        
        if (imageWidthLarger ? newX < 0 : newX > 0) {
            double xRight = newX + imageDimension.getWidth();
            double windowRight = windowDimension.getWidth();
            if (imageWidthLarger ? xRight < windowRight : xRight > windowRight) {
                xLeft = windowDimension.getWidth() - imageDimension.getWidth();
            }
            else {
                xLeft = newX;
            }
        } // Otherwise xLeft already set to 0
        
        if (imageHeightLarger ? newY < 0 : newY > 0) {
            double yBottom = newY + imageDimension.getHeight();
            double windowBottom = windowDimension.getHeight();
            if (imageHeightLarger ? yBottom < windowBottom : yBottom > windowBottom) {
                yTop = windowDimension.getHeight() - imageDimension.getHeight();
            }
            else {
                yTop = newY;
            }
        } // Otherwise yTop already set to 0
        
        return new Point((int) xLeft, (int) yTop);
        
    }
    
    /**
     * Takes the Dimension of an image and scales it down to within the Dimension
     * of a window, preserving the aspect ratio.
     * 
     * @param imageDimension Dimension of the image
     * @param windowDimension Dimension of the window
     * @return New scaled Dimension for the image
     */
    public static Dimension scaleToWindow(Dimension imageDimension, Dimension windowDimension) {
        
        assert imageDimension != null && windowDimension != null;
        
        double targetAspectRatio = imageDimension.getWidth() / imageDimension.getHeight();
        Dimension targetDimension = new Dimension();
        
        if (windowDimension.getWidth() / targetAspectRatio <= windowDimension.getHeight()) {
            targetDimension.setSize(windowDimension.getWidth(), windowDimension.getWidth() / targetAspectRatio);
        }
        else if (windowDimension.getHeight() * targetAspectRatio <= windowDimension.getWidth()) {
            targetDimension.setSize(windowDimension.getHeight() * targetAspectRatio, windowDimension.getHeight());
        }
        else {
            // This should never happen
        }
        
        return targetDimension;
        
    }
    
    /**
     * Finds the top-left coordinate that balances an image in the center of a
     * window.
     * 
     * @param balanceDimension Dimension to balance
     * @param inDimension Dimension in which to balance it
     * @return Point representing the top-left coordinate that balances the balanceDimension
     */
    public static Point balanceInWindow(Dimension balanceDimension, Dimension inDimension) {
        
        assert balanceDimension != null && inDimension != null;
        
        Point topLeftCoords = new Point(0, 0);
        
        topLeftCoords.x = (inDimension.width - balanceDimension.width) / 2;
        topLeftCoords.y = (inDimension.height - balanceDimension.height) / 2;
        
        return topLeftCoords;
        
    }
    
    /**
     * Invert the colors of a grey scale .png.
     * 
     * @param image the .png image to invert.
     * @return The inverted .png formatted image.
     */
    public static BufferedImage invert(BufferedImage image) {
        
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = image.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        BufferedImage copy = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, false);
                color = new Color(255 - color.getRed(),
                                  255 - color.getBlue(),
                                  255 - color.getGreen());
                copy.setRGB(x, y, color.getRGB());
            }
        }
        
        return copy;
        
    }

}
