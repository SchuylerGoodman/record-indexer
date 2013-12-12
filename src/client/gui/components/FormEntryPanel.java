
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.cell.*;
import client.gui.model.record.*;
import client.gui.model.record.RecordModel.SuggestionDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import shared.model.Field;

/**
 * Panel for entering recorded info as a form.
 *
 * @author schuyler
 */
public class FormEntryPanel extends JPanel {
    
    private final static int WAITING_ON_SELECTION = 0;
    private final static int RECEIVING_SELECTION = 1;
    private final static int SENDING_SELECTION = 2;
    private final static int FINISHED_SELECTION = 3;

    private RecordNotifier recordNotifier;
    private CellNotifier cellNotifier;
    
    private JList recordList;
    private RecordForm recordForm;
    private JScrollPane entryForm;
    
    private ArrayList<Field> fields;
    private int recordCount;
    
    private int selectionAction;
    private int currentRow;
    private int currentColumn;
    
    public FormEntryPanel(RecordLinker recordLinker, CellLinker cellLinker) {
        
        super();
        
        this.recordNotifier = recordLinker.getRecordNotifier();
        recordLinker.subscribe(recordSubscriber);
        
        this.cellNotifier = cellLinker.getCellNotifier();
        cellLinker.subscribe(cellSubscriber);
        
    }
    
    /**
     * Creates components for this panel.
     */
    private void createComponents() {
        
        Dimension thisSize = this.getSize();
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < recordCount; ++i) {
            numbers.add(i);
        }
        
        recordList = new JList(numbers.toArray());
        recordList.addListSelectionListener(selectionListener);
        JScrollPane listPane = new JScrollPane(recordList);
        listPane.setPreferredSize(new Dimension(thisSize.width / 3, thisSize.height));
        
        select(0, 0);
        
        this.add(listPane);
        
        recordForm = new RecordForm(fields, thisSize);
        entryForm = new JScrollPane(recordForm);
        
        this.add(entryForm);
        
