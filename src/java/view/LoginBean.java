/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.UserEntityFacade;
import java.io.Serializable;
import java.util.Objects;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import model.UserEntity;

/**
 *
 * Handles the login process. It has a View context, so the error messages and
 * input from the user will remain if the login is not successful.
 */
@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

    private String username = "";
    private String password = "";
    private boolean errorPassword = false;
    private boolean errorUserName = false;
    private boolean errorDatabase =false;
    private boolean conversationInitiated = false;
    @Inject
    UserBean userbean;
    @Inject
    UserEntityFacade uef;

    public LoginBean() {
    }

    /**
     * Checks if the username exists and the password provided is correct. If they are not, 
     * sets errors to show in the next view.
     * @return navigation command to the next web page. If the login is not successful, returns "logInKO". 
     * Otherwise, depending on the user privileges, "logInOKadmin" or "logInOKuser".
     * 
     */
    public String login() {

        errorPassword = false;
        errorUserName = false;
        errorDatabase = false;
        //Try to find the user
        try {
            UserEntity newUser = uef.find(username);
            if (newUser != null) {
                if (newUser.getPassword().equals(password)) { //Correct password
                    userbean.setUser(newUser);


                    if (userbean.getCredentials().isManageAccountsPermission()) //ADMINISTRATOR
                    {
                        return "logInOKadmin";
                    } else {
                        return "logInOKuser";
                    }
                } else { //Incorrect password
                    errorPassword = true;

                }

            } else {//Not a valid username
                errorUserName = true;

            }
        }catch(EJBException ex){
            errorDatabase=true;
        }
        return null;
    }

    /**
     * 
     * @return true if the password provided is incorrect. false otherwise 
     */
    public boolean isErrorPassword() {
        return errorPassword;
    }

    public void setErrorPassword(boolean errorPassword) {
        this.errorPassword = errorPassword;
    }

    /**
     * 
     * @return true if the username provided does not exist in the system. false otherwise 
     */
    public boolean isErrorUserName() {
        return errorUserName;
    }

    public void setErrorUserName(boolean errorUserName) {
        this.errorUserName = errorUserName;
    }

    public boolean isErrorDatabase() {
        return errorDatabase;
    }

    public void setErrorDatabase(boolean errorDatabase) {
        this.errorDatabase = errorDatabase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoginBean other = (LoginBean) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (this.errorPassword != other.errorPassword) {
            return false;
        }
        if (this.errorUserName != other.errorUserName) {
            return false;
        }
        if (this.errorDatabase != other.errorDatabase) {
            return false;
        }
        if (this.conversationInitiated != other.conversationInitiated) {
            return false;
        }
        if (!Objects.equals(this.userbean, other.userbean)) {
            return false;
        }
        if (!Objects.equals(this.uef, other.uef)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.password);
        hash = 97 * hash + (this.errorPassword ? 1 : 0);
        hash = 97 * hash + (this.errorUserName ? 1 : 0);
        hash = 97 * hash + (this.errorDatabase ? 1 : 0);
        hash = 97 * hash + (this.conversationInitiated ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.userbean);
        hash = 97 * hash + Objects.hashCode(this.uef);
        return hash;
    }
    
    
}
