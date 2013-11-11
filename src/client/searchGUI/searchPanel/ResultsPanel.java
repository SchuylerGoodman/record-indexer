/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel;

import client.searchGUI.SearchGui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import shared.communication.Search_Result;

/**
 *
 * @author schuyler
 */
public class ResultsPanel extends JPanel {
    
    private ResultPanel resultPanel;
    private ResultViewPanel resultViewPanel;
    
    public ResultsPanel(ResultPanel.Context resultContext, ResultViewPanel.Context resultViewContext) {
        
        super();
        
        createComponents(resultContext, resultViewContext);
        
    }
    
    private void createComponents(ResultPanel.Context resultContext, ResultViewPanel.Context resultViewContext) {
        
        this.setLayout(new BorderLayout());
        
        resultPanel = new ResultPanel(resultContext);
        resultPanel.setPreferredSize(new Dimension(SearchGui.guiWidth / 3, SearchGui.guiHeight * 2 / 3));
        this.add(resultPanel, BorderLayout.WEST);
        
        resultViewPanel = new ResultViewPanel(resultViewContext);
        resultViewPanel.setPreferredSize(new Dimension(SearchGui.guiWidth * 2 / 3, SearchGui.guiHeight * 2 / 3));
        this.add(resultViewPanel, BorderLayout.CENTER);
        
    }
    
    public void searchCompleted(Search_Result result) {
        resultPanel.searchCompleted(result);
    }
    
    public void downloadImage(String path, Integer imageId) {
        resultViewPanel.downloadImage(path, imageId);
    }
    
}
