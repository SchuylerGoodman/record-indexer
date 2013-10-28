package server.database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import shared.model.*;

/**
 * Main Database Access class - assigns tasks to minion DA classes.
 * 
 * @author schuyler
 */
public class Database {
    
    public static final String DATABASE_PATH = "db" + File.separator
            + "main" + File.separator + "record-indexer.sqlite";
    public static final String STATEMENT_PATH = "db" + File.separator + "DatabaseCreate.sql";
    
    public static final String USERS = "USERS";
    public static final String PROJECTS = "PROJECTS";
    public static final String FIELDS = "FIELDS";
    public static final String RECORDS = "RECORDS";
    public static final String IMAGES = "IMAGES";
    
    private Connection connection;
    
    private Users users;
    private Projects projects;
    private Fields fields;
    private Records records;
    private Images images;
    
    public static class InsertFailedException extends Exception {
        public InsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class UpdateFailedException extends Exception {
        public UpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class GetFailedException extends Exception {
        public GetFailedException(String message) {
            super(message);
        }
    }
    
    public static class DeleteFailedException extends Exception {
        public DeleteFailedException(String message) {
            super(message);
        }
    }
    
    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }
   
    public Database() throws DatabaseException {
        
        users = new Users();
        projects = new Projects();
        fields = new Fields();
        records = new Records();
        images = new Images();
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        
    }
    
    public void startTransaction() throws DatabaseException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:db"
                        + File.separator + "main" + File.separator
                        + "record-indexer.sqlite");
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
    
    public void endTransaction(boolean success) {
        if (connection == null) {
            return;
        }
        try {
            if (success) {
                connection.commit();
            }
            else {
                connection.rollback();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            connection = null;
        }
    }
    
    /**
     * Creates a new row for the given model class in the database.
     * 
     * @param newModel shared.model.ModelClass A model class to insert into the database.
     * @return shared.model.ModelClass A model class from the shared.model package.
     */
    public ModelClass insert(ModelClass newModel)
            throws DatabaseException, InsertFailedException {

        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.insert()");
        ModelClass returnModel = null;
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        if (newModel == null) {
            throw new DatabaseException("Input parameter is null.");
        }
        if (!newModel.canInsert()) {
            throw new DatabaseException("Input parameter does not have enough information to insert.");
        }
        
        String tableName = newModel.getTableName();
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                returnModel = users.insert(connection, (User) newModel);
            }
            else if (PROJECTS.equalsIgnoreCase(tableName)) {
                returnModel = projects.insert(connection, (Project) newModel);
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                returnModel = fields.insert(connection, (Field) newModel);
            }
            else if (RECORDS.equalsIgnoreCase(tableName)) {
                returnModel = records.insert(connection, (Record) newModel);
            }
            else if (IMAGES.equalsIgnoreCase(tableName)) {
                returnModel =  images.insert(connection, (Image) newModel);
            }
            else {
                throw new InsertFailedException("Invalid input model class.");
            }
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        catch (Users.UserInsertFailedException
               | Projects.ProjectInsertFailedException
               | Fields.FieldInsertFailedException
               | Records.RecordInsertFailedException
               | Images.ImageInsertFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Insert into %s table failed.", tableName.toLowerCase()), ex);
            throw new InsertFailedException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.insert()");
        return returnModel;
        
    }

    /**
     * Updates the given model class in the database.
     * 
     * @param model shared.model.ModelClass A model class to update in the database.
     */
    public void update(ModelClass model)
            throws DatabaseException, UpdateFailedException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.update()");
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        if (model == null) {
            throw new DatabaseException("Input parameter is null.");
        }
        
        String tableName = model.getTableName();
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                users.update(connection, (User) model);
            }
            else if (PROJECTS.equalsIgnoreCase(tableName)) {
                projects.update(connection, (Project) model);
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                fields.update(connection, (Field) model);
            }
            else if (RECORDS.equalsIgnoreCase(tableName)) {
                records.update(connection, (Record) model);
            }
            else if (IMAGES.equalsIgnoreCase(tableName)) {
                images.update(connection, (Image) model);
            }
            else {
                throw new UpdateFailedException("Invalid input model class.");
            }
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        catch (Users.UserUpdateFailedException
               | Projects.ProjectUpdateFailedException
               | Fields.FieldUpdateFailedException
               | Records.RecordUpdateFailedException
               | Images.ImageUpdateFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Update to %s table failed.", tableName.toLowerCase()), ex);
            throw new UpdateFailedException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.update()");
        
    }

