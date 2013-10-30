package shared.model;

import java.io.Serializable;
import java.util.Objects;
import server.database.Database;

/**
 * Model class to contain database information for a record in memory.
 * 
 * @author schuyler
 */
public class Record extends ModelClass implements Serializable {
    
    private int recordId;
    private int imageId;
    private int fieldId;
    private int rowNumber;
    private String value;
    
    public Record() {
        recordId = 0;
        imageId = 0;
        fieldId = 0;
        rowNumber = 0;
        value = null;
    }
    
    public Record(int inImageId, int inFieldId, int inRowNumber, String inValue) {
        recordId = 0;
        imageId = inImageId;
        fieldId = inFieldId;
        rowNumber = inRowNumber;
        value = inValue;
    }
    
    public Record(int inRecordId, int inImageId, int inFieldId, int inRowNumber, String inValue) {
        recordId = inRecordId;
        imageId = inImageId;
        fieldId = inFieldId;
        rowNumber = inRowNumber;
        value = inValue;
    }
    
    /**
     * Getter method for the record id
     * 
     * @return the record id
     */
    public int recordId() {
        return recordId;
    }
    
    /**
     * Getter method for the image id that this record belongs to.
     * 
     * @return the image id
     */
    public int imageId() {
        return imageId;
    }
    
    /**
     * Getter method for the field id that this record belongs to.
     * 
     * @return the field id
     */
    public int fieldId() {
        return fieldId;
    }
    
    /**
     * Getter method for the row number of this record.
     * 
     * @return the row number
     */
    public int rowNumber() {
        return rowNumber;
    }
    
    /**
     * Getter method for the value of this record.
     * 
     * @return the String value
     */
    public String value() {
        return value;
    }
    
    /**
     * Setter for the record id
     * 
     * @param newId ID to set
     */
    public void setRecordId(int newId) {
        recordId = newId;
    }
    
    /**
     * Setter for the image id to which this record belongs.
     * 
     * @param newId ID to set
     */
    public void setImageId(int newId) {
        imageId = newId;
    }
    
    /**
     * Setter for the field id to which this record belongs.
     * 
     * @param newId ID to set
     */
    public void setFieldId(int newId) {
        fieldId = newId;
    }
    
    /**
     * Setter for the number of the row where this record is contained.
     * 
     * @param newNumber number to set
     */
    public void setRowNumber(int newNumber) {
        rowNumber = newNumber;
    }
    
    /**
     * Setter for the value of the record.
     * 
     * @param newValue the record value
     */
    public void setValue(String newValue) {
        value = newValue;
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
        int prime1 = 37;
        int prime2 = 41;
        int prime3 = 43;
        
        int rId = recordId;
        int nIId = imageId;
        int tFId = fieldId;
        int tRNum = rowNumber;
        int tVal;
        if (rId == 0) {
            rId = prime * prime3;
        }
        if (nIId == 0) {
            nIId = prime;
        }
        if (tFId == 0) {
            tFId = prime1;
        }
        if (tRNum == 0) {
            tRNum = prime2;
        }
        if (value == null) {
            tVal = prime3;
        }
        else {
            tVal = value.length();
        }
        int hash = rId + nIId + tFId + tRNum + tVal;
        
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Record other = (Record) obj;
        if (this.recordId != other.recordId) {
            return false;
        }
        if (this.imageId != other.imageId) {
            return false;
        }
        if (this.fieldId != other.fieldId) {
            return false;
        }
        if (this.rowNumber != other.rowNumber) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canInsert() {
        if (imageId < 1) {
            return false;
        }
        if (fieldId < 1) {
            return false;
        }
        if (rowNumber < 0) {
            return false;
        }
        return true;
    }

    @Override
    public String getTableName() {
        return Database.RECORDS;
    }
    
}
