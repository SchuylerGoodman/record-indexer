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
import shared.model.*;

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
    
    @Test
    public void downloadBatch() {
        
        // Test valid
        DownloadBatch_Param param = new DownloadBatch_Param("test1", "test1", 1);
        DownloadBatch_Result result = communicator.downloadBatch(param);
        Project project = new Project(1, "1890 Census", 8, 199, 60);
        Image image = new Image(1, "/images/1890_image0.png", "1890_image0", 1, 0);
        ArrayList<Field> fields = new ArrayList<>();
        fields.add(new Field(1, "Last Name", 60, 300, "/fieldhelp/last_name.html", 1, 1, "/knowndata/1890_last_names.txt"));
        fields.add(new Field(2, "First Name", 360, 280, "/fieldhelp/first_name.html", 2, 1, "/knowndata/1890_first_names.txt"));
        fields.add(new Field(3, "Gender", 640, 205, "/fieldhelp/gender.html", 3, 1, "/knowndata/genders.txt"));
        fields.add(new Field(4, "Age", 845, 120, "/fieldhelp/age.html", 4, 1, ""));
        DownloadBatch_Result fakeResult = new DownloadBatch_Result(project, image, fields);
        assertEquals(result, fakeResult);
        
        // Test invalid username and password
        param = new DownloadBatch_Param("fail", "test", 1);
        result = communicator.downloadBatch(param);
        assertNull(result);
        
        // Test can't assign another batch to user with a batch
        param = new DownloadBatch_Param("test1", "test1", 2);
        result = communicator.downloadBatch(param);
        assertNull(result);
        
        // Test invalid project ID
        param = new DownloadBatch_Param("test2", "test2", 5000);
        result = communicator.downloadBatch(param);
        assertNull(result);
        
    }
    
    public void submitBatch() {
        
        // Test valid
        String records = "a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;a,b,c,d;";
        SubmitBatch_Param param = new SubmitBatch_Param("test1", "test1", 1, records);
        SubmitBatch_Result result = communicator.submitBatch(param);
        SubmitBatch_Result fakeResult = new SubmitBatch_Result(true);
        assertEquals(result, fakeResult);
        
        // Test invalid username and password
        param = new SubmitBatch_Param("fail", "test", 2, records);
        result = communicator.submitBatch(param);
        assertNull(result);
        
        // Test invalid number of fields per record
        param = new SubmitBatch_Param("test2", "test2", 21, records + "a,b,c,d;a,b,c,d;");
        result = communicator.submitBatch(param);
        assertNull(result);
        
        // Test invalid number of records
        records = "a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;";
        param = new SubmitBatch_Param("test2", "test2", 21, records);
        result = communicator.submitBatch(param);
        assertNull(result);
        
        // Test valid
        records = records + "a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;a,b,c,d,e;";
        param = new SubmitBatch_Param("test2", "test2", 21, records);
        result = communicator.submitBatch(param);
        assertEquals(result, fakeResult);
        
    }
    
    @Test
    public void getFields() throws GetFields_Result.GetFields_ResultException {
        
        // Test valid
        GetFields_Param param = new GetFields_Param("test1", "test1", 1);
        GetFields_Result result = communicator.getFields(param);
        ArrayList<Integer> projectIds = new ArrayList<>();
        projectIds.add(new Integer(1));
        projectIds.add(new Integer(1));
        projectIds.add(new Integer(1));
        projectIds.add(new Integer(1));
        ArrayList<Integer> fieldIds = new ArrayList<>();
        fieldIds.add(new Integer(1));
        fieldIds.add(new Integer(2));
        fieldIds.add(new Integer(3));
        fieldIds.add(new Integer(4));
        ArrayList<String> fieldTitles = new ArrayList<>();
        fieldTitles.add("Last Name");
        fieldTitles.add("First Name");
        fieldTitles.add("Gender");
        fieldTitles.add("Age");
        GetFields_Result fakeResult = new GetFields_Result(projectIds, fieldIds, fieldTitles);
        assertEquals(result, fakeResult);
        
        // Test valid get all fields
        param = new GetFields_Param("test1", "test1", "");
        result = communicator.getFields(param);
        projectIds.add(new Integer(2));
        projectIds.add(new Integer(2));
        projectIds.add(new Integer(2));
        projectIds.add(new Integer(2));
        projectIds.add(new Integer(2));
        projectIds.add(new Integer(3));
        projectIds.add(new Integer(3));
        projectIds.add(new Integer(3));
        projectIds.add(new Integer(3));
        fieldIds.add(new Integer(5));
        fieldIds.add(new Integer(6));
        fieldIds.add(new Integer(7));
        fieldIds.add(new Integer(8));
        fieldIds.add(new Integer(9));
        fieldIds.add(new Integer(10));
        fieldIds.add(new Integer(11));
        fieldIds.add(new Integer(12));
        fieldIds.add(new Integer(13));
        fieldTitles.add("Gender");
        fieldTitles.add("Age");
        fieldTitles.add("Last Name");
        fieldTitles.add("First Name");
        fieldTitles.add("Ethnicity");
        fieldTitles.add("Last Name");
        fieldTitles.add("First Name");
        fieldTitles.add("Age");
        fieldTitles.add("Ethnicity");
        fakeResult = new GetFields_Result(projectIds, fieldIds, fieldTitles);
        assertEquals(result, fakeResult);
        
        // Test invalid username and password
        param = new GetFields_Param("fail", "test", 1);
        result = communicator.getFields(param);
        assertNull(result);
        
        // Test invalid projectId
        param = new GetFields_Param("test1", "test1", 5000);
        result = communicator.getFields(param);
        assertNull(result);
        
    }
    
    @Test
    public void search() throws Search_Result.Search_ResultException {
        
        // Test valid
        String fields = "10";
        String values = "FOX";
        Search_Param param = new Search_Param("test1", "test1", fields, values);
        Search_Result result = communicator.search(param);
        ArrayList<Integer> imageIds = new ArrayList<>();
        imageIds.add(new Integer(41));
        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add("/images/draft_image0.png");
        ArrayList<Integer> rowNumbers = new ArrayList<>();
        rowNumbers.add(new Integer(0));
        ArrayList<Integer> fieldIds = new ArrayList<>();
        fieldIds.add(new Integer(10));
        Search_Result fakeResult = new Search_Result(imageIds, imagePaths, rowNumbers, fieldIds);
        assertEquals(result, fakeResult);
        
        // Test invalid username and password
        param = new Search_Param("fail", "test", fields, values);
        result = communicator.search(param);
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