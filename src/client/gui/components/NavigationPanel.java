/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.image.*;
import client.gui.model.save.settings.ImageSettings;
import client.gui.util.GuiImageManipulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author schuyler
 */
public class NavigationPanel extends JPanel {
    
    private ImageNotifier imageNotifier;

    private ImageSettings settings;    
    private BufferedImage originalImage;
    
    private Image scaledImage;
    private Dimension targetDimension;
    private Point targetPoint;
    private Dimension viewDimension = new Dimension();
    
    private boolean rescale = true;
    
    public NavigationPanel(ImageLinker imageLinker) {
        
        super();
        
        imageNotifier = imageLinker.getImageNotifier();
        imageLinker.subscribe(imageSubscriber);
        
        this.addComponentListener(componentListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g2);
        
        if (settings != null) {
            if (settings.originalImage() != null && settings.originalDimension() != null &&
                    settings.viewRatio() != null &&  settings.viewTopLeftRatio() != null) {
                
                if (rescale) {
                    targetDimension = GuiImageManipulator.scaleToWindow(
                                        settings.originalDimension(), this.getSize());
                    targetPoint = GuiImageManipulator.balanceInWindow(
                                        targetDimension, this.getSize());
                    viewDimension.setSize(settings.viewRatio().getX() * targetDimension.getWidth(),
                                          settings.viewRatio().getY() * targetDimension.getHeight());
                    scaledImage = settings.originalImage().getScaledInstance(targetDimension.width,
                                                                             targetDimension.height,
                                                                             Image.SCALE_SMOOTH);
                    rescale = false;
                }
                assert scaledImage != null;
                g2.drawImage(scaledImage, targetPoint.x, targetPoint.y, null);
                
                int viewWidth = (int) (targetDimension.getWidth() * settings.viewRatio().getX());
                int viewHeight = (int) (targetDimension.getHeight() * settings.viewRatio().getY());
                
                int viewTLX = (int) (targetPoint.getX() - targetDimension.getWidth()
                                        * settings.viewTopLeftRatio().getX());
                int viewTLY = (int) (targetPoint.getY() - targetDimension.getHeight()
                                        * settings.viewTopLeftRatio().getY());
                
                g2.setColor(new Color(128, 128, 128, 64));
                g2.fillRect(viewTLX, viewTLY, viewWidth, viewHeight);
                
            }
        }
        
    }
    
    private AbstractImageSubscriber imageSubscriber = new AbstractImageSubscriber() {

        @Override
        public void init(ImageSettings settings) {
            NavigationPanel.this.settings = settings;
            repaint();
        }

        @Override
        public void update(ImageSettings settings) {
            NavigationPanel.this.settings = settings;
            repaint();
        }

    };
    
    private ComponentAdapter componentListener = new ComponentAdapter() {
      
        @Override
        public void componentResized(ComponentEvent e) {
            rescale = true;
        }
        
    };
    
    private MouseAdapter mouseListener = new MouseAdapter() {
        
        private int initialX;
        private int initialY;
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            if (targetDimension != null && targetPoint != null) {
                initialX = e.getX();
                initialY = e.getY();
                Point2D.Double p = new Point2D.Double(
                        (targetPoint.getX() + e.getX() - viewDimension.getWidth() / 2) / targetDimension.getWidth(),
                        (targetPoint.getY() + e.getY() - viewDimension.getHeight() / 2) / targetDimension.getHeight());
                imageNotifier.moveWindow(p);
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicked(e);
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (targetPoint != null && targetDimension != null) {
                double newX = (targetPoint.getX() - e.getX() + viewDimension.getWidth() / 2) / targetDimension.getWidth();
                double newY = (targetPoint.getY() - e.getY() + viewDimension.getHeight() / 2) / targetDimension.getHeight();
                Point2D.Double p = new Point2D.Double(newX, newY);
                imageNotifier.moveWindow(p);
            }
        }
        
    };
    
}
