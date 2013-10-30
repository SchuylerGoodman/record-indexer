package shared.communication;

/**
 * Communication class for the getSampleImage API
 * 
 * @author schuyler
 */
public class GetSampleImage_Result {
    
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
    
}
