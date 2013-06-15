/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.ParticipantEntityFacade;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import model.ParticipantEntity;

/**
 *Handles all the presentation of participants in a data table, editing removing, 
 * filtering and ordering. It also has an internal cache to minimize queries to 
 * the database. Since it uses a view scope, the same user can have more than
 * one web browser tabs and apply different filters to the data.
 * Depending on the credentials of the users they can edit or not the entries.
 * 
 */
@ManagedBean
@ViewScoped
//@Named
//@SessionScoped
public class ParticipantsBean implements Serializable {

    private boolean errorDatabase = false;
    //STATISTICS
    private List<String> statisticCountry;
    private List<String> statisticSport;
    //FILTER
    private String filterSport = "ALL";
    private String filterCountry = "ALL";
    private String filterGender = "ALL";
    //SORT
    private SortFilterModel<ParticipantEntity> model;
    //STATE
    private ParticipantEntity newParticipant;
    private ParticipantEntity editingParticipant; //Stores the participant that is currently being edited
    private String sortedBy = "";
    List<ParticipantEntity> participants;
    @Inject
    private ParticipantEntityFacade pef;

    public ParticipantsBean() {

        newParticipant = new ParticipantEntity();

    }

    /**
     * Caches the users from the database. It is not possible to do it in the constructor,
     * since the UserEntityFacade is injected after the construction.
     */
    @PostConstruct
    private void initialize() {
        participants = pef.findAll();//If this one fails, let the exception be throwed! Huge problem!
        model = new SortFilterModel<>(new ListDataModel<>(participants));
        statistics();
    }

    /**
     * 
     * @return list of participants, especially formatted to be used with the <code>h:dataTable</code> JSF tag
     */
    public DataModel<ParticipantEntity> getParticipants() {
        return model;
    }
//JSF needs it

    public void setParticipants(DataModel<ParticipantEntity> model) {
    }
//STATISTICS

    /**
     * Updates the statistics with values from the server.
     */
    public void statistics() {
        try {
            statisticCountry = pef.statisticCountry();
            statisticSport = pef.statisticSport();
        } catch (EJBException ex) {
            //TODO : for now, just ignore the exception. Probably the user will hit the update button again, and things will go well.
            errorDatabase = true;
        }
    }

    /**
     * 
     * @return list with strings with the following layout: "y: x" , where y
     * is a specific country and x is the number of participants in that country
     */
    public List<String> getStatisticCountry() {
        return statisticCountry;
    }

    public void setStatisticCountry(List<String> statisticCountry) {
    }

    /**
     * 
     * @return list with strings with the following layout: "y: x" , where y
     * is a specific sport and x is the number of participants in that country
     */
    public List<String> getStatisticSport() {
        return statisticSport;
    }

    public void setStatisticSport(List<String> statisticSport) {
    }

    //FILTER
    /**
     * filters the list of participants displayed in the table
     */
    public void filter() {
        try {
            participants = pef.findFilter(filterGender, filterSport, filterCountry);
            updateModel();
        } catch (EJBException ex) {
            //TODO
            errorDatabase = true;
        }
    }

    public String getFilterCountry() {
        return filterCountry;
    }

    public void setFilterCountry(String filterCountry) {
        this.filterCountry = filterCountry;
    }

    public String getFilterGender() {
        return filterGender;
    }

    public void setFilterGender(String filterGender) {
        this.filterGender = filterGender;
    }

    public String getFilterSport() {
        return filterSport;
    }

    public void setFilterSport(String filterSport) {
        this.filterSport = filterSport;
    }
    //EDIT PARTICIPANTS

    /**
     * Set the participant to edit. If there was another being edited, persist its 
     * changes
     * @param part participant that the user wants to edit 
     */
    public void editing(ParticipantEntity part) {
        if (editingParticipant != null) {//Someone was editing something else
            edit(editingParticipant);
        }
        editingParticipant = part;
    }

    /**
     * 
     * @param part participant entry that we want to check if it is being edited
     * @return true if part is currently being edited, otherwise false.
     */
    public boolean isEditing(ParticipantEntity part) {

        return part == editingParticipant;
    }

    /**
     * Updates the model used by the dataTable. It should be used every time a new entry is
     * added or removed from <code>participants</code>
     * The ordering of the participants is preserved.
     */
    private void updateModel() {
        model.changeModel(new ListDataModel<>(participants));
        switch (sortedBy) {
            case "FIRSTNAME":
                sortByFirstName();
                break;
            case "LASTNAME":
                sortByLastName();
                break;
            case "GENDER":
                sortByGender();
                break;
            case "COUNTRY":
                sortByCountry();
                break;
            case "BIRTHDAY":
                sortByBirthDate();
                break;
            case "WEIGHT":
                sortByWeight();
                break;
            case "HEIGHT":
                sortByHeight();
                break;
            case "SPORT":
                sortBySport();
                break;
            default:
                //Do nothing
                break;
        }
    }

