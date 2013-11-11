/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.children;

import client.searchGUI.SearchGui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author schuyler
 */
public class StringPanel extends JPanel {

    public static final int PANEL_HEIGHT = 40;
    
    private Context context;
    
    private JTextField stringField;
    private JButton searchButton;
    
    private String inputString;

    public interface Context {

        public void runSearch();

    }
    
    public StringPanel(Context context) {
        
        super();
        
        this.context = context;
        
        inputString = "";
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        this.setLayout(new BorderLayout());
        
        stringField = new JTextField();
        stringField.setPreferredSize(new Dimension(SearchGui.guiWidth - 100, PANEL_HEIGHT));
        stringField.setToolTipText("Enter comma- or space-separated values to search in the records.");
        this.add(stringField, BorderLayout.CENTER);
        
        searchButton = new JButton();
        searchButton.setText("Search");
        searchButton.setPreferredSize(new Dimension(100, PANEL_HEIGHT));
        searchButton.setToolTipText("Click to search!");
        searchButton.addActionListener(searchListener);
        this.add(searchButton, BorderLayout.EAST);
        
    }
    
    public String getInputString() {
        return inputString;
    }
    
    private String parseInputString(String inputString) {
        
        String[] params = inputString.split("[\\s,]+");
        StringBuilder output = new StringBuilder();
        for (String s : params) {
            if (output.length() != 0) {
                output.append(",");
            }
            output.append(s);
        }
        return output.toString();
        
    }

    private ActionListener searchListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            inputString = parseInputString(stringField.getText());
            context.runSearch();
            
        }
        
    };
    
}

