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
import com.mycompany.fishclubwebdb.entity.Distance;
import com.mycompany.fishclubwebdb.entity.Lakes;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.fishclubwebdb.entity.Lived;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class LakesDAO implements Serializable {

    public LakesDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lakes lakes) throws PreexistingEntityException, Exception {
        if (lakes.getDistanceList() == null) {
            lakes.setDistanceList(new ArrayList<Distance>());
        }
        if (lakes.getLivedList() == null) {
            lakes.setLivedList(new ArrayList<Lived>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Distance> attachedDistanceList = new ArrayList<Distance>();
            for (Distance distanceListDistanceToAttach : lakes.getDistanceList()) {
                distanceListDistanceToAttach = em.getReference(distanceListDistanceToAttach.getClass(), distanceListDistanceToAttach.getDistanceId());
                attachedDistanceList.add(distanceListDistanceToAttach);
            }
            lakes.setDistanceList(attachedDistanceList);
            List<Lived> attachedLivedList = new ArrayList<Lived>();
            for (Lived livedListLivedToAttach : lakes.getLivedList()) {
                livedListLivedToAttach = em.getReference(livedListLivedToAttach.getClass(), livedListLivedToAttach.getLivedId());
                attachedLivedList.add(livedListLivedToAttach);
            }
            lakes.setLivedList(attachedLivedList);
            em.persist(lakes);
            for (Distance distanceListDistance : lakes.getDistanceList()) {
                Lakes oldLakeIdOfDistanceListDistance = distanceListDistance.getLakeId();
                distanceListDistance.setLakeId(lakes);
                distanceListDistance = em.merge(distanceListDistance);
                if (oldLakeIdOfDistanceListDistance != null) {
                    oldLakeIdOfDistanceListDistance.getDistanceList().remove(distanceListDistance);
                    oldLakeIdOfDistanceListDistance = em.merge(oldLakeIdOfDistanceListDistance);
                }
            }
            for (Lived livedListLived : lakes.getLivedList()) {
                Lakes oldLakeIdOfLivedListLived = livedListLived.getLakeId();
                livedListLived.setLakeId(lakes);
                livedListLived = em.merge(livedListLived);
                if (oldLakeIdOfLivedListLived != null) {
                    oldLakeIdOfLivedListLived.getLivedList().remove(livedListLived);
                    oldLakeIdOfLivedListLived = em.merge(oldLakeIdOfLivedListLived);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLakes(lakes.getId()) != null) {
                throw new PreexistingEntityException("Lakes " + lakes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lakes lakes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lakes persistentLakes = em.find(Lakes.class, lakes.getId());
            List<Distance> distanceListOld = persistentLakes.getDistanceList();
            List<Distance> distanceListNew = lakes.getDistanceList();
            List<Lived> livedListOld = persistentLakes.getLivedList();
            List<Lived> livedListNew = lakes.getLivedList();
            List<Distance> attachedDistanceListNew = new ArrayList<Distance>();
            for (Distance distanceListNewDistanceToAttach : distanceListNew) {
                distanceListNewDistanceToAttach = em.getReference(distanceListNewDistanceToAttach.getClass(), distanceListNewDistanceToAttach.getDistanceId());
                attachedDistanceListNew.add(distanceListNewDistanceToAttach);
            }
            distanceListNew = attachedDistanceListNew;
            lakes.setDistanceList(distanceListNew);
            List<Lived> attachedLivedListNew = new ArrayList<Lived>();
            for (Lived livedListNewLivedToAttach : livedListNew) {
                livedListNewLivedToAttach = em.getReference(livedListNewLivedToAttach.getClass(), livedListNewLivedToAttach.getLivedId());
                attachedLivedListNew.add(livedListNewLivedToAttach);
            }
            livedListNew = attachedLivedListNew;
            lakes.setLivedList(livedListNew);
            lakes = em.merge(lakes);
            for (Distance distanceListOldDistance : distanceListOld) {
                if (!distanceListNew.contains(distanceListOldDistance)) {
                    distanceListOldDistance.setLakeId(null);
                    distanceListOldDistance = em.merge(distanceListOldDistance);
                }
            }
            for (Distance distanceListNewDistance : distanceListNew) {
                if (!distanceListOld.contains(distanceListNewDistance)) {
                    Lakes oldLakeIdOfDistanceListNewDistance = distanceListNewDistance.getLakeId();
                    distanceListNewDistance.setLakeId(lakes);
                    distanceListNewDistance = em.merge(distanceListNewDistance);
                    if (oldLakeIdOfDistanceListNewDistance != null && !oldLakeIdOfDistanceListNewDistance.equals(lakes)) {
                        oldLakeIdOfDistanceListNewDistance.getDistanceList().remove(distanceListNewDistance);
                        oldLakeIdOfDistanceListNewDistance = em.merge(oldLakeIdOfDistanceListNewDistance);
                    }
                }
            }
            for (Lived livedListOldLived : livedListOld) {
                if (!livedListNew.contains(livedListOldLived)) {
                    livedListOldLived.setLakeId(null);
                    livedListOldLived = em.merge(livedListOldLived);
                }
            }
            for (Lived livedListNewLived : livedListNew) {
                if (!livedListOld.contains(livedListNewLived)) {
                    Lakes oldLakeIdOfLivedListNewLived = livedListNewLived.getLakeId();
                    livedListNewLived.setLakeId(lakes);
                    livedListNewLived = em.merge(livedListNewLived);
                    if (oldLakeIdOfLivedListNewLived != null && !oldLakeIdOfLivedListNewLived.equals(lakes)) {
                        oldLakeIdOfLivedListNewLived.getLivedList().remove(livedListNewLived);
                        oldLakeIdOfLivedListNewLived = em.merge(oldLakeIdOfLivedListNewLived);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lakes.getId();
                if (findLakes(id) == null) {
                    throw new NonexistentEntityException("The lakes with id " + id + " no longer exists.");
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
            Lakes lakes;
            try {
                lakes = em.getReference(Lakes.class, id);
                lakes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lakes with id " + id + " no longer exists.", enfe);
            }
            List<Distance> distanceList = lakes.getDistanceList();
            for (Distance distanceListDistance : distanceList) {
                distanceListDistance.setLakeId(null);
                distanceListDistance = em.merge(distanceListDistance);
            }
            List<Lived> livedList = lakes.getLivedList();
            for (Lived livedListLived : livedList) {
                livedListLived.setLakeId(null);
                livedListLived = em.merge(livedListLived);
            }
            em.remove(lakes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lakes> findLakesEntities() {
        return findLakesEntities(true, -1, -1);
    }

    public List<Lakes> findLakesEntities(int maxResults, int firstResult) {
        return findLakesEntities(false, maxResults, firstResult);
    }

    private List<Lakes> findLakesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lakes.class));
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

    public Lakes findLakes(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lakes.class, id);
        } finally {
            em.close();
        }
    }

    public int getLakesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lakes> rt = cq.from(Lakes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
