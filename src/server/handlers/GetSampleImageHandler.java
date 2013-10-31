/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.logging.*;
import server.Server;
import server.database.Database;

/**
 *
 * @author schuyler
 */
public class GetSampleImageHandler implements HttpHandler {
    
    private static Logger logger;
    
    static {
        logger = Server.logger;
    }
    
    @Override
    public void handle(HttpExchange exchange) {

        logger.info("GetSampleImage request received.");
        logger.finer(exchange.getRequestHeaders().toString());
        
        try {
            
            HandlerRunner handler = new HandlerRunner(exchange);
            
            handler.run("getSampleImage");
            exchange.close();
            
        } catch (Database.DatabaseException ex) {
            logger.log(Level.SEVERE, "Could not initialize handler in GetSampleImageHandler.", ex);
            exchange.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error closing response body.", ex);
        }

    }

}
