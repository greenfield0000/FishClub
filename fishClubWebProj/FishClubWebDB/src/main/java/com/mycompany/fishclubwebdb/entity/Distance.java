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
    @NamedQuery(name = "Distance.findAll", query = "SELECT d FROM Distance d")
    , @NamedQuery(name = "Distance.findByDistanceId", query = "SELECT d FROM Distance d WHERE d.distanceId = :distanceId")
    , @NamedQuery(name = "Distance.findByDistance", query = "SELECT d FROM Distance d WHERE d.distance = :distance")})
public class Distance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "distance_id")
    private Long distanceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double distance;
    @JoinColumn(name = "fisher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishers fisherId;
    @JoinColumn(name = "lake_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Lakes lakeId;

    public Distance() {
    }

    public Distance(Long distanceId) {
        this.distanceId = distanceId;
    }

    public Long getDistanceId() {
        return distanceId;
    }

    public void setDistanceId(Long distanceId) {
        this.distanceId = distanceId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Fishers getFisherId() {
        return fisherId;
    }

    public void setFisherId(Fishers fisherId) {
        this.fisherId = fisherId;
    }

    public Lakes getLakeId() {
        return lakeId;
    }

    public void setLakeId(Lakes lakeId) {
        this.lakeId = lakeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (distanceId != null ? distanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distance)) {
            return false;
        }
        Distance other = (Distance) object;
        if ((this.distanceId == null && other.distanceId != null) || (this.distanceId != null && !this.distanceId.equals(other.distanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Distance[ distanceId=" + distanceId + " ]";
    }
    
}
