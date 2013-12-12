/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record;

import client.gui.model.communication.*;
import client.gui.model.save.*;
import client.gui.model.save.settings.IndexerState;
import java.awt.Frame;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
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
    
    protected boolean needsSuggestion(Point cell) {
        
        if (needsSuggestion != null) {
            String value = records.get(cell.x).get(cell.y);
            if (needsSuggestion.containsKey(cell)) {
                return true;
            }
        }
        return false;
        
    }
    
    protected SuggestionDialog getSuggestionDialog(int row, int column) {
        
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
    
    private boolean isKnownValue(int field, String value) {
        if (dictionaries == null || dictionaries.size() <= field) {
            return true;
        }
        return dictionaries.get(field).contains(value);
    }
    
    private TreeSet<String> getSuggestions(int field, String value) {
        
    }
    
    private AbstractCommunicationSubscriber communicationSubscriber
                                    = new AbstractCommunicationSubscriber() {
        
        @Override
        public void setBatch(DownloadBatch_Result result) {
   
            if (!result.equals(new DownloadBatch_Result())) {

                init(result.numRecords(), result.numFields());
                fields = (ArrayList<Field>) result.fields();
                initDictionaries.execute();
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
            initDictionaries.execute();
            setRecords();
//            checkKnownsWorker.execute();
            
        }
        
        private SwingWorker<Void, Void> checkKnownsWorker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                
                PointHash cell = new PointHash(0, 0);
                for (int field = 0; field < records.size(); ++field) {
                    cell.x = field;
                    ArrayList<String> columns = records.get(field);
                    for (int row = 0; row < columns.size(); ++row) {
                        cell.y = row;
                        String s = columns.get(row);
                        if (!isKnownValue(field, s)) {
                            needsSuggestion.put(cell, getSuggestions(field, s));
                        }
                    }
                }
                return null;
                
            }
            
        };
        
    };
    
    private SwingWorker<Void, Void> initDictionaries = new SwingWorker<Void, Void>() {

        @Override
        protected Void doInBackground() throws Exception {
            
            // Convert known data text files to treeset dictionaries
            // (because String.hashCode() is really slow)
            dictionaries = new ArrayList<>();
            if (fields != null && !fields.isEmpty()) {
                for (int i = 0; i < fields.size(); ++i) {
                    Field f = fields.get(i);
                    TreeSet<String> dictionary = new TreeSet<>();
                    if (f.knownData() != null) {
                        String s = communicationNotifier.downloadHtml(f.knownData());
                        List<String> list = Arrays.asList(s.split(","));
                        dictionary.addAll(list);
                    }
                    dictionaries.add(dictionary);
                }
            }
            
            return null;
            
        }
        
    };
    
    public class SuggestionDialog extends JDialog {
        
        public SuggestionDialog() {
            super((Frame)null, "Suggestions", true);
            
            createComponents();
        }
        
        private void createComponents() {
            
        }
        
    }
    
    public class PointHash extends Point {
        
        public PointHash(int x, int y) {
            super(x, y);
        }
        
        @Override
        public int hashCode() {
            return x * 31 + y * 47;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PointHash) {
                return super.equals(obj);
            }
            return false;
        }
    }

}