        selectionAction = WAITING_ON_SELECTION;
        
    }
    
    /**
     * Set the selected list item and focused text field.
     * 
     * @param row the index of the selected row.
     * @param column the index of the selected column.
     */
    private void select(int row, int column) {
        
        if (recordList != null && entryForm != null) {
            currentColumn = column;
            if (currentRow != row) {
                currentRow = row;
                recordList.setSelectedIndex(row);
            }
            recordForm.setFocusAt(column);
        }
        
    }
    
    /**
     * Set the values for the text fields at the current row.
     * 
     * @param row the index of the selected row.
     * @param column the index of the selected column.
     */
    private void setRowValues(int row, int column) {

        String[] values = new String[fields.size()];
        for (int i = 0; i < fields.size(); ++i) {
            values[i] = recordNotifier.getValueAt(row, i);
        }
        recordForm.setValues(values);
        
    }
    
    /**
     * Set the keyboard focus on the current row and column.
     */
    public void setFocus() {
        
        selectionAction = FINISHED_SELECTION;
        if (recordForm != null) {
            recordForm.setFocusAt(currentColumn);
        }
        selectionAction = WAITING_ON_SELECTION;
        
    }
    
    private RecordSubscriber recordSubscriber = new RecordSubscriber() {

        @Override
        public void recordChanged(int row, int column, String newValue) {
            // If this row is currently selected, update it. Otherwise it will
            // be updated when rows are changed anyways.
            if (currentRow == row) {
                recordForm.setValueAt(newValue, column);
//                if (recordNotifier.needsSuggestion(row, column)) {
//                    recordList.
//                }
            }
        }

        @Override
        public void setRecords() {
            // Initialize the data and components for this panel.
            fields = recordNotifier.getFields();
            recordCount = recordNotifier.getRowCount();
            
            createComponents();
            
        }
        
        @Override
        public void empty() {
            FormEntryPanel.this.removeAll();
            repaint();
        }
        
    };
    
    private CellSubscriber cellSubscriber = new CellSubscriber() {

        @Override
        public void selected(int row, int column, int rowHeight,
                             int columnWidth, int firstXCoordinate,
                             int firstYCoordinate) {
            
            if (row >= 0 && column >= 0 && selectionAction != FINISHED_SELECTION) {
                selectionAction = RECEIVING_SELECTION;
                select(row, column);
            }
            selectionAction = WAITING_ON_SELECTION;
            
        }
        
    };
    
    /**
     * Focus listener for the entry text fields.
     */
    private FocusAdapter focusListener = new FocusAdapter() {

        @Override
        public void focusGained(FocusEvent e) { // Notify GUI to select

            currentColumn = ((FormEntry.EntryField)e.getComponent()).getColumn();
            if (selectionAction == WAITING_ON_SELECTION) {
                selectionAction = SENDING_SELECTION;
            }
            if (selectionAction == SENDING_SELECTION) {
                selectionAction = FINISHED_SELECTION;
                cellNotifier.select(currentRow, currentColumn);
            }

        }
        
        @Override
        public void focusLost(FocusEvent e) { // Save recorded value to records
            String newValue = recordForm.getValueAt(currentColumn);
            recordNotifier.changeRecord(currentRow, currentColumn, newValue);
        }
        
    };
    
    private ListSelectionListener selectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            // Set the current row and fill the text field entries with row data.
            currentRow = recordList.getSelectedIndex();
            setRowValues(currentRow, currentColumn);
            if (selectionAction == WAITING_ON_SELECTION) {
                selectionAction = SENDING_SELECTION;
            }
            if (selectionAction == SENDING_SELECTION) {
                selectionAction = FINISHED_SELECTION;
                cellNotifier.select(currentRow, currentColumn); // Notify GUI to select
            }
                
            
        }
        
    };
    
    private class RecordForm extends JPanel {
        
        private ArrayList<FormEntry> entries;
        
        public RecordForm(ArrayList<Field> fields, Dimension parentSize) {
            
            super();
            
            this.setPreferredSize(new Dimension(parentSize.width * 2 / 3,
                                                parentSize.height));
            
            createComponents(fields);
            
        }
        
        private void createComponents(ArrayList<Field> fields) {
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            entries = new ArrayList<>();
            
            for (int i = 0; i < fields.size(); ++i) {
                FormEntry entry = new FormEntry(fields.get(i).title(), i);
                entries.add(entry);
                this.add(entry);
                this.add(Box.createVerticalGlue());
            }
            
        }
        
        public void setValueAt(String value, int index) {
            entries.get(index).setValueAt(value);
        }
        
        public void setValues(String[] values) {
            for (int i = 0; i < values.length; ++i) {
                setValueAt(values[i], i);
            }
        }
        
        public String getValueAt(int index) {
            return entries.get(index).getValue();
        }
        
        public void setFocusAt(int index) {
            if (index >= entries.size()) {
                index = 0;
            }
            entries.get(index).setFocus();
        }
        
    }
    
    private class FormEntry extends JPanel {
        
        private EntryField entry;
        
        private int column;
        private boolean needsSuggestion;
        
        public FormEntry(String fieldName, int column) {
            
            super();
            
            this.column = column;
            
            createComponents(fieldName);
            
        }
        
        private void createComponents(String fieldName) {
            
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            
            Dimension entrySize = new Dimension(100, 17);
            
            JLabel field = new JLabel(fieldName);
            field.setHorizontalAlignment(JLabel.LEFT);
            field.setMinimumSize(entrySize);
            field.setPreferredSize(entrySize);
            field.setMaximumSize(entrySize);
            this.add(field);
            
            this.add(Box.createHorizontalGlue());
            
            entry = new EntryField();
            entry.setMinimumSize(entrySize);
            entry.setPreferredSize(entrySize);
            entry.setMaximumSize(entrySize);
            entry.addFocusListener(focusListener);
            this.add(entry);
            
            this.add(Box.createHorizontalGlue());
            
        }
        
        public void setNeedsSuggestion(boolean needs) {
            
            if (needs) {
                entry.setBackground(Color.red);
            }
            else {
                entry.setBackground(Color.white);
            }
            needsSuggestion = needs;
            
        }
        
        public int getColumn() {
            return column;
        }
        
        public void setValueAt(String value) {
            entry.setText(value);
            setNeedsSuggestion(recordNotifier.needsSuggestion(currentRow, column));
        }
        
        public String getValue() {
            return entry.getText();
        }
        
        public void setFocus() {
            entry.requestFocusInWindow();
        }
        
        private class EntryField extends JTextField {
            
            private JPopupMenu menuPopup = new JPopupMenu();
            
            public EntryField() {
                
                super();
                this.addKeyListener(keyAdapter);
                this.addMouseListener(mouseAdapter);
                
                JMenuItem suggest = new JMenuItem("See Suggestions");
                
                suggest.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SuggestionDialog sd = recordNotifier.getSuggestionDialog(currentRow, column);
                        sd.setVisible(true);
                    }
                });
                
                menuPopup.add(suggest);
                
            }
            
            public int getColumn() {
                return column;
            }
            
            private KeyAdapter keyAdapter = new KeyAdapter() {
                // Save record when pressing 'Enter'
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == '\n') {
                        recordNotifier.changeRecord(currentRow, column, getText());
                    }
                }

            };
            
            private MouseAdapter mouseAdapter = new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (needsSuggestion && e.getButton() == MouseEvent.BUTTON3) {
                        menuPopup.show(EntryField.this, e.getX(), e.getY());
                    }
                }
                
            };
            
        }
        
    }
    
}
