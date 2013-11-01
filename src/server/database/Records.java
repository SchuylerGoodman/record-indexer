package server.database;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.model.Record;

/**
 * Database Access class for the records table.
 * 
 * @author schuyler
 */
public class Records {
    
    public static class RecordInsertFailedException extends Database.InsertFailedException {
        public RecordInsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class RecordUpdateFailedException extends Database.UpdateFailedException {
        public RecordUpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class RecordGetFailedException extends Database.GetFailedException {
        public RecordGetFailedException(String message) {
            super(message);
        }
    }
    
    public static class RecordDeleteFailedException extends Database.DeleteFailedException {
        public RecordDeleteFailedException(String message) {
            super(message);
        }
    }
    
    public static class RecordSearchFailedException extends Database.DatabaseException {
        public RecordSearchFailedException(String message) {
            super(message);
        }
    }
    
    /**
     * Inserts a new Record into the database.
     * 
     * @param connection open database connection.
     * @param newRecord shared.model.Record with the information to insert.
     * @return shared.model.Record with the generated record ID
     */
    protected Record insert(Connection connection, Record newRecord)
            throws RecordInsertFailedException, SQLException {
        
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.insert()");
        if (connection == null) {
            throw new RecordInsertFailedException("Database connection has not been initialized.");
        }
        PreparedStatement stmt = null;
        Statement kStmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "INSERT INTO records (imageId, fieldId, rowNumber, value)"
                    + "VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql.toString());
            stmt.setInt(1, newRecord.imageId());
            stmt.setInt(2, newRecord.fieldId());
            stmt.setInt(3, newRecord.rowNumber());
            stmt.setString(4, newRecord.value());
            
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                int id = rs.getInt(1);
                newRecord.setRecordId(id);
            }
            else {
                throw new RecordInsertFailedException("Record was not inserted and no SQLException was thrown.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Records.class.getName()).log(Level.INFO, "Record might already exist in the database.");
            throw new RecordInsertFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (kStmt != null) kStmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Leaving Records.insert()");
        return newRecord;

    }

    /**
     * Update a record in the database.
     * 
     * @param connection open database connection.
     * @param record shared.model.Record with the information to update.
     */
    protected void update(Connection connection, Record record)
            throws RecordUpdateFailedException, SQLException {
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.update()");
        if (connection == null) {
            throw new RecordUpdateFailedException(
                    "Database connection has not been initialized.");
        }
        if (record.recordId() < 1
                && (record.imageId() < 1
                    || record.fieldId() < 1
                    || record.rowNumber() < 1)) {
            throw new RecordUpdateFailedException(
                    "No way to find this Record in the database with the given information.");
        }
        PreparedStatement stmt = null;
            
        try {
            
            if (record.value() == null) {
                Logger.getLogger(Record.class.getName()).log(
                        Level.WARNING, "Attempted update with no updatable information.");
                throw new RecordUpdateFailedException("No information given to update.");
            }
            
            String sql = "update records set value=? ";
            if (record.recordId() < 1) {
                sql += String.format(
                        "where imageId=%d and fieldId=%d and rowNumber=%d",
                        record.imageId(), record.fieldId(), record.rowNumber());
            }
            else {
                sql += String.format("where recordId=%d", record.recordId());
            }
            
            stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, record.value());

            if (stmt.executeUpdate() != 1) {
                throw new RecordUpdateFailedException(
                        "Record was not found or could not be updated.");
            }
        }
        catch (SQLException ex) {
            throw new RecordUpdateFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Leaving Records.update()");
        
    }
    
    /**
     * Get all matching Records from the database.
     * 
     * @param connection open database connection.
     * @param record Record object - all initialized information will be used to
     * get all matching records from the database. Calling this function with the
     * empty Record constructor will return all records from the database.
     * 
     * @return List of shared.model.Record objects with the requested information.
     * @throws RecordGetFailedException
     * @throws SQLException
     */
    protected List<Record> get(Connection connection, Record record)
            throws RecordGetFailedException, SQLException {

        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.get()");
        if (connection == null) {
            throw new RecordGetFailedException(
                    "Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Record> records = new ArrayList<>();
        
        try {
            
            StringBuilder sql = new StringBuilder();
            StringBuilder wheres = new StringBuilder();
            sql.append("select * from records");

            if (record.recordId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                wheres.append("recordId=").append(record.recordId());
            }
            if (record.imageId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("imageId=").append(record.imageId());
            }
            if (record.fieldId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("fieldId=").append(record.fieldId());
            }
            if (record.rowNumber() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("rowNumber=").append(record.rowNumber());
            }
            if (record.value() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("value=\'").append(record.value()).append("\'");
            }
            sql.append(wheres);
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(new Record(rs.getInt(1), rs.getInt(2),
                                       rs.getInt(3), rs.getInt(4),
                                       rs.getString(5)));
            }
            
        }
        catch (SQLException ex) {
            throw new RecordGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Leaving Records.get()");
        return records;
        
    }

    /**
     * Deletes a record from the database using the unique record id.
     * 
     * @param connection open database connection.
     * @param recordId ID of the record to delete from the database
     * @throws RecordDeleteFailedException
     * @throws SQLException
     */
    protected void delete(Connection connection, int recordId)
            throws RecordDeleteFailedException, SQLException {
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.delete()");
        if (connection == null) {
            throw new RecordDeleteFailedException("Database connection has not been initialized.");
        }
        if (recordId < 1) {
            throw new RecordDeleteFailedException(String.format("Invalid record id: %d", recordId));
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = String.format("delete from records where recordId=%d", recordId);
            stmt = connection.prepareStatement(sql);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new RecordDeleteFailedException(String.format("ID %d does not exist in the 'records' table.",
                        recordId));
            }
        }
        catch (SQLException ex) {
            throw new RecordDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Leaving Records.delete()");
        
    }
    
    /**
     * Deletes a record from the database indexed by a combination of the imageId, fieldId, and rowNumber.
     * 
     * @param connection open database connection.
     * @param imageId ID of the image to which this record belongs
     * @param fieldId ID of the field to which this record belongs
     * @param rowNumber Row number of this record in the image
     * @throws RecordDeleteFailedException
     * @throws SQLException
     */
    protected void delete(Connection connection, int imageId, int fieldId, int rowNumber)
            throws RecordDeleteFailedException, SQLException {
        
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.delete()");
        if (connection == null) {
            throw new RecordDeleteFailedException("Database connection has not been initialized.");
        }
        if (imageId < 1 || fieldId < 1 || rowNumber < 1) {
            throw new RecordDeleteFailedException("One or more of the given IDs is invalid.");
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = "delete from records where imageId=? and fieldId=? and rowNumber=?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, imageId);
            stmt.setInt(2, fieldId);
            stmt.setInt(3, rowNumber);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new RecordDeleteFailedException(
                        "No record was found with the given IDs");
            }
        }
        catch (SQLException ex) {
            throw new RecordDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Leaving Records.delete()");
        
    }
    
    /**
     * Searches the database for Records matching the given field IDs and matching strings.
     * 
     * @param connection Open database connection
     * @param fieldIds List of field IDs to match
     * @param matches List of Strings to match
     * 
     * @return List of matched Records
     * @throws server.database.Records.RecordDeleteFailedException
     * @throws SQLException 
     */
    public List<Record> search(Connection connection, List<String> fieldIds, List<String> matches)
            throws RecordSearchFailedException, SQLException {
        
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Entering Records.search()");
        if (connection == null) {
            throw new RecordSearchFailedException("Database connection has not been initialized.");
        }
        ArrayList<Record> records = new ArrayList<>();
        if (fieldIds.isEmpty() || matches.isEmpty()) {
            Logger.getLogger(Records.class.getName()).log(
                    Level.INFO, "Leaving Records.search() - One or more input parameters are empty.");
            return records;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            StringBuilder sqlValue = new StringBuilder();
            for (String id : fieldIds) {
                if (sql.length() > 0) {
                    sql.append(" or ");
                }
                sql.append("fieldId=").append(id);
            }
            sql.insert(0, "select * from records where (");
            sql.append(") and (");
            for (String value : matches) {
                if (sqlValue.length() > 0) {
                    sqlValue.append(" or ");
                }
                sqlValue.append("value like \'%").append(value).append("%\'");
            }
            sqlValue.append(")");
            sql.append(sqlValue);
            
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Record newRecord = new Record(rs.getInt(1), rs.getInt(2),
                                              rs.getInt(3), rs.getInt(4),
                                              rs.getString(5));
                records.add(newRecord);
            }
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Records.class.getName()).log(Level.FINE, "Exiting Records.search()");
        return records;
        
    }
    
}