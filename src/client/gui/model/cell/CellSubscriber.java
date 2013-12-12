/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

/**
 *
 * @author schuyler
 */
public interface CellSubscriber {
    
    /**
     * Process the selection of a cell.
     * 
     * @param row the row number with origin at 0.
     * @param column the column number with origin at 0.
     * @param rowHeight the height of the cell in world coordinates.
     * @param columnWidth the width of the column in world coordinates.
     * @param firstXCoordinate the first X coordinate for this column.
     * @param firstYCoordinate the first Y coordinate for all rows.
     */
    public void selected(int row, int column, int rowHeight, int columnWidth,
                         int firstXCoordinate, int firstYCoordinate);
    
}
