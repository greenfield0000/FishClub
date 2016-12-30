/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
    @NamedQuery(name = "Lakes.findAll", query = "SELECT l FROM Lakes l")
    , @NamedQuery(name = "Lakes.findById", query = "SELECT l FROM Lakes l WHERE l.id = :id")
    , @NamedQuery(name = "Lakes.findByArea", query = "SELECT l FROM Lakes l WHERE l.area = :area")
    , @NamedQuery(name = "Lakes.findByDepth", query = "SELECT l FROM Lakes l WHERE l.depth = :depth")
    , @NamedQuery(name = "Lakes.findByName", query = "SELECT l FROM Lakes l WHERE l.name = :name")})
public class Lakes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double area;
    private Double depth;
    private String name;
    @OneToMany(mappedBy = "lakeId", fetch = FetchType.EAGER)
    private List<Distance> distanceList;
    @OneToMany(mappedBy = "lakeId", fetch = FetchType.EAGER)
    private List<Lived> livedList;

    public Lakes() {
    }

    public Lakes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Distance> getDistanceList() {
        return distanceList;
    }

    public void setDistanceList(List<Distance> distanceList) {
        this.distanceList = distanceList;
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
        if (!(object instanceof Lakes)) {
            return false;
        }
        Lakes other = (Lakes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.fishclubwebdb.entity.Lakes[ id=" + id + " ]";
    }
    
}
