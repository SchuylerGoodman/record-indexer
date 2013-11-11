package client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import javax.imageio.ImageIO;
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
    
    private XStream xstream;
    
    private String protocol;
    private String host;
    private int port;

    public Communicator() {
        this("HTTP", null, 0);
    }
    
    public Communicator(String protocol, String host, int port) {
        this.initialize(protocol, host, port);
    }
    
    public final void initialize(String protocol, String host, int port) {
        xstream = new XStream(new DomDriver());
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
    
    private boolean isInitialized() {
        if (this.protocol == null || this.host == null || this.port < 1) {
            return false;
        }
        return true;
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
        return (ValidateUser_Result) doRequest("/ValidateUser", "POST", params);
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
        return (GetProjects_Result) doRequest("/GetProjects", "POST", params);
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
        return (GetSampleImage_Result) doRequest("/GetSampleImage", "POST", params);
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
        return (DownloadBatch_Result) doRequest("/DownloadBatch", "POST", params);
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
        return (SubmitBatch_Result) doRequest("/SubmitBatch", "POST", params);
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
        return (GetFields_Result) doRequest("/GetFields", "POST", params);
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
        return (Search_Result) doRequest("/Search", "POST", params);
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
        return (CreateUser_Result) doRequest("/CreateUser", "POST", params);
    }
    
    public BufferedImage downloadImage(String path) {
        
        BufferedImage image = null;
        try {
            InputStream in = connect(path, "GET", null);
            image = (BufferedImage) ImageIO.read(in);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
        
    }

    /**
     * Does an HTTP request to the host and port specified in the Object's members.
     * 
     * @param urlPath Path on the server to the correct handler
     * @param requestMethod A String containing either "POST" or "GET"
     * @param postData Data to send to the server for processing
     * 
     * @return Result from the server (either null or a RequestResult object)
     */
    private Object doRequest(String urlPath, String requestMethod, Object data) {
        
        Object result = null;
        try {
            InputStream in = connect(urlPath, requestMethod, data);
            if (in != null) {
                result = xstream.fromXML(in);
                in.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }
    
    private InputStream connect(String urlPath, String requestMethod, Object data) {
        
        if (!isInitialized()) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, "Communicator has not been initialized.");
        }
        
        Object result = null;
        try {
            // Build URL
            URL url = new URL(protocol, host, port, urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(requestMethod);

            connection.connect();
            
            // Serialize parameters
            if (requestMethod.equalsIgnoreCase("POST")) {
                try (BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream())) {
                    xstream.toXML(data, out);
                }
            }
            if (!requestMethod.equalsIgnoreCase("POST") && !requestMethod.equalsIgnoreCase("GET")) {
                return null;
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                return in;
            }
            else {
                Logger.getLogger(Communicator.class.getName()).log(
                        Level.WARNING, String.format("Request failed with response code %d", connection.getResponseCode()));
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) throws MalformedURLException, IOException {

//        Server.main(args);
        Communicator comm = new Communicator("http", "localhost", 39641);
//        URL url = new URL("HTTP://localhost:39641/images/1890_image0.png");
        BufferedImage b = comm.downloadImage("/images/1890_image0.png");
        ImageIO.write(b, "png", new File("1890_image0.png"));
//            ValidateUser_Param param = new ValidateUser_Param("sheila", "parker");
//            ValidateUser_Result res = comm.validateUser(param);
//            System.out.println(res.toString());
        
    }
    
}
