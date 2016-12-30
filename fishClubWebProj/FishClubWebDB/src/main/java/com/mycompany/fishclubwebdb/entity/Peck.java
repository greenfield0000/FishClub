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
    @NamedQuery(name = "Peck.findAll", query = "SELECT p FROM Peck p")
    , @NamedQuery(name = "Peck.findByPeckId", query = "SELECT p FROM Peck p WHERE p.peckId = :peckId")})
public class Peck implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "peck_id")
    private Long peckId;
    @JoinColumn(name = "fish_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Fishs fishId;
    @JoinColumn(name = "lure_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Lures lureId;

    public Peck() {
    }

    public Peck(Long peckId) {
        this.peckId = peckId;
    }

    public Long getPeckId() {
        return peckId;
    }

    public void setPeckId(Long peckId) {
        this.peckId = peckId;
    }

    public Fishs getFishId() {
        return fishId;
    }

    public void setFishId(Fishs fishId) {
        this.fishId = fishId;
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
        hash += (peckId != null ? peckId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peck)) {
            return false;
        }
        Peck other = (Peck) object;
        if ((this.peckId == null && other.peckId != null) || (this.peckId != null && !this.peckId.equals(other.peckId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Peck[ peckId=" + peckId + " ]";
    }
    
}
