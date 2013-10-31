/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

import java.util.logging.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import server.database.Database;
import shared.model.User;

/**
 *
 * @author schuyler
 */
public class UserHandler extends DefaultHandler {
    
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String EMAIL = "EMAIL";
    private static final String INDEXEDRECORDS = "INDEXEDRECORDS";
    
    private XMLReader reader;
    private Database database;
    private IndexerHandler parent;
    private User user;
    private StringBuilder content;
    
    public UserHandler(XMLReader reader, Database database, IndexerHandler parent) {
        
        this.reader = reader;
        this.database = database;
        this.parent = parent;
        user = new User();
        content = new StringBuilder();
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes atts)
        throws SAXException {
        
        content.setLength(0);
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        
        content.append(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {

        Logger.getLogger(UserHandler.class.getName()).log(Level.FINER,
                String.format("Setting %s in new user to %s.", qName, content.toString()));
        
        if (qName.equalsIgnoreCase(USERNAME)) {
            user.setUsername(content.toString());
        }
        if (qName.equalsIgnoreCase(PASSWORD)) {
            user.setPassword(content.toString());
        }
        if (qName.equalsIgnoreCase(FIRSTNAME)) {
            user.setFirstName(content.toString());
        }
        if (qName.equalsIgnoreCase(LASTNAME)) {
            user.setLastName(content.toString());
        }
        if (qName.equalsIgnoreCase(EMAIL)) {
            user.setEmail(content.toString());
        }
        if (qName.equalsIgnoreCase(INDEXEDRECORDS)) {
            user.setIndexedRecords(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(IndexerHandler.USER)) {
            try {
                database.startTransaction();
                database.insert(user);
                database.endTransaction(true);
            }
            catch (Database.DatabaseException | Database.InsertFailedException ex) {
                try {
                    database.endTransaction(false);
                } catch (Database.DatabaseException ex1) {
                    Logger.getLogger(FieldHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
                finally {
                    throw new SAXException(ex.getMessage());
                }
            }
            reader.setContentHandler(parent);
        }
    }
}
