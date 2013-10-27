/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import server.database.tools.DatabaseCreator;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
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
    
    private static String databasePath = "db" + File.separator + "test" + File.separator
                + "test-record-indexer.sqlite";
    
    private static String createStatementsPath = "db" + File.separator + "DatabaseCreate.sql";
    
    public FieldsTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            fields = new Fields();
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
            Field u0 = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
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
                u02.setHelpHtml(new URL(rs.getString(5)));
                u02.setColumnNumber(rs.getInt(6));
                u02.setProjectId(rs.getInt(7));
                u02.setKnownData(new URL(rs.getString(8)));
            }
            Assert.assertEquals(u01, u02);
        }
        catch (SQLException
                | Fields.FieldInsertFailedException
                | MalformedURLException ex) {
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
            Field u0 = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            Field u1 = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            fields.insert(connection, u0);
            exception.expect(FieldInsertFailedException.class);
            fields.insert(connection, u1);
        } catch (SQLException | MalformedURLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Field base = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            Field u0 = new Field("Age", 2, 3, null, 4, 0, new URL("http://hooper.com/data2"));
            
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
                u02.setHelpHtml(new URL(rs.getString(5)));
                u02.setColumnNumber(rs.getInt(6));
                u02.setProjectId(rs.getInt(7));
                u02.setKnownData(new URL(rs.getString(8)));
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
                    | FieldInsertFailedException
                    | MalformedURLException ex) {
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
            Field u0 = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            exception.expect(FieldUpdateFailedException.class);
            exception.expectMessage("No field ID found in input Field parameter.");
            fields.update(connection, u0);
        }
        catch (SQLException | MalformedURLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsFieldUpdateFailedExceptionIdNotFound()
            throws FieldUpdateFailedException {
        try {
            Field u0 = new Field(100, "Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            exception.expect(FieldUpdateFailedException.class);
            exception.expectMessage("ID 100 not found in 'fields' table.");
            fields.update(connection, u0);
        } catch (SQLException | MalformedURLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Field base = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            base = fields.insert(connection, base);

            // Returns a correct result
            int id = base.fieldId();
            Field result = fields.get(connection, id);

            Assert.assertEquals(result, base);
            
            // Returns null if not found
            ++id;
            result = fields.get(connection, id);
            
            Assert.assertNull(result);
            
        } catch (SQLException
                | FieldInsertFailedException
                | FieldGetFailedException
                | MalformedURLException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }

    @Test
    public void getWithProjectIdAndColumnNumber() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Field base = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
            base = fields.insert(connection, base);

            // Returns a correct result
            int projectId = base.projectId();
            int columnNumber = base.columnNumber();
            Field result = fields.get(connection, projectId, columnNumber);

            Assert.assertEquals(result, base);
            
            // Returns null if not found
            ++projectId;
            result = fields.get(connection, projectId);
            
            Assert.assertNull(result);
            
        } catch (SQLException
                | FieldInsertFailedException
                | FieldGetFailedException
                | MalformedURLException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void delete() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Field base = new Field("Name", 1, 2, new URL("http://hooper.com/help"), 3, 4, new URL("http://hooper.com/data"));
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
                | FieldInsertFailedException
                | MalformedURLException ex) {
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