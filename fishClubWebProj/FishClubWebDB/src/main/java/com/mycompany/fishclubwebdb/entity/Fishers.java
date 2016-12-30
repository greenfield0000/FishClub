/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roman
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fishers.findAll", query = "SELECT f FROM Fishers f")
    , @NamedQuery(name = "Fishers.findById", query = "SELECT f FROM Fishers f WHERE f.id = :id")
    , @NamedQuery(name = "Fishers.findByBirthday", query = "SELECT f FROM Fishers f WHERE f.birthday = :birthday")
    , @NamedQuery(name = "Fishers.findByGender", query = "SELECT f FROM Fishers f WHERE f.gender = :gender")
    , @NamedQuery(name = "Fishers.findByLastName", query = "SELECT f FROM Fishers f WHERE f.lastName = :lastName")
    , @NamedQuery(name = "Fishers.findByMiddleName", query = "SELECT f FROM Fishers f WHERE f.middleName = :middleName")
    , @NamedQuery(name = "Fishers.findByFirstName", query = "SELECT f FROM Fishers f WHERE f.firstName = :firstName")})
public class Fishers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private Integer gender;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "first_name")
    private String firstName;
    @OneToMany(mappedBy = "fisherId", fetch = FetchType.EAGER)
    private List<Prefers> prefersList;
    @OneToMany(mappedBy = "fisherId", fetch = FetchType.EAGER)
    private List<Distance> distanceList;
    @OneToMany(mappedBy = "fisherId", fetch = FetchType.EAGER)
    private List<Availability> availabilityList;
    @OneToMany(mappedBy = "fkFishermanId", fetch = FetchType.EAGER)
    private List<Users> usersList;

    public Fishers() {
    }

    public Fishers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlTransient
    public List<Prefers> getPrefersList() {
        return prefersList;
    }

    public void setPrefersList(List<Prefers> prefersList) {
        this.prefersList = prefersList;
    }

    @XmlTransient
    public List<Distance> getDistanceList() {
        return distanceList;
    }

    public void setDistanceList(List<Distance> distanceList) {
        this.distanceList = distanceList;
    }

    @XmlTransient
    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fishers)) {
            return false;
        }
        Fishers other = (Fishers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Fishers[ id=" + id + " ]";
    }
    
}
