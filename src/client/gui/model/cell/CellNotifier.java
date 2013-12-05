/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.cell;

/**
 *
 * @author schuyler
 */
public class CellNotifier {
    
    private CellModel model;
    
    public CellNotifier(CellModel model) {
        this.model = model;
    }

    /**
     * Tell the CellModel that a cell was selected.
     * 
     * @param row the row of the selected cell with origin at 0.
     * @param column the column of the selected cell with origin at 0.
     */
    public void select(int row, int column) {
        
    }
    
}
