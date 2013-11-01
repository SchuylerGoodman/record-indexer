/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools;

import server.database.tools.parseHandlers.IndexerHandler;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import server.database.Database;

/**
 *
 * @author schuyler
 */
public class DatabaseImporter {

    public DatabaseImporter() throws Database.DatabaseException {}

    public static void main(String[] args) {
        
        try {
            String recordPath = null;
            if (args.length != 1) {
                usage();
            }
            else {
                recordPath = args[0];
            }
            DatabaseCreator dbc = new DatabaseCreator();
            dbc.createDatabase(new File(Database.DATABASE_PATH), new File(Database.STATEMENT_PATH));
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser SaxParser = spf.newSAXParser();
            XMLReader reader = SaxParser.getXMLReader();
            reader.setContentHandler(new IndexerHandler(reader)); // Set initial parsing handler
            reader.parse(new InputSource(new BufferedInputStream(new FileInputStream(recordPath))));
            copyFiles(recordPath);
            
        } catch (ParserConfigurationException
               | SAXException
               | IOException
               | Database.DatabaseException
               | SQLException ex) {
            Logger.getLogger(DatabaseImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void copyFiles(String xmlPath) {
        Path path = Paths.get(xmlPath);
        try {
            Files.walkFileTree(path.getParent(), new FileImporter());
        } catch (IOException ex) {
            Logger.getLogger(DatabaseImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void usage() {
        System.out.println("Usage: DatabaseImporter [path_to_Records.xml]");
        System.exit(0);
    }

}
