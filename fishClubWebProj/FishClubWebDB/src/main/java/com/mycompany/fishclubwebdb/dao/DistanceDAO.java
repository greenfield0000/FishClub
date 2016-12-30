/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.dao;

import com.mycompany.fishclubwebdb.dao.exceptions.NonexistentEntityException;
import com.mycompany.fishclubwebdb.dao.exceptions.PreexistingEntityException;
import com.mycompany.fishclubwebdb.entity.Distance;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.fishclubwebdb.entity.Fishers;
import com.mycompany.fishclubwebdb.entity.Lakes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class DistanceDAO implements Serializable {

    public DistanceDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Distance distance) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishers fisherId = distance.getFisherId();
            if (fisherId != null) {
                fisherId = em.getReference(fisherId.getClass(), fisherId.getId());
                distance.setFisherId(fisherId);
            }
            Lakes lakeId = distance.getLakeId();
            if (lakeId != null) {
                lakeId = em.getReference(lakeId.getClass(), lakeId.getId());
                distance.setLakeId(lakeId);
            }
            em.persist(distance);
            if (fisherId != null) {
                fisherId.getDistanceList().add(distance);
                fisherId = em.merge(fisherId);
            }
            if (lakeId != null) {
                lakeId.getDistanceList().add(distance);
                lakeId = em.merge(lakeId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDistance(distance.getDistanceId()) != null) {
                throw new PreexistingEntityException("Distance " + distance + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Distance distance) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distance persistentDistance = em.find(Distance.class, distance.getDistanceId());
            Fishers fisherIdOld = persistentDistance.getFisherId();
            Fishers fisherIdNew = distance.getFisherId();
            Lakes lakeIdOld = persistentDistance.getLakeId();
            Lakes lakeIdNew = distance.getLakeId();
            if (fisherIdNew != null) {
                fisherIdNew = em.getReference(fisherIdNew.getClass(), fisherIdNew.getId());
                distance.setFisherId(fisherIdNew);
            }
            if (lakeIdNew != null) {
                lakeIdNew = em.getReference(lakeIdNew.getClass(), lakeIdNew.getId());
                distance.setLakeId(lakeIdNew);
            }
            distance = em.merge(distance);
            if (fisherIdOld != null && !fisherIdOld.equals(fisherIdNew)) {
                fisherIdOld.getDistanceList().remove(distance);
                fisherIdOld = em.merge(fisherIdOld);
            }
            if (fisherIdNew != null && !fisherIdNew.equals(fisherIdOld)) {
                fisherIdNew.getDistanceList().add(distance);
                fisherIdNew = em.merge(fisherIdNew);
            }
            if (lakeIdOld != null && !lakeIdOld.equals(lakeIdNew)) {
                lakeIdOld.getDistanceList().remove(distance);
                lakeIdOld = em.merge(lakeIdOld);
            }
            if (lakeIdNew != null && !lakeIdNew.equals(lakeIdOld)) {
                lakeIdNew.getDistanceList().add(distance);
                lakeIdNew = em.merge(lakeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = distance.getDistanceId();
                if (findDistance(id) == null) {
                    throw new NonexistentEntityException("The distance with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distance distance;
            try {
                distance = em.getReference(Distance.class, id);
                distance.getDistanceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distance with id " + id + " no longer exists.", enfe);
            }
            Fishers fisherId = distance.getFisherId();
            if (fisherId != null) {
                fisherId.getDistanceList().remove(distance);
                fisherId = em.merge(fisherId);
            }
            Lakes lakeId = distance.getLakeId();
            if (lakeId != null) {
                lakeId.getDistanceList().remove(distance);
                lakeId = em.merge(lakeId);
            }
            em.remove(distance);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Distance> findDistanceEntities() {
        return findDistanceEntities(true, -1, -1);
    }

    public List<Distance> findDistanceEntities(int maxResults, int firstResult) {
        return findDistanceEntities(false, maxResults, firstResult);
    }

    private List<Distance> findDistanceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Distance.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Distance findDistance(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Distance.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistanceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Distance> rt = cq.from(Distance.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
