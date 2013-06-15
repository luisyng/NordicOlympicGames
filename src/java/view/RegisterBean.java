/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.UserEntityFacade;
import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import model.UserEntity;

/**
 *
 *  * Handles the registration process.It has a View context, so the error messages and
 * input from the user will remain if the login is not successful.
 */
@ManagedBean
@ViewScoped
public class RegisterBean implements Serializable {

    private UserEntity user = new UserEntity();
    @Inject
    private UserBean userBean;
    @Inject
    private UserEntityFacade uef;
    private boolean errorMessage;
 

    public RegisterBean() {
    }

/**
     * Registers a user in the system. Checks if the username exists. If it does, an error flag is set to true, which can be used in 
     * JSF.
     * @return navigation command to the next web page. If the register is successful, returns "registerOK". 
     * Otherwise, returns null(No navigation command, so the view context will be maintained).
     * 
     */
    public String register() {
        errorMessage=false;
        UserEntity u = uef.find(user.getUsername()); //Detached from the Entity Manager!
        if (u == null) {
            
            uef.create(user);
            userBean.setUser(user);
            return "registerOK";
        } else {
            errorMessage = true;
            return null; //returns to the same page
        }

    }

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

    public boolean isErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(boolean errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegisterBean other = (RegisterBean) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.userBean, other.userBean)) {
            return false;
        }
        if (!Objects.equals(this.uef, other.uef)) {
            return false;
        }
        if (this.errorMessage != other.errorMessage) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.user);
        hash = 29 * hash + Objects.hashCode(this.userBean);
        hash = 29 * hash + Objects.hashCode(this.uef);
        hash = 29 * hash + (this.errorMessage ? 1 : 0);
        return hash;
    }

    

   
}
