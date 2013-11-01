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
import org.junit.rules.ExpectedException;
import server.database.Projects.*;
import shared.model.Project;
import static org.junit.Assert.*;

/**
 *
 * @author schuyler
 */
public class ProjectsTest {

    static {
        try {
            Database.initialize();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Projects projects;
    
    private Connection connection;
    
    private ArrayList<Project> projectList;
    
    public ProjectsTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            projects = new Projects();
            projectList = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProjectsTest.class.getName()).log(Level.SEVERE, null, ex);
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
            assertEquals(p01, p02);
        }
        catch (SQLException | Projects.ProjectInsertFailedException ex) {
            fail(ex.getMessage());
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
            fail(ex.getMessage());
        }
    }
    
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Project base = new Project("Hoop", 1, 2, 3);
            Project p0 = new Project("Boop", 0, 2, -1);
            
            insertProject(base);
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
            
            assertEquals(p0.projectId(), p02.projectId());
            assertEquals(p0.title(), p02.title());
            assertEquals(base.recordCount(), p02.recordCount()); // Null and 0 values not updated
            assertEquals(p0.firstYCoord(), p02.firstYCoord());
            assertEquals(base.fieldHeight(), p02.fieldHeight()); // Null and 0 values not updated
            
        }
        catch (SQLException | Projects.ProjectUpdateFailedException ex) {
            fail(ex.getMessage());
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
            fail(ex.getMessage());
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
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Project base = new Project("Hoop", 1, 2, 3);
            insertProject(base);
            Project get = new Project(base.projectId(), null, -1, -1, -1);

            // Returns a correct result
            projectList.addAll(projects.get(connection, get));
            assertEquals(1, projectList.size());
            assertEquals(projectList.get(0), base);
            projectList.clear();
            
            // Returns null if not found
            get.setProjectId(base.projectId() + 1);
            projectList.addAll(projects.get(connection, get));
            assertEquals(0, projectList.size());
            
        }
        catch (SQLException | ProjectGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            projectList.clear();
        }
    }
    
    @Test
    public void delete() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Project base = new Project("Hoop", 1, 2, 3);
            insertProject(base);
            
            int rightId = base.projectId();
            
            projects.delete(connection, rightId);
            
            String sql = "select * from projects where projectId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fail("Query returned results, none should exist.");
            }
            
        }
        catch (ProjectDeleteFailedException | SQLException ex) {
            fail(ex.getMessage());
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
            fail(ex.getMessage());
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
            fail(ex.getMessage());
        }
    }
    
    private void insertProject(Project project) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement kStmt = null;
        
        try {
            String insertSql = "insert into projects (title, recordCount, firstYCoord, fieldHeight) "
                    + "values (?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertSql);
            stmt.setString(1, project.title());
            stmt.setInt(2, project.recordCount());
            stmt.setInt(3, project.firstYCoord());
            stmt.setInt(4, project.fieldHeight());
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                project.setProjectId(rs.getInt(1));
            }
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (kStmt != null) kStmt.close();
        }
    }

}