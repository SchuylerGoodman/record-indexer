/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Client;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author schuyler
 */
public class SampleViewDialog extends JDialog {
    
    private SampleViewer viewPanel;
    
    public SampleViewDialog(JDialog parent, String name, BufferedImage image) {
        
        super(parent, null, true);
        
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setResizable(false);
        
        createComponents(name, image);
        
        this.pack();
        
    }
    
    private void createComponents(String name, BufferedImage image) {
        
        Dimension d = getDesiredDimension(image);
        this.setLocation(Client.SCREEN_WIDTH / 2 - d.width / 2,
                         Client.SCREEN_HEIGHT / 2 - d.height / 2);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(d.width, d.height + 50));

        this.setName(name);
        viewPanel = new SampleViewer(image, d);
        mainPanel.add(viewPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(closeAction);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel);
        
        this.add(mainPanel);
        
    }
    
    private Dimension getDesiredDimension(BufferedImage image) {
        
        double aspect = (double) image.getWidth() / (double) image.getHeight();
        Dimension d = new Dimension((int) (Client.SCREEN_HEIGHT / 2 * aspect),
                                           Client.SCREEN_HEIGHT / 2);
        return d;
        
    }
    
    private ActionListener closeAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            SampleViewDialog.this.setVisible(false);
        }
        
    };
    
    /**
     * Class for creating a simple image view panel.
     */
    private class SampleViewer extends JPanel {
        
        private Image sample;
        
        public SampleViewer(BufferedImage image, Dimension dimension) {

            super();
            
            this.setPreferredSize(dimension);
            
            setImage(image, dimension);
            
        }
        
        public final void setImage(BufferedImage image, Dimension dimension) {

            sample = image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
            
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            
            Graphics2D g2 = (Graphics2D) g;
            super.paintComponent(g2);
            
            g2.drawImage(sample, 0, 0, null);
            
        }
        
    }
    
}
