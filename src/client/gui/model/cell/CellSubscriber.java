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
     */
    public void selected(int row, int column);
    
}
