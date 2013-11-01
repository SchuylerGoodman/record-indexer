/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.*;
import java.util.logging.*;
import server.Server;

/**
 *
 * @author schuyler
 */
public class DownloadHandler implements HttpHandler {
    
    private static Logger logger;
    
    static {
        logger = Server.logger;
    }
    
    @Override
    public void handle(HttpExchange exchange) {
        
        try {
            logger.info("Download request received.");
            logger.finer(exchange.getRequestHeaders().toString());
            
            Path path = Paths.get("Files" + exchange.getRequestURI().getPath());
            
            int response;
            if (Files.exists(path)) {
                response = HttpURLConnection.HTTP_OK;
            }
            else {
                response = HttpURLConnection.HTTP_NOT_FOUND;
            }
            exchange.sendResponseHeaders(response, 0);

            byte[] data = Files.readAllBytes(path);
            BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody());
            out.write(data);
            out.flush();
            
            exchange.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
            exchange.close();
        }
        
    }
    
}
