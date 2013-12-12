/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.image;

import client.gui.model.save.settings.ImageSettings;
import java.awt.image.BufferedImage;

/**
 *
 * @author schuyler
 */
public class AbstractImageSubscriber implements ImageSubscriber {

    @Override
    public void init(ImageSettings settings) {
    }

    @Override
    public void update(ImageSettings settings) {
    }

    @Override
    public void invert(BufferedImage invertedImage) {
    }
    
}
