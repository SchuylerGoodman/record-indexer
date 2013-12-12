/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.communication.*;
import client.gui.model.image.*;
import client.gui.model.save.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Button bar for the ImagePanel.
 *
 * @author schuyler
 */
public class ImageButtons extends JPanel {
    
    private static final int BUFFER = 5;
    
    private CommunicationNotifier communicationNotifier;
    private ImageNotifier imageNotifier;
    private SaveNotifier saveNotifier;
    
    private ImageButton zoomIn;
    private ImageButton zoomOut;
    private ImageButton invert;
    private ImageButton highlight;
    private ImageButton save;
    private ImageButton submit;

    public ImageButtons(CommunicationLinker communicationLinker,
                        ImageLinker imageLinker,
                        SaveLinker saveLinker) {
        
        super();
        
        this.communicationNotifier = communicationLinker.getCommunicationNotifier();
        
        this.imageNotifier = imageLinker.getImageNotifier();
        
        this.saveNotifier = saveLinker.getSaveNotifier();
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        // Add buffer space between the top and bottom of all the buttons
        this.setBorder(BorderFactory.createEmptyBorder(BUFFER, 0, BUFFER, 0));
        
        // Create Buttons
        zoomIn = new ImageButton("Zoom In", zoomInListener);
        zoomOut = new ImageButton("Zoom Out", zoomOutListener);
        invert = new ImageButton("Invert", invertListener);
        highlight = new ImageButton("Toggle Highlights", highlightListener);
        save = new ImageButton("Save", saveListener);
        submit = new ImageButton("Submit", submitListener);
        
        // Add buttons to the button bar
        this.add(zoomIn);
        this.add(zoomOut);
        this.add(invert);
        this.add(highlight);
        this.add(save);
        this.add(submit);

        enabled(false);
        
    }
    
    /**
     * Tells the button bar to enable or disable all buttons.
     * 
     * @param enabled true if enabled, false if disabled.
     */
    public void enabled(boolean enabled) {
        
        zoomIn.setEnabled(enabled);
        zoomOut.setEnabled(enabled);
        invert.setEnabled(enabled);
        highlight.setEnabled(enabled);
        save.setEnabled(enabled);
        submit.setEnabled(enabled);
        
    }
    
    private ActionListener zoomInListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            imageNotifier.zoom(10);
        }
        
    };

    private ActionListener zoomOutListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            imageNotifier.zoom(-10);
        }
        
    };

    private ActionListener invertListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            imageNotifier.invert();
        }
        
    };
    
    private ActionListener highlightListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            imageNotifier.toggleHighlights();
        }
        
    };
    
    private ActionListener saveListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveNotifier.save();
        }
        
    };
    
    private ActionListener submitListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            communicationNotifier.submitBatch();
        }
        
    };

    /**
     * JButton wrapper for the buttons in the ImagePanel button bar.
     * Adds blank buffer space between components.
     */
    public class ImageButton extends JPanel {

        private JButton button;

        public ImageButton(String text, ActionListener actionListener) {

            super();
            
            createComponents(text, actionListener);

        }
        
        /**
         * Creates components for this panel.
         */
        private void createComponents(String text, ActionListener actionListener) {
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            
            button = new JButton(text);
            button.addActionListener(actionListener);
            
            this.add(button);
            
            this.add(Box.createRigidArea(new Dimension(BUFFER * 2, 0)));
            
        }

        @Override
        public void setEnabled(boolean enabled) {
            button.setEnabled(enabled);
        }

    }
    
}
