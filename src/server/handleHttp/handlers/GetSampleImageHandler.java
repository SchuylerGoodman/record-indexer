/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handleHttp.handlers;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.*;
import java.util.logging.*;
import server.database.Database;
import server.handleHttp.*;
import shared.communication.*;

/**
 *
 * @author schuyler
 */
public class GetSampleImageHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        int response;
        Database database = null;
        try {

            // De-serialize request body
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            BufferedInputStream in = new BufferedInputStream(exchange.getRequestBody());
            GetSampleImage_Param params = (GetSampleImage_Param) xstream.fromXML(in);
            in.close();

            database = new Database();
            database.startTransaction();
            API api = new API(database);
            GetSampleImage_Result result = api.getSampleImage(params);
            if (result != null) {
                BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody());
                xstream.toXML(result, out);
                out.close();
                database.endTransaction(true);
                response = Server.RESPONSE_SUCCESS;
            }
            else {
                response = Server.RESPONSE_FAILURE;
                database.endTransaction(false);
            }
            
        }
        catch (Database.DatabaseException | IOException ex) {
            response = Server.RESPONSE_FAILURE;
            if (database != null) {
                database.endTransaction(false);
            }
        }
        try {
            exchange.sendResponseHeaders(response, 0);
        }
        catch (IOException ex) {
            Logger.getLogger(GetSampleImageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
