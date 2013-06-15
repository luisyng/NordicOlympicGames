
package controller;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *Provides methods to find, edit and remove managed entities from a persistence 
 * context.
 * 
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
/**
     * Creates a new entity in the database.
     * 
     * @param entity entity to be created
     * @throws EJBException the transaction has failed (The entity already exists either it is not a valid entity).
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }
/**
     * Merges changes of a detached entity with the persisted entity. 
     * @param entity 
     * @throws EJBException the transaction has failed (Optimistic locking fails)
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
/**
     * Removes the entity from the persistence store
     * @param entity 
     * @throws EJBException the transaction has failed
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }
/**
     * 
     * @param id primary key from the entity that we want to find
     * @return entity with the id, <code>null</code> if it is not found. 
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }
/**
     * 
     * @return list with all the entities persisted 
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
