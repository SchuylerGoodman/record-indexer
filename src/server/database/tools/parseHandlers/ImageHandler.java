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
public class ImageHandler extends DefaultHandler {

    protected static final String RECORD = "RECORD";
    
    private static String FILE = "FILE";
    
    private XMLReader reader;
    private Database database;
    private ProjectHandler parent;
    private Image image;
    private StringBuilder content;
    private int currentRow;
    
    public ImageHandler(XMLReader reader, Database database, ProjectHandler parent, int projectId) {
        this.reader = reader;
        this.database = database;
        this.parent = parent;
        image = new Image();
        image.setProjectId(projectId);
        content = new StringBuilder();
        currentRow = -1;
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes atts)
        throws SAXException {
        
        if (qName.equalsIgnoreCase(RECORD)) {
            tryInsertImage();
            ++currentRow;
            reader.setContentHandler(new RecordHandler(reader, database, this, image.projectId(), image.imageId(), currentRow));
        }
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
        
        if (qName.equalsIgnoreCase(FILE)) {
            try {
                image.setPath(new URL(API.URLPREFIX + content.toString()));
            } catch (MalformedURLException ex) {
                throw new SAXException("Invalid url given for image path.");
            }
            image.setTitle(content.toString());
            tryInsertImage();
        }
        if (qName.equalsIgnoreCase(ProjectHandler.IMAGE)) {
            reader.setContentHandler(parent);
        }
    }
    
    public void tryInsertImage() throws SAXException {
        if (image.imageId() == 0) {
            try {
                database.startTransaction();
                database.insert(image);
                database.endTransaction(true);
            }
            catch (Database.DatabaseException | Database.InsertFailedException ex) {
                database.endTransaction(false);
                throw new SAXException(ex.getMessage());
            }
        }
    }
}
