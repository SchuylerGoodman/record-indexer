package server.database;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import shared.model.User;

/**
 * Database Access class for the users table.
 * 
 * @author schuyler
 */
public class Users {
    
    public static class UserInsertFailedException extends Database.InsertFailedException {
        public UserInsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class UserUpdateFailedException extends Database.UpdateFailedException {
        public UserUpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class UserGetFailedException extends Database.GetFailedException {
        public UserGetFailedException(String message) {
            super(message);
        }
    }
    
    public static class UserDeleteFailedException extends Database.DeleteFailedException {
        public UserDeleteFailedException(String message) {
            super(message);
        }
    }
    
    public Users() {}
    
    /**
     * Creates a new user in the database.
     * 
     * @param connection open database connection
     * @param newUser shared.model.User model class with data to insert.
     * @return shared.model.User with generated field ID.
     * @throws UserInsertFailedException
     * @throws SQLException
     */
    protected User insert(Connection connection, User newUser)
            throws SQLException, UserInsertFailedException {
        
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Entering Users.insert()");
        if (connection == null) {
            throw new UserInsertFailedException("Database connection has not been initialized.");
        }
        PreparedStatement stmt = null;
        Statement kStmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO users (username, first, last, password, email");
            if (newUser.indexedRecords() > -1) {
                sql.append(", records");
            }
            sql.append(") VALUES (?, ?, ?, ?, ?");
            if (newUser.indexedRecords() > -1) {
                sql.append(", ?");
            }
            sql.append(")");
            stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, newUser.username());
            stmt.setString(2, newUser.firstName());
            stmt.setString(3, newUser.lastName());
            stmt.setString(4, newUser.password());
            stmt.setString(5, newUser.email());
            if (newUser.indexedRecords() > -1) {
                stmt.setInt(6, newUser.indexedRecords());
            }
            
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                int id = rs.getInt(1);
                newUser.setUserId(id);
            }
            else {
                throw new UserInsertFailedException("User was not inserted and no SQLException was thrown.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.INFO, "User might already exist in the database.");
            throw new UserInsertFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (kStmt != null) kStmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Leaving Users.insert()");
        return newUser;

    }

    /**
     * Updates a User in the database.
     *
     * @param connection open database connection
     * @param user with the updated information.  Null or 0 values will not be updated.
     * @throws UserUpdateFailedException
     * @throws SQLException
     */
    protected void update(Connection connection, User user)
            throws UserUpdateFailedException, SQLException {

        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Entering Users.update()");
        if (connection == null) {
            throw new UserUpdateFailedException("Database connection has not been initialized.");
        }
        if (user.userId() < 1) {
            throw new UserUpdateFailedException("No user ID found in input User parameter.");
        }
        PreparedStatement stmt = null;
            
        try {
            StringBuilder sql = new StringBuilder();

            if (user.username() != null) {
                sql.append(" username=\"").append(user.username()).append("\"");
            }
            if (user.firstName() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" first=\"").append(user.firstName()).append("\"");
            }
            if (user.lastName() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" last=\"").append(user.lastName()).append("\"");
            }
            if (user.password() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" password=\"").append(user.password()).append("\"");
            }
            if (user.email() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" email=\"").append(user.email()).append("\"");
            }
            if (user.indexedRecords() > -1) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" records=\"").append(user.indexedRecords()).append("\"");
            }
            if (sql.length() > 0) {
                sql.insert(0, "update users set");
                sql.append(" where userId = ").append(user.userId());
            }
            else {
                Logger.getLogger(User.class.getName()).log(
                        Level.WARNING, "Attempted update with no updatable information.");
                throw new UserUpdateFailedException("No information given to update.");
            }
            
            stmt = connection.prepareStatement(sql.toString());

            if (stmt.executeUpdate() != 1) {
                throw new UserUpdateFailedException(String.format("ID %d not found in 'users' table.", user.userId()));
            }
        }
        catch (SQLException ex) {
            throw new UserUpdateFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Leaving Users.update()");
        
    }

    /**
     * Gets all matching Users from the database.
     * 
     * @param connection open database connection
     * @param user User object - all initialized information will be used to
     * get all matching users from the database. Calling this function with the
     * empty User constructor will return all users from the database.
     * 
     * @return List of shared.model.User objects with the requested information.
     * @throws UserGetFailedException
     * @throws SQLException
     */
    protected List<User> get(Connection connection, User user)
            throws UserGetFailedException, SQLException {
        
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Entering Users.get()");
        if (connection == null) {
            throw new UserGetFailedException("Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();
        
        try {
            
            StringBuilder sql = new StringBuilder();
            StringBuilder wheres = new StringBuilder();
            sql.append("select * from users");

            if (user.userId() > 0) {
                wheres.append(" where ");
                wheres.append("userId=").append(user.userId());
            }
            if (user.username() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("username=\"").append(user.username()).append("\"");
            }
            if (user.firstName() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("first=\"").append(user.firstName()).append("\"");
            }
            if (user.lastName() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("last=\"").append(user.lastName()).append("\"");
            }
            if (user.password() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("password=\"").append(user.password()).append("\"");
            }
            if (user.email() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("email=\"").append(user.email()).append("\"");
            }
            if (user.indexedRecords() > -1) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("records=").append(user.indexedRecords());
            }
            sql.append(wheres);
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2),
                                    rs.getString(3), rs.getString(4),
                                    rs.getString(5), rs.getString(6),
                                    rs.getInt(7)));
            }
            
        }
        catch (SQLException ex) {
            throw new UserGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Leaving Users.get()");
        return users;
        
    }
    
    /**
     * Deletes a User from the database.
     * 
     * @param connection Open database connection
     * @param userId Id of the user to delete from the database
     * @throws UserDeleteFailedException
     * @throws SQLException
     */
    protected void delete(Connection connection, int userId)
            throws UserDeleteFailedException, SQLException {
        
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Entering Users.delete()");
        if (connection == null) {
            throw new UserDeleteFailedException("Database connection has not been initialized.");
        }
        if (userId < 1) {
            throw new UserDeleteFailedException(String.format("Invalid user id: %d", userId));
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = String.format("delete from users where userId=%d", userId);
            stmt = connection.prepareStatement(sql);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new UserDeleteFailedException(String.format("ID %d does not exist in the 'users' table.",
                        userId));
            }
        }
        catch (SQLException ex) {
            throw new UserDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Users.class.getName()).log(Level.FINE, "Leaving Users.delete()");
        
    }

}
