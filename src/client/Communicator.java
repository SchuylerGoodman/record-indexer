package client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import server.Server;
import shared.communication.*;

/**
 * Client Communicator class with a connection to the server.
 * 
 * @author goodm4n
 */
public class Communicator {
    
    public static class CommunicatorException extends Exception {
        public CommunicatorException(String message) {
            super(message);
        }
        public CommunicatorException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
    
    private String protocol;
    private String host;
    private int port;

    public Communicator(String protocol, String host, int port) throws CommunicatorException {
        if (protocol == null || host == null || port < 1) {
            throw new CommunicatorException("Protocol and host cannot be null, and port must be greater than 0.");
        }
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * Queries the database to validate the input shared.communication.ValidateUser_Param object.
     * 
     * @param params a shared.communication.ValidateUser_Param container for
     * the User's username and password.
     * @return shared.communication.ValidateUser_Result container class for a 
     * User matching the input parameters, or null if the parameters did not match a user in the database.
     */
    public ValidateUser_Result validateUser(ValidateUser_Param params) {
        return (ValidateUser_Result) doPost("/ValidateUser", params);
    }
    
    /**
     * Queries the database to find the projects available to this User.
     * 
     * @param params shared.communication.GetProjects_Param container for
     * the User's username and password.
     * @return shared.communication.GetProjects_Result container class for an
     * array of available Projects.
     */
    public GetProjects_Result getProjects(GetProjects_Param params) {
        return (GetProjects_Result) doPost("/GetProjects", params);
    }
    
    /**
     * Queries the database for the URL for a sample image of the given Project.
     * 
     * @param params shared.communication.getSampleImage_Param container for
     * the User's username, password, and the requested Project ID.
     * @return shared.communication.getSampleImage_Result container class
     * for a URL pointing to the sample image or null if the parameters do not 
     * match anything in the database.
     */
    public GetSampleImage_Result getSampleImage(GetSampleImage_Param params) {
        return (GetSampleImage_Result) doPost("/GetSampleImage", params);
    }
    
    /**
     * Sends a request to the server to download the Project, Batch, Fields, 
     * and known records associated with the parameters in batchParams.
     * <p>
     * Assigns this Image to the User.
     * 
     * @param params shared.communication.DownloadBatch_Param container for
     * the User's username and password, and a Project ID.
     * @return shared.communication.DownloadBatch_Result container for a Project
     * object containing all the useful infos, or null if the batchParams did 
     * not match anything in the database.
     */
    public DownloadBatch_Result downloadBatch(DownloadBatch_Param params) {
        return (DownloadBatch_Result) doPost("/DownloadBatch", params);
    }

    /**
     * Attempts to submit the indexed records for the current Image to the database.
     * <p>
     * If successful, the User is un-assigned from this Image.
     * 
     * @param params shared.communication.SubmitBatch_Param container for 
     * the User's username and password, the Image ID, and a collection of 
     * Record objects.
     * @return shared.communication.SubmitBatch_Result container which says if 
     * the submission was successful or not.
     */
    public SubmitBatch_Result submitBatch(SubmitBatch_Param params) {
        return (SubmitBatch_Result) doPost("/SubmitBatch", params);
    }
    
    /**
     * Gets a collection of fields from the database.
     * 
     * @param params shared.communication.GetFields_Param container for 
     * the User's username and password, and an Object specifying from which 
     * Project to get the fields.
     * @return shared.communication.GetFields_Result container for the Collection 
     * of returned Field objects.
     */
    public GetFields_Result getFields(GetFields_Param params) {
        return (GetFields_Result) doPost("/GetFields", params);
    }
    
    /**
     * Searches the database for fields.
     * 
     * @param params shared.communication.Search_Param container for 
     * the User's username and password, a comma-separated list of field ids, 
     * and a comma-separated list of search strings.
     * @return shared.communication.Search_Result container for a Collection 
     * of a Collection of Objects.
     *      Each inner Collection contains:
     *          Integer batchId
     *          URL imageURL
     *          Integer recordNumber
     *          Integer fieldId
     */
    public Search_Result search(Search_Param params) {
        return (Search_Result) doPost("/Search", params);
    }
    
    /**
     * Create a new user in the database.
     * 
     * @param params shared.communication.CreateUser_Param container for all 
     * of the necessary parameters to create a new user.
     * 
     * @return shared.communication.CreateUser_Result container for the new user 
     * id generated on creation.
     */
    public CreateUser_Result createUser(CreateUser_Param params) {
        return (CreateUser_Result) doPost("/CreateUser", params);
    }
    
    public byte[] downloadFile(URL path) {
        return (byte[]) doPost(path.getPath(), null);
    }
    
    private Object doGet(String urlPath) {
        
        Object result = null;
        try {
            URL url = new URL(protocol, host, port, urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            connection.connect();

            XStream xstream = new XStream(new DomDriver());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                 try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                     result = xstream.fromXML(in);
                 }
            }
            else {
                 Logger.getLogger(Communicator.class.getName()).log(
                         Level.WARNING, String.format("Request failed with response code %d", connection.getResponseCode()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * Does a post request to the host and port specified in the Object's members.
     * 
     * @param urlPath Path on the server to the correct handler
     * @param postData Data to send to the server for processing
     * 
     * @return Result from the server (either null or a RequestResult object)
     */
    private Object doPost(String urlPath, Object postData) {
        
        Object result = null;
        try {
            // Build URL
            URL url = new URL(protocol, host, port, urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            
            connection.connect();
            
            // Serialize parameters
            XStream xstream = new XStream(new DomDriver());
            try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                xstream.toXML(postData, out);
            }
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
                    result = xstream.fromXML(in);
                }
            }
            else {
                Logger.getLogger(Communicator.class.getName()).log(
                        Level.WARNING, String.format("Request failed with response code %d", connection.getResponseCode()));
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static void main(String[] args) throws MalformedURLException {
        try {
            Server.main(args);
            Communicator comm = new Communicator("http", "localhost", 6464);
            URL url = new URL("HTTP://localhost:6464/images/1890_image0.png");
            byte[] b = comm.downloadFile(url);
            b = b;
//            ValidateUser_Param param = new ValidateUser_Param("sheila", "parker");
//            ValidateUser_Result res = comm.validateUser(param);
//            System.out.println(res.toString());
        } catch (CommunicatorException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
