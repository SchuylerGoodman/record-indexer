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
public final class FieldListModel extends AbstractListModel {

    private HashMap<Integer, HashMap<String, Integer>> fieldMap;
    private ArrayList<String> fieldNames;
    private Integer projectId;
    
    public FieldListModel() {
        
        fieldMap = new HashMap<>();
        fieldNames = new ArrayList<>();
        
    }
    
    public FieldListModel(Integer projectId, List<String> fieldNames, List<Integer> fieldIds) {
        this.replaceAll(projectId, fieldNames, fieldIds);
    }
    
    @Override
    public int getSize() {
        return fieldNames.size();
    }

    @Override
    public Object getElementAt(int index) {
        return fieldNames.get(index);
    }
    
    public List<Integer> getFieldIdsByName(List<String> fieldNames) {
        
        ArrayList<Integer> fieldIds = new ArrayList<>();
        for (String field : fieldNames) {
            fieldIds.add(fieldMap.get(projectId).get(field));
        }
        return fieldIds;
        
    }
    
    public boolean useProjectId(Integer projectId) {
        
        if (!containsProjectId(projectId)) {
            return false;
        }
        
        this.projectId = projectId;
        fieldNames = new ArrayList<>(fieldMap.get(projectId).keySet());
        this.fireContentsChanged(this, 0, this.fieldNames.size() - 1);
        return true;
        
    }
    
    public boolean replaceAll(Integer projectId, List<String> fieldNames, List<Integer> fieldIds) {
        
        if (containsProjectId(projectId)) {
            return useProjectId(projectId);
        }
        
        this.projectId = projectId;
        HashMap<String, Integer> newFieldMapEntry = (HashMap) mapFields(fieldNames, fieldIds);
        fieldMap.put(projectId, newFieldMapEntry);
        
        this.fieldNames = (ArrayList) fieldNames;
        this.fireContentsChanged(this, 0, this.fieldNames.size() - 1);
        return true;
        
    }
    
    private boolean containsProjectId(Integer projectId) {
        return fieldMap.containsKey(projectId);
    }
    
    private Map<String, Integer> mapFields(List<String> fieldNames, List<Integer> fieldIds) {
        
        assert fieldNames.size() == fieldIds.size();
        
        HashMap<String, Integer> map = new HashMap<>();
        
        int size = fieldNames.size();
        
        for (int i = 0; i < size; ++i) {
            if (!map.containsKey(fieldNames.get(i))) {
                map.put(fieldNames.get(i), fieldIds.get(i));
            }
        }
        
        return map;
        
    }
    
}
