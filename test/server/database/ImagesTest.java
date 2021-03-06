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
import server.database.Images.*;
import shared.model.Image;
import static org.junit.Assert.*;

/**
 *
 * @author schuyler
 */
public class ImagesTest {
    
    static {
        try {
            Database.initialize();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(ImagesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Images images;
    
    private Connection connection;
    
    private ArrayList<Image> imageList;
    
    public ImagesTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            images = new Images();
            imageList = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ImagesTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ImagesTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ImagesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() throws SQLException {

        if (connection != null) {
            Statement stmt = null;
            try {
                connection.rollback();
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from images");
                connection.commit();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ImagesTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Image u0 = new Image("image.png", "image", 1);
            Image u01 = images.insert(connection, u0);
            String sql = "select * from images where imageId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, u01.imageId());
            rs = stmt.executeQuery();
            Image u02 = new Image();
            while (rs.next()) {
                u02.setImageId(rs.getInt(1));
                u02.setPath(rs.getString(2));
                u02.setTitle(rs.getString(3));
                u02.setProjectId(rs.getInt(4));
                u02.setCurrentUser(rs.getInt(5));
            }
            assertEquals(u01, u02);
        }
        catch (SQLException
                | Images.ImageInsertFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsImageInsertFailedExceptionIfInsertingDuplicateURL()
            throws ImageInsertFailedException {
        try {
            Image u0 = new Image("image.png", "image", 1);
            Image u1 = new Image("image.png", "plumage", 1);
            images.insert(connection, u0);
            
            exception.expect(ImageInsertFailedException.class);
            images.insert(connection, u1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Image base = new Image("image.png", "image", 1);
            Image u0 = new Image("image2.png", null, 1, 5);
            
            insertImage(base);
            u0.setImageId(base.imageId());
            images.update(connection, u0);
            
            String sql = "select * from images where imageId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.imageId());
            u0.setImageId(base.imageId());
            rs = stmt.executeQuery();
            
            Image u02 = new Image();
            while (rs.next()) {
                u02.setImageId(rs.getInt(1));
                u02.setPath(rs.getString(2));
                u02.setTitle(rs.getString(3));
                u02.setProjectId(rs.getInt(4));
                u02.setCurrentUser(rs.getInt(5));
            }
            
            assertEquals(u0.imageId(), u02.imageId());
            assertEquals(u0.path(), u02.path());
            assertEquals(base.title(), u02.title()); // Null and 0 values not updated
            assertEquals(base.projectId(), u02.projectId());
            assertEquals(u0.currentUser(), u02.currentUser());
        }
        catch (SQLException | Images.ImageUpdateFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsImageUpdateFailedExceptionWithNoImageId()
            throws ImageUpdateFailedException {
        try {
            Image u0 = new Image("image.png", "image", 1);
            exception.expect(ImageUpdateFailedException.class);
            exception.expectMessage("No image ID found in input Image parameter.");
            images.update(connection, u0);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsImageUpdateFailedExceptionIdNotFound()
            throws ImageUpdateFailedException {
        try {
            Image u0 = new Image(100, "image.png", "image", 1);
            exception.expect(ImageUpdateFailedException.class);
            exception.expectMessage("ID 100 not found in 'images' table.");
            images.update(connection, u0);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsImageUpdateFailedExceptionDuplicatePath()
            throws ImageUpdateFailedException {
        try {
            Image base = new Image("image.png", "image", 1);
            images.insert(connection, base);
            Image u0 = new Image("image2.png", "image2", 1);
            u0 = images.insert(connection, u0);
            u0.setPath("image.png");
            exception.expect(ImageUpdateFailedException.class);
            exception.expectMessage("column path is not unique");
            images.update(connection, u0);
        } catch (ImageInsertFailedException | SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Image base = new Image("image.png", "image", 1);
            insertImage(base);
            Image get = new Image();
            get.setImageId(base.imageId());

            // Returns a correct result
            imageList.addAll(images.get(connection, get));
            assertEquals(1, imageList.size());
            assertEquals(imageList.get(0), base);
            imageList.clear();
            
            // Returns null if not found
            get.setImageId(base.imageId() + 1);
            imageList.addAll(images.get(connection, get));
            assertEquals(0, imageList.size());
            
        }
        catch (SQLException | ImageGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            imageList.clear();
        }
    }
    
    @Test
    public void delete() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Image base = new Image("image.png", "image", 1);
            insertImage(base);
            
            int rightId = base.imageId();
            
            images.delete(connection, rightId);
            
            String sql = "select * from images where imageId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fail("Should have been deleted.");
            }
            
        }
        catch (ImageDeleteFailedException | SQLException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsImageDeleteFailedExceptionInvalidInputId()
            throws ImageDeleteFailedException {
        try {
            int id = 0;
            exception.expect(ImageDeleteFailedException.class);
            exception.expectMessage(String.format("Invalid image id: %d", id));
            images.delete(connection, id);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsImageDeleteFailedExceptionImageDoesNotExist()
            throws ImageDeleteFailedException {
        try {
            int id = 1;
            exception.expect(ImageDeleteFailedException.class);
            exception.expectMessage(String.format(
                    "ID %d does not exist in the 'images' table.", id));
            images.delete(connection, id);
            
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    private void insertImage(Image image) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement kStmt = null;
        
        try {
            String insertSql = "insert into images (path, title, projectId, currentUser) "
                    + "values (?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertSql);
            stmt.setString(1, image.path());
            stmt.setString(2, image.title());
            stmt.setInt(3, image.projectId());
            stmt.setInt(4, image.currentUser());
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                image.setImageId(rs.getInt(1));
            }
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (kStmt != null) kStmt.close();
        }
    }

}