/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

import client.gui.model.communication.AbstractCommunicationSubscriber;
import client.gui.model.communication.CommunicationLinker;
import client.gui.model.record.RecordLinker;
import client.gui.model.record.RecordNotifier;
import client.gui.model.save.AbstractSaveSubscriber;
import client.gui.model.save.SaveLinker;
import client.gui.model.save.settings.IndexerState;
import java.awt.Point;
import java.util.ArrayList;
import shared.communication.DownloadBatch_Result;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class CellModel {
    
    private ArrayList<CellSubscriber> subscribers;
    
    private RecordNotifier recordNotifier;
    
    private int fieldCount;
    private int rowCount;
    
    private ArrayList<Field> fields;
    private int currentRow;
    private int currentColumn;
    private int currentCellWidth;
    private int cellHeight;
    private int currentFirstXCoordinate;
    private int currentFirstYCoordinate;
    private int firstYCoordinate;
    
    public CellModel() {
        subscribers = new ArrayList<>();
    }
    
    protected void link(CommunicationLinker communicationLinker,
                        RecordLinker recordLinker, SaveLinker saveLinker) {
        
        communicationLinker.subscribe(communicationSubscriber);
        
        this.recordNotifier = recordLinker.getRecordNotifier();
        
        saveLinker.subscribe(saveSubscriber);
        
    }
    
    /**
     * Add a subscriber to the subscriber list.
     * 
     * @param subscriber the subscriber to receive notifications.
     */
    protected void subscribe(CellSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    /**
     * Notification receiver for cell selection.
     * 
     * Will send a notification to all subscribers.
     * 
     * @param row the row of the selected cell with origin at 0.
     * @param column the column of the selected cell with origin at 0.
     */
    protected void select(int row, int column) {
        
        this.currentRow = row;
        this.currentColumn = column;
        this.selected(row, column);
        
    }
    
    /**
     * Notification receiver for cell selection.
     * 
     * @param selectedPoint the selected point in the image in world coordinates.
     */
    protected void select(Point selectedPoint) {
        
        int totalHeight = rowCount * cellHeight + firstYCoordinate;
        // If the point was within the bounds of the height of all records
        if (selectedPoint.y > firstYCoordinate && selectedPoint.y < totalHeight) {
            // Find the selected row
            int rowIndex = ( selectedPoint.y - firstYCoordinate ) / cellHeight;
            // Find the selected column
            for (Field f : fields) {
                if (selectedPoint.x > f.xCoordinate()) {
                    if (selectedPoint.x < ( f.xCoordinate() + f.width() )) {
                        // Select this cell
                        selected(rowIndex, f.columnNumber() - 1);
                        return;
                    }
                }
            }
        }
        
    }
    
    /**
     * Sends a notification to all subscribers.
     * 
     * @param row the row of the selected cell with origin at 0.
     * @param column the column of the selected cell with origin at 0.
     */
    private void selected(int row, int column) {
        
        if (row < 0 || column < 0) {
            this.currentCellWidth = 0;
            this.cellHeight = 0;
            this.currentFirstXCoordinate = 0;
            this.currentFirstYCoordinate = 0;
        }
        else {
            Field field = fields.get(column);
            if (field == null) {
                return;
            }

            this.currentCellWidth = field.width();
            this.currentFirstXCoordinate = field.xCoordinate();

            if (cellHeight == 0 || firstYCoordinate == 0) {
                return;
            }

            this.currentFirstYCoordinate = cellHeight * row + firstYCoordinate;
        }
        for (CellSubscriber s : subscribers) {
            s.selected(row, column, cellHeight, currentCellWidth,
                       currentFirstXCoordinate, currentFirstYCoordinate);
        }
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber =
                                        new AbstractCommunicationSubscriber() {

        @Override
        public void setBatch(DownloadBatch_Result result) {
        
            CellModel.this.fields = (ArrayList<Field>) result.fields();
            CellModel.this.fieldCount = result.numFields();
            CellModel.this.rowCount = result.numRecords();
            CellModel.this.cellHeight = result.recordHeight();
            CellModel.this.firstYCoordinate = result.firstYCoord();
            if (!result.equals(new DownloadBatch_Result())) {
                selected(0, 0);
            }
            else {
                selected(-1, -1);
            }
            
        }
                                            
    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {
        
        @Override
        public void setIndexerState(IndexerState state) {
            
            fields = state.fields();
            fieldCount = state.columnNumber();
            rowCount = state.rowNumber();
            cellHeight = state.recordHeight();
            firstYCoordinate = state.firstYCoordinate();
            selected(0, 0);
            
        }
        
    };

}
