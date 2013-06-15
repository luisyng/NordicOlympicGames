/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.ParticipantEntity;

/**
 *
 * APIs to get, edit, remove or update ParticipantEntity(s) from the persistence context.
 */
@Stateless
public class ParticipantEntityFacade extends AbstractFacade<ParticipantEntity> {

    @PersistenceContext(unitName = "ProjectID2212PU")
    private EntityManager em;

    public ParticipantEntityFacade() {
        super(ParticipantEntity.class);
    }

    protected EntityManager getEntityManager() {
        return em;
    }
    /**
    * Query the database country statistics
    * @return list with strings with the following layout: "y: x" , where y
    * is a specific country and x is the number of participants in that country
    */
    public List<String> statisticCountry(){
        List<String> list = new ArrayList<>();
        Query q = em.createNamedQuery("participantsCountry");
        List<Object[]> results = q.getResultList();
        StringBuilder sb = new StringBuilder(); 
        for (Object[] result : results) {
            list.add(result[0]+":"+result[1]+"\n");
        }
        return list;
    }
    /**
    * Query the database sport statistics
    * @return list with strings with the following layout: "y: x" , where y
    * is a specific sport and x is the number of participants in that country
    */
    public List<String> statisticSport(){
        List<String> list = new ArrayList<>();
        Query q = em.createNamedQuery("participantsSport");
        List<Object[]> results = q.getResultList();
        StringBuilder sb = new StringBuilder(); 
        for (Object[] result : results) {
            list.add(result[0]+":"+result[1]+"\n");
        }
        return list;
    }
    
//    public int numCountries(){
//        Query q = em.createNamedQuery("numCountries");
//        return q.getFirstResult();
//    } 
    
    /**
     * 
     * @param filterGender 
     * @param filterSport
     * @param filterCountry
     * @return list of <code>ParticipantEntities</code> that match the filters applied
     */
    public List<ParticipantEntity> findFilter(String filterGender, String filterSport, String filterCountry) {
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ParticipantEntity p ");
        if (!filterGender.equals("ALL") || !filterSport.equals("ALL") || !filterCountry.equals("ALL")) { //There is a filter
            query.append("WHERE ");
            if (!filterGender.equals("ALL")) {
                query.append("p.gender = model.enums.Gender.").append(filterGender).append(" ");
            }
            if (!filterSport.equals("ALL")) {
                if(!filterGender.equals("ALL"))
                    query.append("AND ");
                query.append("p.sport = model.enums.Sport.").append(filterSport).append(" ");
            }
            if (!filterCountry.equals("ALL")) {
                if(!filterGender.equals("ALL") || !filterSport.equals("ALL") )
                    query.append("AND ");
                query.append("p.country = model.enums.Country.").append(filterCountry).append(" ");
            }
        }


        Query q = em.createQuery(query.toString(), ParticipantEntity.class);
       
        return  q.getResultList();
    }
}
