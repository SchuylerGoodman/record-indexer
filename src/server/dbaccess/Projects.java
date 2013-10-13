/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dbaccess;

import java.util.Collection;

/**
 *
 * @author schuyler
 */
public class Projects {
    
    /**
     * Query the database for the projects available to the current User.
     * <p>
     * Precondition: Username and password belong to an actual User.
     * 
     * @param username
     * @param password
     * 
     * @return Collection of a Collection of Objects - Inner Container will have 
     * a [Integer, String] => [Project Id, Project Name] pair.
     */
    protected Collection<Collection<Object>> getProjects(String username, String password) {
        return null;
    }
    
    /**
     * Finds the Project keyed by projectId and sends it back up the line.
     * <p>
     * Precondition: Username and password belong to an actual User.
     * 
     * @param projectId Unique identifier for the Project to download.
     * @return shared.model.Project The container for the Project, Batch, Field, 
     * and Record data to be downloaded.
     * @return null if the user or project ID does not exist.
     */
    protected shared.model.Project downloadBatch(int projectId) {
        return null;
    }
    
}
