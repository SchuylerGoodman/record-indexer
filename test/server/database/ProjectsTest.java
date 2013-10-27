/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import server.database.tools.DatabaseCreator;
import java.io.File;
import java.sql.*;
import java.util.logging.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import server.database.Projects.*;
import shared.model.Project;

/**
 *
 * @author schuyler
 */
public class ProjectsTest {
    
    private Projects projects;
    
    private Connection connection;
    
    private static String databasePath = "db" + File.separator + "test" + File.separator
                + "test-record-indexer.sqlite";
    
    private static String createStatementsPath = "db" + File.separator + "DatabaseCreate.sql";
    
    public ProjectsTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            projects = new Projects();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() throws SQLException {

        if (connection != null) {
            Statement stmt = null;
            try {
                connection.rollback();
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from projects");
                connection.commit();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Project p0 = new Project("Hoop", 1, 2, 3);
            Project p01 = projects.insert(connection, p0);
            String sql = "select * from projects where projectId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, p01.projectId());
            rs = stmt.executeQuery();
            Project p02 = new Project();
            while (rs.next()) {
                p02.setProjectId(rs.getInt(1));
                p02.setTitle(rs.getString(2));
                p02.setRecordCount(rs.getInt(3));
                p02.setFirstYCoord(rs.getInt(4));
                p02.setFieldHeight(rs.getInt(5));
            }
            Assert.assertEquals(p01, p02);
        }
        catch (SQLException | Projects.ProjectInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsProjectInsertFailedExceptionIfInsertingDuplicate()
            throws ProjectInsertFailedException {
        try {
            Project p0 = new Project("Hoop", 1, 2, 3);
            Project p1 = new Project("Hoop", 1, 2, 3);
            projects.insert(connection, p0);
            exception.expect(ProjectInsertFailedException.class);
            projects.insert(connection, p1);
        } catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Project base = new Project("Hoop", 1, 2, 3);
            Project p0 = new Project("Boop", 0, 2, -1);
            
            base = projects.insert(connection, base);
            p0.setProjectId(base.projectId());
            projects.update(connection, p0);
            
            String sql = "select * from projects where projectId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.projectId());
            p0.setProjectId(base.projectId());
            rs = stmt.executeQuery();
            
            Project p02 = new Project();
            while (rs.next()) {
                p02.setProjectId(rs.getInt(1));
                p02.setTitle(rs.getString(2));
                p02.setRecordCount(rs.getInt(3));
                p02.setFirstYCoord(rs.getInt(4));
                p02.setFieldHeight(rs.getInt(5));
            }
            
            Assert.assertEquals(p0.projectId(), p02.projectId());
            Assert.assertEquals(p0.title(), p02.title());
            Assert.assertEquals(base.recordCount(), p02.recordCount()); // Null and 0 values not updated
            Assert.assertEquals(p0.firstYCoord(), p02.firstYCoord());
            Assert.assertEquals(base.fieldHeight(), p02.fieldHeight()); // Null and 0 values not updated
            
        }
        catch (SQLException
                    | Projects.ProjectUpdateFailedException
                    | ProjectInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsProjectUpdateFailedExceptionWithNoProjectId()
            throws ProjectUpdateFailedException {
        try {
            Project p0 = new Project("Hoop", 1, 2, 3);
            exception.expect(ProjectUpdateFailedException.class);
            exception.expectMessage("No project ID found in input Project parameter.");
            projects.update(connection, p0);
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsProjectUpdateFailedExceptionIdNotFound()
            throws ProjectUpdateFailedException {
        try {
            Project p0 = new Project(100, "Hoop", 1, 2, 3);
            exception.expect(ProjectUpdateFailedException.class);
            exception.expectMessage("ID 100 not found in 'projects' table.");
            projects.update(connection, p0);
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Project base = new Project("Hoop", 1, 2, 3);
            base = projects.insert(connection, base);

            // Returns a correct result
            int id = base.projectId();
            Project result = projects.get(connection, id);
            
            Assert.assertEquals(result, base);
            
            // Returns null if not found
            ++id;
            result = projects.get(connection, id);
            
            Assert.assertNull(result);
            
        }
        catch (SQLException
                | ProjectInsertFailedException
                | ProjectGetFailedException ex) {
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
            Project base = new Project("Hoop", 1, 2, 3);
            base = projects.insert(connection, base);
            
            int rightId = base.projectId();
            
            projects.delete(connection, rightId);
            
            String sql = "select * from projects where projectId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Assert.fail("Query returned results, none should exist.");
            }
            
        }
        catch (ProjectDeleteFailedException
                | SQLException
                | ProjectInsertFailedException ex) {
            Assert.fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsProjectDeleteFailedExceptionInvalidInputId()
            throws ProjectDeleteFailedException {
        try {
            int id = 0;
            exception.expect(ProjectDeleteFailedException.class);
            exception.expectMessage(String.format("Invalid project id: %d", id));
            projects.delete(connection, id);
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsProjectDeleteFailedExceptionProjectDoesNotExist()
            throws ProjectDeleteFailedException {
        try {
            int id = 1;
            exception.expect(ProjectDeleteFailedException.class);
            exception.expectMessage(String.format(
                    "ID %d does not exist in the 'projects' table.", id));
            projects.delete(connection, id);
            
        }
        catch (SQLException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
}