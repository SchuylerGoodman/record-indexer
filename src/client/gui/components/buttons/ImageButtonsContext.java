/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components.buttons;

/**
 * Context by which the this button bar communicates with the other
 * components in the GUI.
 */
public interface ImageButtonsContext {

    /**
     * Tells the rest of the GUI to zoom in.
     * 
     * @param zoomLevels How many levels to zoom in.
     */
    public void zoomIn(int zoomLevels);

    /**
     * Tells the GUI to zoom out.
     * 
     * @param zoomLevels How many levels to zoom out.
     */
    public void zoomOut(int zoomLevels);

    /**
     * Tells the GUI to invert the image.
     */
    public void invert();

    /**
     * Tells the GUI to toggle the highlights.
     */
    public void toggleHighlights();

    /**
     * Tells the GUI to save the currently indexed records locally.
     */
    public void save();

    /**
     * Tells the GUI to submit the currently indexed records to the server.
     */
    public void submit();

}
