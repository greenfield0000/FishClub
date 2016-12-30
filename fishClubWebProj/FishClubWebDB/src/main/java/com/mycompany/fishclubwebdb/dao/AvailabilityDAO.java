/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.dao;

import com.mycompany.fishclubwebdb.dao.exceptions.NonexistentEntityException;
import com.mycompany.fishclubwebdb.dao.exceptions.PreexistingEntityException;
import com.mycompany.fishclubwebdb.entity.Availability;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.fishclubwebdb.entity.Fishers;
import com.mycompany.fishclubwebdb.entity.Lures;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class AvailabilityDAO implements Serializable {

    public AvailabilityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Availability availability) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishers fisherId = availability.getFisherId();
            if (fisherId != null) {
                fisherId = em.getReference(fisherId.getClass(), fisherId.getId());
                availability.setFisherId(fisherId);
            }
            Lures lureId = availability.getLureId();
            if (lureId != null) {
                lureId = em.getReference(lureId.getClass(), lureId.getId());
                availability.setLureId(lureId);
            }
            em.persist(availability);
            if (fisherId != null) {
                fisherId.getAvailabilityList().add(availability);
                fisherId = em.merge(fisherId);
            }
            if (lureId != null) {
                lureId.getAvailabilityList().add(availability);
                lureId = em.merge(lureId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAvailability(availability.getAvailabilityId()) != null) {
                throw new PreexistingEntityException("Availability " + availability + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Availability availability) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Availability persistentAvailability = em.find(Availability.class, availability.getAvailabilityId());
            Fishers fisherIdOld = persistentAvailability.getFisherId();
            Fishers fisherIdNew = availability.getFisherId();
            Lures lureIdOld = persistentAvailability.getLureId();
            Lures lureIdNew = availability.getLureId();
            if (fisherIdNew != null) {
                fisherIdNew = em.getReference(fisherIdNew.getClass(), fisherIdNew.getId());
                availability.setFisherId(fisherIdNew);
            }
            if (lureIdNew != null) {
                lureIdNew = em.getReference(lureIdNew.getClass(), lureIdNew.getId());
                availability.setLureId(lureIdNew);
            }
            availability = em.merge(availability);
            if (fisherIdOld != null && !fisherIdOld.equals(fisherIdNew)) {
                fisherIdOld.getAvailabilityList().remove(availability);
                fisherIdOld = em.merge(fisherIdOld);
            }
            if (fisherIdNew != null && !fisherIdNew.equals(fisherIdOld)) {
                fisherIdNew.getAvailabilityList().add(availability);
                fisherIdNew = em.merge(fisherIdNew);
            }
            if (lureIdOld != null && !lureIdOld.equals(lureIdNew)) {
                lureIdOld.getAvailabilityList().remove(availability);
                lureIdOld = em.merge(lureIdOld);
            }
            if (lureIdNew != null && !lureIdNew.equals(lureIdOld)) {
                lureIdNew.getAvailabilityList().add(availability);
                lureIdNew = em.merge(lureIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = availability.getAvailabilityId();
                if (findAvailability(id) == null) {
                    throw new NonexistentEntityException("The availability with id " + id + " no longer exists.");
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
            Availability availability;
            try {
                availability = em.getReference(Availability.class, id);
                availability.getAvailabilityId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The availability with id " + id + " no longer exists.", enfe);
            }
            Fishers fisherId = availability.getFisherId();
            if (fisherId != null) {
                fisherId.getAvailabilityList().remove(availability);
                fisherId = em.merge(fisherId);
            }
            Lures lureId = availability.getLureId();
            if (lureId != null) {
                lureId.getAvailabilityList().remove(availability);
                lureId = em.merge(lureId);
            }
            em.remove(availability);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Availability> findAvailabilityEntities() {
        return findAvailabilityEntities(true, -1, -1);
    }

    public List<Availability> findAvailabilityEntities(int maxResults, int firstResult) {
        return findAvailabilityEntities(false, maxResults, firstResult);
    }

    private List<Availability> findAvailabilityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Availability.class));
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

    public Availability findAvailability(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Availability.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvailabilityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Availability> rt = cq.from(Availability.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
