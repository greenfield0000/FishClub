/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author roman
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Availability.findAll", query = "SELECT a FROM Availability a")
    , @NamedQuery(name = "Availability.findByAvailabilityId", query = "SELECT a FROM Availability a WHERE a.availabilityId = :availabilityId")
    , @NamedQuery(name = "Availability.findByCountLure", query = "SELECT a FROM Availability a WHERE a.countLure = :countLure")})
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "availability_id")
    private Long availabilityId;
    @Column(name = "count_lure")
    private Integer countLure;
    @JoinColumn(name = "fisher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishers fisherId;
    @JoinColumn(name = "lure_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Lures lureId;

    public Availability() {
    }

    public Availability(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Integer getCountLure() {
        return countLure;
    }

    public void setCountLure(Integer countLure) {
        this.countLure = countLure;
    }

    public Fishers getFisherId() {
        return fisherId;
    }

    public void setFisherId(Fishers fisherId) {
        this.fisherId = fisherId;
    }

    public Lures getLureId() {
        return lureId;
    }

    public void setLureId(Lures lureId) {
        this.lureId = lureId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (availabilityId != null ? availabilityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Availability)) {
            return false;
        }
        Availability other = (Availability) object;
        if ((this.availabilityId == null && other.availabilityId != null) || (this.availabilityId != null && !this.availabilityId.equals(other.availabilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Availability[ availabilityId=" + availabilityId + " ]";
    }
    
}
