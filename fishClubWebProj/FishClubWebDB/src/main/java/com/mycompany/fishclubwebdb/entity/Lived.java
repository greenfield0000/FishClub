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
    @NamedQuery(name = "Lived.findAll", query = "SELECT l FROM Lived l")
    , @NamedQuery(name = "Lived.findByLivedId", query = "SELECT l FROM Lived l WHERE l.livedId = :livedId")
    , @NamedQuery(name = "Lived.findByCountFish", query = "SELECT l FROM Lived l WHERE l.countFish = :countFish")})
public class Lived implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "lived_id")
    private Long livedId;
    @Column(name = "count_fish")
    private Integer countFish;
    @JoinColumn(name = "fish_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishs fishId;
    @JoinColumn(name = "lake_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Lakes lakeId;

    public Lived() {
    }

    public Lived(Long livedId) {
        this.livedId = livedId;
    }

    public Long getLivedId() {
        return livedId;
    }

    public void setLivedId(Long livedId) {
        this.livedId = livedId;
    }

    public Integer getCountFish() {
        return countFish;
    }

    public void setCountFish(Integer countFish) {
        this.countFish = countFish;
    }

    public Fishs getFishId() {
        return fishId;
    }

    public void setFishId(Fishs fishId) {
        this.fishId = fishId;
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
        hash += (livedId != null ? livedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lived)) {
            return false;
        }
        Lived other = (Lived) object;
        if ((this.livedId == null && other.livedId != null) || (this.livedId != null && !this.livedId.equals(other.livedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Lived[ livedId=" + livedId + " ]";
    }
    
}
