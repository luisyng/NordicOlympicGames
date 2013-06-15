/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.UserEntityFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import model.UserEntity;

/**
 *
 * Handles the management of user accounts by the administrator. No concurrency safety
 * is provided. If more than one admin can exist, exception handling should be done on the methods
 * accessing the UserEntityFacade.
 */
@ManagedBean(name="manage")
@ViewScoped
public class ManageUserBean implements Serializable {

    //STATE
    List<UserEntity> users;
    @Inject
    private UserEntityFacade uef;

    public ManageUserBean() {
    }
/**
     * Caches the users from the database. It is not possible to do it in the constructor,
     * since the UserEntityFacade is injected after the construction.
     */
    @PostConstruct
    private void initialize() {
        users = uef.findAll();

    }
 //EDIT USERS
    /**
     * 
     * @param user user to be edited. The changes are persisted.
     */
   
  
    public void edit(UserEntity user) {
        System.out.println("Usuario modificado");
            uef.edit(user);
      
    }
/**
     * 
     * @param user user to be deleted from the system 
     */
    public void remove(UserEntity user) {
       

            users.remove(user);//Remove from the model (acting as a cache)
            uef.remove(user);
       
    }
/**
     * 
     * @return list with all the users connected in the system database. It is a cached list,
     * to have the updated list, use the <code>updateList</code> method.
     */
    public List<UserEntity> getUsers() {
        return users;
    }
    //For JSF
    public void setUsers(List<UserEntity> users) {
        
    }
/**
     * Connects with the database and updates the cache.
     */
    public void updateList() {
        //Merge all changes
        for(UserEntity user:users){
            uef.edit(user);
        }
        //Browse for new users
        users = uef.findAll();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ManageUserBean other = (ManageUserBean) obj;
        if (!Objects.equals(this.users, other.users)) {
            return false;
        }
        if (!Objects.equals(this.uef, other.uef)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.users);
        hash = 53 * hash + Objects.hashCode(this.uef);
        return hash;
    }
    
    
}
