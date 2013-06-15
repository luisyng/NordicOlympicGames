/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 *Represents the rights that have the user in the web application
 * 
 */


@Embeddable
//It is important to define the access type (property or getter) in an embeddable. 
//This way the map to the data base will be independent from the one used in the entity
//containing the embeddable
@Access(AccessType.FIELD)
public class Credentials implements Serializable {

    private boolean manageAccountsPermission;
    private boolean editRecordsPermission;

    public Credentials() {
        
        editRecordsPermission =false;
        manageAccountsPermission=false;
    }
/**
     * 
     * @return <code>true</code> if the user is allowed to edit participant records,
     * <coce>false</code> if not allowed
     */
    public boolean isEditRecordsPermission() {
        return editRecordsPermission;
    }

    public void setEditRecordsPermission(boolean editRecordsPermission) {
        this.editRecordsPermission = editRecordsPermission;
    }
     /**
     * 
     * @return <code>true</code> if the user is allowed to edit other user permissions,
     * <coce>false</code> if not allowed. Normally the only user with this privilege will 
     * be the master of the system
     */
    public boolean isManageAccountsPermission() {
        return manageAccountsPermission;
    }

    public void setManageAccountsPermission(boolean manageAccountsPermission) {
        this.manageAccountsPermission = manageAccountsPermission;
    }

    
}
