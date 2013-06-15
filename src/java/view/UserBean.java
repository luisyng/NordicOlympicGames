/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Credentials;
import model.UserEntity;

/**
 *
 *
 * Represents a session of a user connected to the system. Contains a <code>UserEntity</code> 
 * and its state of connection (logged or not logged)
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private UserEntity user;
    //STATE
    private boolean logged = false;
    //SYSTEM
    @Inject
    private SystemBean system;

    public UserBean() {
        user = new UserEntity();
    }

    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user that will be represented in the actual session. This user is automatically
     * logged in the system.
     * @param user user that will be logged in the system 
     */
    public void setUser(UserEntity user) {
        if (logged==true) {//I should log out from the SystemBean this user!
            system.logout(this.user.getUsername());
        }
        logged = true;
        system.login(user.getUsername());
        this.user = user;
    }

    /**
     * Logs out the user and redirects it to the index page.
     */
    public String logout() {
        if (user == null) {
            //TODO: Maybe exception?
        }
        system.logout(user.getUsername());
        user = new UserEntity();
        logged = false;
        return "index";
    }

    /**
     * 
     * @return true if the user is logged, false otherwise 
     */
    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
    }

    //NEEDED FOR PROPERTY ACCESS FROM JSF
    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public void setCredentials(Credentials credentials) {
        user.setCredentials(credentials);
    }

    public int hashCode() {
        return user.hashCode();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public Credentials getCredentials() {
        return user.getCredentials();
    }

    
    public boolean equals(Object obj) {
        return user.equals(obj);
    }
    
}
