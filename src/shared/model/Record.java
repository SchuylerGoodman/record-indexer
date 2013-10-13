package shared.model;

/**
 * Model class to contain database information for a record in memory.
 * 
 * @author schuyler
 */
public class Record {
    
    /**
     * Getter method for the total number of columns in this record.
     * 
     * @return int
     */
    public int totalColumns() {
        return 0;
    }
    
    /**
     * Getter method for the value of the record at a certain column.
     * 
     * @param column the column number
     * @return String the value for this column, or null if it has not yet been 
     * indexed.
     */
    public String getFromColumn(int column) {
        return null;
    }
    
    /**
     * Getter method for the row number of this Record.
     * 
     * @return int
     */
    public int rowNumber() {
        return 0;
    }
    
}
