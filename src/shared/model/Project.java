/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author Schuyler Goodman
 */
public interface Project {
    
    /**
     * Loads project data (batch ids, fields, etc.) keyed by
     * <code>project_id</code> from the database into the project object.
     * <p>
     * Loads the first batch from this project into the project object.
     * 
     * @param project_id the unique id of the given project in the database.
     */
    public void loadProject(int project_id);
    
    /**
     * Loads the next batch in line from the database into the project.
     * 
     * @return next
     */
    public Batch nextBatch();
    
    /**
     * Loads the last batch in line from the database into the project.
     * 
     * @return last
     */
    public Batch lastBatch();
    
    /**
     * Submit the current project and save the input information into the database.
     */
    public void submit();
    
    
}
