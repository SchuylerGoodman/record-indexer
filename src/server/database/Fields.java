package server.database;

import java.sql.*;
import java.util.ArrayList;
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
            stmt.setString(4, newField.helpHtml());
            stmt.setInt(5, newField.columnNumber());
            stmt.setInt(6, newField.projectId());
            if (newField.knownData() != null) {
                stmt.setString(7, newField.knownData());
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
            if (field.xCoordinate() > -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" xCoordinate=").append(field.xCoordinate());
            }
            if (field.width() > -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" width=").append(field.width());
            }
            if (field.helpHtml() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" helpHtml=\"").append(field.helpHtml()).append("\"");
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
                sql.append(" knownData=\"").append(field.knownData()).append("\"");
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
     * Gets all matching Fields from the database.
     * 
     * @param connection Open database connection
     * @param field Field object - all initialized information will be used to
     * get all matching fields from the database. Calling this function with the
     * empty Field constructor will return all fields from the database.
     * 
     * @return shared.model.Field object with the requested data.
     * @throws FieldGetFailedException
     * @throws SQLException
     */
    protected List<Field> get(Connection connection, Field field)
            throws SQLException, FieldGetFailedException {
        
        Logger.getLogger(Fields.class.getName()).log(Level.FINE, "Entering Fields.get()");
        if (connection == null) {
            throw new FieldGetFailedException("Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Field> fields = new ArrayList<>();
        
        try {
            
            StringBuilder sql = new StringBuilder();
            StringBuilder wheres = new StringBuilder();
            sql.append("select * from fields");

            if (field.fieldId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                wheres.append("fieldId=").append(field.fieldId());
            }
            if (field.title() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("title=\"").append(field.title()).append("\"");
            }
            if (field.xCoordinate() > -1) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("xCoordinate=").append(field.xCoordinate());
            }
            if (field.width() > -1) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("width=").append(field.width());
            }
            if (field.helpHtml() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("helpHtml=\"").append(field.helpHtml()).append("\"");
            }
            if (field.columnNumber() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("columnNumber=").append(field.columnNumber());
            }
            if (field.projectId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("projectId=").append(field.projectId());
            }
            if (field.knownData() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("knownData=\"").append(field.knownData()).append("\"");
            }
            sql.append(wheres);
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            int j = 0;
            while (rs.next()) {
                fields.add(new Field(rs.getInt(1), rs.getString(2),
                                    rs.getInt(3), rs.getInt(4),
                                    rs.getString(5), rs.getInt(6),
                                    rs.getInt(7)));
                if (rs.getString(8) != null) {
                    fields.get(j).setKnownData(rs.getString(8));
                }
                ++j;
            }
            
        }
        catch (SQLException ex) {
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
