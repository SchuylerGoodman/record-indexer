/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import com.sun.net.httpserver.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            exchange.sendResponseHeaders(200, 0);

            byte[] data = Files.readAllBytes(path);
            BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody());
            out.write(data);
            
            exchange.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DownloadHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
