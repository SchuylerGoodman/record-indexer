/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.model.cell.*;
import client.gui.model.communication.*;
import client.gui.model.record.*;
import java.awt.Color;
import java.util.*;
import javax.swing.*;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
class HelpPanel extends JPanel {
    
    private CommunicationNotifier communicationNotifier;
    private RecordNotifier recordNotifier;
    
    private JEditorPane editorPane;
    
    private ArrayList<String> helpText;
    
    public HelpPanel(CommunicationLinker communicationLinker,
                     RecordLinker recordLinker, CellLinker cellLinker) {
        
        super();
        
        this.setBackground(Color.WHITE);
        
        this.communicationNotifier = communicationLinker.getCommunicationNotifier();
        
        this.recordNotifier = recordLinker.getRecordNotifier();
        recordLinker.subscribe(recordSubscriber);
        
        cellLinker.subscribe(cellSubscriber);
        
        helpText = new ArrayList<>();

        createComponents();
        
    }
    
    private void createComponents() {
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        editorPane = new JEditorPane();        
        editorPane.setContentType("text/html");
        JScrollPane sp = new JScrollPane(editorPane);
        sp.setPreferredSize(this.getSize());
        
        this.add(sp);
        
    }
    
    private AbstractRecordSubscriber recordSubscriber = new AbstractRecordSubscriber() {

        @Override
        public void setRecords() {
            helpText = new ArrayList<>();
            int fieldCount = recordNotifier.getColumnCount();
            for (int i = 0; i < fieldCount; ++i) {
                helpText.add("");
            }
            editorPane.setText("");
            worker.execute();
        }
        
        @Override
        public void empty() {
            helpText = null;
            editorPane.setText("");
        }
        
        private SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {
                
                ArrayList<Field> fields = recordNotifier.getFields();
                for (Field f : fields) { // Load all help html from server
                    // If we don't have the text yet, load it from the server.
                    if (helpText.get(f.columnNumber() - 1).isEmpty()) {
                        helpText.set(f.columnNumber() - 1, communicationNotifier.downloadHtml(f.helpHtml()));
                    }
                }
                return null;
                
            }
            
        };
        
    };
    
    private CellSubscriber cellSubscriber = new CellSubscriber() {

        @Override
        public void selected(int row, int column, int rowHeight,
                             int columnWidth, int firstXCoordinate,
                             int firstYCoordinate) {

            if (helpText != null && helpText.size() > column) {
                if (!helpText.get(column).isEmpty()) { // If we have the text, set it
                    editorPane.setText(helpText.get(column));
                }
                else { // otherwise, load it from server
                    Field f = recordNotifier.getFieldAt(column);
                    helpText.set(column, communicationNotifier.downloadHtml(f.helpHtml()));
                    editorPane.setText(helpText.get(column));
                }
            }            
            
        }
        
    };
    
}
