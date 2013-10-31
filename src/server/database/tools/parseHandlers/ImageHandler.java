/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

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
            int lastSeparator = content.lastIndexOf("/");
            image.setTitle(content.substring(lastSeparator + 1));
            
            if (content.charAt(0) != '/') {
                content.insert(0, '/');
            }
            image.setPath(content.toString());

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
                try {
                    database.endTransaction(false);
                } catch (Database.DatabaseException ex1) {
                    Logger.getLogger(FieldHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
                finally {
                    throw new SAXException(ex.getMessage());
                }
            }
        }
    }
}
