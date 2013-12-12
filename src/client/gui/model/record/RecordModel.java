/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

import client.gui.model.communication.*;
import client.gui.model.record.quality.QualityChecker;
import client.gui.model.record.quality.QualityChecker.PointHash;
import client.gui.model.save.*;
import client.gui.model.save.settings.IndexerState;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import shared.communication.*;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class RecordModel {
    
    private ArrayList<RecordSubscriber> subscribers;
    
    private SaveNotifier saveNotifier;
    private CommunicationNotifier communicationNotifier;
    
    private QualityChecker qualityChecker;
    
    private ArrayList<ArrayList<String>> records;
    private ArrayList<Field> fields;
    private ArrayList<TreeSet<String>> dictionaries;
    private HashMap<PointHash, TreeSet<String>> needsSuggestion;
    
    public RecordModel() {
        
        this.subscribers = new ArrayList<>();
        records = new ArrayList<>();
        fields = new ArrayList<>();
        needsSuggestion = new HashMap<>();
        
    }
    
    protected void link(CommunicationLinker communicationLinker,
                        SaveLinker saveLinker) {
        
        this.communicationNotifier = communicationLinker.getCommunicationNotifier();
        communicationLinker.subscribe(communicationSubscriber);
        
        this.saveNotifier = saveLinker.getSaveNotifier();
        saveLinker.subscribe(saveSubscriber);
        
        qualityChecker = new QualityChecker(communicationLinker);
        
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
        
        if (records.get(row).get(column).equals(value)) {
            return;
        }
        
        records.get(row).set(column, value);
        
        for (RecordSubscriber s : subscribers) {
            s.recordChanged(row, column, value);
        }
        
    }
    
    protected void setRecords() {
        
        for (RecordSubscriber s : subscribers) {
            s.setRecords();
        }
        
    }
    
    /**
     * Getter for the number of rows in the table.
     * 
     * @return the number of rows in the current table.
     */
    protected int getRowCount() {
        return records.size();
    }

    /**
     * Getter for the number of columns in the table.
     * 
     * @return the number of columns in the current table.
     */
    protected int getColumnCount() {
        return fields.size();
    }

    /**
     * Getter for the name of a column.
     * 
     * @param columnIndex the index of the desired column.
     * @return the name of the desired column.
     */
    protected String getColumnName(int columnIndex) {
        return fields.get(columnIndex).title();
    }
    
    /**
     * Getter for the Field corresponding to a column.
     * 
     * @param columnIndex the index of the desired column.
     * @return the Field at the desired column.
     */
    protected Field getFieldAt(int columnIndex) {
        return fields.get(columnIndex);
    }
    
    /**
     * Gets the value of the requested record in the table.
     * 
     * @param rowIndex the row number of the record.
     * @param columnIndex the column number of the record.
     * @return the value of the record.
     */
    protected String getValueAt(int rowIndex, int columnIndex) {
        return records.get(rowIndex).get(columnIndex);
    }
    
    /**
     * Gets the fields for the current project.
     * 
     * @return a list of fields for the current project.
     */
    protected ArrayList<Field> getFields() {
        return fields;
    }
    
    /**
     * Empties all record values without saving and then notifies all
     * subscribers.
     */
    protected void empty() {
        
        records = new ArrayList<>();
        fields = new ArrayList<>();
        dictionaries = new ArrayList<>();
        
        for (RecordSubscriber s : subscribers) {
            s.empty();
        }
        
    }
    
    /**
     * Checks if the value at an index is incorrect and needs a suggestion.
     * 
     * @param row the row of the value to check.
     * @param column the column of the value to check.
     * @return true if it needs a suggestion, otherwise false.
     */
    protected boolean needsSuggestion(int row, int column) {
        return qualityChecker.needsSuggestion(row, column);
    }
    
    /**
     * Returns a dialog for displaying the suggestions for and changing a word.
     * 
     * @param row the row of the value to change.
     * @param column the column of the value to change.
     * @return a SuggestionDialog modal window object to call setVisible(true)
     * on. When closed it will dispose of itself.
     */
    protected SuggestionDialog getSuggestionDialog(int row, int column) {
        
        SuggestionDialog dialog = null;
        if (qualityChecker.needsSuggestion(row, column)) {
            String[] suggestions = (String[])qualityChecker.getSuggestions(row, column);
            dialog = new SuggestionDialog(suggestions, row, column);
        }
        return dialog;
        
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
        
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < records.size(); ++x) {
            ArrayList<String> currentList = records.get(x);
            for (int y = 0; y < currentList.size(); ++y) {
                sb.append(currentList.get(y));
                sb.append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), ";");
        }
        return sb.toString();
        
    }
    
    protected URL pathToURL(String path) {
        return communicationNotifier.pathToURL(path);
    }
    
    private void init(int numRecords, int numFields) {
        
        records = new ArrayList<>(numRecords);
        ArrayList<String> emptyFields = new ArrayList<>();
        for (int i = 0; i < numFields; ++i) {
            emptyFields.add("");
        }
        for (int j = 0; j < numRecords; ++j) {
            records.add(new ArrayList<>(emptyFields));
        }
        
    }

    private AbstractCommunicationSubscriber communicationSubscriber
                                    = new AbstractCommunicationSubscriber() {
        
        @Override
        public void setBatch(DownloadBatch_Result result) {
   
            if (!result.equals(new DownloadBatch_Result())) {

                init(result.numRecords(), result.numFields());
                fields = (ArrayList<Field>) result.fields();
                qualityChecker.init(records, fields);
                setRecords();
                
            }
            else {
                empty();
            }
            
            
        }
        
    };
    
    private AbstractSaveSubscriber saveSubscriber = new AbstractSaveSubscriber() {
        
        @Override
        public IndexerState saveIndexerState() {
            if (records == null || records.isEmpty() ||
                    fields == null || fields.isEmpty()) {
                return new IndexerState();
            }
            return new IndexerState(records, fields);
        }
        
        @Override
        public void setIndexerState(IndexerState state) {
            
            records = state.records();
            fields = state.fields();
            qualityChecker.init(records, fields);
            setRecords();
            
        }
   
    };

    public class SuggestionDialog extends JDialog {
        
        private static final int DIALOG_WIDTH = 252;
        private static final int DIALOG_HEIGHT = 215;
        
        private JList suggestionList;
        
        private int row;
        private int column;
        
        public SuggestionDialog(String[] suggestions, int row, int column) {
            
            super((Frame)null, "Suggestions", true);
            
            this.row = row;
            this.column = column;
            
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
            this.setLocation(600, 300);
            
            createComponents(suggestions);

//            this.setLocation(Client.SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
//                             Client.SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2);
            this.pack();
            this.setResizable(false);
            
        }
        
        private void createComponents(String[] suggestions) {
            
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            Dimension sides = new Dimension(37, 0);
            
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
            listPanel.add(Box.createRigidArea(sides));
            
            suggestionList = new JList(suggestions);
            suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(suggestionList);
            listPanel.add(scrollPane);
            
            listPanel.add(Box.createRigidArea(sides));
            mainPanel.add(listPanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(cancelListener);
            buttonPanel.add(cancel);
            
            buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            
            JButton useSuggestion = new JButton("Use Suggestion");
            buttonPanel.add(useSuggestion);
            useSuggestion.addActionListener(useSuggestionListener);
            
            mainPanel.add(buttonPanel);
            
            this.add(mainPanel);
            
        }
        
        private ActionListener cancelListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuggestionDialog.this.dispose();
            }
            
        };
        
        private ActionListener useSuggestionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                changeRecord(row, column, (String)suggestionList.getSelectedValue());
                SuggestionDialog.this.dispose();
            }
            
        };
        
    }

}
