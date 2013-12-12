/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.save.settings;

import java.util.ArrayList;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class IndexerState implements DataModel {
    
    // Add state information as members
    private int projectId;
    private int imageId;
    private int recordHeight;
    private int firstYCoordinate;
    private ArrayList<ArrayList<String>> records;
    private ArrayList<Field> fields;
    
    public IndexerState() {
        this.set(0, 0, 0, 0, null, null);
    }
    
    public IndexerState(int projectId, int imageId, int recordHeight,
                        int firstYCoordinate) {
        this.set(projectId, imageId, recordHeight, firstYCoordinate, null, null);
    }
    
    public IndexerState(ArrayList<ArrayList<String>> records, ArrayList<Field> fields) {
        this.set(0, 0, 0, 0, records, fields);
    }
    
    public IndexerState(int projectId, int imageId, int recordHeight,
                        int firstYCoordinate, ArrayList<ArrayList<String>> records,
                        ArrayList<Field> fields) {
        this.set(projectId, imageId, recordHeight, firstYCoordinate, records, fields);
    }
    
    public final void set(int projectId, int imageId, int recordHeight,
                          int firstYCoordinate, ArrayList<ArrayList<String>> records,
                          ArrayList<Field> fields) {
        
        if (records != null && fields != null && !isRecordsValid(records, fields.size())) {
            throw new IllegalArgumentException();
        }
        
        this.projectId = projectId;
        this.imageId = imageId;
        this.recordHeight = recordHeight;
        this.firstYCoordinate = firstYCoordinate;
        this.records = records;
        this.fields = fields;
        
    }
    
    public int projectId() {
        return projectId;
    }
    
    public int imageId() {
        return imageId;
    }
    
    public int recordHeight() {
        return recordHeight;
    }
    
    public int firstYCoordinate() {
        return firstYCoordinate;
    }
    
    public ArrayList<ArrayList<String>> records() {
        return records;
    }
    
    public int rowNumber() {
        return records.size();
    }
    
    public ArrayList<Field> fields() {
        return fields;
    }
    
    public int columnNumber() {
        return fields.size();
    }
    
    /**
     * Combine other indexer states with this one.
     * 
     * Uses indexer state for this first if initialized, otherwise it uses
     * the state from otherState.
     * 
     * @param otherState the IndexerState to combine with this.
     */
    public void combine(IndexerState otherState) {
        
        projectId = this.projectId < 1 ?
                            otherState.projectId : this.projectId;
        
        imageId = this.imageId < 1 ?
                            otherState.imageId : this.imageId;
        
        recordHeight = this.recordHeight < 1 ?
                            otherState.recordHeight : this.recordHeight;
        
        firstYCoordinate = this.firstYCoordinate < 1 ?
                            otherState.firstYCoordinate : this.firstYCoordinate;
        
        records = records == null ?
                            otherState.records : this.records;
        
        fields = fields == null ?
                            otherState.fields : this.fields;
        
    }
    
    /**
     * Makes sure that the records list is the correct size.
     * 
     * @param records The list of all records indexed by row and column.
     * @param columnNumber the number of columns there should be.
     * @return true if correct, otherwise false.
     */
    private boolean isRecordsValid(ArrayList<ArrayList<String>> records,
                                      int columnNumber) {
        
        if (records == null || columnNumber < 1) {
            return false;
        }
        if (records.isEmpty()) {
            return false;
        }
        else {
            for (ArrayList<String> record : records) {
                if (record.size() != columnNumber) {
                    return false;
                }
            }
        }
        return true;
        
    }

    @Override
    public boolean hasData() {
        if (this.projectId == 0 &&
                this.imageId == 0 &&
                this.recordHeight == 0 &&
                this.firstYCoordinate == 0 &&
                this.records == null &&
                this.fields == null) {
            
            return false;
        }
        return true;
    }

}
