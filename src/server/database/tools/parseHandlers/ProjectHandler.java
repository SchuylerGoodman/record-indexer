/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database.tools.parseHandlers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import server.database.Database;
import shared.model.Project;

/**
 *
 * @author schuyler
 */
public class ProjectHandler extends DefaultHandler {

    protected static final String FIELD = "FIELD";
    protected static final String IMAGE = "IMAGE";
    
    private static String TITLE = "TITLE";
    private static String RECORDSPERIMAGE = "RECORDSPERIMAGE";
    private static String FIRSTYCOORD = "FIRSTYCOORD";
    private static String RECORDHEIGHT = "RECORDHEIGHT";
    
    private XMLReader reader;
    private Database database;
    private IndexerHandler parent;
    private Project project;
    private StringBuilder content;
    protected int currentColumn;
    
    public ProjectHandler(XMLReader reader, Database database, IndexerHandler parent) {
        this.reader = reader;
        this.database = database;
        this.parent = parent;
        project = new Project();
        content = new StringBuilder();
        currentColumn = 0;
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes atts)
        throws SAXException {
        if (qName.equalsIgnoreCase(FIELD)) {
            if (project.projectId() == 0) {
                insertProject();
            }
            ++currentColumn;
            reader.setContentHandler(new FieldHandler(reader, database, this, project.projectId()));
        }
        if (qName.equalsIgnoreCase(IMAGE)) {
            if (project.projectId() == 0) {
                insertProject();
            }
            reader.setContentHandler(new ImageHandler(reader, database, this, project.projectId()));
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
        
        if (qName.equalsIgnoreCase(TITLE)) {
            project.setTitle(content.toString());
        }
        if (qName.equalsIgnoreCase(RECORDSPERIMAGE)) {
            project.setRecordCount(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(FIRSTYCOORD)) {
            project.setFirstYCoord(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(RECORDHEIGHT)) {
            project.setFieldHeight(Integer.parseInt(content.toString()));
        }
        if (qName.equalsIgnoreCase(IndexerHandler.PROJECT)) {
            reader.setContentHandler(parent);
        }
    }
        
    public void insertProject() throws SAXException {
        try {
            database.startTransaction();
            project = (Project) database.insert(project);
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
