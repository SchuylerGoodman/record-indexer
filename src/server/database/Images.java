package server.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import shared.model.Image;

/**
 * Database Access class for the images table.
 * 
 * @author schuyler
 */
public class Images {

    public static class ImageInsertFailedException extends Database.InsertFailedException {
        public ImageInsertFailedException(String message) {
            super(message);
        }
    }
    
    public static class ImageUpdateFailedException extends Database.UpdateFailedException {
        public ImageUpdateFailedException(String message) {
            super(message);
        }
    }
    
    public static class ImageGetFailedException extends Database.GetFailedException {
        public ImageGetFailedException(String message) {
            super(message);
        }
    }
    
    public static class ImageDeleteFailedException extends Database.DeleteFailedException {
        public ImageDeleteFailedException(String message) {
            super(message);
        }
    }
    
    /**
     * Inserts an new Image into the database.
     * 
     * @param connection Open database connection
     * @param newImage shared.model.Image model class to insert into the database.
     * @return shared.model.Image with the generated image ID
     */
    protected Image insert(Connection connection, Image newImage)
            throws ImageInsertFailedException, SQLException {

        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Entering Images.insert()");
        if (connection == null) {
            throw new ImageInsertFailedException(
                    "Database connection has not been initialized.");
        }
        PreparedStatement stmt = null;
        Statement kStmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO images (path, title, projectId");
            if (newImage.currentUser() > 0) {
                sql.append(", currentUser");
            }
            sql.append(") VALUES (?, ?, ?");
            if (newImage.currentUser() > 0) {
                sql.append(", ?");
            }
            sql.append(")");
            stmt = connection.prepareStatement(sql.toString());
            stmt.setString(1, newImage.path());
            stmt.setString(2, newImage.title());
            stmt.setInt(3, newImage.projectId());
            if (newImage.currentUser() > 0) {
                stmt.setInt(4, newImage.currentUser());
            }
            
            if (stmt.executeUpdate() == 1) {
                kStmt = connection.createStatement();
                rs = kStmt.executeQuery("select last_insert_rowid()");
                rs.next();
                int id = rs.getInt(1);
                newImage.setImageId(id);
            }
            else {
                throw new ImageInsertFailedException(
                        "Image was not inserted and no SQLException was thrown.");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Images.class.getName()).log(
                    Level.INFO, "Image might already exist in the database.");
            throw new ImageInsertFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (kStmt != null) kStmt.close();
            if (rs != null) rs.close();
        }
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Leaving Images.insert()");
        return newImage;
        
    }

    /**
     * Updates an Image in the database.
     * 
     * @param connection Open database connection
     * @param image shared.model.Image to update.
     */
    protected void update(Connection connection, Image image)
            throws ImageUpdateFailedException, SQLException {

        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Entering Images.update()");
        if (connection == null) {
            throw new ImageUpdateFailedException("Database connection has not been initialized.");
        }
        if (image.imageId() == 0) {
            throw new ImageUpdateFailedException("No image ID found in input Image parameter.");
        }
        PreparedStatement stmt = null;
            
        try {
            StringBuilder sql = new StringBuilder();

            if (image.path() != null) {
                sql.append(" path=\"").append(image.path()).append("\"");
            }
            if (image.title() != null) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" title=\"").append(image.title()).append("\"");
            }
            if (image.projectId() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" projectId=").append(image.projectId());
            }
            if (image.currentUser() > 0) {
                if (sql.length() > 0) sql.append(",");
                sql.append(" currentUser=").append(image.currentUser());
            }
            if (sql.length() > 0) {
                sql.insert(0, "update images set");
                sql.append(" where imageId = ").append(image.imageId());
            }
            else {
                Logger.getLogger(Image.class.getName()).log(
                        Level.WARNING, "Attempted update with no updatable information.");
                throw new ImageUpdateFailedException("No information given to update.");
            }
            
            stmt = connection.prepareStatement(sql.toString());

            if (stmt.executeUpdate() != 1) {
                throw new ImageUpdateFailedException(String.format(
                        "ID %d not found in 'images' table.", image.imageId()));
            }
        }
        catch (SQLException ex) {
            throw new ImageUpdateFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Leaving Images.update()");
        
    }
    
    /**
     * Gets all matching Images from the database.
     * 
     * @param connection Open database connection
     * @param image Image object - all initialized information will be used to
     * get all matching images from the database. Calling this function with the
     * empty Image constructor will return all images from the database.
     * 
     * @return shared.model.Image object with the requested info.
     * @throws ImageGetFailedException
     * @throws SQLException
     */
    protected List<Image> get(Connection connection, Image image)
            throws ImageGetFailedException, SQLException {
        
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Entering Images.get()");
        if (connection == null) {
            throw new ImageGetFailedException("Database connection has not been initialized.");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Image> images = new ArrayList<>();
        
        try {
            
            StringBuilder sql = new StringBuilder();
            StringBuilder wheres = new StringBuilder();
            sql.append("select * from images");

            if (image.imageId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                wheres.append("imageId=").append(image.imageId());
            }
            if (image.path() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("path=\"").append(image.path()).append("\"");
            }
            if (image.title() != null) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("title=\"").append(image.title()).append("\"");
            }
            if (image.projectId() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("projectId=").append(image.projectId());
            }
            if (image.currentUser() > 0) {
                if (wheres.length() < 1) wheres.append(" where ");
                else wheres.append(" and ");
                wheres.append("currentUser=").append(image.currentUser());
            }
            sql.append(wheres);
            stmt = connection.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                images.add(new Image(rs.getInt(1), rs.getString(2),
                                     rs.getString(3), rs.getInt(4),
                                     rs.getInt(5)));
            }
            
        }
        catch (SQLException ex) {
            throw new ImageGetFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
        
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Leaving Images.get()");
        return images;
        
    }
    
