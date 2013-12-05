/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.components.ImagePanel.ImagePanel;
import client.gui.components.buttons.ImageButtons;
import client.gui.Client;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Main split pane for the image panel and the lower panel.
 *
 * @author schuyler
 */
public class MainPanel extends JPanel {
    
    private ImageButtons buttonBar;
    private JSplitPane splitPane;
    private ImagePanel imagePanel;
    private UtilityPanel utilityPanel;
    
    public MainPanel() {
        
        super();
        
        createComponents();
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
//        this.setLayout(new GridBagLayout());
        this.setLayout(new BorderLayout());
        
        buttonBar = new ImageButtons();
//        this.add(buttonBar, gbc(0, 1, 0));
        this.add(buttonBar, BorderLayout.NORTH);
        
        imagePanel = new ImagePanel();
        utilityPanel = new UtilityPanel();
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, imagePanel, utilityPanel);
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerLocation(Client.ORIG_HEIGHT / 2);
        
//        this.add(splitPane, gbc(1, 15, 1.0));
        this.add(splitPane, BorderLayout.CENTER);
        
    }
    
    private GridBagConstraints gbc(int yOffset, int yCells, double yWeight) {
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridy = yOffset;
        gbc.gridheight = yCells;
        gbc.weighty = yWeight;
        
        return gbc;
        
    }
    
}
