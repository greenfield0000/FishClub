/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.dao;

import com.mycompany.fishclubwebdb.dao.exceptions.NonexistentEntityException;
import com.mycompany.fishclubwebdb.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.fishclubwebdb.entity.Fishs;
import com.mycompany.fishclubwebdb.entity.Lures;
import com.mycompany.fishclubwebdb.entity.Peck;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class PeckDAO implements Serializable {

    public PeckDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peck peck) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishs fishId = peck.getFishId();
            if (fishId != null) {
                fishId = em.getReference(fishId.getClass(), fishId.getId());
                peck.setFishId(fishId);
            }
            Lures lureId = peck.getLureId();
            if (lureId != null) {
                lureId = em.getReference(lureId.getClass(), lureId.getId());
                peck.setLureId(lureId);
            }
            em.persist(peck);
            if (fishId != null) {
                fishId.getPeckList().add(peck);
                fishId = em.merge(fishId);
            }
            if (lureId != null) {
                lureId.getPeckList().add(peck);
                lureId = em.merge(lureId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPeck(peck.getPeckId()) != null) {
                throw new PreexistingEntityException("Peck " + peck + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Peck peck) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Peck persistentPeck = em.find(Peck.class, peck.getPeckId());
            Fishs fishIdOld = persistentPeck.getFishId();
            Fishs fishIdNew = peck.getFishId();
            Lures lureIdOld = persistentPeck.getLureId();
            Lures lureIdNew = peck.getLureId();
            if (fishIdNew != null) {
                fishIdNew = em.getReference(fishIdNew.getClass(), fishIdNew.getId());
                peck.setFishId(fishIdNew);
            }
            if (lureIdNew != null) {
                lureIdNew = em.getReference(lureIdNew.getClass(), lureIdNew.getId());
                peck.setLureId(lureIdNew);
            }
            peck = em.merge(peck);
            if (fishIdOld != null && !fishIdOld.equals(fishIdNew)) {
                fishIdOld.getPeckList().remove(peck);
                fishIdOld = em.merge(fishIdOld);
            }
            if (fishIdNew != null && !fishIdNew.equals(fishIdOld)) {
                fishIdNew.getPeckList().add(peck);
                fishIdNew = em.merge(fishIdNew);
            }
            if (lureIdOld != null && !lureIdOld.equals(lureIdNew)) {
                lureIdOld.getPeckList().remove(peck);
                lureIdOld = em.merge(lureIdOld);
            }
            if (lureIdNew != null && !lureIdNew.equals(lureIdOld)) {
                lureIdNew.getPeckList().add(peck);
                lureIdNew = em.merge(lureIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = peck.getPeckId();
                if (findPeck(id) == null) {
                    throw new NonexistentEntityException("The peck with id " + id + " no longer exists.");
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
            Peck peck;
            try {
                peck = em.getReference(Peck.class, id);
                peck.getPeckId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peck with id " + id + " no longer exists.", enfe);
            }
            Fishs fishId = peck.getFishId();
            if (fishId != null) {
                fishId.getPeckList().remove(peck);
                fishId = em.merge(fishId);
            }
            Lures lureId = peck.getLureId();
            if (lureId != null) {
                lureId.getPeckList().remove(peck);
                lureId = em.merge(lureId);
            }
            em.remove(peck);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Peck> findPeckEntities() {
        return findPeckEntities(true, -1, -1);
    }

    public List<Peck> findPeckEntities(int maxResults, int firstResult) {
        return findPeckEntities(false, maxResults, firstResult);
    }

    private List<Peck> findPeckEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peck.class));
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

    public Peck findPeck(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peck.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeckCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peck> rt = cq.from(Peck.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
