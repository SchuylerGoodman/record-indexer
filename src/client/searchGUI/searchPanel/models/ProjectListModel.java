/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;

/**
 *
 * @author schuyler
 */
public final class ProjectListModel extends AbstractListModel {

    private HashMap<String, Integer> projectMap;
    
    private ArrayList<String> projectNames;

    public ProjectListModel() {
        
        projectMap = new HashMap<>();
        projectNames = new ArrayList<>();
        
    }
    
    public ProjectListModel(List<String> projectNames, List<Integer> projectIds) {
        this.replaceAll(projectNames, projectIds);
    }
    
    @Override
    public int getSize() {
        return projectNames.size();
    }

    @Override
    public Object getElementAt(int index) {
        return projectNames.get(index);
    }
    
    public Integer getProjectIdByName(String projectName) {
        return projectMap.get(projectName);
    }
    
    public void replaceAll(List<String> projectNames, List<Integer> projectIds) {
        
        this.projectNames = (ArrayList) projectNames;
        
        projectMap = (HashMap) mapProjects(projectNames, projectIds);
        
        this.fireContentsChanged(this, 0, this.projectNames.size() - 1);
        
    }
    
    private Map<String, Integer> mapProjects(List<String> projectNames, List<Integer> projectIds) {
        
        assert projectNames.size() == projectIds.size();
        
        HashMap<String, Integer> map = new HashMap<>();
        
        int size = projectNames.size();
        
        for (int i = 0; i < size; ++i) {
            if (!map.containsKey(projectNames.get(i))) {
                map.put(projectNames.get(i), projectIds.get(i));
            }
        }
        
        return map;
        
    }
    
}