    /**
     * Updates the internal cache and persist the changes. Everything is handled by
     * a transaction using optimistic locking. If there is a problem (like lost update),
     * the entity affected is recovered querying it from the persistence system.
     * @param part 
     */
    public void edit(ParticipantEntity part) {
        try {
            System.out.println("Editing");
            pef.edit(part);
            //If successful, update the version number
            part.setVersion(part.getVersion() + 1);
        } catch (EJBException ex) {//Exception! 

            ParticipantEntity newPart = pef.find(part.getId()); //Some problem with the Locking. Let's update the model with info from the database
            if (newPart == null) {//The entry was removed! We have to reload the model
                participants.remove(part);

            } else {//Not removed. We can mantain the model. Just update it
                int index = participants.indexOf(part);
                participants.remove(part);
                participants.add(index, newPart);

            }
            updateModel();
        }
        editingParticipant = null;
    }

    /**
     * 
     * @param part participant entry to be removed 
     */
    public void remove(ParticipantEntity part) {
        try {
            if (editingParticipant == part) {
                editingParticipant = null;
            }
            participants.remove(part);//Remove from the model (acting as a cache)
            updateModel();
            pef.remove(part);
        } catch (Exception ex) { //The entry was removed by someone else. It doesn's matter!
            //TODO
        }
    }

    //ADD A NEW PARTICIPANT
    /**
     * 
     * @return empty participant entity 
     */
    public ParticipantEntity getNewParticipant() {
        return newParticipant;
    }

    public void setNewParticipant(ParticipantEntity newParticipant) {
        this.newParticipant = newParticipant;
    }

    /**
     * Adds the newParticipant private <code>ParticipantEntity<code/> to the database.
     */
    public void add() {
        try {
            pef.create(newParticipant);
            participants.add(newParticipant);//Add it to our local copy
            updateModel();
            newParticipant = new ParticipantEntity();//Since we have added the last one, now prepare it for a new entry. 
        } catch (EJBException ex) {
            errorDatabase = true;
        }
    }

    //SORT METHODS
    /**
     * Sorts the participants by their first name
     */
    public void sortByFirstName() {
        sortedBy = "FIRSTNAME";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getFirstName().compareTo(o2.getFirstName());
            }
        });
    }

    public void sortByLastName() {
        sortedBy = "LASTNAME";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
    }

    public void sortByGender() {
        sortedBy = "GENDER";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getGender().compareTo(o2.getGender());
            }
        });
    }

    public void sortByBirthDate() {
        sortedBy = "BIRTHDATE";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getBirthDate().compareTo(o2.getBirthDate());
            }
        });
    }

    public void sortByWeight() {
        sortedBy = "WEIGHT";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return (int) (Double.compare(o1.getWeight(), o2.getWeight()));
            }
        });
    }

    public void sortByHeight() {
        sortedBy = "HEIGHT";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return (Double.compare(o1.getHeight(), o2.getHeight()));
            }
        });
    }

    public void sortByCountry() {
        sortedBy = "COUNTRY";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getCountry().toString().compareTo(
                        o2.getCountry().toString());
            }
        });
    }

    public void sortBySport() {
        sortedBy = "SPORT";
        model.sortBy(new Comparator<ParticipantEntity>() {

            @Override
            public int compare(ParticipantEntity o1, ParticipantEntity o2) {
                return o1.getSport().toString().compareTo(
                        o2.getSport().toString());
            }
        });
    }

    public boolean isErrorDatabase() {
        return errorDatabase;
    }

    public void setErrorDatabase(boolean errorDatabase) {
        this.errorDatabase = errorDatabase;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParticipantsBean other = (ParticipantsBean) obj;
        if (this.errorDatabase != other.errorDatabase) {
            return false;
        }
        if (!Objects.equals(this.statisticCountry, other.statisticCountry)) {
            return false;
        }
        if (!Objects.equals(this.statisticSport, other.statisticSport)) {
            return false;
        }
        if (!Objects.equals(this.filterSport, other.filterSport)) {
            return false;
        }
        if (!Objects.equals(this.filterCountry, other.filterCountry)) {
            return false;
        }
        if (!Objects.equals(this.filterGender, other.filterGender)) {
            return false;
        }
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        if (!Objects.equals(this.newParticipant, other.newParticipant)) {
            return false;
        }
        if (!Objects.equals(this.editingParticipant, other.editingParticipant)) {
            return false;
        }
        if (!Objects.equals(this.sortedBy, other.sortedBy)) {
            return false;
        }
        if (!Objects.equals(this.participants, other.participants)) {
            return false;
        }
        if (!Objects.equals(this.pef, other.pef)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.errorDatabase ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.statisticCountry);
        hash = 67 * hash + Objects.hashCode(this.statisticSport);
        hash = 67 * hash + Objects.hashCode(this.filterSport);
        hash = 67 * hash + Objects.hashCode(this.filterCountry);
        hash = 67 * hash + Objects.hashCode(this.filterGender);
        hash = 67 * hash + Objects.hashCode(this.model);
        hash = 67 * hash + Objects.hashCode(this.newParticipant);
        hash = 67 * hash + Objects.hashCode(this.editingParticipant);
        hash = 67 * hash + Objects.hashCode(this.sortedBy);
        hash = 67 * hash + Objects.hashCode(this.participants);
        hash = 67 * hash + Objects.hashCode(this.pef);
        return hash;
    }
    
    
    
}
