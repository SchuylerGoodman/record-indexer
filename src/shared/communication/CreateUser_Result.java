/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.communication;

import java.io.Serializable;

/**
 *
 * @author schuyler
 */
public class CreateUser_Result implements Serializable {
 
    private int userId;
    
    public CreateUser_Result(int userId) {
        this.userId = userId;
    }
    
    public int userId() {
        return userId;
    }
    
}
