/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.searchGUI.searchPanel.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author schuyler
 */
public final class ResultListModel extends AbstractListModel {
    
    // Maps result names to image paths
    private HashMap<String, String> resultsMap;
    
    private ArrayList<String> results;

    public ResultListModel() {
        
        resultsMap = new HashMap<>();
        results = new ArrayList<>();
        
    }
    
    public ResultListModel(List<String> imagePaths, List<Integer> imageIds, List<Integer> fieldIds, List<Integer> rowNumbers) {
        this.replaceAll(imagePaths, imageIds, fieldIds, rowNumbers);
    }
    
    @Override
    public int getSize() {
        return results.size();
    }

    @Override
    public Object getElementAt(int index) {
        return results.get(index);
    }
    
    public String getImagePathByResultName(String resultName) {
        return resultsMap.get(resultName);
    }
    
    public void replaceAll(List<String> imagePaths, List<Integer> imageIds, List<Integer> fieldIds, List<Integer> rowNumbers) {
        
        this.results = (ArrayList<String>) createResultNames(imageIds, fieldIds, rowNumbers);
        
        mapResults(this.results, imagePaths);
        
        this.fireContentsChanged(this, 0, this.results.size());
        
    }
    
    public Integer parseResultNameForImageId(String resultName) {
        
        int imageIdIndex = "ImageID:".length();
        int fieldIdTextIndex = resultName.indexOf(" FieldID:");
        
        Integer imageId = new Integer(Integer.parseInt(resultName.substring(imageIdIndex, fieldIdTextIndex)));
        return imageId;
        
    }
        
    private List<String> createResultNames(List<Integer> imageIds, List<Integer> fieldIds, List<Integer> rowNumbers) {
        
        assert imageIds.size() == fieldIds.size() && fieldIds.size() == rowNumbers.size();
        
        ArrayList<String> resultNames = new ArrayList<>();
        StringBuilder resultName = new StringBuilder();
        
        int size = imageIds.size();
        for (int i = 0; i < size; ++i) {
            resultName.setLength(0);
            resultName.append("ImageID:").append(imageIds.get(i));
            resultName.append(" FieldID:").append(fieldIds.get(i));
            resultName.append(" Row:").append(rowNumbers.get(i).intValue() + 1);
            resultNames.add(resultName.toString());
        }
        
        return resultNames;
        
    }

    private void mapResults(List<String> resultNames, List<String> imagePaths) {
        
        assert resultNames.size() == imagePaths.size();
        
        int size = resultNames.size();
        
        for (int i = 0; i < size; ++i) {
            if (!resultsMap.containsKey(resultNames.get(i))) {
                resultsMap.put(resultNames.get(i), imagePaths.get(i));
            }
        }
        
    }
    
}
