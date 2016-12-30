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
    @NamedQuery(name = "Fishs.findAll", query = "SELECT f FROM Fishs f")
    , @NamedQuery(name = "Fishs.findById", query = "SELECT f FROM Fishs f WHERE f.id = :id")
    , @NamedQuery(name = "Fishs.findByDepthLiving", query = "SELECT f FROM Fishs f WHERE f.depthLiving = :depthLiving")
    , @NamedQuery(name = "Fishs.findByFamily", query = "SELECT f FROM Fishs f WHERE f.family = :family")
    , @NamedQuery(name = "Fishs.findByMaxWeight", query = "SELECT f FROM Fishs f WHERE f.maxWeight = :maxWeight")
    , @NamedQuery(name = "Fishs.findByMinWeight", query = "SELECT f FROM Fishs f WHERE f.minWeight = :minWeight")
    , @NamedQuery(name = "Fishs.findByName", query = "SELECT f FROM Fishs f WHERE f.name = :name")})
public class Fishs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "depth_living")
    private Double depthLiving;
    private String family;
    @Column(name = "max_weight")
    private Double maxWeight;
    @Column(name = "min_weight")
    private Double minWeight;
    private String name;
    @OneToMany(mappedBy = "fishId", fetch = FetchType.EAGER)
    private List<Prefers> prefersList;
    @OneToMany(mappedBy = "fishId", fetch = FetchType.EAGER)
    private List<Peck> peckList;
    @OneToMany(mappedBy = "fishId", fetch = FetchType.EAGER)
    private List<Lived> livedList;

    public Fishs() {
    }

    public Fishs(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDepthLiving() {
        return depthLiving;
    }

    public void setDepthLiving(Double depthLiving) {
        this.depthLiving = depthLiving;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Prefers> getPrefersList() {
        return prefersList;
    }

    public void setPrefersList(List<Prefers> prefersList) {
        this.prefersList = prefersList;
    }

    @XmlTransient
    public List<Peck> getPeckList() {
        return peckList;
    }

    public void setPeckList(List<Peck> peckList) {
        this.peckList = peckList;
    }

    @XmlTransient
    public List<Lived> getLivedList() {
        return livedList;
    }

    public void setLivedList(List<Lived> livedList) {
        this.livedList = livedList;
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
        if (!(object instanceof Fishs)) {
            return false;
        }
        Fishs other = (Fishs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Fishs[ id=" + id + " ]";
    }
    
}
