/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.children;

import client.gui.util.GuiImageManipulator;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * Panel for displaying downloaded image from the server.
 * 
 * @author schuyler
 */
public class ResultViewPanel extends JPanel {
    
    private Context context;
    
    // Maps Image IDs to BufferedImages as a way of caching to prevent repeated
    // download of the same image.
    private HashMap<Integer, BufferedImage> viewedImages;
    
    private BufferedImage image; // The current image being shown in the panel.
    private Image scaledImage;
    
    private Dimension imageDimension; // Dimension of the original image
    private Dimension scaledToWindowDimension; // Dimension of the image scaled to the window
    private Point scaledImageTopLeftCoords; // Top-left point of the image
    private Dimension scaledImageDimension; // Dimension of the scaled (and possibly zoomed) image
    
    private double aspectRatio; // Aspect ratio of the image
    private int zoomLevel; // Arbitrary zoom level
    private static final int MAX_ZOOM_LEVEL = 2000; // Maximum zoom
    private double zoomIncrement; // Increment per zoom level
    private boolean translate; // If image is currently being translated
    
    public interface Context {
        
        public BufferedImage downloadImage(String path);
        
    }
    
    public ResultViewPanel(Context context) {
        
        super();
        
        assert context != null;
        
        this.setFocusable(true);
        
        this.context = context;
        this.viewedImages = new HashMap<>();
        this.image = null;
        this.scaledImage = null;
        
        this.imageDimension = new Dimension();
        this.scaledImageTopLeftCoords = new Point(0, 0);
        this.scaledImageDimension = new Dimension();
        
        this.aspectRatio = 0;
        this.zoomIncrement = .5;
        
        this.addMouseListener(mouseInputAdapter);
        this.addMouseMotionListener(mouseInputAdapter);
        this.addMouseWheelListener(mouseInputAdapter);
        
    }
    
    /**
     * Downloads an image from the server (or gets it from the cache) and puts
     * it in the panel.
     * 
     * @param path Relative path to the image on the server
     * @param imageId ID of the image for caching purposes
     */
    public void downloadImage(String path, Integer imageId) {
        
        assert path != null;
        assert imageId != null;
        
        if (viewedImages.containsKey(imageId)) {
            image = viewedImages.get(imageId);
        }
        else {
            image = context.downloadImage(path);
            viewedImages.put(imageId, image);
        }
        this.zoomLevel = 0;
        imageDimension = new Dimension(image.getWidth(), image.getHeight());
        aspectRatio = imageDimension.getWidth() / image.getHeight();
        
        scaledToWindowDimension = GuiImageManipulator.scaleToWindow(imageDimension, this.getSize());
        
        repaint();
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        
        if (image != null) {
            // Scale image to window, in case the window has been resized
            scaledToWindowDimension = GuiImageManipulator.scaleToWindow(imageDimension, this.getSize());
            if (zoomLevel == 0) { // If not zoomed in, then scale and center in the window
                scaledImageDimension = scaledToWindowDimension;
                scaledImageTopLeftCoords = GuiImageManipulator.balanceInWindow(scaledImageDimension, this.getSize());
            }
            else { // Otherwise, zoom and bound translation to keep image in window context
                double zoomTotal = zoomIncrement * zoomLevel;
                scaledImageDimension.setSize(scaledToWindowDimension.getWidth() + zoomTotal,
                                             scaledToWindowDimension.getHeight() + (zoomTotal / aspectRatio));
                scaledImageTopLeftCoords = GuiImageManipulator.translateWithBounds(scaledImageTopLeftCoords, scaledImageDimension, this.getSize());
            }
            if (!translate) { // If not translating, rescale image (results in more efficient translation)
                scaledImage = image.getScaledInstance(scaledImageDimension.width, scaledImageDimension.height, Image.SCALE_SMOOTH);
            }
            translate = false;
        }

        g2.drawImage(scaledImage, scaledImageTopLeftCoords.x, scaledImageTopLeftCoords.y, null);
        
    }
    
    private MouseInputAdapter mouseInputAdapter = new MouseInputAdapter() {
        
        private int startX;
        private int startY;
        private int scaledImageX;
        private int scaledImageY;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            ResultViewPanel.this.requestFocusInWindow();
        }
        
        /**
         * An attempt, not sure that it works, since I don't have a mouse right now.
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            zoomLevel += e.getWheelRotation() * 5.0;
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            
            this.startX = e.getX();
            this.startY = e.getY();
            this.scaledImageX = scaledImageTopLeftCoords.x;
            this.scaledImageY = scaledImageTopLeftCoords.y;
            
        }
        
        /**
         * If control is pressed, zooms in on image, otherwise it translates.
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            int xMoved = e.getX() - this.startX;
            if (e.isControlDown()) {
                // Zoom
                
                zoomLevel += xMoved;
                if (zoomLevel < 0) {
                    zoomLevel = 0;
                }
                if (zoomLevel > MAX_ZOOM_LEVEL) {
                    zoomLevel = MAX_ZOOM_LEVEL;
                }
            }
            else {
                // Translate
                translate = true;
                int yMoved = e.getY() - this.startY;
                int destinationX = this.scaledImageX + xMoved;
                int destinationY = this.scaledImageY + yMoved;
                scaledImageTopLeftCoords.move(destinationX, destinationY);
            }
            repaint();
            
        }
        
    };
    
}
