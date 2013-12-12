/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.cell.*;
import client.gui.model.record.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Panel for the entering record info as a table.
 *
 * @author schuyler
 */
public class TableEntryPanel extends JPanel {
    
    private final static int WAITING_ON_SELECTION = 0;
    private final static int RECEIVING_SELECTION = 1;
    private final static int SENDING_SELECTION = 2;
    private final static int FINISHED_SELECTION = 3;
    
    private CellNotifier cellNotifier;
    
    private TableEntryModel tableEntryModel;
    private JTable entryTable;
    
    private int selectedRow;
    private int selectedColumn;
    private int selectionAction;
    
    public TableEntryPanel(RecordLinker recordLinker, CellLinker cellLinker) {
        
        super();
        
        this.cellNotifier = cellLinker.getCellNotifier();
        cellLinker.subscribe(cellSubscriber);
        
        selectionAction = WAITING_ON_SELECTION;
        
        createComponents(recordLinker, cellLinker);
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents(RecordLinker recordLinker, CellLinker cellLinker) {

        this.setLayout(new BorderLayout());
        
        TableEntryModel model = new TableEntryModel(recordLinker, cellLinker);
        entryTable = new JTable(model);
        model.setColumnModel(entryTable.getColumnModel());
        entryTable.getSelectionModel().addListSelectionListener(selectionListener);
        entryTable.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
        entryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        entryTable.setGridColor(Color.BLACK);
        entryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entryTable.setCellSelectionEnabled(true);
        entryTable.getTableHeader().setResizingAllowed(true);
        entryTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(entryTable);
        
        this.add(scrollPane, BorderLayout.CENTER);
        
    }
    
    private CellSubscriber cellSubscriber = new CellSubscriber() {

        @Override
        public void selected(int row, int column, int rowHeight,
                             int columnWidth, int firstXCoordinate,
                             int firstYCoordinate) {
            
            if (row >= 0 && column >= 0 && selectionAction != FINISHED_SELECTION) {
                selectionAction = RECEIVING_SELECTION;

                selectedColumn = column + 1;
                entryTable.setColumnSelectionInterval(selectedColumn, selectedColumn);

                selectedRow = row;
                entryTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
            selectionAction = WAITING_ON_SELECTION;
            
        }
        
    };

    private ListSelectionListener selectionListener = new ListSelectionListener() {

        private int numChanges;
        
        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (selectionAction == WAITING_ON_SELECTION) {
                numChanges = 0;
                selectionAction = SENDING_SELECTION;
            }
            if (selectionAction == SENDING_SELECTION && numChanges <= 2) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (!e.getValueIsAdjusting() && !lsm.isSelectionEmpty()) {
                    --numChanges;
                    selectedRow = entryTable.getSelectedRow();
                    selectedColumn = entryTable.getSelectedColumn();
                    if (numChanges <= 0) {
                        int sc = selectedColumn - 1;
                        if (sc < 0) sc = 0;
                        selectionAction = FINISHED_SELECTION;
                        cellNotifier.select(selectedRow, sc);
                    }
                }
                else if(e.getValueIsAdjusting()) {
                    ++numChanges;
                    if (numChanges > 2) numChanges = 2;
                }
            }
            else if (selectionAction == SENDING_SELECTION){
                numChanges = 0;
            }
            
        }
        
    };
    
}
