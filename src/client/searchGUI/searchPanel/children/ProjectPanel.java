/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.children;

import client.searchGUI.searchPanel.models.ProjectListModel;
import client.searchGUI.SearchGui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class ProjectPanel extends JPanel {

    private Context context;
    
    private ProjectListModel projectListModel;    
    private JList projectList;
    private JScrollPane projectScroller;
    
    public interface Context {

        public GetProjects_Result getProjects();
        
        public void projectSelected(Integer projectId);
        
    }
    
    public ProjectPanel(Context context) {

        super();
        
        assert context != null;
        
        this.context = context;

        createComponents();

    }
    
    private void createComponents() {
        
        this.setLayout(new BorderLayout());
        
        projectListModel = new ProjectListModel();
        
        projectList = new JList(projectListModel);
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectList.setLayoutOrientation(JList.VERTICAL);
        projectList.setVisibleRowCount(-1);
        projectList.addListSelectionListener(projectSelectionListener);
        
        projectScroller = new JScrollPane(projectList);
        projectScroller.setPreferredSize(new Dimension(SearchGui.guiWidth / 2, SearchGui.guiHeight / 3 - StringPanel.PANEL_HEIGHT));
        
        this.add(projectScroller, BorderLayout.CENTER);
        
    }
    
    public void loggedIn(boolean success) {
        
        if (success) {

            GetProjects_Result result = this.context.getProjects();

            if (result != null) {
                projectListModel.replaceAll(result.getProjectNames(), result.getProjectIds());
            }
            
        }
        
    }

    private ListSelectionListener projectSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = projectList.getSelectedIndex();
                String selectedProjectName = (String) projectListModel.getElementAt(selectedIndex);

                context.projectSelected(projectListModel.getProjectIdByName(selectedProjectName));
            }
            
        }
        
    };
    
}
