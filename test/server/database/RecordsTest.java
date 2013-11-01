/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import server.database.tools.DatabaseCreator;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import org.junit.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import server.database.Records.*;
import shared.model.Record;
import static org.junit.Assert.*;

/**
 *
 * @author schuyler
 */
public class RecordsTest {

    static {
        try {
            Database.initialize();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(RecordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Records records;
    
    private Connection connection;
    
    private ArrayList<Record> recordList;
    
    public RecordsTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            records = new Records();
            recordList = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void setUpClass() {
        try {
            File databaseFile = new File(server.ServerUnitTests.TEST_DATABASE_PATH);
            File createStatementFile = new File(server.database.Database.STATEMENT_PATH);
            DatabaseCreator dbc = new DatabaseCreator();
            dbc.createDatabase(databaseFile, createStatementFile);
        }
        catch (SQLException ex) {
            Logger.getLogger(RecordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + server.ServerUnitTests.TEST_DATABASE_PATH);
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(RecordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() throws SQLException {

        if (connection != null) {
            Statement stmt = null;
            try {
                connection.rollback();
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from records");
                connection.commit();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecordsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                if (stmt != null) stmt.close();
            }
        }
    }
    
    @Test
    public void insert() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record u0 = new Record(1, 2, 3, "value");
            Record u01 = records.insert(connection, u0);
            String sql = "select * from records where recordId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, u01.recordId());
            rs = stmt.executeQuery();
            Record u02 = new Record();
            while (rs.next()) {
                u02.setRecordId(rs.getInt(1));
                u02.setImageId(rs.getInt(2));
                u02.setFieldId(rs.getInt(3));
                u02.setRowNumber(rs.getInt(4));
                u02.setValue(rs.getString(5));
            }
            assertEquals(u01, u02);
        }
        catch (SQLException | Records.RecordInsertFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsRecordInsertFailedExceptionIfInsertingDuplicate()
            throws RecordInsertFailedException {
        try {
            Record u0 = new Record(1, 2, 3, "value");
            Record u1 = new Record(1, 2, 3, "value");
            records.insert(connection, u0);
            exception.expect(RecordInsertFailedException.class);
            records.insert(connection, u1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void updateUseRecordId() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(1, 2, 3, "value");
            Record u0 = new Record(0, 0, 0, "next value");
            
            insertRecord(base);
            u0.setRecordId(base.recordId());
            records.update(connection, u0);
            
            String sql = "select * from records where recordId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.recordId());
            u0.setRecordId(base.recordId());
            rs = stmt.executeQuery();
            
            Record u02 = new Record();
            while (rs.next()) {
                u02.setRecordId(rs.getInt(1));
                u02.setImageId(rs.getInt(2));
                u02.setFieldId(rs.getInt(3));
                u02.setRowNumber(rs.getInt(4));
                u02.setValue(rs.getString(5));
            }
            
            assertEquals(u0.recordId(), u02.recordId());
            assertEquals(base.imageId(), u02.imageId());
            assertEquals(base.fieldId(), u02.fieldId());
            assertEquals(base.rowNumber(), u02.rowNumber());
            assertEquals(u0.value(), u02.value());
            
        }
        catch (SQLException | Records.RecordUpdateFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void updateUseImageFieldIdsAndRowNumber() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(1, 2, 3, "value");
            Record u0 = new Record(1, 2, 3, "next value");
            
            insertRecord(base);
//            u0.setRecordId(base.recordId());
            records.update(connection, u0);
            
            String sql = "select * from records where recordId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.recordId());
            u0.setRecordId(base.recordId());
            rs = stmt.executeQuery();
            
            Record u02 = new Record();
            while (rs.next()) {
                u02.setRecordId(rs.getInt(1));
                u02.setImageId(rs.getInt(2));
                u02.setFieldId(rs.getInt(3));
                u02.setRowNumber(rs.getInt(4));
                u02.setValue(rs.getString(5));
            }
            
            assertEquals(base.recordId(), u02.recordId());
            assertEquals(u0.imageId(), u02.imageId());
            assertEquals(u0.fieldId(), u02.fieldId());
            assertEquals(u0.rowNumber(), u02.rowNumber());
            assertEquals(u0.value(), u02.value());
            
        }
        catch (SQLException | Records.RecordUpdateFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsRecordUpdateFailedExceptionWithNoIds()
            throws RecordUpdateFailedException {
        try {
            Record u0 = new Record(0, 0, 0, "value");
            exception.expect(RecordUpdateFailedException.class);
            exception.expectMessage(
                    "No way to find this Record in the database with the given information.");
            records.update(connection, u0);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsRecordUpdateFailedExceptionWithNoValue()
            throws RecordUpdateFailedException {
        try {
            Record u0 = new Record(1, 2, 3, null);
            exception.expect(RecordUpdateFailedException.class);
            exception.expectMessage("No information given to update.");
            records.update(connection, u0);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsRecordUpdateFailedExceptionIdNotFound()
            throws RecordUpdateFailedException {
        try {
            Record u0 = new Record(100, 1, 2, 3, "value");
            exception.expect(RecordUpdateFailedException.class);
            exception.expectMessage("Record was not found or could not be updated.");
            records.update(connection, u0);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void getUseRecordId() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(0, 0, 0, "value");
            insertRecord(base);
            Record get = new Record();
            get.setRecordId(base.recordId());

            // Returns a correct result
            recordList.addAll(records.get(connection, get));
            assertEquals(1, recordList.size());
            assertEquals(recordList.get(0), base);
            recordList.clear();
            
            // Returns null if not found
            get.setRecordId(base.recordId() + 1);
            recordList.addAll(records.get(connection, get));
            assertEquals(0, recordList.size());
            
        }
        catch (SQLException | RecordGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            recordList.clear();
        }
    }
    
    @Test
    public void getUseImageFieldIdsAndRowNumber() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(1, 2, 3, "value");
            insertRecord(base);
            Record get = new Record();
            get.setImageId(base.imageId());
            get.setFieldId(base.fieldId());
            get.setRowNumber(base.rowNumber());

            // Returns a correct result
            recordList.addAll(records.get(connection, get));
            assertEquals(1, recordList.size());
            assertEquals(recordList.get(0), base);
            recordList.clear();
            
            // Returns null if not found
            get.setImageId(base.imageId() + 1);
            recordList.addAll(records.get(connection, get));
            assertEquals(0, recordList.size());
            
        }
        catch (SQLException | RecordGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            recordList.clear();
        }
    }
    
    @Test
    public void deleteUseRecordId() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(0, 0, 0, "value");
            insertRecord(base);
            
            int rightId = base.recordId();
            
            records.delete(connection, rightId);
            
            String sql = "select * from records where recordId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fail("Should have been deleted.");
            }
            
        }
        catch (RecordDeleteFailedException | SQLException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void deleteUseImageFieldIdsAndRowNumber() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Record base = new Record(1, 2, 3, "value");
            insertRecord(base);
            
            int imageId = base.imageId();
            int fieldId = base.fieldId();
            int rowNumber = base.rowNumber();
            
            records.delete(connection, imageId, fieldId, rowNumber);
            
            String sql = "select * from records where recordId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.recordId());
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fail("Should have been deleted.");
            }
            
        }
        catch (RecordDeleteFailedException | SQLException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsRecordDeleteFailedExceptionInvalidInputRecordId()
            throws RecordDeleteFailedException {
        try {
            int id = 0;
            exception.expect(RecordDeleteFailedException.class);
            exception.expectMessage(String.format("Invalid record id: %d", id));
            records.delete(connection, id);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsRecordDeleteFailedExceptionInvalidInputImageFieldIdsAndRowNumber()
            throws RecordDeleteFailedException {
        try {
            int imageId = 0;
            int fieldId = 0;
            int rowNumber = 0;
            exception.expect(RecordDeleteFailedException.class);
            exception.expectMessage("One or more of the given IDs is invalid.");
            records.delete(connection, imageId, fieldId, rowNumber);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsRecordDeleteFailedExceptionRecordIdDoesNotExist()
            throws RecordDeleteFailedException {
        try {
            int id = 1;
            exception.expect(RecordDeleteFailedException.class);
            exception.expectMessage(String.format(
                    "ID %d does not exist in the 'records' table.", id));
            records.delete(connection, id);
            
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsRecordDeleteFailedExceptionImageFieldIdsAndRowNumberCombDoesNotExist()
            throws RecordDeleteFailedException {
        try {
            int imageId = 1;
            int fieldId = 1;
            int rowNumber = 1;
            exception.expect(RecordDeleteFailedException.class);
            exception.expectMessage("No record was found with the given IDs");
            records.delete(connection, imageId, fieldId, rowNumber);
            
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    private void insertRecord(Record record) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement kStmt = null;
        
        try {
            String insertSql = "insert into records (imageId, fieldId, rowNumber, value) "
                    + "values (?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertSql);
            stmt.setInt(1, record.imageId());
            stmt.setInt(2, record.fieldId());
            stmt.setInt(3, record.rowNumber());
            stmt.setString(4, record.value());
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                record.setRecordId(rs.getInt(1));
            }
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (kStmt != null) kStmt.close();
        }
    }

}