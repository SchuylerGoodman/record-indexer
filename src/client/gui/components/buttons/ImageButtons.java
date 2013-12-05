/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components.buttons;

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
    
    private ImageButtonsContext context;
    
    private ImageButton zoomIn;
    private ImageButton zoomOut;
    private ImageButton invert;
    private ImageButton highlight;
    private ImageButton save;
    private ImageButton submit;

    public ImageButtons() {
        
        super();
        
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
        
    }
    
    /**
     * Uses context to tell the image panel and the navigation panel that a zoom
     * in has occurred.
     */
    private ActionListener zoomInListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.zoomIn(10);
        }
        
    };

    /**
     * Uses context to tell the image panel and the navigation panel that a zoom
     * out has occurred.
     */
    private ActionListener zoomOutListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.zoomOut(10);
        }
        
    };

    /**
     * Uses context to tell the image panel to invert the colors in the image.
     */
    private ActionListener invertListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.invert();
        }
        
    };
    
    /**
     * Uses context to tell the image panel to toggle record highlighting.
     */
    private ActionListener highlightListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.toggleHighlights();
        }
        
    };
    
    /**
     * Uses context to tell the GUI to save the currently indexed records locally.
     */
    private ActionListener saveListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.save();
        }
        
    };
    
    /**
     * Uses context to tell the GUI to submit the currently indexed records to
     * the server.
     */
    private ActionListener submitListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            context.submit();
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

    }
    
}