    /**
     * Loads all instances of a model class from the database.
     * 
     * @param tableName String case-insensitive: containing the name of the table to query. 
     *    Output of ModelClass.getTableName()
     *    Options:
     *      USERS
     *      PROJECTS
     *      FIELDS
     *      RECORDS
     *      IMAGES
     * @param uniqueId database id to get from the table.
     * @return Model Class with the requested information.
     */
    public List<ModelClass> get(String tableName)
            throws DatabaseException, GetFailedException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.get()");
        ArrayList<ModelClass> returnClasses = new ArrayList<>();
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                returnClasses.addAll(users.get(connection));
            }
            else if (PROJECTS.equalsIgnoreCase(tableName)) {
                returnClasses.addAll(projects.get(connection));
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                returnClasses.addAll(fields.get(connection));
            }
            else if (RECORDS.equalsIgnoreCase(tableName)) {
                returnClasses.addAll(records.get(connection));
            }
            else if (IMAGES.equalsIgnoreCase(tableName)) {
                returnClasses.addAll(images.get(connection));
            }
            else {
                throw new GetFailedException("Invalid input table name.");
            }
        }
        catch (Users.UserGetFailedException
               | Projects.ProjectGetFailedException
               | Fields.FieldGetFailedException
               | Records.RecordGetFailedException
               | Images.ImageGetFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Get from %s table failed.", tableName.toLowerCase()), ex);
            throw new GetFailedException(ex.getMessage());
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.get()");
        return returnClasses;
        
    }
    
    /**
     * Loads a model class from the database.
     * 
     * @param tableName String case-insensitive: containing the name of the table to query. 
     *    Output of ModelClass.getTableName()
     *    Options:
     *      USERS
     *      PROJECTS
     *      FIELDS
     *      RECORDS
     *      IMAGES
     * @param uniqueId database id to get from the table.
     * @return Model Class with the requested information.
     */
    public ModelClass get(String tableName, int uniqueId)
            throws DatabaseException, GetFailedException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.get()");
        ModelClass returnClass = null;
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                returnClass = users.get(connection, uniqueId);
            }
            else if (PROJECTS.equalsIgnoreCase(tableName)) {
                returnClass = projects.get(connection, uniqueId);
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                returnClass = fields.get(connection, uniqueId);
            }
            else if (RECORDS.equalsIgnoreCase(tableName)) {
                returnClass = records.get(connection, uniqueId);
            }
            else if (IMAGES.equalsIgnoreCase(tableName)) {
                returnClass = images.get(connection, uniqueId);
            }
            else {
                throw new GetFailedException("Invalid input table name.");
            }
        }
        catch (Users.UserGetFailedException
               | Projects.ProjectGetFailedException
               | Fields.FieldGetFailedException
               | Records.RecordGetFailedException
               | Images.ImageGetFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Get from %s table failed.", tableName.toLowerCase()), ex);
            throw new GetFailedException(ex.getMessage());
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.get()");
        return returnClass;
        
    }

    /**
     * Loads a model class from the database for operations requiring two input parameters.
     * Currently only supported for User and Field lookups.
     * 
     * @param tableName String case-insensitive: containing the name of the table to query. 
     *    Output of ModelClass.getTableName()
     *    Options:
     *      USERS - Requires String username and String password
     *      <Strike>PROJECTS<\Strike>
     *      FIELDS - Requires Integer project ID and Integer columnNumber
     *      <Strike>RECORDS<\Strike>
     *      <Strike>IMAGES<\Strike>
     * @param uniqueId database id to get from the table.
     * @return Model Class with the requested information.
     */
    public ModelClass get(String tableName, Object firstId, Object secondId)
            throws DatabaseException, GetFailedException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.get()");
        ModelClass returnClass = null;
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                if (firstId.getClass() == secondId.getClass() && firstId.getClass() == String.class) {
                    String firstIdString = (String) firstId;
                    String secondIdString = (String) secondId;
                    returnClass = users.get(connection, firstIdString, secondIdString);
                }
                else {
                    throw new GetFailedException(
                            "Get from users table requires String inputs.");
                }
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                if (firstId.getClass() == secondId.getClass() && firstId.getClass() == Integer.class) {
                    Integer firstIdInt = (Integer) firstId;
                    Integer secondIdInt = (Integer) secondId;
                    returnClass = fields.get(connection, firstIdInt.intValue(), secondIdInt.intValue());
                }
                else {
                    throw new GetFailedException(
                            "Get from fields table requires Integer inputs.");
                }
            }
            else {
                throw new GetFailedException("Invalid input table name.");
            }
        }
        catch (Users.UserGetFailedException
             | Fields.FieldGetFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Get from %s table failed.", tableName.toLowerCase()), ex);
            throw new GetFailedException(ex.getMessage());
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.get()");
        return returnClass;
        
    }
    
    /**
     * Deletes information from the database.
     * 
     * @param deleteModel The model class to delete.
     */
    public void delete(String tableName, int uniqueId)
            throws DatabaseException, DeleteFailedException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.delete()");
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        
        try {
            if (USERS.equalsIgnoreCase(tableName)) {
                users.delete(connection, uniqueId);
            }
            else if (PROJECTS.equalsIgnoreCase(tableName)) {
                projects.delete(connection, uniqueId);
            }
            else if (FIELDS.equalsIgnoreCase(tableName)) {
                fields.delete(connection, uniqueId);
            }
            else if (RECORDS.equalsIgnoreCase(tableName)) {
                records.delete(connection, uniqueId);
            }
            else if (IMAGES.equalsIgnoreCase(tableName)) {
                images.delete(connection, uniqueId);
            }
            else {
                throw new DeleteFailedException("Invalid input table name.");
            }
        }
        catch (Users.UserDeleteFailedException
               | Projects.ProjectDeleteFailedException
               | Fields.FieldDeleteFailedException
               | Records.RecordDeleteFailedException
               | Images.ImageDeleteFailedException ex) {
            Logger.getLogger(Database.class.getName()).log(
                    Level.WARNING, String.format("Delete from %s table failed.", tableName.toLowerCase()), ex);
            throw new DeleteFailedException(ex.getMessage());
        }
        catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Leaving Database.delete()");
        
    }
    
    public List<Record> search(List<String> fieldIds, List<String> matches)
            throws DatabaseException {
        
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.search()");
        if (connection == null) {
            throw new DatabaseException("Database transaction has not been started.");
        }
        ArrayList<Record> recordOutput = new ArrayList<>();
        
        try {
            recordOutput = (ArrayList) records.search(connection, fieldIds, matches);
        }
        catch (Records.RecordSearchFailedException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.WARNING, "Record search failed.", ex);
            throw new DatabaseException(ex.getMessage());
        }
        Logger.getLogger(Database.class.getName()).log(Level.FINE, "Entering Database.search()");
        return recordOutput;
        
    }
    
}
