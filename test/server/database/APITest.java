/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.database;

import java.util.logging.Level;
import java.util.logging.Logger;
import server.API;
import org.junit.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class APITest {
    
    private API api;
    private Database database;
    
    public APITest() throws API.APIException {
        try {
            Database.initialize();
            database = new Database();
        } catch (Database.DatabaseException ex) {
            Logger.getLogger(APITest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void validateUser() throws ValidateUser_Result.ValidateUser_ResultException {

        String username = "HH";
        String password = "hooops";
        ValidateUser_Param params = new ValidateUser_Param(username, password);
        ValidateUser_Result fakeResult = new ValidateUser_Result(false);
        ValidateUser_Result realResult = API.validateUser(database, params);
        Assert.assertEquals(fakeResult, realResult);
//        
//        ValidateUser_Param params2 = new ValidateUser_Param("test1", "test1");
//        ValidateUser_Result fakeResult2 = new ValidateUser_Result(true, 1, "Test", "One", 8);
//        ValidateUser_Result realResult2 = api.validateUser(params2);
//        Assert.assertEquals(fakeResult2, realResult2);
    }
    
//    @Test
//    public void getProjects() {
//        
//        String username = "test1";
//        String password = "test1";
//        GetProjects_Param params = new GetProjects_Param(username, password);
//        GetProjects_Result result = api.getProjects(params);
//                
//    }
//    
//    @Test
//    public void getSampleImage() {
//        
//        String username = "test1";
//        String password = "test1";
//        GetSampleImage_Param params = new GetSampleImage_Param(username, password, 1);
//        GetSampleImage_Result result = api.getSampleImage(params);
//        
//    }
//    
//    @Test
//    public void downloadBatch() {
//        
//        String username = "test2";
//        String password = "test2";
//        int projectId = 2;
//        DownloadBatch_Param params = new DownloadBatch_Param(username, password, projectId);
//        DownloadBatch_Result result = api.downloadBatch(params);
//    }
//    
//    @Test
//    public void submitBatch() {
//        
//        String username = "test1";
//        String password = "test1";
//        int batchId = 1;
//        String records = "Lee,Robert,M,45;"
//                + "Rea,Botulism,F,15;"
//                + "Kay,Tay,F,42;"
//                + "Lee,Ree,F,31;"
//                + ",,aoe,;"
//                + "Mar,Tar,M,98;"
//                + "The Pirate,Yar,M,56;"
//                + "Lee II,Robert,M,23;";
//        SubmitBatch_Param params = new SubmitBatch_Param(username, password, batchId, records);
//        SubmitBatch_Result result = api.submitBatch(params);
//    }
//    
//    @Test
//    public void getFields() throws API.APIException {
//        String username = "test1";
//        String password = "test1";
//        Integer projectId = new Integer(1);
//        GetFields_Param params = new GetFields_Param(username, password, projectId);
//        GetFields_Result result = api.getFields(params);
//        String projectString = "";
//        params = new GetFields_Param(username, password, projectString);
//        GetFields_Result result2 = api.getFields(params);
//    }
//    
//    @Test
//    public void search() {
//        String username = "test1";
//        String password = "test1";
//        String fields = "10,3";
//        String values = "FR,F";
//        Search_Param params = new Search_Param(username, password, fields, values);
//        Search_Result result = api.search(params);
//    }
    
}