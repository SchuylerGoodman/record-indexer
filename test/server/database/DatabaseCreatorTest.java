/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import server.database.tools.DatabaseCreator;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author schuyler
 */
public class DatabaseCreatorTest {
    
    DatabaseCreator dbc;
    
    public DatabaseCreatorTest() {
        dbc = new DatabaseCreator();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    /**
     * Makes sure that the DatabaseCreator.testCreateDatabase() method works
     * correctly.
     * <p>
     * It should create a new database, make sure that the schema is correct,
     * and make sure that all tables are dropped on creation.
     * 
     * @throws SQLException 
     */
    @Test
    public void testCreateDatabase() throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        String databasePath = "db" + File.separator + "test" + File.separator
                + "test-create-database.sqlite";
        File newDatabaseFile = new File(databasePath); // Will be deleted
        String statementPath = "db" + File.separator + "DatabaseCreate.sql";
        File statementFile = new File(statementPath);

        try {
            dbc.createDatabase(newDatabaseFile, statementFile);
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            String sql = "select * from fields, images, projects, records, users";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Assert.fail("Database should be empty.");
            }
            String[] statements = new String[5];
            statements[0] = "insert into fields (title, xCoord, width, helpHTML, "
                    + "col, refProjectId) values ('field', 123, 123, 'url', 2, 1)";
            statements[1] = "insert into images (filePath, imageTitle, "
                    + "refProjectId, currentUser) "
                    + "values ('url', 'title', 123, 1)";
            statements[2] = "insert into projects (title, fieldsPerImage, "
                    + "firstYCoord, recordHeight) "
                    + "values ('title', 123, 123, 123)";
            statements[3] = "insert into records (refImageId, refFieldId, "
                    + "rowNumber, value) "
                    + "values (123, 123, 123, 'value')";
            statements[4] = "insert into users (username, first, last, "
                    + "password, email, records) "
                    + "values ('username', 'first', 'last', 'password', "
                    + "'email', 123)";
            for (int i = 0; i < statements.length; ++i) {
                stmt = connection.prepareStatement(statements[i]);
                int result = stmt.executeUpdate();
                if (result != 1) {
                    Assert.fail(String.format("Query (%s) failed in created table", statements[i]));
                }
            }
            
            dbc.createDatabase(newDatabaseFile, statementFile);
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Assert.fail("Database should be empty.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("SQL Error caused by DatabaseCreator");
        }
        finally {
            if (connection != null) connection.close();
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            Assert.assertTrue(newDatabaseFile.delete());
        }
        
    }

}