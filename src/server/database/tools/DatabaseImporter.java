/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools;

import server.database.tools.parseHandlers.IndexerHandler;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import server.database.Database;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class DatabaseImporter {

    private static final String USER = "USER";
    private static final String PROJECT = "PROJECT";
    private static final String FIELD = "FIELD";
    private static final String RECORD = "RECORD";
    private static final String IMAGE = "IMAGE";
    
    private Database database;
    private String currentTable;
    private int currentProjectId;
    
    private boolean onUser;
    private boolean onProject;
    private boolean onField;
    private boolean onRecord;
    private boolean onImage;
    
    public DatabaseImporter() throws Database.DatabaseException {
        
        database = new Database();
        currentTable = null;
        currentProjectId = 0;
        
        onUser = false;
        onProject = false;
        onField = false;
        onRecord = false;
        onImage = false;
        
    }

    public static void main(String[] args) {
        
        try {
            DatabaseCreator dbc = new DatabaseCreator();
            dbc.createDatabase(new File(Database.DATABASE_PATH), new File(Database.STATEMENT_PATH));
            
            String filename = args[0];
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser SaxParser = spf.newSAXParser();
            XMLReader reader = SaxParser.getXMLReader();
            reader.setContentHandler(new IndexerHandler(reader));
            reader.parse(new InputSource(new BufferedInputStream(new FileInputStream(filename))));
            
            ModelClass u = new User();
            System.out.println(u.getClass().getName());
        } catch (ParserConfigurationException
               | SAXException
               | IOException
               | Database.DatabaseException
               | SQLException ex) {
            Logger.getLogger(DatabaseImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
