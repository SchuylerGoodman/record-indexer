/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

import java.util.ArrayList;
import java.util.logging.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import server.database.Database;
import static server.database.tools.parseHandlers.FieldHandler.RECORD;
import shared.model.*;

/**
 *
 * @author schuyler
 */
public class RecordHandler extends DefaultHandler {
    
    private static String VALUE = "VALUE";
    
    private XMLReader reader;
    private Database database;
    private ImageHandler parent;
    private StringBuilder content;
    private int currentColumn;
    private int currentFieldId;
    private int currentRow;
    private int projectId;
    private int imageId;
    
    public RecordHandler(XMLReader reader, Database database, ImageHandler parent,
            int projectId, int imageId, int currentRow) {
        this.reader = reader;
        this.database = database;
        this.parent = parent;
        content = new StringBuilder();
        this.currentColumn = 0;
        this.currentFieldId = 0;
        this.currentRow = currentRow;
        this.projectId = projectId;
        this.imageId = imageId;
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
        
        if (qName.equalsIgnoreCase(VALUE)) {
            try {
                ++currentColumn; // Columns are indexed from 1 to n. I need to fix this.
                database.startTransaction();
                Field tField = new Field();
                tField.setProjectId(projectId);
                tField.setColumnNumber(currentColumn);
                ArrayList<Field> tFields = (ArrayList) database.get(tField);
                // Should only return one match because the projectId and currentColumn
                // combination is constrained to be unique in the database.
                assert tFields.size() == 1;
                
                int firstFieldIndex = 0;
                currentFieldId = tFields.get(firstFieldIndex).fieldId();
                database.insert(new Record(imageId, currentFieldId, currentRow, content.toString()));
                database.endTransaction(true);
            } catch (Database.DatabaseException
                   | Database.GetFailedException
                   | Database.InsertFailedException ex) {
                database.endTransaction(false);
                throw new SAXException(ex.getMessage());
            }
        }
        if (qName.equalsIgnoreCase(RECORD)) {
            reader.setContentHandler(parent);
        }
    }
}
