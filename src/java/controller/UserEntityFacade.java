/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.UserEntity;

/**
 *
 * APIs to get, edit, remove or update UserEntity(s) from the persistence context.
 */
@Stateless
public class UserEntityFacade extends AbstractFacade<UserEntity> {
    @PersistenceContext(unitName = "ProjectID2212PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserEntityFacade() {
        super(UserEntity.class);
    }
    
}
