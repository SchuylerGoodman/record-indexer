/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Server;
import server.database.tools.DatabaseImporter;
import shared.communication.*;

/**
 * INTEGRATION tests for client communicator
 * 
 * @author schuyler
 */
public class CommunicatorTest {
    private final static String HOST = "localhost";
    private final static String PORT = "39755";

    private Communicator communicator;
    
    public CommunicatorTest() {
        communicator = new Communicator();
    }
    
    @BeforeClass
    public static void setUpClass() {
        DatabaseImporter.main(new String[]{"Records/Records.xml"});
        Server.main(new String[]{"39755"});
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        communicator.initialize("HTTP", HOST, Integer.parseInt(PORT));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void validateUser() throws ValidateUser_Result.ValidateUser_ResultException {
        
        // Test valid
        ValidateUser_Param param = new ValidateUser_Param("test1", "test1");
        ValidateUser_Result result = communicator.validateUser(param);
        ValidateUser_Result fakeResult = new ValidateUser_Result(true, 1, "Test", "One", 0);
        assertEquals(result, fakeResult);
        
        // Test invalid
        param = new ValidateUser_Param("fail", "test");
        result = communicator.validateUser(param);
        fakeResult = new ValidateUser_Result(false);
        assertEquals(result, fakeResult);
        
    }
    
    @Test
    public void getProjects() throws GetProjects_Result.GetProjects_ResultException {
        
        // Test valid
        GetProjects_Param param = new GetProjects_Param("test1", "test1");
        GetProjects_Result result = communicator.getProjects(param);
        
        ArrayList<String> names = new ArrayList<>();
        names.add("1890 Census");
        names.add("1900 Census");
        names.add("Draft Records");
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        
        GetProjects_Result fakeResult = new GetProjects_Result(ids, names);
        assertEquals(result, fakeResult);
        
        // Test invalid
        param = new GetProjects_Param("fail", "test");
        result = communicator.getProjects(param);
        assertNull(result);
        
    }
    
    @Test
    public void getSampleImage() {
        
        // Test valid
        GetSampleImage_Param param = new GetSampleImage_Param("test1", "test1", 1);
        GetSampleImage_Result result = communicator.getSampleImage(param);
        GetSampleImage_Result fakeResult = new GetSampleImage_Result("/images/1890_image0.png");
        assertEquals(result, fakeResult);
        
        // Test invalid username and password
        param = new GetSampleImage_Param("fail", "test", 1);
        result = communicator.getSampleImage(param);
        assertNull(result);
        
        // Test invalid Project ID
        param = new GetSampleImage_Param("test1", "test1", 5000);
        result = communicator.getSampleImage(param);
        assertNull(result);
        
    }
    
//    @Test
//    public void getFields() {
//        
//        // Test valid - one field
//        GetFields_Param param = new GetFields_Param()
//    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}