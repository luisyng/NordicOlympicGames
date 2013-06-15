/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.enums.Country;
import model.enums.Gender;
import model.enums.Sport;

/**
 *
 * Contains the global state of the web application. Every Bean can access this one using the
 * name <code>system</code> in a JNDI lookup or Injection.
 */
@Named("system")
@ApplicationScoped
public class SystemBean implements Serializable{

    private List<String> usersConnected = new ArrayList<>();
    private List<String> sports = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    @PersistenceContext(unitName = "ProjectID2212PU")
    EntityManager em;

    public SystemBean() {
    }

    //USERS
    /**
     * Logs a user as connected in the system. If a user is connected, it can use the chat
     * ability.
     * @param name username of the user logged. It should be unique in the system. Acquiring a username
     * from a <code>UserEntity</code> assures the uniqueness.
     */
    public void login(String name) {
        if (usersConnected.contains(name)) {
            //TODO: Exception? Whatever..
        } else {
            usersConnected.add(name);
        }
    }
    /**
     * Logs out a user from the system. Its name will not appear in the list of connected users.
     * @param name username of the user logged out.
     */
    public void logout(String name) {
        usersConnected.remove(name);
    }

    //GETTERS
    /**
     * 
     * @return array with the countries registered. To be used by the JSF form 
     */
    public Country[] getCountries() {
        return Country.values();
    }
/**
     * 
     * @return array with the sports registered. To be used by the JSF form 
     */
    public Sport[] getSports() {
        return Sport.values();
    }
/**
     * 
     * @return array with the genders registered. To be used by the JSF form. It will
     * remain unchanged unless the gender "Alien" appears.
     */
    public Gender[] getGenders() {
        return Gender.values();
    }
/**
     * 
     * @return list of usernames connected. 
     */
    public String[] otherUsersConnected(String user) {
        String[] uc = usersConnected.toArray(new String[0]);
        List<String> otherUsersConnected = new ArrayList<>();
        for(String s: usersConnected) {
            if(user == null || !user.equals(s)) {
                otherUsersConnected.add(s);
            }
        }
        return otherUsersConnected.toArray(new String[0]);
    }

    //SETTERS
    public void setUsersConnected(List<String> usersConnected) {
    }

    public void setGenders() {//JSF needs it
    }
    public void setSports() {//JSF needs it
    }
    public void setCountries() {//JSF needs it
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemBean other = (SystemBean) obj;
        if (!Objects.equals(this.usersConnected, other.usersConnected)) {
            return false;
        }
        if (!Objects.equals(this.sports, other.sports)) {
            return false;
        }
        if (!Objects.equals(this.countries, other.countries)) {
            return false;
        }
        if (!Objects.equals(this.em, other.em)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.usersConnected);
        hash = 97 * hash + Objects.hashCode(this.sports);
        hash = 97 * hash + Objects.hashCode(this.countries);
        hash = 97 * hash + Objects.hashCode(this.em);
        return hash;
    }
    
    
}
