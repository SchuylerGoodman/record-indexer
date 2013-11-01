/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import java.io.File;
import org.junit.*;

/**
 *
 * @author schuyler
 */
public class DatabaseTest {
    
    public static final String TEST_DATABASE_PATH = "db" + File.separator + "test" + File.separator
                + "test-create-database.sqlite";
    
    private Database database;
    
    public DatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
//    @Before
//    public void setUp() {
//        database = new Database();
//    }
    
    @After
    public void tearDown() {
    }
    
//    @Test
//    public void initialize() {
//        database.initialize();
//    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}