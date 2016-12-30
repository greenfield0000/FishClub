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
import com.mycompany.fishclubwebdb.entity.Lakes;
import com.mycompany.fishclubwebdb.entity.Lived;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class LivedDAO implements Serializable {

    public LivedDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lived lived) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishs fishId = lived.getFishId();
            if (fishId != null) {
                fishId = em.getReference(fishId.getClass(), fishId.getId());
                lived.setFishId(fishId);
            }
            Lakes lakeId = lived.getLakeId();
            if (lakeId != null) {
                lakeId = em.getReference(lakeId.getClass(), lakeId.getId());
                lived.setLakeId(lakeId);
            }
            em.persist(lived);
            if (fishId != null) {
                fishId.getLivedList().add(lived);
                fishId = em.merge(fishId);
            }
            if (lakeId != null) {
                lakeId.getLivedList().add(lived);
                lakeId = em.merge(lakeId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLived(lived.getLivedId()) != null) {
                throw new PreexistingEntityException("Lived " + lived + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lived lived) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lived persistentLived = em.find(Lived.class, lived.getLivedId());
            Fishs fishIdOld = persistentLived.getFishId();
            Fishs fishIdNew = lived.getFishId();
            Lakes lakeIdOld = persistentLived.getLakeId();
            Lakes lakeIdNew = lived.getLakeId();
            if (fishIdNew != null) {
                fishIdNew = em.getReference(fishIdNew.getClass(), fishIdNew.getId());
                lived.setFishId(fishIdNew);
            }
            if (lakeIdNew != null) {
                lakeIdNew = em.getReference(lakeIdNew.getClass(), lakeIdNew.getId());
                lived.setLakeId(lakeIdNew);
            }
            lived = em.merge(lived);
            if (fishIdOld != null && !fishIdOld.equals(fishIdNew)) {
                fishIdOld.getLivedList().remove(lived);
                fishIdOld = em.merge(fishIdOld);
            }
            if (fishIdNew != null && !fishIdNew.equals(fishIdOld)) {
                fishIdNew.getLivedList().add(lived);
                fishIdNew = em.merge(fishIdNew);
            }
            if (lakeIdOld != null && !lakeIdOld.equals(lakeIdNew)) {
                lakeIdOld.getLivedList().remove(lived);
                lakeIdOld = em.merge(lakeIdOld);
            }
            if (lakeIdNew != null && !lakeIdNew.equals(lakeIdOld)) {
                lakeIdNew.getLivedList().add(lived);
                lakeIdNew = em.merge(lakeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lived.getLivedId();
                if (findLived(id) == null) {
                    throw new NonexistentEntityException("The lived with id " + id + " no longer exists.");
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
            Lived lived;
            try {
                lived = em.getReference(Lived.class, id);
                lived.getLivedId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lived with id " + id + " no longer exists.", enfe);
            }
            Fishs fishId = lived.getFishId();
            if (fishId != null) {
                fishId.getLivedList().remove(lived);
                fishId = em.merge(fishId);
            }
            Lakes lakeId = lived.getLakeId();
            if (lakeId != null) {
                lakeId.getLivedList().remove(lived);
                lakeId = em.merge(lakeId);
            }
            em.remove(lived);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lived> findLivedEntities() {
        return findLivedEntities(true, -1, -1);
    }

    public List<Lived> findLivedEntities(int maxResults, int firstResult) {
        return findLivedEntities(false, maxResults, firstResult);
    }

    private List<Lived> findLivedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lived.class));
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

    public Lived findLived(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lived.class, id);
        } finally {
            em.close();
        }
    }

    public int getLivedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lived> rt = cq.from(Lived.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
