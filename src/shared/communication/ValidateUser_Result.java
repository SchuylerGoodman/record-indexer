package shared.communication;

import java.util.Objects;

/**
 * Communication class for the validateUser API.
 * 
 * @author schuyler
 */
public class ValidateUser_Result {
    
    public static class ValidateUser_ResultException extends Exception {
        public ValidateUser_ResultException(String message) {
            super(message);
        }
    }
    
    private boolean valid;
    private int userId;
    private String firstName;
    private String lastName;
    private int recordsIndexed;
    
    public ValidateUser_Result(boolean inValid) throws ValidateUser_ResultException {
        valid = inValid;
        if (valid) {
            throw new ValidateUser_ResultException(
                    "Cannot return result for valid user without first name, last name, and number of indexed records.");
        }
        userId = 0;
        firstName = null;
        lastName = null;
        recordsIndexed = 0;
    }
    
    public ValidateUser_Result(boolean inValid, int inId, String inFirst, String inLast, int inRecordNum) {
        valid = inValid;
        if (valid) {
            userId = inId;
            firstName = inFirst;
            lastName = inLast;
            recordsIndexed = inRecordNum;
        }
    }
    
    /**
     * Getter method for if the requested user was valid.
     * 
     * @return true if exists, otherwise false.
     */
    public boolean validated() {
        return valid;
    }
    
    /**
     * Getter method for the User ID.
     * 
     * @return user ID
     */
    public int userId() {
        return userId;
    }
    
    /**
     * Getter method for the first name of the User.
     * 
     * @return String
     */
    public String firstName() {
        return firstName;
    }
    
    /**
     * Getter method for the last name of the User.
     * 
     * @return String
     */
    public String lastName() {
        return lastName;
    }
    
    /**
     * Getter method for the number of records indexed by the User.
     * 
     * @return int
     */
    public int recordsIndexed() {
        return recordsIndexed;
    }
    
    @Override
    public int hashCode() {
        
        int tId;
        if (userId == 0) {
            tId = 33;
        }
        else {
            tId = userId;
        }
        int tFirst;
        if (firstName == null) {
            tFirst = 41;
        }
        else {
            tFirst = firstName.length();
        }
        int tLast;
        if (lastName == null) {
            tLast = 37;
        }
        else {
            tLast = lastName.length();
        }
        int tNum;
        if (recordsIndexed == 0) {
            tNum = 49;
        }
        else {
            tNum = recordsIndexed;
        }
        if (valid) {
            return tId * tFirst * tLast * tNum * tNum;
        }
        else {
            return tId + tFirst + tLast * tNum * tNum;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidateUser_Result other = (ValidateUser_Result) obj;
        if (this.valid != other.valid) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (this.recordsIndexed != other.recordsIndexed) {
            return false;
        }
        return true;
    }
    
}
