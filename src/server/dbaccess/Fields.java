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

    /**
     * Creates a new Field in the database.
     * 
     * @param newField shared.model.Field object to insert into the database.
     * @return Field model class with the new field ID
     */
    protected shared.model.Field insert(shared.model.Field newField) {
        return null;
    }
    
    /**
     * Updates a Field in the database.
     * 
     * @param field shared.model.Field object to update in the database.
     */
    protected void update(shared.model.Field field) {
    }
    
    /**
     * Gets a Field from the database.
     * 
     * @param fieldIds Collection of Field IDs to get from the database.
     * @return shared.model.Field object with the requested data.
     */
    protected Collection<shared.model.Field> get(Collection<Integer> fieldIds) {
        return null;
    }

    /**
     * Deletes a Field from the database.
     * 
     * @param deleteModel The model class to delete.
     */
    protected void delete(shared.model.ModelClass deleteModel) {
    }
    
}
