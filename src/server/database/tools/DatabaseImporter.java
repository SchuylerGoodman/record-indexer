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
import server.database.Database;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class DatabaseImporter {

    public DatabaseImporter() throws Database.DatabaseException {}

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
            
        } catch (ParserConfigurationException
               | SAXException
               | IOException
               | Database.DatabaseException
               | SQLException ex) {
            Logger.getLogger(DatabaseImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
