package server.database;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.*;
import shared.model.Field;

/**
 * Database Access class for the fields table.
 * 
 * @author schuyler
 */
public class Fields {
    
    public static class FieldInsertFailedException extends Database.InsertFailedException {
        public FieldInsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class FieldUpdateFailedException extends Database.UpdateFailedException {
        public FieldUpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class FieldGetFailedException extends Database.GetFailedException {
        public FieldGetFailedException(String message) {
            super(message);
        }
    }
    
    public static class FieldDeleteFailedException extends Database.DeleteFailedException {
        public FieldDeleteFailedException(String message) {
            super(message);
        }
    }
    
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
    protected Field insert(Connection connection, Field newField)
            throws SQLException, FieldInsertFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.insert()");
        if (connection == null) {
            throw new FieldInsertFailedException("Database connection has not been initialized.");
        }
        PreparedStatement stmt = null;
        Statement kStmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO fields ");
            sql.append("(title, xCoordinate, width, helpHtml, columnNumber, projectId");
            if (newField.knownData() != null) {
                sql.append(", knownData");
            }
            sql.append(") VALUES (?, ?, ?, ?, ?, ?");
            if (newField.knownData() != null) {
                sql.append(", ?");
            }
            sql.append(")");
            stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, newField.title());
            stmt.setInt(2, newField.xCoordinate());
            stmt.setInt(3, newField.width());
            stmt.setString(4, newField.helpHtml().toString());
            stmt.setInt(5, newField.columnNumber());
            stmt.setInt(6, newField.projectId());
            if (newField.knownData() != null) {
                stmt.setString(7, newField.knownData().toString());
            }
            
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                int id = rs.getInt(1);
                newField.setFieldId(id);
            }
            else {
                throw new FieldInsertFailedException("Field was not inserted and no SQLException was thrown.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Fields.class.getName()).log(Level.INFO, "Field might already exist in the database.");
            throw new FieldInsertFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (kStmt != null) kStmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.insert()");
        return newField;

    }
    
    /**
     * Updates a Field in the database.
     * 
     * @param field shared.model.Field object to update in the database.
     */
    protected void update(Connection connection, Field field)
            throws SQLException, FieldUpdateFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.update()");
        if (connection == null) {
            throw new FieldUpdateFailedException("Database connection has not been initialized.");
        }
        if (field.fieldId() < 1) {
            throw new FieldUpdateFailedException("No field ID found in input Field parameter.");
        }
        PreparedStatement stmt = null;
            
        try {
            StringBuilder sql = new StringBuilder();

            if (field.title() != null) {
                sql.append(" title=\"").append(field.title()).append("\"");
            }
            if (field.xCoordinate() != -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" xCoordinate=").append(field.xCoordinate());
            }
            if (field.width() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" width=").append(field.width());
            }
            if (field.helpHtml() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" helpHtml=\"").append(field.helpHtml().toString()).append("\"");
            }
            if (field.columnNumber() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" columnNumber=").append(field.columnNumber());
            }
            if (field.projectId() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" projectId=").append(field.projectId());
            }
            if (field.knownData() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" knownData=\"").append(field.knownData().toString()).append("\"");
            }
            if (sql.length() > 0) {
                sql.insert(0, "update fields set");
                sql.append(" where fieldId = ").append(field.fieldId());
            }
            else {
                Logger.getLogger(Field.class.getName()).log(
                        Level.WARNING, "Attempted update with no updatable information.");
                throw new FieldUpdateFailedException("No information given to update.");
            }
            
            stmt = connection.prepareStatement(sql.toString());

            if (stmt.executeUpdate() != 1) {
                throw new FieldUpdateFailedException(String.format("ID %d not found in 'fields' table.", field.fieldId()));
            }
        }
        catch (SQLException ex) {
            throw new FieldUpdateFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.update()");
        
    }
    
    /**
     * Gets all Fields from the database.
     * 
     * @param connection Open database connection
     * 
     * @return shared.model.Field object with the requested data.
     * @throws FieldGetFailedException
     * @throws SQLException
     */
    protected List<Field> get(Connection connection)
            throws SQLException, FieldGetFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.get()");
        if (connection == null) {
            throw new FieldGetFailedException("Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Field> fields = new ArrayList<>();
        
        try {
            
            String sql = "select * from fields";
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Field field = new Field(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    new URL(rs.getString(5)), rs.getInt(6),
                                    rs.getInt(7));
                if (!rs.getString(8).isEmpty()) {
                    field.setKnownData(new URL(rs.getString(8)));
                }
                fields.add(field);
            }
            
        }
        catch (SQLException | MalformedURLException ex) {
            throw new FieldGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.get()");
        return fields;
        
    }
    
    /**
     * Gets a Field from the database.
     * 
     * @param connection Open database connection
     * @param fieldId Field ID to get from the database.
     * 
     * @return shared.model.Field object with the requested data.
     * @throws FieldGetFailedException
     * @throws SQLException
     */
    protected Field get(Connection connection, int fieldId)
            throws SQLException, FieldGetFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.get()");
        if (connection == null) {
            throw new FieldGetFailedException("Database connection has not been initialized.");
        }
        if (fieldId < 1) {
            throw new FieldGetFailedException("%d is an invalid field ID.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Field field = null;
        
        try {
            
            String sql = "select * from fields where fieldId = ?";
            stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, fieldId);
            rs = stmt.executeQuery();
            
            int j = 0;
            while (rs.next()) {
                field = new Field(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    new URL(rs.getString(5)), rs.getInt(6),
                                    rs.getInt(7));
                if (!rs.getString(8).isEmpty()) {
                    field.setKnownData(new URL(rs.getString(8)));
                }
                ++j;
            }
            if (j > 1) {
                throw new FieldGetFailedException(
                        String.format("Only one Field should have been returned. Found %d", j));
            }
            
        }
        catch (SQLException | MalformedURLException ex) {
            throw new FieldGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.get()");
        return field;
        
    }
    
    /**
     * Gets a Field from the database by the project id and column number.
     * 
     * @param connection Open database connection
     * @param projectId Project ID to which the requested field belongs
     * @param columnNumber the column in the project in which this field is found
     * @return shared.model.Field object with the requested data.
     */
    protected Field get(Connection connection, int projectId, int columnNumber)
            throws SQLException, FieldGetFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.get()");
        if (connection == null) {
            throw new FieldGetFailedException("Database connection has not been initialized.");
        }
        if (projectId < 1 || columnNumber < 1) {
            throw new FieldGetFailedException("Invalid input IDs.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Field field = null;
        
        try {
            
            String sql = "select * from fields where projectId = ? and columnNumber = ?";
            stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, projectId);
            stmt.setInt(2, columnNumber);
            rs = stmt.executeQuery();
            
            int j = 0;
            while (rs.next()) {
                field = new Field(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    new URL(rs.getString(5)), rs.getInt(6),
                                    rs.getInt(7));
                if (!rs.getString(8).isEmpty()) {
                    field.setKnownData(new URL(rs.getString(8)));
                }
                ++j;
            }
            if (j > 1) {
                throw new FieldGetFailedException(
                        String.format("Only one Field should have been returned. Found %d", j));
            }
            
        }
        catch (SQLException | MalformedURLException ex) {
            throw new FieldGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.get()");
        return field;
        
    }
    
    /**
     * Deletes a Field from the database.
     * 
     * @param deleteModel The model class to delete.
     */
    protected void delete(Connection connection, int fieldId)
            throws FieldDeleteFailedException, SQLException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.delete()");
        if (connection == null) {
            throw new FieldDeleteFailedException("Database connection has not been initialized.");
        }
        if (fieldId < 1) {
            throw new FieldDeleteFailedException(String.format("Invalid field id: %d", fieldId));
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = String.format("delete from fields where fieldId=%d", fieldId);
            stmt = connection.prepareStatement(sql);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new FieldDeleteFailedException(String.format("ID %d does not exist in the 'fields' table.",
                        fieldId));
            }
        }
        catch (SQLException ex) {
            throw new FieldDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Leaving Fields.delete()");
        
    }
    
}
