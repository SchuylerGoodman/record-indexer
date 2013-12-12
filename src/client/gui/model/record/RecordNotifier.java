/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

import java.net.URL;
import java.util.ArrayList;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class RecordNotifier {
    
    private RecordModel model;
    
    public RecordNotifier(RecordModel model) {
        this.model = model;
    }
    
    /**
     * Notify the RecordModel that a record needs to be changed.
     * 
     * @param row the row of the changed record.
     * @param column the column of the changed record.
     * @param newValue the new value of the changed record.
     */
    public void changeRecord(int row, int column, String newValue) {
        model.changeRecord(row, column, newValue);
    }
    
    /**
     * Asks the RecordModel for the records formatted for submission.
     * 
     * Format:
     * Columns are comma-separated.
     * Rows are semicolon-separated.
     * i.e. a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;
     */
    public String getRecordsForSubmission() {
        return model.formatRecords();
    }
    
    /**
     * Asks the RecordModel for the number of rows in the table.
     * 
     * @return the number of rows.
     */
    public int getRowCount() {
        return model.getRowCount();
    }
    
    /**
     * Asks the RecordModel for the number of columns in the table.
     * 
     * @return the number of columns.
     */
    public int getColumnCount() {
        return model.getColumnCount();
    }
    
    /**
     * Asks the RecordModel for the name of a column.
     * 
     * @param columnIndex the column number of the requested name.
     * @return the column name
     */
    public String getColumnName(int columnIndex) {
        return model.getColumnName(columnIndex);
    }
    
    /**
     * Asks the RecordModel for the field that corresponds with a column.
     * 
     * @param columnIndex the column of the requested field.
     * @return the Field model for the column.
     */
    public Field getFieldAt(int columnIndex) {
        return model.getFieldAt(columnIndex);
    }
    
    /**
     * Asks the RecordModel for the value of a record at the specified indeces.
     * 
     * @param rowIndex the row number of the record.
     * @param columnIndex the column number of the record.
     * @return the value of the record.
     */
    public String getValueAt(int rowIndex, int columnIndex) {
        return model.getValueAt(rowIndex, columnIndex);
    }
    
    /**
     * Asks the RecordModel for the fields for the current project.
     * 
     * @return the fields for the current field.
     */
    public ArrayList<Field> getFields() {
        return model.getFields();
    }
    
    /**
     * Asks the RecordModel to empty all indexed record values without saving.
     */
    public void empty() {
        model.empty();
    }
    
    public URL pathToURL(String path) {
        return model.pathToURL(path);
    }
    
}