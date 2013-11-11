package shared.communication;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

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
    
    @Override
    public int hashCode() {
        
        return imagePath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GetSampleImage_Result other = (GetSampleImage_Result) obj;
        if (!Objects.equals(this.imagePath, other.imagePath)) {
            return false;
        }
        return true;
    }
    
}
