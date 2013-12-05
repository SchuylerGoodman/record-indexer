/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components.ImagePanel;

import client.gui.Client;
import java.awt.*;
import java.awt.event.*;
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
    
    private ImagePanelContext context;
    
    private boolean highlights;
    
    private BufferedImage originalImage;
    private BufferedImage viewImage;
    
    public ImagePanel() {
        
        super();
        
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(preferredSize);
        
        highlights = true;
        
        originalImage = null;
        viewImage = null;
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        
        // TODO - zoom, translate, invert, and highlight.
        
    }
    
    private MouseAdapter mouseListener = new MouseAdapter() {

        /**
         * Detects if a record was selected and tells the GUI which one it was.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        /**
         * Saves the current position of the mouse to calculate translation.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        /**
         * Updates the image position based on movement distance and notifies
         * the GUI that translation has occurred.
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        /**
         * Updates the zoom level and notifies the GUI that a zoom has occurred.
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    };
    
}
