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
    @NamedQuery(name = "Prefers.findAll", query = "SELECT p FROM Prefers p")
    , @NamedQuery(name = "Prefers.findByPrefersId", query = "SELECT p FROM Prefers p WHERE p.prefersId = :prefersId")})
public class Prefers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "prefers_id")
    private Long prefersId;
    @JoinColumn(name = "fisher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishers fisherId;
    @JoinColumn(name = "fish_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishs fishId;

    public Prefers() {
    }

    public Prefers(Long prefersId) {
        this.prefersId = prefersId;
    }

    public Long getPrefersId() {
        return prefersId;
    }

    public void setPrefersId(Long prefersId) {
        this.prefersId = prefersId;
    }

    public Fishers getFisherId() {
        return fisherId;
    }

    public void setFisherId(Fishers fisherId) {
        this.fisherId = fisherId;
    }

    public Fishs getFishId() {
        return fishId;
    }

    public void setFishId(Fishs fishId) {
        this.fishId = fishId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prefersId != null ? prefersId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prefers)) {
            return false;
        }
        Prefers other = (Prefers) object;
        if ((this.prefersId == null && other.prefersId != null) || (this.prefersId != null && !this.prefersId.equals(other.prefersId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Prefers[ prefersId=" + prefersId + " ]";
    }
    
}
