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

    // Map used for caching purposes to limit GetFields requests to the server
    // ProjectID -> FieldNames ==> FieldName -> FieldID
    private HashMap<Integer, HashMap<String, Integer>> fieldMap;
    // Fields displayed by the FieldList
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
    
    /**
     * Uses the FieldMap to get all the FieldIds that match the selected Field names.
     * 
     * @param fieldNames List of Field names selected by the user.
     * @return List of Field IDs as Integers
     */
    public List<Integer> getFieldIdsByName(List<String> fieldNames) {
        
        ArrayList<Integer> fieldIds = new ArrayList<>();
        for (String field : fieldNames) {
            fieldIds.add(fieldMap.get(projectId).get(field));
        }
        return fieldIds;
        
    }
    
    /**
     * Attempts to find the Fields belonging to the selected project in the FieldMap.
     * If found, the fields are automatically displayed.
     * 
     * @param projectId Project ID for requested Fields
     * @return true if found, otherwise false
     */
    public boolean useProjectId(Integer projectId) {
        
        if (!containsProjectId(projectId)) {
            return false;
        }
        
        this.projectId = projectId;
        fieldNames = new ArrayList<>(fieldMap.get(projectId).keySet());
        this.fireContentsChanged(this, 0, this.fieldNames.size() - 1);
        return true;
        
    }
    
    /**
     * Replaces the currently viewed Fields with the ones belonging to a
     * certain project.
     * *IMPORTANT* Will use cached values if they exist.
     * 
     * @param projectId ID of the project containing the requested fields
     * @param fieldNames Names of the fields
     * @param fieldIds IDs of the fields, in a 1 to 1 correspondence with fieldNames
     * @return true if replaced, false if not
     */
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
    
    /**
     * Checks to see if the fields for a project are cached.
     * 
     * @param projectId Project ID containing fields to check
     * @return true if they are cached, otherwise false
     */
    private boolean containsProjectId(Integer projectId) {
        return fieldMap.containsKey(projectId);
    }
    
    /**
     * Maps field names to field IDs
     * 
     * @param fieldNames List of field names
     * @param fieldIds List of field IDs, in a 1 to 1 correspondence with fieldNames
     * @return Map of FieldName -> FieldID pairs
     */
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
