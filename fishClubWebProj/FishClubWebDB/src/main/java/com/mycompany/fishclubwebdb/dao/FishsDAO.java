/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.dao;

import com.mycompany.fishclubwebdb.dao.exceptions.NonexistentEntityException;
import com.mycompany.fishclubwebdb.dao.exceptions.PreexistingEntityException;
import com.mycompany.fishclubwebdb.entity.Fishs;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.fishclubwebdb.entity.Prefers;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.fishclubwebdb.entity.Peck;
import com.mycompany.fishclubwebdb.entity.Lived;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class FishsDAO implements Serializable {

    public FishsDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fishs fishs) throws PreexistingEntityException, Exception {
        if (fishs.getPrefersList() == null) {
            fishs.setPrefersList(new ArrayList<Prefers>());
        }
        if (fishs.getPeckList() == null) {
            fishs.setPeckList(new ArrayList<Peck>());
        }
        if (fishs.getLivedList() == null) {
            fishs.setLivedList(new ArrayList<Lived>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Prefers> attachedPrefersList = new ArrayList<Prefers>();
            for (Prefers prefersListPrefersToAttach : fishs.getPrefersList()) {
                prefersListPrefersToAttach = em.getReference(prefersListPrefersToAttach.getClass(), prefersListPrefersToAttach.getPrefersId());
                attachedPrefersList.add(prefersListPrefersToAttach);
            }
            fishs.setPrefersList(attachedPrefersList);
            List<Peck> attachedPeckList = new ArrayList<Peck>();
            for (Peck peckListPeckToAttach : fishs.getPeckList()) {
                peckListPeckToAttach = em.getReference(peckListPeckToAttach.getClass(), peckListPeckToAttach.getPeckId());
                attachedPeckList.add(peckListPeckToAttach);
            }
            fishs.setPeckList(attachedPeckList);
            List<Lived> attachedLivedList = new ArrayList<Lived>();
            for (Lived livedListLivedToAttach : fishs.getLivedList()) {
                livedListLivedToAttach = em.getReference(livedListLivedToAttach.getClass(), livedListLivedToAttach.getLivedId());
                attachedLivedList.add(livedListLivedToAttach);
            }
            fishs.setLivedList(attachedLivedList);
            em.persist(fishs);
            for (Prefers prefersListPrefers : fishs.getPrefersList()) {
                Fishs oldFishIdOfPrefersListPrefers = prefersListPrefers.getFishId();
                prefersListPrefers.setFishId(fishs);
                prefersListPrefers = em.merge(prefersListPrefers);
                if (oldFishIdOfPrefersListPrefers != null) {
                    oldFishIdOfPrefersListPrefers.getPrefersList().remove(prefersListPrefers);
                    oldFishIdOfPrefersListPrefers = em.merge(oldFishIdOfPrefersListPrefers);
                }
            }
            for (Peck peckListPeck : fishs.getPeckList()) {
                Fishs oldFishIdOfPeckListPeck = peckListPeck.getFishId();
                peckListPeck.setFishId(fishs);
                peckListPeck = em.merge(peckListPeck);
                if (oldFishIdOfPeckListPeck != null) {
                    oldFishIdOfPeckListPeck.getPeckList().remove(peckListPeck);
                    oldFishIdOfPeckListPeck = em.merge(oldFishIdOfPeckListPeck);
                }
            }
            for (Lived livedListLived : fishs.getLivedList()) {
                Fishs oldFishIdOfLivedListLived = livedListLived.getFishId();
                livedListLived.setFishId(fishs);
                livedListLived = em.merge(livedListLived);
                if (oldFishIdOfLivedListLived != null) {
                    oldFishIdOfLivedListLived.getLivedList().remove(livedListLived);
                    oldFishIdOfLivedListLived = em.merge(oldFishIdOfLivedListLived);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFishs(fishs.getId()) != null) {
                throw new PreexistingEntityException("Fishs " + fishs + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fishs fishs) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishs persistentFishs = em.find(Fishs.class, fishs.getId());
            List<Prefers> prefersListOld = persistentFishs.getPrefersList();
            List<Prefers> prefersListNew = fishs.getPrefersList();
            List<Peck> peckListOld = persistentFishs.getPeckList();
            List<Peck> peckListNew = fishs.getPeckList();
            List<Lived> livedListOld = persistentFishs.getLivedList();
            List<Lived> livedListNew = fishs.getLivedList();
            List<Prefers> attachedPrefersListNew = new ArrayList<Prefers>();
            for (Prefers prefersListNewPrefersToAttach : prefersListNew) {
                prefersListNewPrefersToAttach = em.getReference(prefersListNewPrefersToAttach.getClass(), prefersListNewPrefersToAttach.getPrefersId());
                attachedPrefersListNew.add(prefersListNewPrefersToAttach);
            }
            prefersListNew = attachedPrefersListNew;
            fishs.setPrefersList(prefersListNew);
            List<Peck> attachedPeckListNew = new ArrayList<Peck>();
            for (Peck peckListNewPeckToAttach : peckListNew) {
                peckListNewPeckToAttach = em.getReference(peckListNewPeckToAttach.getClass(), peckListNewPeckToAttach.getPeckId());
                attachedPeckListNew.add(peckListNewPeckToAttach);
            }
            peckListNew = attachedPeckListNew;
            fishs.setPeckList(peckListNew);
            List<Lived> attachedLivedListNew = new ArrayList<Lived>();
            for (Lived livedListNewLivedToAttach : livedListNew) {
                livedListNewLivedToAttach = em.getReference(livedListNewLivedToAttach.getClass(), livedListNewLivedToAttach.getLivedId());
                attachedLivedListNew.add(livedListNewLivedToAttach);
            }
            livedListNew = attachedLivedListNew;
            fishs.setLivedList(livedListNew);
            fishs = em.merge(fishs);
            for (Prefers prefersListOldPrefers : prefersListOld) {
                if (!prefersListNew.contains(prefersListOldPrefers)) {
                    prefersListOldPrefers.setFishId(null);
                    prefersListOldPrefers = em.merge(prefersListOldPrefers);
                }
            }
            for (Prefers prefersListNewPrefers : prefersListNew) {
                if (!prefersListOld.contains(prefersListNewPrefers)) {
                    Fishs oldFishIdOfPrefersListNewPrefers = prefersListNewPrefers.getFishId();
                    prefersListNewPrefers.setFishId(fishs);
                    prefersListNewPrefers = em.merge(prefersListNewPrefers);
                    if (oldFishIdOfPrefersListNewPrefers != null && !oldFishIdOfPrefersListNewPrefers.equals(fishs)) {
                        oldFishIdOfPrefersListNewPrefers.getPrefersList().remove(prefersListNewPrefers);
                        oldFishIdOfPrefersListNewPrefers = em.merge(oldFishIdOfPrefersListNewPrefers);
                    }
                }
            }
            for (Peck peckListOldPeck : peckListOld) {
                if (!peckListNew.contains(peckListOldPeck)) {
                    peckListOldPeck.setFishId(null);
                    peckListOldPeck = em.merge(peckListOldPeck);
                }
            }
            for (Peck peckListNewPeck : peckListNew) {
                if (!peckListOld.contains(peckListNewPeck)) {
                    Fishs oldFishIdOfPeckListNewPeck = peckListNewPeck.getFishId();
                    peckListNewPeck.setFishId(fishs);
                    peckListNewPeck = em.merge(peckListNewPeck);
                    if (oldFishIdOfPeckListNewPeck != null && !oldFishIdOfPeckListNewPeck.equals(fishs)) {
                        oldFishIdOfPeckListNewPeck.getPeckList().remove(peckListNewPeck);
                        oldFishIdOfPeckListNewPeck = em.merge(oldFishIdOfPeckListNewPeck);
                    }
                }
            }
            for (Lived livedListOldLived : livedListOld) {
                if (!livedListNew.contains(livedListOldLived)) {
                    livedListOldLived.setFishId(null);
                    livedListOldLived = em.merge(livedListOldLived);
                }
            }
            for (Lived livedListNewLived : livedListNew) {
                if (!livedListOld.contains(livedListNewLived)) {
                    Fishs oldFishIdOfLivedListNewLived = livedListNewLived.getFishId();
                    livedListNewLived.setFishId(fishs);
                    livedListNewLived = em.merge(livedListNewLived);
                    if (oldFishIdOfLivedListNewLived != null && !oldFishIdOfLivedListNewLived.equals(fishs)) {
                        oldFishIdOfLivedListNewLived.getLivedList().remove(livedListNewLived);
                        oldFishIdOfLivedListNewLived = em.merge(oldFishIdOfLivedListNewLived);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fishs.getId();
                if (findFishs(id) == null) {
                    throw new NonexistentEntityException("The fishs with id " + id + " no longer exists.");
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
            Fishs fishs;
            try {
                fishs = em.getReference(Fishs.class, id);
                fishs.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fishs with id " + id + " no longer exists.", enfe);
            }
            List<Prefers> prefersList = fishs.getPrefersList();
            for (Prefers prefersListPrefers : prefersList) {
                prefersListPrefers.setFishId(null);
                prefersListPrefers = em.merge(prefersListPrefers);
            }
            List<Peck> peckList = fishs.getPeckList();
            for (Peck peckListPeck : peckList) {
                peckListPeck.setFishId(null);
                peckListPeck = em.merge(peckListPeck);
            }
            List<Lived> livedList = fishs.getLivedList();
            for (Lived livedListLived : livedList) {
                livedListLived.setFishId(null);
                livedListLived = em.merge(livedListLived);
            }
            em.remove(fishs);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fishs> findFishsEntities() {
        return findFishsEntities(true, -1, -1);
    }

    public List<Fishs> findFishsEntities(int maxResults, int firstResult) {
        return findFishsEntities(false, maxResults, firstResult);
    }

    private List<Fishs> findFishsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fishs.class));
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

    public Fishs findFishs(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fishs.class, id);
        } finally {
            em.close();
        }
    }

    public int getFishsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fishs> rt = cq.from(Fishs.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
