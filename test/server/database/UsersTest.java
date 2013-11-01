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
import server.database.Users.*;
import shared.model.User;
import static org.junit.Assert.*;

/**
 *
 * @author schuyler
 */
public class UsersTest {

    static {
        try {
            Database.initialize();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(UsersTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Users users;
    
    private Connection connection;
    
    private ArrayList<User> userList;
    
    public UsersTest() {
        try {
            Class.forName("org.sqlite.JDBC");
            users = new Users();
            userList = new ArrayList<>();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UsersTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UsersTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UsersTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() throws SQLException {

        if (connection != null) {
            Statement stmt = null;
            try {
                connection.rollback();
                stmt = connection.createStatement();
                stmt.executeUpdate("delete from users");
                connection.commit();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsersTest.class.getName()).log(Level.SEVERE, null, ex);
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
            User u0 = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            User u01 = users.insert(connection, u0);
            String sql = "select * from users where userId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, u01.userId());
            rs = stmt.executeQuery();
            User u02 = new User();
            while (rs.next()) {
                u02.setUserId(rs.getInt(1));
                u02.setUsername(rs.getString(2));
                u02.setFirstName(rs.getString(3));
                u02.setLastName(rs.getString(4));
                u02.setPassword(rs.getString(5));
                u02.setEmail(rs.getString(6));
                u02.setIndexedRecords(rs.getInt(7));
            }
            assertEquals(u01, u02);
        }
        catch (SQLException | Users.UserInsertFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsUserInsertFailedExceptionIfInsertingDuplicate()
            throws UserInsertFailedException {
        try {
            User u0 = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            User u1 = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            users.insert(connection, u0);
            exception.expect(UserInsertFailedException.class);
            users.insert(connection, u1);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    
    @Test
    public void update() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            User base = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            User u0 = new User("HB", "Harry", "Blooper", "blooops", null, -1);
            
            insertUser(base);
            u0.setUserId(base.userId());
            users.update(connection, u0);
            
            String sql = "select * from users where userId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, base.userId());
            u0.setUserId(base.userId());
            rs = stmt.executeQuery();
            
            User u02 = new User();
            while (rs.next()) {
                u02.setUserId(rs.getInt(1));
                u02.setUsername(rs.getString(2));
                u02.setFirstName(rs.getString(3));
                u02.setLastName(rs.getString(4));
                u02.setPassword(rs.getString(5));
                u02.setEmail(rs.getString(6));
                u02.setIndexedRecords(rs.getInt(7));
            }
            
            assertEquals(u0.userId(), u02.userId());
            assertEquals(u0.username(), u02.username());
            assertEquals(u0.firstName(), u02.firstName());
            assertEquals(u0.lastName(), u02.lastName());
            assertEquals(u0.password(), u02.password());
            assertEquals(base.email(), u02.email()); // Null and 0 values not updated
            assertEquals(base.indexedRecords(), u02.indexedRecords()); // Null and 0 values not updated
            
        }
        catch (SQLException | Users.UserUpdateFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsUserUpdateFailedExceptionWithNoUserId()
            throws UserUpdateFailedException {
        try {
            User u0 = new User("HB", "Harry", "Blooper", "blooops", "hb@bloops.com", 2);
            exception.expect(UserUpdateFailedException.class);
            exception.expectMessage("No user ID found in input User parameter.");
            users.update(connection, u0);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsUserUpdateFailedExceptionIdNotFound()
            throws UserUpdateFailedException {
        try {
            User u0 = new User(100, "HB", "Harry", "Blooper", "blooops", "hb@bloops.com", 2);
            exception.expect(UserUpdateFailedException.class);
            exception.expectMessage("ID 100 not found in 'users' table.");
            users.update(connection, u0);
        } catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void get() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            User base = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);

            insertUser(base);
            User get = new User(base.userId(), null, null, null, null, null);
                    
            // Returns a correct result
            userList.addAll(users.get(connection, get));
            assertEquals(1, userList.size());
            assertEquals(userList.get(0), base);
            userList.clear();
            
            // Returns null if not found
            get.setUserId(base.userId() + 1);
            userList.addAll(users.get(connection, get));
            assertEquals(0, userList.size());
            
        } catch (SQLException | UserGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            userList.clear();
        }
    }
    
    @Test
    public void getWithUsernameAndPassword() throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            User base = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            insertUser(base);
            User get = new User(0, base.username(), null, null, base.password(), null);

            // Returns a correct result
            userList.addAll(users.get(connection, get));
            assertEquals(1, userList.size());
            assertEquals(userList.get(0), base);
            userList.clear();
            
            // Returns null
            get.setUsername("HL");
            userList.addAll(users.get(connection, get));
            assertEquals(0, userList.size());
            
        } catch (SQLException | UserGetFailedException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            userList.clear();
        }
    }
        
    @Test
    public void delete() throws SQLException {
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            User base = new User("HH", "Harry", "Hooper", "hooops", "hh@hoops.com", 0);
            insertUser(base);
            
            int rightId = base.userId();
            
            users.delete(connection, rightId);
            
            String sql = "select * from users where userId = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, rightId);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                fail("Should have been deleted.");
            }
            
        }
        catch (UserDeleteFailedException | SQLException ex) {
            fail(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    @Test
    public void throwsUserDeleteFailedExceptionInvalidInputId()
            throws UserDeleteFailedException {
        try {
            int id = 0;
            exception.expect(UserDeleteFailedException.class);
            exception.expectMessage(String.format("Invalid user id: %d", id));
            users.delete(connection, id);
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    @Test
    public void throwsUserDeleteFailedExceptionUserDoesNotExist()
            throws UserDeleteFailedException {
        try {
            int id = 1;
            exception.expect(UserDeleteFailedException.class);
            exception.expectMessage(String.format(
                    "ID %d does not exist in the 'users' table.", id));
            users.delete(connection, id);
            
        }
        catch (SQLException ex) {
            fail(ex.getMessage());
        }
    }
    
    private void insertUser(User user) throws SQLException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement kStmt = null;
        
        try {
            String insertSql = "insert into users (username, first, last, password, email, records) "
                    + "values (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertSql);
            stmt.setString(1, user.username());
            stmt.setString(2, user.firstName());
            stmt.setString(3, user.lastName());
            stmt.setString(4, user.password());
            stmt.setString(5, user.email());
            stmt.setInt(6, user.indexedRecords());
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                user.setUserId(rs.getInt(1));
            }
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (kStmt != null) kStmt.close();
        }
    }

}
