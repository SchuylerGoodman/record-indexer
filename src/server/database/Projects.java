package server.database;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.model.Project;

/**
 * Database Access class for the projects table.
 * 
 * @author schuyler
 */
public class Projects {
    
    public static class ProjectInsertFailedException extends Database.InsertFailedException {
        public ProjectInsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class ProjectUpdateFailedException extends Database.UpdateFailedException {
        public ProjectUpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class ProjectGetFailedException extends Database.GetFailedException {
        public ProjectGetFailedException(String message) {
            super(message);
        }
    }
    
    public static class ProjectDeleteFailedException extends Database.DeleteFailedException {
        public ProjectDeleteFailedException(String message) {
            super(message);
        }
    }
    
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
     * @param connection An open database connection.
     * @param newProject shared.model.Project with the info to insert into the database.
     * @return shared.model.Project with the generated Project ID
     */
    protected Project insert(Connection connection, Project newProject)
            throws ProjectInsertFailedException, SQLException {
        
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Entering Projects.insert()");
        if (connection == null) {
            throw new ProjectInsertFailedException("Database connection has not been initialized.");
        }
        PreparedStatement stmt = null;
        Statement kStmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO projects (title, recordCount, firstYCoord, fieldHeight)");
            sql.append(" VALUES (?, ?, ?, ?)");
            
            stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, newProject.title());
            stmt.setInt(2, newProject.recordCount());
            stmt.setInt(3, newProject.firstYCoord());
            stmt.setInt(4, newProject.fieldHeight());
            
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                int id = rs.getInt(1);
                newProject.setProjectId(id);
            }
            else {
                throw new Projects.ProjectInsertFailedException("Project was not inserted and no SQLException was thrown.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.INFO, "Project might already exist in the database.");
            throw new Projects.ProjectInsertFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (kStmt != null) kStmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Leaving Projects.insert()");
        return newProject;

    }

    /**
     * Updates a project from the database.
     * 
     * @param connection Open database connection
     * @param project shared.model.Project with the updated information.
     * @throws ProjectUpdateFailedException
     * @throws SQLException
     */
    protected void update(Connection connection, Project project)
            throws ProjectUpdateFailedException, SQLException {
        
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Entering Projects.update()");
        if (connection == null) {
            throw new ProjectUpdateFailedException("Database connection has not been initialized.");
        }
        if (project.projectId() < 1) {
            throw new ProjectUpdateFailedException("No project ID found in input Project parameter.");
        }
        PreparedStatement stmt = null;
            
        try {
            StringBuilder sql = new StringBuilder();

            if (project.title() != null) {
                sql.append(" title=\"").append(project.title()).append("\"");
            }
            if (project.recordCount() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" recordCount=").append(project.recordCount());
            }
            if (project.firstYCoord() > -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" firstYCoord=").append(project.firstYCoord());
            }
            if (project.fieldHeight() > -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" fieldHeight=").append(project.fieldHeight());
            }
            if (sql.length() > 0) {
                sql.insert(0, "update projects set");
                sql.append(" where projectId = ").append(project.projectId());
            }
            else {
                Logger.getLogger(Project.class.getName()).log(
                        Level.WARNING, "Attempted update with no updatable information.");
                throw new ProjectUpdateFailedException("No information given to update.");
            }

            stmt = connection.prepareStatement(sql.toString());

            if (stmt.executeUpdate() != 1) {
                throw new ProjectUpdateFailedException(String.format("ID %d not found in 'projects' table.", project.projectId()));
            }
            
        }
        catch (SQLException ex) {
            throw new ProjectUpdateFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Leaving Projects.update()");
        
    }
    
    /**
     * Gets all Projects from the database.
     * 
     * @param connection Open database connection
     * 
     * @return List of shared.model.Project objects with the requested information.
     * @throws ProjectGetFailedException
     * @throws SQLException
     */
    protected List<Project> get(Connection connection)
            throws ProjectGetFailedException, SQLException {
        
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Entering Projects.get()");
        if (connection == null) {
            throw new ProjectGetFailedException("Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Project> projects = new ArrayList<>();
        
        try {
            
            String sql = "select * from projects";
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                projects.add(new Project(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    rs.getInt(5)));
            }
            
        }
        catch (SQLException ex) {
            throw new ProjectGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Leaving Projects.get()");
        return projects;
        
    }
    
    /**
     * Gets a Project from the database.
     * 
     * @param connection Open database connection
     * @param projectId ID whose project we want.
     * 
     * @return shared.model.Project object with the requested information.
     * @throws ProjectGetFailedException
     * @throws SQLException
     */
    protected Project get(Connection connection, int projectId)
            throws ProjectGetFailedException, SQLException {
        
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Entering Projects.get()");
        if (connection == null) {
            throw new ProjectGetFailedException("Database connection has not been initialized.");
        }
        if (projectId < 1) {
            throw new ProjectGetFailedException("%d is an invalid project ID.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Project project = null;
        
        try {
            
            String sql = "select * from projects where projectId = ?";
            stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, projectId);
            rs = stmt.executeQuery();
            
            int j = 0;
            while (rs.next()) {
                project = new Project(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    rs.getInt(5));
                ++j;
            }
            if (j > 1) {
                throw new ProjectGetFailedException(
                        String.format("Only one Project should have been returned. Found %d", j));
            }
            
        }
        catch (SQLException ex) {
            throw new ProjectGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Leaving Projects.get()");
        return project;
        
    }
    
    
    protected void delete(Connection connection, int projectId)
            throws ProjectDeleteFailedException, SQLException {
        
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Entering Projects.delete()");
        if (connection == null) {
            throw new ProjectDeleteFailedException(
                    "Database connection has not been initialized.");
        }
        if (projectId < 1) {
            throw new ProjectDeleteFailedException(
                    String.format("Invalid project id: %d", projectId));
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = String.format("delete from projects where projectId=%d", projectId);
            stmt = connection.prepareStatement(sql);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new ProjectDeleteFailedException(String.format(
                        "ID %d does not exist in the 'projects' table.", projectId));
            }
        }
        catch (SQLException ex) {
            throw new ProjectDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Projects.class.getName()).log(Level.FINE, "Leaving Projects.delete()");
        
    }
    
}
