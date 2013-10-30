/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handleHttp;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import server.database.*;
import server.database.Database.*;
import server.handleHttp.handlers.*;

/**
 *
 * @author schuyler
 */
public class Server {
    
    public static final int RESPONSE_SUCCESS = 200;
    public static final int RESPONSE_FAILURE = 500;
    public static final int RESPONSE_NOT_FOUND = 404;
    
    private static final int SERVER_PORT_NUMBER = 8080;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    
    private static Logger logger;
    
    static {
            try {
                    initLog();
            }
            catch (IOException e) {
                    System.out.println("Could not initialize log: " + e.getMessage());
            }
    }

    private static void initLog() throws IOException {

            Level logLevel = Level.FINE;

            logger = Logger.getLogger("contactmanager"); 
            logger.setLevel(logLevel);
            logger.setUseParentHandlers(false);

            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(logLevel);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("log.txt", false);
            fileHandler.setLevel(logLevel);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
    }

    private HttpServer server;
    private ValidateUserHandler validateUserHandler;
    private GetProjectsHandler getProjectsHandler;
    private GetSampleImageHandler getSampleImageHandler;
    private DownloadBatchHandler downloadBatchHandler;
    private SubmitBatchHandler submitBatchHandler;
    private GetFieldsHandler getFieldsHandler;
    private SearchHandler searchHandler;

    private Server() {
        validateUserHandler = new ValidateUserHandler();
        getProjectsHandler = new GetProjectsHandler();
        getSampleImageHandler = new GetSampleImageHandler();
        downloadBatchHandler = new DownloadBatchHandler();
        submitBatchHandler = new SubmitBatchHandler();
        getFieldsHandler = new GetFieldsHandler();
        searchHandler = new SearchHandler();
        run();
    }

    private void run() {

        try {
            Database.initialize();
        }
        catch (DatabaseException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        logger.info("Initializing HTTP Server.");

        try {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        server.setExecutor(null);

        server.createContext("/ValidateUser", validateUserHandler);
        server.createContext("/GetProjects", getProjectsHandler);
        server.createContext("/GetSampleImage", getSampleImageHandler);
        server.createContext("/DownloadBatch", downloadBatchHandler);
        server.createContext("/SubmitBatch", submitBatchHandler);
        server.createContext("/GetFields", getFieldsHandler);
        server.createContext("/Search", searchHandler);
        
        logger.info("Starting HTTP Server");
        
        server.start();

    }
    
    public static void main(String[] args) {
        Server serve = new Server();
    }

}
