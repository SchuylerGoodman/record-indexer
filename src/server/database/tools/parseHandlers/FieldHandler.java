/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

import java.net.*;
import java.util.logging.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import server.database.*;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class FieldHandler extends DefaultHandler {
    
    protected static final String RECORD = "RECORD";
    
    private static String TITLE = "TITLE";
    private static String XCOORD = "XCOORD";
    private static String WIDTH = "WIDTH";
    private static String HELPHTML = "HELPHTML";
    private static String KNOWNDATA = "KNOWNDATA";
    
    private XMLReader reader;
    private Database database;
    private ProjectHandler parent;
    private Field field;
    private StringBuilder content;
    
    public FieldHandler(XMLReader reader, Database database, ProjectHandler parent, int projectId) {
        this.reader = reader;
        this.database = database;
        this.parent = parent;
        field = new Field();
        field.setProjectId(projectId);
        field.setColumnNumber(parent.currentColumn);
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
        
        if (qName.equalsIgnoreCase(TITLE)) {
            field.setTitle(content.toString());
        }
        if (qName.equalsIgnoreCase(XCOORD)) {
            field.setXCoordinate(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(WIDTH)) {
            field.setWidth(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(HELPHTML)) {
            try {
                field.setHelpHtml(new URL(API.URLPREFIX + content.toString()));
            } catch (MalformedURLException ex) {
                throw new SAXException("Invalid URL given for known data path.");
            }
        }
        if (qName.equalsIgnoreCase(KNOWNDATA)) {
            try {
                field.setKnownData(new URL(API.URLPREFIX + content.toString()));
            } catch (MalformedURLException ex) {
                throw new SAXException("Invalid URL given for known data path.");
            }
        }
        if (qName.equalsIgnoreCase(ProjectHandler.FIELD)) {
            try {
                database.startTransaction();
                database.insert(field);
                database.endTransaction(true);
            }
            catch (Database.DatabaseException | Database.InsertFailedException ex) {
                database.endTransaction(false);
                throw new SAXException(ex.getMessage());
            }
            reader.setContentHandler(parent);
        }
    }
}
