package shared.communication;

import java.net.URL;

/**
 * Communication class for the getSampleImage API
 * 
 * @author schuyler
 */
public class GetSampleImage_Result {
    
    private URL imageURL;
    
    public GetSampleImage_Result(URL inURL) {
        imageURL = inURL;
    }
    
    /**
     * Getter method for the sample image URL.
     * 
     * @return URL - the path to the sample image
     */
    public URL imageURL() {
        return null;
    }
    
}
