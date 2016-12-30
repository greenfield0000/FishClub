/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author roman
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lures.findAll", query = "SELECT l FROM Lures l")
    , @NamedQuery(name = "Lures.findById", query = "SELECT l FROM Lures l WHERE l.id = :id")
    , @NamedQuery(name = "Lures.findByNumberHooks", query = "SELECT l FROM Lures l WHERE l.numberHooks = :numberHooks")
    , @NamedQuery(name = "Lures.findByDivingDepth", query = "SELECT l FROM Lures l WHERE l.divingDepth = :divingDepth")
    , @NamedQuery(name = "Lures.findByIsImitation", query = "SELECT l FROM Lures l WHERE l.isImitation = :isImitation")
    , @NamedQuery(name = "Lures.findByName", query = "SELECT l FROM Lures l WHERE l.name = :name")
    , @NamedQuery(name = "Lures.findByWeight", query = "SELECT l FROM Lures l WHERE l.weight = :weight")})
public class Lures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Long id;
    @Column(name = "number_hooks")
    private Integer numberHooks;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "diving_depth")
    private Double divingDepth;
    @Column(name = "is_imitation")
    private Boolean isImitation;
    private String name;
    private Double weight;
    @OneToMany(mappedBy = "lureId", fetch = FetchType.EAGER)
    private List<Peck> peckList;
    @OneToMany(mappedBy = "lureId", fetch = FetchType.EAGER)
    private List<Availability> availabilityList;

    public Lures() {
    }

    public Lures(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberHooks() {
        return numberHooks;
    }

    public void setNumberHooks(Integer numberHooks) {
        this.numberHooks = numberHooks;
    }

    public Double getDivingDepth() {
        return divingDepth;
    }

    public void setDivingDepth(Double divingDepth) {
        this.divingDepth = divingDepth;
    }

    public Boolean getIsImitation() {
        return isImitation;
    }

    public void setIsImitation(Boolean isImitation) {
        this.isImitation = isImitation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @XmlTransient
    public List<Peck> getPeckList() {
        return peckList;
    }

    public void setPeckList(List<Peck> peckList) {
        this.peckList = peckList;
    }

    @XmlTransient
    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
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
        if (!(object instanceof Lures)) {
            return false;
        }
        Lures other = (Lures) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Lures[ id=" + id + " ]";
    }
    
}
