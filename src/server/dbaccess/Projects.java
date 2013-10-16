package server.dbaccess;

import java.util.Collection;

/**
 * Database Access class for the projects table.
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
    
    /**
     * Inserts a new Project into the database.
     * 
     * @param newProject shared.model.Project with the info to insert into the database.
     * @return shared.model.Project with the generated Project ID
     */
    protected shared.model.Project insert(shared.model.Project newProject) {
        return null;
    }

    /**
     * Updates a project from the database.
     * 
     * @param project shared.model.Project with the updated information.
     */
    protected void update(shared.model.Project project) {
    }
    
    /**
     * Gets a collection of Projects from the database.
     * 
     * @param projectIds Collection of IDs whose projects we want.
     * @return Collection of shared.model.Project objects with the requested information.
     */
    protected Collection<shared.model.Project> get(Collection<Integer> projectIds) {
        return null;
    }
    
}
