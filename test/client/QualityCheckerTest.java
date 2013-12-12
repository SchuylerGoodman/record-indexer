/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.gui.model.record.quality.QualityChecker;
import java.util.ArrayList;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shared.model.Field;
import shared.model.Record;

/**
 *
 * @author schuyler
 */
public class QualityCheckerTest {
    
    private ArrayList<Field> fields = new ArrayList<>();
    private ArrayList<ArrayList<String>> records = new ArrayList<>();
    private ArrayList<TreeSet<String>> dictionaries = new ArrayList<>();
    private QualityChecker quality = new QualityChecker(null);
    
    public QualityCheckerTest() {
        setUpFields();
        setUpRecords();
        setUpDictionaries();
        quality.init(records, fields);
        quality.initDictionary(dictionaries);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void checkNeedsSuggestion() {
        
        assertTrue(quality.needsSuggestion(0, 0)); // "0a"
        assertFalse(quality.needsSuggestion(0, 1)); // "00"
        assertFalse(quality.needsSuggestion(1, 0)); // "op"
        assertTrue(quality.needsSuggestion(1, 1)); // "555"
        assertTrue(quality.needsSuggestion(2, 0)); // "xyz"
        assertTrue(quality.needsSuggestion(2, 1)); // "9."
        
    }
    
    @Test
    public void checkSuggestions() {
        
        String[] s = quality.getSuggestions(0, 0); // "0a"
        assertTrue(s.length == 7);
        assertArrayEquals(null, s, new String[]{"a", "ab", "b", "op", "py", "x", "zy"});
        
        s = quality.getSuggestions(0, 1); // "00"
        assertTrue(s.length == 0);

        s = quality.getSuggestions(1, 0); // "op"
        assertTrue(s.length == 0);
        
        s = quality.getSuggestions(1, 1); // "a55"
        assertTrue(s.length == 2);
        assertArrayEquals(null, s, new String[]{"5", "55"});

        s = quality.getSuggestions(2, 0); // "xyz"
        assertTrue(s.length == 8);
        assertArrayEquals(null, s, new String[]{"py", "wpz", "x", "xyzaa",
                                                "xzy", "yzk", "zy", "zyx"});
        
        s = quality.getSuggestions(2, 1); // "9."
        assertTrue(s.length == 1);
        assertArrayEquals(null, s, new String[]{"9"});
        
    }
    
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    private void setUpFields() {
        fields.add(new Field("Name", 1, 2, "help.html", 3, 4, "data.html"));
        fields.add(new Field("Age", 2, 3, null, 4, 0, "data2.html"));
    }
    
    private void setUpRecords() {
            ArrayList<String> row = new ArrayList<>();
            row.add("0a"); // needs suggestions
            row.add("00"); // is fine
            records.add(row);
            row = new ArrayList<>();
            row.add("op"); // is fine
            row.add("a55"); // needs suggestions
            records.add(row);
            row = new ArrayList<>();
            row.add("xyz"); // needs suggestions
            row.add("9."); // needs suggestions
            records.add(row);
    }
    
    private void setUpDictionaries() {
        for (Field f : fields) {
            TreeSet<String> columns = new TreeSet<>();
            if (f.title().equals("Name")) {
                columns.add("a"); // deletion
                columns.add("ab"); // deletion and insertion
                columns.add("b"); // deletion and alteration
                columns.add("op"); // Fine as-is
                columns.add("xzy"); // transposition
                columns.add("zyx"); // double transposition
                columns.add("zy"); // transposition and deletion
                columns.add("yzk"); // transposition and alteration
                columns.add("xyzaa"); // double insertion
                columns.add("wpz"); // double alteration
                columns.add("x"); // double deletion
                columns.add("py"); // alteration and deletion
            }
            else {
                columns.add("00"); // Fine as-is (and deletion and insertion)
                columns.add("5"); // double deletion
                columns.add("9"); // single deletion
                // No alteration for number fields
            }
            dictionaries.add(columns);
        }
    }
    
}