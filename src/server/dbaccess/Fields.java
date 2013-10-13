package server.dbaccess;

import java.util.Collection;

/**
 * Database Access class for the fields table.
 * 
 * @author schuyler
 */
public class Fields {
    
    /**
     * Builds a collection of shared.model.Field objects that belong to projectId.
     * <p>
     * Preconditions:   User must actually exist.
     * 
     * @param projectId The unique identifier of the project whose fields we want.
     * @return Collection of shared.model.Field objects
     */
    protected Collection<shared.model.Field> downloadBatch(int projectId) {
        return null;
    }
    
    /**
     * Gets the fields requested by the client.
     * <p>
     * Preconditions:   User must actually exist.
     * 
     * @param project An Object representing either an Integer with a Project ID, 
     * or an empty string signifying to return the fields for all projects.
     * @return A Collection of shared.model.Field objects.
     */
    protected Collection<shared.model.Field> getFields(Object project) {
        return null;
    }
    
}
