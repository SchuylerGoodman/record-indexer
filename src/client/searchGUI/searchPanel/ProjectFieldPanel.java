/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import client.searchGUI.SearchGui;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author schuyler
 */
public class ProjectFieldPanel extends JPanel {
    
    private ProjectPanel projectPanel;
    private FieldPanel fieldPanel;
    
    public ProjectFieldPanel(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext) {
        
        super();
        
        createComponents(projectContext, fieldContext);
        
    }
    
    private void createComponents(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext) {
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        projectPanel = new ProjectPanel(projectContext);
        projectPanel.setPreferredSize(new Dimension(SearchGui.guiWidth / 2, SearchGui.guiHeight / 3 - StringPanel.PANEL_HEIGHT));

        this.add(projectPanel);
        
        fieldPanel = new FieldPanel(fieldContext);
        fieldPanel.setPreferredSize(new Dimension(SearchGui.guiWidth / 2, SearchGui.guiHeight / 3 - StringPanel.PANEL_HEIGHT));

        this.add(fieldPanel);
        
    }
    
    public void loggedIn(boolean success) {
        projectPanel.loggedIn(success);
    }
    
    public void projectSelected(Integer projectId) {
        fieldPanel.projectSelected(projectId);
    }
    
    public String selectedFieldIdsToString() {
        return fieldPanel.selectedIdsToString();
    }
    
}
