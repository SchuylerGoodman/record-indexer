package shared.communication;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Communication class for the getSampleImage API
 * 
 * @author schuyler
 */
public class GetSampleImage_Result extends RequestResult implements Serializable {
    
    private String imagePath;
    
    public GetSampleImage_Result(String inPath) {
        imagePath = inPath;
    }
    
    /**
     * Getter method for the sample image URL.
     * 
     * @return URL - the path to the sample image
     */
    public String imagePath() {
        return imagePath;
    }
    
    public String toString(String protocol, String host, int port)
            throws MalformedURLException {
        
        URL url = new URL(protocol, host, port, imagePath);

        return url.toString() + "\n";
    }
    
}
