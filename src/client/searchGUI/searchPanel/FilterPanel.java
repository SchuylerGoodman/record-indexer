/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import client.searchGUI.SearchGui;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import shared.communication.Search_Param;

/**
 *
 * @author schuyler
 */
public class FilterPanel extends JPanel {
    
    private ProjectFieldPanel pfPanel;
    private StringPanel stringPanel;
    
    public FilterPanel(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext, StringPanel.Context stringContext) {
        
        super();
        
        createComponents(projectContext, fieldContext, stringContext);
        
    }
    
    private void createComponents(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext, StringPanel.Context stringContext) {
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        pfPanel = new ProjectFieldPanel(projectContext, fieldContext);
        pfPanel.setPreferredSize(new Dimension(SearchGui.guiWidth, SearchGui.guiHeight / 3 - StringPanel.PANEL_HEIGHT));

        this.add(pfPanel);
        
        stringPanel = new StringPanel(stringContext);
        stringPanel.setPreferredSize(new Dimension(SearchGui.guiWidth, StringPanel.PANEL_HEIGHT));
        
        this.add(stringPanel);
        
    }
    
    public void loggedIn(boolean success) {
        pfPanel.loggedIn(success);
    }
    
    public void projectSelected(Integer projectId) {
        pfPanel.projectSelected(projectId);
    }
    
    public Search_Param getSearchParameters(String username, String password) {
        
        String selectedIds = pfPanel.selectedFieldIdsToString();
        String searchStrings = stringPanel.getInputString();
        Search_Param params = new Search_Param(username, password, selectedIds, searchStrings);
        
        return params;
        
    }
    
}
