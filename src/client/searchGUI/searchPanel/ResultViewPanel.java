/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author schuyler
 */
public class ResultViewPanel extends JPanel {
    
    private Context context;
    // Maps Image IDs to BufferedImages as a way of caching to prevent repeated
    // download of the same image.
    private HashMap<Integer, BufferedImage> viewedImages;
    // The current image being shown in the panel.
    private BufferedImage image;
    private Image scaledImage;
    
    public interface Context {
        
        public BufferedImage downloadImage(String path);
        
    }
    
    public ResultViewPanel(Context context) {
        
        super();
        
        assert context != null;
        
        this.context = context;
        this.viewedImages = new HashMap<>();
        this.image = null;
        this.scaledImage = null;
        
    }
    
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
        scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        repaint();
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        
        if (image != null) {
            scaledImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        }

        g2.drawImage(scaledImage, 0, 0, null);
        
    }
    
}
