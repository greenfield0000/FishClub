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
import com.mycompany.fishclubwebdb.entity.Peck;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.fishclubwebdb.entity.Availability;
import com.mycompany.fishclubwebdb.entity.Lures;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class LuresDAO implements Serializable {

    public LuresDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lures lures) throws PreexistingEntityException, Exception {
        if (lures.getPeckList() == null) {
            lures.setPeckList(new ArrayList<Peck>());
        }
        if (lures.getAvailabilityList() == null) {
            lures.setAvailabilityList(new ArrayList<Availability>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Peck> attachedPeckList = new ArrayList<Peck>();
            for (Peck peckListPeckToAttach : lures.getPeckList()) {
                peckListPeckToAttach = em.getReference(peckListPeckToAttach.getClass(), peckListPeckToAttach.getPeckId());
                attachedPeckList.add(peckListPeckToAttach);
            }
            lures.setPeckList(attachedPeckList);
            List<Availability> attachedAvailabilityList = new ArrayList<Availability>();
            for (Availability availabilityListAvailabilityToAttach : lures.getAvailabilityList()) {
                availabilityListAvailabilityToAttach = em.getReference(availabilityListAvailabilityToAttach.getClass(), availabilityListAvailabilityToAttach.getAvailabilityId());
                attachedAvailabilityList.add(availabilityListAvailabilityToAttach);
            }
            lures.setAvailabilityList(attachedAvailabilityList);
            em.persist(lures);
            for (Peck peckListPeck : lures.getPeckList()) {
                Lures oldLureIdOfPeckListPeck = peckListPeck.getLureId();
                peckListPeck.setLureId(lures);
                peckListPeck = em.merge(peckListPeck);
                if (oldLureIdOfPeckListPeck != null) {
                    oldLureIdOfPeckListPeck.getPeckList().remove(peckListPeck);
                    oldLureIdOfPeckListPeck = em.merge(oldLureIdOfPeckListPeck);
                }
            }
            for (Availability availabilityListAvailability : lures.getAvailabilityList()) {
                Lures oldLureIdOfAvailabilityListAvailability = availabilityListAvailability.getLureId();
                availabilityListAvailability.setLureId(lures);
                availabilityListAvailability = em.merge(availabilityListAvailability);
                if (oldLureIdOfAvailabilityListAvailability != null) {
                    oldLureIdOfAvailabilityListAvailability.getAvailabilityList().remove(availabilityListAvailability);
                    oldLureIdOfAvailabilityListAvailability = em.merge(oldLureIdOfAvailabilityListAvailability);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLures(lures.getId()) != null) {
                throw new PreexistingEntityException("Lures " + lures + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lures lures) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lures persistentLures = em.find(Lures.class, lures.getId());
            List<Peck> peckListOld = persistentLures.getPeckList();
            List<Peck> peckListNew = lures.getPeckList();
            List<Availability> availabilityListOld = persistentLures.getAvailabilityList();
            List<Availability> availabilityListNew = lures.getAvailabilityList();
            List<Peck> attachedPeckListNew = new ArrayList<Peck>();
            for (Peck peckListNewPeckToAttach : peckListNew) {
                peckListNewPeckToAttach = em.getReference(peckListNewPeckToAttach.getClass(), peckListNewPeckToAttach.getPeckId());
                attachedPeckListNew.add(peckListNewPeckToAttach);
            }
            peckListNew = attachedPeckListNew;
            lures.setPeckList(peckListNew);
            List<Availability> attachedAvailabilityListNew = new ArrayList<Availability>();
            for (Availability availabilityListNewAvailabilityToAttach : availabilityListNew) {
                availabilityListNewAvailabilityToAttach = em.getReference(availabilityListNewAvailabilityToAttach.getClass(), availabilityListNewAvailabilityToAttach.getAvailabilityId());
                attachedAvailabilityListNew.add(availabilityListNewAvailabilityToAttach);
            }
            availabilityListNew = attachedAvailabilityListNew;
            lures.setAvailabilityList(availabilityListNew);
            lures = em.merge(lures);
            for (Peck peckListOldPeck : peckListOld) {
                if (!peckListNew.contains(peckListOldPeck)) {
                    peckListOldPeck.setLureId(null);
                    peckListOldPeck = em.merge(peckListOldPeck);
                }
            }
            for (Peck peckListNewPeck : peckListNew) {
                if (!peckListOld.contains(peckListNewPeck)) {
                    Lures oldLureIdOfPeckListNewPeck = peckListNewPeck.getLureId();
                    peckListNewPeck.setLureId(lures);
                    peckListNewPeck = em.merge(peckListNewPeck);
                    if (oldLureIdOfPeckListNewPeck != null && !oldLureIdOfPeckListNewPeck.equals(lures)) {
                        oldLureIdOfPeckListNewPeck.getPeckList().remove(peckListNewPeck);
                        oldLureIdOfPeckListNewPeck = em.merge(oldLureIdOfPeckListNewPeck);
                    }
                }
            }
            for (Availability availabilityListOldAvailability : availabilityListOld) {
                if (!availabilityListNew.contains(availabilityListOldAvailability)) {
                    availabilityListOldAvailability.setLureId(null);
                    availabilityListOldAvailability = em.merge(availabilityListOldAvailability);
                }
            }
            for (Availability availabilityListNewAvailability : availabilityListNew) {
                if (!availabilityListOld.contains(availabilityListNewAvailability)) {
                    Lures oldLureIdOfAvailabilityListNewAvailability = availabilityListNewAvailability.getLureId();
                    availabilityListNewAvailability.setLureId(lures);
                    availabilityListNewAvailability = em.merge(availabilityListNewAvailability);
                    if (oldLureIdOfAvailabilityListNewAvailability != null && !oldLureIdOfAvailabilityListNewAvailability.equals(lures)) {
                        oldLureIdOfAvailabilityListNewAvailability.getAvailabilityList().remove(availabilityListNewAvailability);
                        oldLureIdOfAvailabilityListNewAvailability = em.merge(oldLureIdOfAvailabilityListNewAvailability);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lures.getId();
                if (findLures(id) == null) {
                    throw new NonexistentEntityException("The lures with id " + id + " no longer exists.");
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
            Lures lures;
            try {
                lures = em.getReference(Lures.class, id);
                lures.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lures with id " + id + " no longer exists.", enfe);
            }
            List<Peck> peckList = lures.getPeckList();
            for (Peck peckListPeck : peckList) {
                peckListPeck.setLureId(null);
                peckListPeck = em.merge(peckListPeck);
            }
            List<Availability> availabilityList = lures.getAvailabilityList();
            for (Availability availabilityListAvailability : availabilityList) {
                availabilityListAvailability.setLureId(null);
                availabilityListAvailability = em.merge(availabilityListAvailability);
            }
            em.remove(lures);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lures> findLuresEntities() {
        return findLuresEntities(true, -1, -1);
    }

    public List<Lures> findLuresEntities(int maxResults, int firstResult) {
        return findLuresEntities(false, maxResults, firstResult);
    }

    private List<Lures> findLuresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lures.class));
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

    public Lures findLures(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lures.class, id);
        } finally {
            em.close();
        }
    }

    public int getLuresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lures> rt = cq.from(Lures.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
