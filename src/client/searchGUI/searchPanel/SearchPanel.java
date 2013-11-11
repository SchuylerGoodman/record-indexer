/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import client.searchGUI.searchPanel.children.ProjectPanel;
import client.searchGUI.searchPanel.children.ResultViewPanel;
import client.searchGUI.searchPanel.children.FilterPanel;
import client.searchGUI.searchPanel.children.FieldPanel;
import client.searchGUI.searchPanel.children.StringPanel;
import client.searchGUI.searchPanel.children.ResultPanel;
import client.searchGUI.searchPanel.children.ResultsPanel;
import client.searchGUI.SearchGui;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import shared.communication.Search_Param;
import shared.communication.Search_Result;

/**
 *
 * @author schuyler
 */
public class SearchPanel extends JPanel {

    private FilterPanel filterPanel;
    private ResultsPanel resultsPanel;
    
    public SearchPanel(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext,
                       StringPanel.Context stringContext, ResultPanel.Context resultContext,
                       ResultViewPanel.Context resultViewContext) {
        
        super();
        
        createComponents(projectContext, fieldContext, stringContext, resultContext, resultViewContext);
        
    }
    
    private void createComponents(ProjectPanel.Context projectContext, FieldPanel.Context fieldContext,
                                  StringPanel.Context stringContext, ResultPanel.Context resultContext,
                                  ResultViewPanel.Context resultViewContext) {
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        filterPanel = new FilterPanel(projectContext, fieldContext, stringContext);
        filterPanel.setPreferredSize(new Dimension(SearchGui.guiWidth, SearchGui.guiHeight / 3));
        this.add(filterPanel);
        
        resultsPanel = new ResultsPanel(resultContext, resultViewContext);
        resultsPanel.setPreferredSize(new Dimension(SearchGui.guiWidth, SearchGui.guiHeight * 2 / 3));
        this.add(resultsPanel);
        
    }
    
    public void loggedIn(boolean success) {
        filterPanel.loggedIn(success);
    }
    
    public void projectSelected(Integer projectId) {
        filterPanel.projectSelected(projectId);
    }

    public Search_Param getSearchParameters(String username, String password) {
        
        assert username != null && !username.isEmpty();
        assert password != null && !password.isEmpty();
        
        return filterPanel.getSearchParameters(username, password);
        
    }
    
    public void searchCompleted(Search_Result result) {
        resultsPanel.searchCompleted(result);
    }
    
    public void downloadImage(String path, Integer imageId) {
        resultsPanel.downloadImage(path, imageId);
    }

}
