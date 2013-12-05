/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

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
        
    }
    
    /**
     * Asks the RecordModel for the name of a field.
     * 
     * @param column the column of the desired field.
     * @return the name of the field at the desired column.
     */
    public String getFieldName(int column) {
        return null;
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
        return null;
    }
    
}
