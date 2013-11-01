/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.util.logging.*;
import server.API;
import server.Server;
import server.database.Database;
import shared.communication.*;

/**
 * Uses reflection to call the methods passed in by the handlers.
 * 
 * @author schuyler
 */
public class APIHandler {
        
    private static Logger logger;
    
    static {
        logger = Server.logger;
    }
    
    private static final String API_CLASS = "src.server.API";
    
    XStream xstream;
    HttpExchange exchange;
    Database database;
    int response;
    
    public APIHandler(HttpExchange exchange) throws Database.DatabaseException {

        assert exchange != null;

        this.exchange = exchange;
        xstream = new XStream(new DomDriver());
        database = new Database();
        response = HttpURLConnection.HTTP_OK;
    }
    
    /**
     * Runs the handle using reflection
     * 
     * @param apiName Name of the function in server.API to run
     */
    public void run(String apiName) throws IOException {
        
        try {
            RequestParam params = deserialize(); // Get parameters
            database.startTransaction();
            RequestResult result = runMethod(apiName, params); // Run function (reflection)
            int responseCode = validateResult(result); // Validates result and ends transaction
            serialize(result, responseCode);
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error running handler.", ex);
            try {
                database.endTransaction(false);
                response = HttpURLConnection.HTTP_BAD_REQUEST;
            } catch (Database.DatabaseException ex1) {
                logger.log(Level.SEVERE, "Could not close database.", ex1);
                response = HttpURLConnection.HTTP_INTERNAL_ERROR;
            }
            exchange.sendResponseHeaders(response, 0);
        }
        
    }
    
    /**
     * Deserializes the XML parameter passed in by the client
     * 
     * @return RequestParam base class container for the API parameters
     * @throws IOException 
     */
    private RequestParam deserialize() throws IOException {
        
        RequestParam parameters = (RequestParam) xstream.fromXML(exchange.getRequestBody());
        
        return parameters;
    }
    
    /**
     * Gets the API method and runs it.
     * 
     * @param apiName Name of the server.API method to run
     * @param params Parameters needed for the server.API method
     * 
     * @return RequestResult wrapper holding the results of the API
     * @throws Exception 
     */
    private RequestResult runMethod(String apiName, RequestParam params)
            throws Exception {
        
        Method m = getMethod(apiName);
        m.setAccessible(true);
        logger.log(Level.FINE, String.format("Invoking server.API.%s", apiName));
        RequestResult result = (RequestResult) m.invoke(null, database, params);
        logger.log(Level.FINE,
                String.format("server.API.%s returned %s", apiName, result == null ? "null" : "successfully"));
        
        return result;
    }
    
    /**
     * Gets the Method object that represents the API to call.
     * 
     * @param apiName Name of the server.API method to run
     * 
     * @return Method object to invoke
     * @throws Exception 
     */
    private Method getMethod(String apiName) throws Exception {
        
        Class<API> APIclass = API.class;
        API api = APIclass.newInstance();
        
        Method[] allMethods = APIclass.getDeclaredMethods();
        Method rightMethod = null;
        int index = 0;
        while (rightMethod == null && index < allMethods.length) {
            Method currentMethod = allMethods[index];
            String mName = currentMethod.getName();
            if (mName.equals(apiName)) {
                rightMethod = currentMethod;
            }
            ++index;
        }
        
        if (rightMethod == null) {
            logger.log(Level.SEVERE, String.format("Failure finding function %s in class server.API.", apiName));
            throw new Exception("Function not found in class server.API");
        }
        
        return rightMethod;
    }
    
    /**
     * Validates the result returned by runMethod and ends the database transaction.
     * 
     * @param result Result to validate
     * 
     * @return response code to send back to the client
     * @throws server.database.Database.DatabaseException 
     */
    private int validateResult(RequestResult result)
            throws Database.DatabaseException {
        
        int responseCode;
        if (result != null) {
            database.endTransaction(true);
            responseCode = HttpURLConnection.HTTP_OK;
        }
        else {
            responseCode = HttpURLConnection.HTTP_BAD_REQUEST;
            database.endTransaction(false);
        }
        
        return responseCode;
        
    }
    
    /**
     * Serializes the result to send it back to the client.
     * 
     * @param result Results of running the API
     * @param responseCode HTML response code to send back to the client
     * 
     * @throws IOException 
     */
    private void serialize(RequestResult result, int responseCode)
            throws IOException {
        
        exchange.sendResponseHeaders(responseCode, 0);
        
        xstream.toXML(result, exchange.getResponseBody());
        
    }
    
}
