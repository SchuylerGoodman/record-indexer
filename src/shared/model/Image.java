/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.net.URL;

/**
 *
 * @author schuyler
 */
public class Image {
    
    /**
     * Getter method for the unique Image ID.
     * 
     * @return int image ID
     */
    public int imageId() {
        return 0;
    }
    
    /**
     * Getter method for the URL path to the Image
     * 
     * @return URL path to this Image
     */
    public URL url() {
        return null;
    }
    
    /**
     * Getter method for the title of this Image.
     * 
     * @return String
     */
    public String title() {
        return null;
    }
    
    /**
     * Getter method for the unique Project ID that this Image belongs to.
     * 
     * @return int
     */
    public int projectId() {
        return 0;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(Object o) {
        return false;
    }
    
}
