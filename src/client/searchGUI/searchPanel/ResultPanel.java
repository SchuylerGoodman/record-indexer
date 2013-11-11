/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import client.searchGUI.SearchGui;
import client.searchGUI.ErrorLabel;
import client.searchGUI.searchPanel.models.ResultListModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import shared.communication.Search_Result;

/**
 *
 * @author schuyler
 */
public class ResultPanel extends JPanel {
    
    private Context context;
    
    private ErrorLabel errorLabel;
    private ResultListModel resultListModel;
    private JList resultList;
    private JScrollPane resultScroller;
        
    public interface Context {
        
        public void triggerImageDownload(String path, Integer imageId);
        
    }
    
    public ResultPanel(Context context) {
        
        super();
        
        assert context != null;
        
        this.context = context;
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        this.setLayout(new BorderLayout());
        
        errorLabel = new ErrorLabel();
        this.add(errorLabel, BorderLayout.NORTH);
        
        resultListModel = new ResultListModel();
        
        resultList = new JList(resultListModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setLayoutOrientation(JList.VERTICAL);
        resultList.setVisibleRowCount(-1);
        resultList.addListSelectionListener(resultSelectionListener);
        
        resultScroller = new JScrollPane(resultList);
        resultScroller.setPreferredSize(new Dimension(SearchGui.guiWidth / 3, SearchGui.guiHeight * 2 / 3));
        
        this.add(resultScroller, BorderLayout.CENTER);
        
    }
    
    public void searchCompleted(Search_Result result) {
        
        if (result != null) {
            errorLabel.setErrorText(null);
            
            resultListModel.replaceAll(result.imagePaths(), result.imageIds(),
                                       result.fieldIds(), result.rowNumbers());
        }
        else {
            errorLabel.setErrorText("No search results found.");
        }
        
    }

    private ListSelectionListener resultSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = resultList.getSelectedIndex();
                String selectedResultName = (String) resultListModel.getElementAt(selectedIndex);
                
                context.triggerImageDownload(resultListModel.getImagePathByResultName(selectedResultName),
                                             resultListModel.parseResultNameForImageId(selectedResultName));
            }
            
        }
        
    };
    
}
