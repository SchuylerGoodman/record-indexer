/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Pattern;

/**
 *
 * @author schuyler
 */
public class DatabaseCreator {
    
    List<String> statements;
    
    public DatabaseCreator() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> statements() {
        return this.statements;
    }
    
    public void createDatabase(File databaseFile, File sqliteStatements) throws SQLException {
        parseSqlite(sqliteStatements);
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int result;
        try {
            String path = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
//            String path = "db" + File.separator + "test" + File.separator
//                + "test-create-database.sqlite";
            connection = DriverManager.getConnection(path);
            connection.setAutoCommit(false);

            for (String statement : this.statements) {
                stmt = connection.createStatement();
                result = stmt.executeUpdate(statement);
//                System.out.println(rs.toString());
            }
            connection.commit();
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseCreator.class.getName()).log(Level.SEVERE, null, ex);
            if (connection != null) connection.rollback();
        }
        finally {
            if (connection != null) connection.close();
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        }
    }
    
    private void parseSqlite(File sqliteStatements) {
        if (statements == null) {
            statements = new ArrayList<>();
        }
        else {
            statements.clear();
        }
        try {
            Scanner sc = new Scanner(sqliteStatements);
            sc.useDelimiter(Pattern.compile("(?<=;)\\s*"));
            while (sc.hasNext()) {
                statements.add(sc.next());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseCreator.class.getName()).log(Level.SEVERE, null, ex);
            statements = null;
        }
    }
}
