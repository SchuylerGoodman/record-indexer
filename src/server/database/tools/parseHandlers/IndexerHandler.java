/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import server.database.Database;

/**
 *
 * @author schuyler
 */
public class IndexerHandler extends DefaultHandler {

    protected static final String USER = "USER";
    protected static final String PROJECT = "PROJECT";
    
    private XMLReader reader;
    private Database database;
    private String currentTable;
    private int currentProjectId;
    
    private boolean onUser;
    private boolean onProject;
    private boolean onField;
    private boolean onRecord;
    private boolean onImage;
    
    public IndexerHandler(XMLReader reader) throws Database.DatabaseException {
        
        this.reader = reader;
        database = new Database();
        currentTable = null;
        currentProjectId = 0;
        
        onUser = false;
        onProject = false;
        onField = false;
        onRecord = false;
        onImage = false;
        
    }

    @Override
    public void startDocument() throws SAXException {
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes atts)
        throws SAXException {
        if (qName.equalsIgnoreCase(USER)) {
            reader.setContentHandler(new UserHandler(reader, database, this));
        }
        if (qName.equalsIgnoreCase(PROJECT)) {
            reader.setContentHandler(new ProjectHandler(reader, database, this));
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) {
    }
    
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
    }
    
    @Override
    public void endDocument() throws SAXException {
    }
    
}
