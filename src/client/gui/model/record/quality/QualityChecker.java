/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.model.record.quality;

import client.gui.model.communication.*;
import java.awt.Point;
import java.util.*;
import javax.swing.SwingWorker;
import shared.model.Field;

/**
 *
 * @author schuyler
 */
public class QualityChecker {
    
    private CommunicationNotifier communicationNotifier;
    
    private ArrayList<ArrayList<String>> records;
    private ArrayList<Field> fields;
    private ArrayList<TreeSet<String>> dictionaries;
    
    public QualityChecker(CommunicationLinker communicationLinker) {
        
        if (communicationLinker != null) {
            communicationNotifier = communicationLinker.getCommunicationNotifier();
        }
        
    }
    
    public void init(ArrayList<ArrayList<String>> records, ArrayList<Field> fields) {
        
        if (records == null) {
            throw new NullPointerException();
        }
        this.records = records;
        this.fields = fields;
        if (communicationNotifier != null) {
            initDictionaries.execute();
        }
        
    }
    
    public void initDictionary(ArrayList<TreeSet<String>> dictionaries) {
        this.dictionaries = dictionaries;
    }
    
    public boolean needsSuggestion(int row, int column) {

        if (isKnownValue(column, records.get(row).get(column))) {
            return false;
        }
        return true;
        
    } 
   
    public String[] getSuggestions(int row, int column) {
        
        PointHash cell = new PointHash(row, column);
        String value = records.get(row).get(column);
        if (!isKnownValue(column, value)) {
            TreeSet<String> newSuggestions = createSuggestions(column, value, null);
            return newSuggestions.toArray(new String[newSuggestions.size()]);
        }
        return new String[]{};
        
    }
    
    private boolean isKnownValue(int field, String value) {
        
        if (value.isEmpty()) {
            return true
;        }
                
        if (isAgeField(field)) {
            if (!isValidNumber(value)) {
                return false;
            }
            else {
                return true;
            }
        }

        if (dictionaries == null || dictionaries.size() <= field) {
            return true;
        }
        
        TreeSet<String> dictionary = dictionaries.get(field);
        if (dictionary == null || dictionary.isEmpty()) {
            return true;
        }
        
        return dictionaries.get(field).contains(value);
        
    }
    
    private boolean isAgeField(int field) {
        return fields.get(field).title().equalsIgnoreCase("AGE");
    }
    
    private boolean isValidNumber(String number) {
        return number.matches("\\p{Digit}+");
    }
    
    /**
     * Get all suggested values for the input value to within two alterations.
     * 
     * @param field the field of the input value.
     * @param value the value to get suggestions for.
     * @param inSet TreeSet to which getSuggestions will add found suggestions.
     * @return TreeSet with all added suggestions.
     */
    private TreeSet<String> createSuggestions(int field, String value, TreeSet<String> inSet) {
        
        if (inSet == null) {
            inSet = new TreeSet<>();
        }
        if (!isValidNumber(value)) {
            boolean number = isAgeField(field);
            int distance = 0;
            int maxDistance = 2;
            Set<String> similar = new TreeSet<>();
            similar.add(value.toLowerCase());
            while (distance < maxDistance) {
                distance++;
                Set<String> similar2 = new TreeSet<>();
                for (String s : similar) {
                    if (number && s.matches(".*\\D+.*")) {
                        similar2.add(s.replaceFirst("\\D", ""));
                    }
                    else {
                        this.getDeletion(s, similar2);
                        this.getTransposition(s, similar2);
                    }
                    if (!number) {
                        this.getAlteration(s, similar2, number);
                        this.getInsertion(s, similar2, number);
                    }
                    for (String s2 : similar2) {
                        if (isKnownValue(field, s2)) {
                            if (!s2.isEmpty()) {
                                inSet.add(s2);
                            }
                        }
                    }
                }
                similar.clear();
                similar = similar2;
            }
        }
        return inSet;
        
    }
    
    private Set<String> getDeletion(String word, Set<String> inList) {

        if (inList == null) {
            inList = new TreeSet<>();
        }
        StringBuilder tWord = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            char tchar = word.charAt(i);
            inList.add(tWord.deleteCharAt(i).toString());
            tWord.insert(i, tchar);
        }
        return inList;
        
    }
    
    private Set<String> getTransposition(String word, Set<String> inList) {

        if (inList == null) {
            inList = new TreeSet<>();
        }
        StringBuilder tWord = new StringBuilder(word);
        for (int i = 0; i < word.length() - 1; i++) {
            char tchar0 = word.charAt(i);
            char tchar1 = word.charAt(i + 1);
            tWord.setCharAt(i, tchar1);
            tWord.setCharAt(i + 1, tchar0);
            inList.add(tWord.toString());
            tWord.setCharAt(i, tchar0);
            tWord.setCharAt(i + 1, tchar1);
        }
        return inList;
        
    }
    
    private Set<String> getAlteration(String word, Set<String> inList, boolean number) {

        int charStart = (int)'a';
        int repetitions = 26;
        if (number) {
            charStart = (int)'0';
            repetitions = 10;
        }
        if (inList == null) {
            inList = new TreeSet<>();
        }
        StringBuilder tWord = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            for (int j = 0; j < repetitions; j++) {
                char tchar = (char) (j + charStart);
                tWord.setCharAt(i, tchar);
                inList.add(tWord.toString());
            }
            tWord.setCharAt(i, c);
        }
        return inList;
        
    }
    
    private Set<String> getInsertion(String word, Set<String> inList, boolean number) {

        int charStart = (int)'a';
        int repetitions = 26;
        if (number) {
            charStart = (int)'0';
            repetitions = 10;
        }
        if (inList == null) {
            inList = new TreeSet<>();
        }
        StringBuilder tWord = new StringBuilder(word);
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < repetitions; j++) {
                char tchar = (char) (j + charStart);
                tWord.insert(i, tchar);
                inList.add(tWord.toString());
                tWord.deleteCharAt(i);
            }
        }
        return inList;
        
    }
     
    private SwingWorker<Void, Void> initDictionaries = new SwingWorker<Void, Void>() {

        @Override
        protected Void doInBackground() throws Exception {
            
            // Convert known data text files to treeset dictionaries
            // (because String.hashCode() is really slow)
            dictionaries = new ArrayList<>();
            if (fields != null && !fields.isEmpty()) {
                for (int i = 0; i < fields.size(); ++i) {
                    Field f = fields.get(i);
                    TreeSet<String> dictionary = new TreeSet<>();
                    if (f.knownData() != null) {
                        if (communicationNotifier != null) {
                            String s = communicationNotifier.downloadHtml(f.knownData()).toLowerCase();
                            List<String> list = Arrays.asList(s.split(","));
                            dictionary.addAll(list);
                        }
                    }
                    dictionaries.add(dictionary);
                }
            }
            return null;
            
        }
        
    };

    public class PointHash extends Point {
        
        public PointHash(int x, int y) {
            super(x, y);
        }
        
        @Override
        public int hashCode() {
            return x * 31 + y * 47;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PointHash) {
                return super.equals(obj);
            }
            return false;
        }
    }

}
