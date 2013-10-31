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
public class CreateUser_Param extends RequestParam implements Serializable {
    
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    
    public CreateUser_Param(String username, String password, String firstName,
                            String lastName, String email) {
        
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public String username() {
        return username;
    }
    
    public String password() {
        return password;
    }
    
    public String firstName() {
        return firstName;
    }
    
    public String lastName() {
        return lastName;
    }
    
    public String email() {
        return email;
    }
    
}
