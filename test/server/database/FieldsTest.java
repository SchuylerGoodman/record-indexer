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
import server.database.Fields.*;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class FieldsTest {
    
    private Fields fields;
    
    private Connection connection;
    
    private ArrayList<Field> fieldList;
    
    private static String databasePath = "db" + File.separator + "test" + File.separator
                + "test-record-indexer.sqlite";
    
    private static String createStatementsPath = "db" + File.separator + "DatabaseCreate.sql";
    
    public FieldsTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            fields = new Fields();
            fieldList = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FieldsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void setUpClass() {
        try {
            File databaseFile = new File(databasePath);
            File createStatementFile = new File(createStatementsPath);
            DatabaseCreator dbc = new DatabaseCreator();
            dbc.createDatabase(databaseFile, createStatementFile);
        }
        catch (SQLException ex) {
            Logger.getLogger(FieldsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(FieldsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() throws SQLException {

        if (connection != null) {
            Statement stmt = null;
            try {
                connection.rollback();
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from fields");
                connection.commit();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(FieldsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Field u0 = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            Field u01 = fields.insert(connection, u0);
            String sql = "select * from fields where fieldId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, u01.fieldId());
            rs = stmt.executeQuery();
            Field u02 = new Field();
            while (rs.next()) {
                u02.setFieldId(rs.getInt(1));
                u02.setTitle(rs.getString(2));
                u02.setXCoordinate(rs.getInt(3));
                u02.setWidth(rs.getInt(4));
                u02.setHelpHtml(rs.getString(5));
                u02.setColumnNumber(rs.getInt(6));
                u02.setProjectId(rs.getInt(7));
                u02.setKnownData(rs.getString(8));
            }
            Assert.assertEquals(u01, u02);
        }
        catch (SQLException
                | Fields.FieldInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsFieldInsertFailedExceptionIfInsertingDuplicate()
            throws FieldInsertFailedException {
        try {
            Field u0 = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            Field u1 = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            fields.insert(connection, u0);
            exception.expect(FieldInsertFailedException.class);
            fields.insert(connection, u1);
        } catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Field base = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            Field u0 = new Field("Age", 2, 3, null, 4, 0, "data2.html");
            
            base = fields.insert(connection, base);
            u0.setFieldId(base.fieldId());
            fields.update(connection, u0);
            
            String sql = "select * from fields where fieldId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.fieldId());
            u0.setFieldId(base.fieldId());
            rs = stmt.executeQuery();
            
            Field u02 = new Field();
            while (rs.next()) {
                u02.setFieldId(rs.getInt(1));
                u02.setTitle(rs.getString(2));
                u02.setXCoordinate(rs.getInt(3));
                u02.setWidth(rs.getInt(4));
                u02.setHelpHtml(rs.getString(5));
                u02.setColumnNumber(rs.getInt(6));
                u02.setProjectId(rs.getInt(7));
                u02.setKnownData(rs.getString(8));
            }
            
            Assert.assertEquals(u0.fieldId(), u02.fieldId());
            Assert.assertEquals(u0.title(), u02.title());
            Assert.assertEquals(u0.xCoordinate(), u02.xCoordinate());
            Assert.assertEquals(u0.width(), u02.width());
            Assert.assertEquals(base.helpHtml(), u02.helpHtml()); // Null and 0 values not updated
            Assert.assertEquals(u0.columnNumber(), u02.columnNumber());
            Assert.assertEquals(base.projectId(), u02.projectId()); // Null and 0 values not updated
            Assert.assertEquals(u0.knownData(), u02.knownData());
            
        }
        catch (SQLException
                    | Fields.FieldUpdateFailedException
                    | FieldInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsFieldUpdateFailedExceptionWithNoFieldId()
            throws FieldUpdateFailedException {
        try {
            Field u0 = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            exception.expect(FieldUpdateFailedException.class);
            exception.expectMessage("No field ID found in input Field parameter.");
            fields.update(connection, u0);
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsFieldUpdateFailedExceptionIdNotFound()
            throws FieldUpdateFailedException {
        try {
            Field u0 = new Field(100, "Name", 1, 2, "help.html", 3, 4, "data.html");
            exception.expect(FieldUpdateFailedException.class);
            exception.expectMessage("ID 100 not found in 'fields' table.");
            fields.update(connection, u0);
        } catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Field base = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            base = fields.insert(connection, base);
            Field get = new Field();
            get.setFieldId(base.fieldId());
            
            // Returns a correct result
            fieldList.addAll(fields.get(connection, get));
            Assert.assertEquals(1, fieldList.size());
            Assert.assertEquals(fieldList.get(0), base);
            fieldList.clear();
            
            // Returns null if not found
            get.setFieldId(base.fieldId() + 1);
            fieldList.addAll(fields.get(connection, get));
            Assert.assertEquals(0, fieldList.size());
            
        } catch (SQLException
                | FieldInsertFailedException
                | FieldGetFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            fieldList.clear();
        }
    }

    @Test
    public void getWithProjectIdAndColumnNumber() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Field base = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            base = fields.insert(connection, base);
            Field get = new Field();
            get.setProjectId(base.projectId());
            get.setColumnNumber(base.columnNumber());
            
            // Returns a correct result
            fieldList.addAll(fields.get(connection, get));
            Assert.assertEquals(1, fieldList.size());
            Assert.assertEquals(fieldList.get(0), base);
            fieldList.clear();
            
            // Returns null if not found
            get.setProjectId(base.projectId() + 1);
            fieldList.addAll(fields.get(connection, get));
            Assert.assertEquals(0, fieldList.size());
            
        } catch (SQLException
                | FieldInsertFailedException
                | FieldGetFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            fieldList.clear();
        }
    }
    
    @Test
    public void delete() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Field base = new Field("Name", 1, 2, "help.html", 3, 4, "data.html");
            base = fields.insert(connection, base);
            
            int rightId = base.fieldId();
            
            fields.delete(connection, rightId);
            
            String sql = "select * from fields where fieldId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Assert.fail("Should have been deleted.");
            }
            
        }
        catch (FieldDeleteFailedException
                | SQLException
                | FieldInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsFieldDeleteFailedExceptionInvalidInputId()
            throws FieldDeleteFailedException {
        try {
            int id = 0;
            exception.expect(FieldDeleteFailedException.class);
            exception.expectMessage(String.format("Invalid field id: %d", id));
            fields.delete(connection, id);
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsFieldDeleteFailedExceptionFieldDoesNotExist()
            throws FieldDeleteFailedException {
        try {
            int id = 1;
            exception.expect(FieldDeleteFailedException.class);
            exception.expectMessage(String.format(
                    "ID %d does not exist in the 'fields' table.", id));
            fields.delete(connection, id);
            
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }

}