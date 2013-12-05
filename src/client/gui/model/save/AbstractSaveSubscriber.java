/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save;

import client.gui.model.save.settings.*;

/**
 * Abstract class to simplify the implementation of a SaveSubscriber.
 *
 * @author schuyler
 */
public abstract class AbstractSaveSubscriber implements SaveSubscriber {
    
    @Override
    public ImageSettings saveImageSettings() {
        return null;
    }
    
    @Override
    public IndexerState saveIndexerState() {
        return null;
    }
    
    @Override
    public WindowSettings saveWindowSettings() {
        return null;
    }
    
    @Override
    public void setImageSettings(ImageSettings settings) {
    }
    
    @Override
    public void setIndexerState(IndexerState state) {
    }
    
    @Override
    public void setWindowSettings(WindowSettings settings) {
    }

}
