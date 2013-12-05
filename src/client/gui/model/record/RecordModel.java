/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

import client.gui.model.communication.AbstractCommunicationSubscriber;
import client.gui.model.communication.CommunicationNotifier;
import client.gui.model.save.AbstractSaveSubscriber;
import client.gui.model.save.SaveNotifier;
import client.gui.model.save.settings.IndexerState;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class RecordModel extends AbstractTableModel {
    
    private ArrayList<RecordSubscriber> subscribers;
    
    private SaveNotifier saveNotifier;
    private CommunicationNotifier communicationNotifier;
    
    private ArrayList<ArrayList<String>> records;
    private ArrayList<String> fieldNames;
    
    public RecordModel(CommunicationNotifier communicationNotifier,
                       SaveNotifier saveNotifier) {
        
        this.saveNotifier = saveNotifier;
        this.communicationNotifier = communicationNotifier;
        this.subscribers = new ArrayList<>();
        records = new ArrayList<>();
        fieldNames = new ArrayList<>();
        // Ask for field names
        
    }
    
    /**
     * Factory method for creating RecordLinkers linked to this RecordModel.
     * 
     * @return a new RecordLinker that allows components or classes to link
     * to this RecordModel.
     */
    public RecordLinker createRecordLinker() {
        return new RecordLinker(this);
    }
    
    /**
     * Adds the subscriber to the list of subscribers.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    protected void subscribe(RecordSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Change the value of a record and notify all subscribers.
     * 
     * @param row the row of the record to change.
     * @param column the column of the record to change.
     * @param value the new value of the record.
     */
    protected void changeRecord(int row, int column, String value) {
        
    }
    
    /**
     * Formats the records for submission to the server.
     * 
     * Format:
     * Columns are comma-separated.
     * Rows are semicolon-separated.
     * i.e. a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;
     * 
     * @return the formatted string with all records.
     */
    protected String formatRecords() {
        return null;
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber
                                    = new AbstractCommunicationSubscriber() {
        
        @Override
        public void setBatch(DownloadBatch_Result result) {
            // Set records size
        }
        
        @Override
        public void setFields(GetFields_Result result) {
            // Set fields
        }
        
    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {
        
        @Override
        public void setIndexerState(IndexerState state) {
            // Set records size
        }
        
    };

    //+++++++++++++++++++++++++++TABLE MODEL METHODS++++++++++++++++++++++++++++
    
    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public int getColumnCount() {
        return fieldNames.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return fieldNames.get(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // call changeRecord
    }

}
