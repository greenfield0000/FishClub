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
import com.mycompany.fishclubwebdb.entity.Fishers;
import com.mycompany.fishclubwebdb.entity.Fishs;
import com.mycompany.fishclubwebdb.entity.Prefers;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class PrefersDAO implements Serializable {

    public PrefersDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prefers prefers) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishers fisherId = prefers.getFisherId();
            if (fisherId != null) {
                fisherId = em.getReference(fisherId.getClass(), fisherId.getId());
                prefers.setFisherId(fisherId);
            }
            Fishs fishId = prefers.getFishId();
            if (fishId != null) {
                fishId = em.getReference(fishId.getClass(), fishId.getId());
                prefers.setFishId(fishId);
            }
            em.persist(prefers);
            if (fisherId != null) {
                fisherId.getPrefersList().add(prefers);
                fisherId = em.merge(fisherId);
            }
            if (fishId != null) {
                fishId.getPrefersList().add(prefers);
                fishId = em.merge(fishId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrefers(prefers.getPrefersId()) != null) {
                throw new PreexistingEntityException("Prefers " + prefers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prefers prefers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prefers persistentPrefers = em.find(Prefers.class, prefers.getPrefersId());
            Fishers fisherIdOld = persistentPrefers.getFisherId();
            Fishers fisherIdNew = prefers.getFisherId();
            Fishs fishIdOld = persistentPrefers.getFishId();
            Fishs fishIdNew = prefers.getFishId();
            if (fisherIdNew != null) {
                fisherIdNew = em.getReference(fisherIdNew.getClass(), fisherIdNew.getId());
                prefers.setFisherId(fisherIdNew);
            }
            if (fishIdNew != null) {
                fishIdNew = em.getReference(fishIdNew.getClass(), fishIdNew.getId());
                prefers.setFishId(fishIdNew);
            }
            prefers = em.merge(prefers);
            if (fisherIdOld != null && !fisherIdOld.equals(fisherIdNew)) {
                fisherIdOld.getPrefersList().remove(prefers);
                fisherIdOld = em.merge(fisherIdOld);
            }
            if (fisherIdNew != null && !fisherIdNew.equals(fisherIdOld)) {
                fisherIdNew.getPrefersList().add(prefers);
                fisherIdNew = em.merge(fisherIdNew);
            }
            if (fishIdOld != null && !fishIdOld.equals(fishIdNew)) {
                fishIdOld.getPrefersList().remove(prefers);
                fishIdOld = em.merge(fishIdOld);
            }
            if (fishIdNew != null && !fishIdNew.equals(fishIdOld)) {
                fishIdNew.getPrefersList().add(prefers);
                fishIdNew = em.merge(fishIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = prefers.getPrefersId();
                if (findPrefers(id) == null) {
                    throw new NonexistentEntityException("The prefers with id " + id + " no longer exists.");
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
            Prefers prefers;
            try {
                prefers = em.getReference(Prefers.class, id);
                prefers.getPrefersId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prefers with id " + id + " no longer exists.", enfe);
            }
            Fishers fisherId = prefers.getFisherId();
            if (fisherId != null) {
                fisherId.getPrefersList().remove(prefers);
                fisherId = em.merge(fisherId);
            }
            Fishs fishId = prefers.getFishId();
            if (fishId != null) {
                fishId.getPrefersList().remove(prefers);
                fishId = em.merge(fishId);
            }
            em.remove(prefers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prefers> findPrefersEntities() {
        return findPrefersEntities(true, -1, -1);
    }

    public List<Prefers> findPrefersEntities(int maxResults, int firstResult) {
        return findPrefersEntities(false, maxResults, firstResult);
    }

    private List<Prefers> findPrefersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prefers.class));
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

    public Prefers findPrefers(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prefers.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrefersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prefers> rt = cq.from(Prefers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
