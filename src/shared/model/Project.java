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
    public void loadProject(int projectId);
    
    /**
     * Loads the next Batch in line from the database into the project.
     * 
     * @return the next Batch in line
     */
    public Batch nextBatch();
    
    /**
     * Loads the last Batch in line from the database into the project.
     * 
     * @return the last Batch in line
     */
    public Batch lastBatch();
    
    /**
     * Returns the Batch object that is currently in view.
     * 
     * @return the current Batch in view, or the first if just initializing
     */
    public Batch currentBatch();
    
    /**
     * Returns the Batch keyed by the batch_id in the database.
     * 
     * @param batch_id The unique batch id from the database.
     * @return a Batch object specified by the batch_id if batch_id belongs to a batch from this project.  Otherwise return <code>null</code>.
     */
    public Batch loadBatch(int batchId);
    
    /**
     * Submit the current project and save the input information into the database.
     */
    public void submit();
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object o);
    
}
