/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.children;

import client.searchGUI.searchPanel.models.FieldListModel;
import client.searchGUI.SearchGui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import shared.communication.GetFields_Result;

/**
 *
 * @author schuyler
 */
public class FieldPanel extends JPanel {
    
    private Context context;
    
    private Integer projectId;
    private List<String> fieldNames;
    private List<Integer> fieldIds;
    private List<Integer> selectedIds;
    
    private FieldListModel fieldListModel;
    private JList fieldList;
    private JScrollPane fieldScroller;
    
    public interface Context {
        
        public GetFields_Result getFields(Integer projectId);
        
    }
    
    public FieldPanel(Context context) {
        
        super();
        
        assert context != null;
        
        this.context = context;
        this.selectedIds = new ArrayList<>();
        
        createComponents();
        
    }
    
    private void createComponents() {
        
        this.setLayout(new BorderLayout());
        
        fieldListModel = new FieldListModel();
        
        fieldList = new JList(fieldListModel);
        fieldList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        fieldList.setLayoutOrientation(JList.VERTICAL);
        fieldList.setVisibleRowCount(-1);
        fieldList.addListSelectionListener(fieldSelectionListener);
        
        fieldScroller = new JScrollPane(fieldList);
        fieldScroller.setPreferredSize(new Dimension(SearchGui.guiWidth / 2, SearchGui.guiHeight / 3 - StringPanel.PANEL_HEIGHT));
        
        this.add(fieldScroller, BorderLayout.CENTER);
        
    }
    
    public void projectSelected(Integer projectId) {
        
        assert projectId != null;
        
        fieldList.clearSelection();
        this.projectId = projectId;
        if (!fieldListModel.useProjectId(projectId)) {
            GetFields_Result result = context.getFields(projectId);

            if (result != null) {
                fieldNames = result.fieldTitles();
                fieldIds = result.fieldIds();

                fieldListModel.replaceAll(projectId, fieldNames, fieldIds);
            }
        }
        
    }
    
    public List<Integer> getSelectedIds() {
        return selectedIds;
    }
    
    public String selectedIdsToString() {
        
        StringBuilder ids = new StringBuilder();
        for (Integer id : selectedIds) {
            if (ids.length() != 0) {
                ids.append(",");
            }
            ids.append(id.intValue());
        }
        return ids.toString();
        
    }
    
    private ListSelectionListener fieldSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            if (!e.getValueIsAdjusting()) {
                selectedIds = fieldListModel.getFieldIdsByName(fieldList.getSelectedValuesList());
            }
            
        }
        
    };
    
}
