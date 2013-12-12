/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import client.gui.model.cell.*;
import client.gui.model.image.*;
import client.gui.model.record.*;
import client.gui.model.save.settings.ImageSettings;
import client.gui.util.GuiImageManipulator;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Panel containing the main view of the image being indexed.
 * 
 * @author schuyler
 */
public class ImagePanel extends JPanel {
    
    private static final Dimension minimumSize = new Dimension(Client.ORIG_WIDTH / 6, Client.ORIG_HEIGHT / 8);
    private static final Dimension preferredSize = new Dimension(Client.ORIG_WIDTH, Client.ORIG_HEIGHT * 2 / 3);

    private RecordNotifier recordNotifier;
    private CellNotifier cellNotifier;
    private ImageNotifier imageNotifier;
    
    private BufferedImage viewImage;
    private Image scaledImage;
    private Dimension imageDimension;
    private Point viewTopLeft;
    private Point2D.Double viewRatio;
    private Point2D.Double viewTopLeftRatio;
    
    private Point selectedCell;
    private int rowHeight;
    private int columnWidth;
    private int firstXCoordinate;
    private int firstYCoordinate;
    private int worldRowHeight;
    private int worldColumnWidth;
    private int worldXCoordinate;
    private int worldYCoordinate;
    
    private boolean highlights;
    private boolean initializing;
    
