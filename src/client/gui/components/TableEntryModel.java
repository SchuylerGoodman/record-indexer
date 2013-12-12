/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.cell.*;
import client.gui.model.record.*;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.*;

/**
 * Table Model for TableEntryPanel
 *
 * @author schuyler
 */
public class TableEntryModel extends AbstractTableModel {
    
    private RecordNotifier recordNotifier;
    
    private TableColumnModel cm;
    
    public TableEntryModel(RecordLinker recordLinker, CellLinker cellLinker) {
        
        this.recordNotifier = recordLinker.getRecordNotifier();
        recordLinker.subscribe(recordSubscriber);
        
    }

    @Override
    public int getRowCount() {
        return recordNotifier.getRowCount();
    }

    @Override
    public int getColumnCount() {
        
        int columns = recordNotifier.getColumnCount();
        if (columns != 0) {
            columns += 1;
        }
        return columns;
        
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Record Number";
        }
        return recordNotifier.getColumnName(columnIndex - 1);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex;
        }
        return recordNotifier.getValueAt(rowIndex, columnIndex - 1);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        recordNotifier.changeRecord(rowIndex, columnIndex - 1, (String) aValue);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        
        if (columnIndex == 0) {
            return false;
        }
        return true;
        
    }
    
    public void setColumnModel(TableColumnModel cm) {
        this.cm = cm;
    }
    
    private void changeAll() {
        
        TableEntryModel.this.fireTableStructureChanged();
        TableEntryModel.this.fireTableDataChanged();      
        
        for (int i = 0; i < cm.getColumnCount(); ++i) {
            TableColumn column = cm.getColumn(i);
            column.setCellRenderer(new CellRenderer());
        }
        
    }
    
    private RecordSubscriber recordSubscriber = new RecordSubscriber() {

        @Override
        public void recordChanged(int row, int column, String newValue) {
            TableEntryModel.this.fireTableCellUpdated(row, column);
        }

        @Override
        public void setRecords() {
            changeAll();
        }
        
        @Override
        public void empty() {
            changeAll();
        }
        
    };

    private class CellRenderer extends DefaultTableCellRenderer {

        private Color blueOpaque;
        
        public CellRenderer() {
            
            super();
            
            blueOpaque = new Color(0, 0, 255, 64);
            
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            
            // TODO - Add suggestions (red background)
            Component returnedComponent = 
                   super.getTableCellRendererComponent(table, value, isSelected,
                                                       hasFocus, row, column);

            if (column == 0) {
                ((JLabel)returnedComponent).setHorizontalAlignment(JLabel.RIGHT);
            }
            if (isSelected) {
                returnedComponent.setBackground(blueOpaque);
            }
            else {
                returnedComponent.setBackground(Color.WHITE);
            }
            
            return returnedComponent;
            
        }
        
    }
    
}