//    /**
//     * Gets data on the requested image from the database.
//     * 
//     * @param connection Open database connection
//     * @param imageId Image ID whose data we want.
//     * 
//     * @return shared.model.Image object with the requested info.
//     * @throws ImageGetFailedException
//     * @throws SQLException
//     */
//    protected Image get(Connection connection, int imageId)
//            throws ImageGetFailedException, SQLException {
//        
//        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Entering Images.get()");
//        if (connection == null) {
//            throw new ImageGetFailedException("Database connection has not been initialized.");
//        }
//        if (imageId < 1) {
//            throw new ImageGetFailedException(
//                    String.format("%d is an invalid image ID.", imageId));
//        }
//        
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Image image = null;
//        
//        try {
//            
//            String sql = "select * from images where imageId = ?";
//            stmt = connection.prepareStatement(sql.toString());
//            stmt.setInt(1, imageId);
//            rs = stmt.executeQuery();
//            
//            int j = 0;
//            while (rs.next()) {
//                image = new Image(rs.getInt(1), rs.getString(2),
//                                  rs.getString(3), rs.getInt(4), rs.getInt(5));
//                ++j;
//            }
//            if (j > 1) {
//                throw new ImageGetFailedException(String.format(
//                        "Only one Image should have been returned. Found %d", j));
//            }
//            
//        }
//        catch (SQLException ex) {
//            throw new ImageGetFailedException(ex.getMessage());
//        }
//        finally {
//            if (stmt != null) stmt.close();
//            if (rs != null) rs.close();
//        }
//        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Leaving Images.get()");
//        return image;
//        
//    }

    /**
     * Deletes an image from the database.
     * 
     * @param connection Open database connection
     * @param imageId Id of the Image to delete from the database.
     */
    protected void delete(Connection connection, int imageId)
            throws ImageDeleteFailedException, SQLException {
        
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Entering Images.delete()");
        if (connection == null) {
            throw new ImageDeleteFailedException("Database connection has not been initialized.");
        }
        if (imageId < 1) {
            throw new ImageDeleteFailedException(String.format("Invalid image id: %d", imageId));
        }
        
        PreparedStatement stmt = null;
        
        try {
            String sql = String.format("delete from images where imageId=%d", imageId);
            stmt = connection.prepareStatement(sql);
            
            int numberDeleted = stmt.executeUpdate();
            if (numberDeleted < 1) {
                throw new ImageDeleteFailedException(String.format(
                        "ID %d does not exist in the 'images' table.", imageId));
            }
        }
        catch (SQLException ex) {
            throw new ImageDeleteFailedException(ex.getMessage());
        }
        finally {
            if (stmt != null) stmt.close();
        }
        Logger.getLogger(Images.class.getName()).log(Level.FINE, "Leaving Images.delete()");
        
    }
}