    public ImagePanel(RecordLinker recordLinker, CellLinker cellLinker,
                      ImageLinker imageLinker) {
        
        super();
        
        this.setBackground(new Color(128, 128, 128));
        
        this.recordNotifier = recordLinker.getRecordNotifier();
        
        this.cellNotifier = cellLinker.getCellNotifier();
        cellLinker.subscribe(cellSubscriber);
        
        this.imageNotifier = imageLinker.getImageNotifier();
        imageLinker.subscribe(imageSubscriber);

        this.addComponentListener(componentListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(mouseListener);
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        selectedCell = new Point(0, 0);
        
        initializing = false;
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // If initialized
        if (viewImage != null && imageDimension != null) {
            if (scaledImage == null || // If not yet scaled, or needs rescaling
               (viewImage.getWidth() != imageDimension.width &&
                viewImage.getHeight() != imageDimension.height)) {
                
                // Rescale
                scaledImage = viewImage.getScaledInstance(imageDimension.width,
                                                          imageDimension.height,
                                                          Image.SCALE_SMOOTH);
                
            }
            // Draw image
            g2.drawImage(scaledImage, viewTopLeft.x, viewTopLeft.y, null);
        }
        
        // If highlights are on
        if (highlights) {
            // highlight selectedCell with height, width, and location
            g2.setColor(new Color(0, 0, 255, 64));
            g2.fillRect(firstXCoordinate, firstYCoordinate,
                        columnWidth, rowHeight);
        }
        
    }
    
    /**
     * Gets the ratio of the view dimensions to the image dimensions.
     * 
     * @return Point2D.Double with the ratios for width and height as x and y.
     */
    private Point2D.Double getViewRatio() {

        if (viewRatio == null) {
            viewRatio = new Point2D.Double(0, 0);
        }
        Dimension viewSize = this.getSize();
        viewRatio.setLocation(viewSize.getWidth() / imageDimension.getWidth(),
                              viewSize.getHeight() / imageDimension.getHeight());
        
        return viewRatio;
        
    }
    
    private void setViewByRatio() {

        Dimension viewSize = this.getSize();
        imageDimension.setSize(viewSize.getWidth() / viewRatio.x,
                               viewSize.getHeight() / viewRatio.y);
        
    }
    
    /**
     * Gets the ratio of the top left point of the view to the image dimensions.
     * 
     * @return Point2D.Double with the ratios for the x and y coordinates.
     */
    private Point2D.Double getViewTopLeftRatio() {
        
        if (viewTopLeftRatio == null) {
            viewTopLeftRatio = new Point2D.Double(0, 0);
        }
        viewTopLeftRatio.setLocation(viewTopLeft.getX() / imageDimension.getWidth(),
                                     viewTopLeft.getY() / imageDimension.getHeight());
        return viewTopLeftRatio;
        
    }
    
    private void setTopLeftByRatio() {

        viewTopLeft.x = (int) (imageDimension.getWidth() * viewTopLeftRatio.x);
        viewTopLeft.y = (int) (imageDimension.getHeight() * viewTopLeftRatio.y);
        
    }
    
    private void calculateCellDimensions() {
        
        if (viewImage != null) {
            rowHeight = (int) (( (double) worldRowHeight  / (double) viewImage.getHeight() )
                                * imageDimension.getHeight());
            columnWidth = (int) (( (double) worldColumnWidth  / (double) viewImage.getWidth() )
                                * imageDimension.getWidth());
            firstXCoordinate = viewTopLeft.x + (int) (( (double) worldXCoordinate / (double) viewImage.getWidth() )
                                * imageDimension.getWidth());
            firstYCoordinate = viewTopLeft.y + (int) (( (double) worldYCoordinate / (double) viewImage.getHeight() )
                                * imageDimension.getHeight());
        }
        
    }
    
    /**
     * Says if an image has been set in the panel. (i.e. a batch is being indexed).
     * 
     * @return true if batch is being index, otherwise false.
     */
    public boolean hasImage() {
        if (viewImage != null) {
            return true;
        }
        return false;
    }
    
    private CellSubscriber cellSubscriber = new CellSubscriber() {

        @Override
        public void selected(int row, int column, int rowHeight,
                             int columnWidth, int firstXCoordinate,
                             int firstYCoordinate) {
            
            // Update selectedCell and Repaint
            if (row < 0 || column < 0) {
                selectedCell = new Point(0, 0);
            }
            else {
                selectedCell.setLocation(row, column);
            }
            worldRowHeight = rowHeight;
            worldColumnWidth = columnWidth;
            worldXCoordinate = firstXCoordinate;
            worldYCoordinate = firstYCoordinate;
            calculateCellDimensions();
            repaint();
            
        }
        
    };
    
    private ImageSubscriber imageSubscriber = new ImageSubscriber() {

        @Override
        public void init(ImageSettings settings) {
            
            viewImage = settings.originalImage();
            imageDimension = GuiImageManipulator.scaleToWindow(settings.originalDimension(),
                                                               getSize());
            viewTopLeft = GuiImageManipulator.balanceInWindow(imageDimension,
                                                              getSize());
            imageNotifier.setView(getViewRatio(), getViewTopLeftRatio());
            highlights = settings.highlights();
            repaint();

        }
        
        @Override
        public void update(ImageSettings settings) {
            
            viewImage = settings.originalImage();        
            viewRatio = settings.viewRatio();        
            viewTopLeftRatio = settings.viewTopLeftRatio();
            if (imageDimension == null) {
                initializing = true;
                imageDimension = new Dimension(settings.originalDimension());
            }
            else {
                setViewByRatio();
            }
            if (viewTopLeft == null) {
                initializing = true;
                viewTopLeft = new Point(0, 0);
            }
            else {
                setTopLeftByRatio();
            }
            highlights = settings.highlights();
            calculateCellDimensions();
            repaint();

        }

        @Override
        public void invert(BufferedImage invertedImage) {
            viewImage = invertedImage;
            scaledImage = viewImage.getScaledInstance(imageDimension.width,
                                                      imageDimension.height,
                                                      Image.SCALE_SMOOTH);
            repaint();
        }   

    };
    
    private ComponentAdapter componentListener = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            if (viewImage != null) {
                if (initializing) {
                    setViewByRatio();
                    setTopLeftByRatio();
                    initializing = false;
                }
                imageNotifier.setView(getViewRatio(), getViewTopLeftRatio());
            }
        }

    };
    
    private MouseAdapter mouseListener = new MouseAdapter() {

        private int initialX;
        private int initialY;
        
        /**
         * Detects if a record was selected and tells the GUI which one it was.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            // If the image was clicked
            if (x > viewTopLeft.x && y > viewTopLeft.y) {
                if (     x < ( viewTopLeft.x + scaledImage.getWidth(null) )
                      && y < ( viewTopLeft.y + scaledImage.getHeight(null))) {
                    
                    // Convert from device to world space.
                    double selectedRatioX = ((double) ( x - viewTopLeft.x ) / (double) scaledImage.getWidth(null));
                    double selectedRatioY = ((double) ( y - viewTopLeft.y ) / (double) scaledImage.getHeight(null));
                    Point p = new Point((int) (selectedRatioX * viewImage.getWidth()),
                                        (int) (selectedRatioY * viewImage.getHeight()));
                    // Ask CellModel to do collision checking
                    cellNotifier.select(p);
                    
                }
            }
        }

        /**
         * Saves the current position of the mouse to calculate translation.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            initialX = e.getX();
            initialY = e.getY();
        }

        /**
         * Updates the image position based on movement distance and notifies
         * the GUI that translation has occurred.
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            
            viewTopLeft.x += e.getX() - initialX;
            viewTopLeft.y += e.getY() - initialY;
            firstXCoordinate += e.getX() - initialX;
            firstYCoordinate += e.getY() - initialY;
            imageNotifier.moveWindow(getViewTopLeftRatio());
            
            initialX = e.getX();
            initialY = e.getY();
            
        }
        
        /**
         * Updates the zoom level and notifies the GUI that a zoom has occurred.
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            imageNotifier.zoom((int) (e.getPreciseWheelRotation() * e.getScrollAmount() * -1));
        }
        
    };
    
}
