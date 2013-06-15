/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.enums.Country;
import model.enums.Gender;
import model.enums.Sport;
import java.io.Serializable;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * Representation of a Participant in the system. Maps all the entries to a database managed
 * by JPA.
 */

@Entity
@NamedQueries({
    @NamedQuery(name="findAll", query="select p from ParticipantEntity p"),
    @NamedQuery(name="participantsCountry",query="SELECT p.country,count(p) FROM ParticipantEntity p GROUP BY p.country"),
    @NamedQuery(name="participantsSport",query="SELECT p.sport,count(p) FROM ParticipantEntity p GROUP BY p.sport"),
    
        
})

public class ParticipantEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String firstName;
    private String lastName;
    //It is better to use EnumType.STRING. This way we can add new items to the
    //enumeration without incoherences between the name and the ordinal
    @Enumerated(EnumType.STRING)  
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Country country;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthDate;
    private double weight;
    private double height;
    @Enumerated(EnumType.STRING)
    private Sport sport;
    @Version 
    private Long version; //In order to use optimistic locking

    public ParticipantEntity() {
        gender=Gender.MALE;
        country= Country.SWEDEN;
        sport= Sport.SKI;
    }
    /**
     * 
     * @param firstName the first name of the participant
     * @param lastName the last name of the participant
     * @param country country of the participant 
     * @param gender gender of the participant 
     * @param birthDate birthDate of the participant 
     * @param weight weight of the participant, >=0 
     * @param height height of the participant,>=0 
     * @param sport  sport of the participant 
     */
    public ParticipantEntity(String firstName, String lastName,
            Country country, Gender gender, Date birthDate, double weight,
            double height, Sport sport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.gender = gender;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.sport = sport;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

 

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParticipantEntity other = (ParticipantEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        if (this.country != other.country) {
            return false;
        }
        if (!Objects.equals(this.birthDate, other.birthDate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.weight) != Double.doubleToLongBits(other.weight)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        if (this.sport != other.sport) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 41 * hash + Objects.hashCode(this.firstName);
        hash = 41 * hash + Objects.hashCode(this.lastName);
        hash = 41 * hash + (this.gender != null ? this.gender.hashCode() : 0);
        hash = 41 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 41 * hash + Objects.hashCode(this.birthDate);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.weight) ^ (Double.doubleToLongBits(this.weight) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        hash = 41 * hash + (this.sport != null ? this.sport.hashCode() : 0);
        hash = 41 * hash + Objects.hashCode(this.version);
        return hash;
    }
    
}
