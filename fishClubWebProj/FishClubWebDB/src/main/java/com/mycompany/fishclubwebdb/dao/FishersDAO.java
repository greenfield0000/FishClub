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
import com.mycompany.fishclubwebdb.entity.Prefers;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.fishclubwebdb.entity.Distance;
import com.mycompany.fishclubwebdb.entity.Availability;
import com.mycompany.fishclubwebdb.entity.Fishers;
import com.mycompany.fishclubwebdb.entity.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class FishersDAO implements Serializable {

    public FishersDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fishers fishers) throws PreexistingEntityException, Exception {
        if (fishers.getPrefersList() == null) {
            fishers.setPrefersList(new ArrayList<Prefers>());
        }
        if (fishers.getDistanceList() == null) {
            fishers.setDistanceList(new ArrayList<Distance>());
        }
        if (fishers.getAvailabilityList() == null) {
            fishers.setAvailabilityList(new ArrayList<Availability>());
        }
        if (fishers.getUsersList() == null) {
            fishers.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Prefers> attachedPrefersList = new ArrayList<Prefers>();
            for (Prefers prefersListPrefersToAttach : fishers.getPrefersList()) {
                prefersListPrefersToAttach = em.getReference(prefersListPrefersToAttach.getClass(), prefersListPrefersToAttach.getPrefersId());
                attachedPrefersList.add(prefersListPrefersToAttach);
            }
            fishers.setPrefersList(attachedPrefersList);
            List<Distance> attachedDistanceList = new ArrayList<Distance>();
            for (Distance distanceListDistanceToAttach : fishers.getDistanceList()) {
                distanceListDistanceToAttach = em.getReference(distanceListDistanceToAttach.getClass(), distanceListDistanceToAttach.getDistanceId());
                attachedDistanceList.add(distanceListDistanceToAttach);
            }
            fishers.setDistanceList(attachedDistanceList);
            List<Availability> attachedAvailabilityList = new ArrayList<Availability>();
            for (Availability availabilityListAvailabilityToAttach : fishers.getAvailabilityList()) {
                availabilityListAvailabilityToAttach = em.getReference(availabilityListAvailabilityToAttach.getClass(), availabilityListAvailabilityToAttach.getAvailabilityId());
                attachedAvailabilityList.add(availabilityListAvailabilityToAttach);
            }
            fishers.setAvailabilityList(attachedAvailabilityList);
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : fishers.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getId());
                attachedUsersList.add(usersListUsersToAttach);
            }
            fishers.setUsersList(attachedUsersList);
            em.persist(fishers);
            for (Prefers prefersListPrefers : fishers.getPrefersList()) {
                Fishers oldFisherIdOfPrefersListPrefers = prefersListPrefers.getFisherId();
                prefersListPrefers.setFisherId(fishers);
                prefersListPrefers = em.merge(prefersListPrefers);
                if (oldFisherIdOfPrefersListPrefers != null) {
                    oldFisherIdOfPrefersListPrefers.getPrefersList().remove(prefersListPrefers);
                    oldFisherIdOfPrefersListPrefers = em.merge(oldFisherIdOfPrefersListPrefers);
                }
            }
            for (Distance distanceListDistance : fishers.getDistanceList()) {
                Fishers oldFisherIdOfDistanceListDistance = distanceListDistance.getFisherId();
                distanceListDistance.setFisherId(fishers);
                distanceListDistance = em.merge(distanceListDistance);
                if (oldFisherIdOfDistanceListDistance != null) {
                    oldFisherIdOfDistanceListDistance.getDistanceList().remove(distanceListDistance);
                    oldFisherIdOfDistanceListDistance = em.merge(oldFisherIdOfDistanceListDistance);
                }
            }
            for (Availability availabilityListAvailability : fishers.getAvailabilityList()) {
                Fishers oldFisherIdOfAvailabilityListAvailability = availabilityListAvailability.getFisherId();
                availabilityListAvailability.setFisherId(fishers);
                availabilityListAvailability = em.merge(availabilityListAvailability);
                if (oldFisherIdOfAvailabilityListAvailability != null) {
                    oldFisherIdOfAvailabilityListAvailability.getAvailabilityList().remove(availabilityListAvailability);
                    oldFisherIdOfAvailabilityListAvailability = em.merge(oldFisherIdOfAvailabilityListAvailability);
                }
            }
            for (Users usersListUsers : fishers.getUsersList()) {
                Fishers oldFkFishermanIdOfUsersListUsers = usersListUsers.getFkFishermanId();
                usersListUsers.setFkFishermanId(fishers);
                usersListUsers = em.merge(usersListUsers);
                if (oldFkFishermanIdOfUsersListUsers != null) {
                    oldFkFishermanIdOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldFkFishermanIdOfUsersListUsers = em.merge(oldFkFishermanIdOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFishers(fishers.getId()) != null) {
                throw new PreexistingEntityException("Fishers " + fishers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fishers fishers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishers persistentFishers = em.find(Fishers.class, fishers.getId());
            List<Prefers> prefersListOld = persistentFishers.getPrefersList();
            List<Prefers> prefersListNew = fishers.getPrefersList();
            List<Distance> distanceListOld = persistentFishers.getDistanceList();
            List<Distance> distanceListNew = fishers.getDistanceList();
            List<Availability> availabilityListOld = persistentFishers.getAvailabilityList();
            List<Availability> availabilityListNew = fishers.getAvailabilityList();
            List<Users> usersListOld = persistentFishers.getUsersList();
            List<Users> usersListNew = fishers.getUsersList();
            List<Prefers> attachedPrefersListNew = new ArrayList<Prefers>();
            for (Prefers prefersListNewPrefersToAttach : prefersListNew) {
                prefersListNewPrefersToAttach = em.getReference(prefersListNewPrefersToAttach.getClass(), prefersListNewPrefersToAttach.getPrefersId());
                attachedPrefersListNew.add(prefersListNewPrefersToAttach);
            }
            prefersListNew = attachedPrefersListNew;
            fishers.setPrefersList(prefersListNew);
            List<Distance> attachedDistanceListNew = new ArrayList<Distance>();
            for (Distance distanceListNewDistanceToAttach : distanceListNew) {
                distanceListNewDistanceToAttach = em.getReference(distanceListNewDistanceToAttach.getClass(), distanceListNewDistanceToAttach.getDistanceId());
                attachedDistanceListNew.add(distanceListNewDistanceToAttach);
            }
            distanceListNew = attachedDistanceListNew;
            fishers.setDistanceList(distanceListNew);
            List<Availability> attachedAvailabilityListNew = new ArrayList<Availability>();
            for (Availability availabilityListNewAvailabilityToAttach : availabilityListNew) {
                availabilityListNewAvailabilityToAttach = em.getReference(availabilityListNewAvailabilityToAttach.getClass(), availabilityListNewAvailabilityToAttach.getAvailabilityId());
                attachedAvailabilityListNew.add(availabilityListNewAvailabilityToAttach);
            }
            availabilityListNew = attachedAvailabilityListNew;
            fishers.setAvailabilityList(availabilityListNew);
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getId());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            fishers.setUsersList(usersListNew);
            fishers = em.merge(fishers);
            for (Prefers prefersListOldPrefers : prefersListOld) {
                if (!prefersListNew.contains(prefersListOldPrefers)) {
                    prefersListOldPrefers.setFisherId(null);
                    prefersListOldPrefers = em.merge(prefersListOldPrefers);
                }
            }
            for (Prefers prefersListNewPrefers : prefersListNew) {
                if (!prefersListOld.contains(prefersListNewPrefers)) {
                    Fishers oldFisherIdOfPrefersListNewPrefers = prefersListNewPrefers.getFisherId();
                    prefersListNewPrefers.setFisherId(fishers);
                    prefersListNewPrefers = em.merge(prefersListNewPrefers);
                    if (oldFisherIdOfPrefersListNewPrefers != null && !oldFisherIdOfPrefersListNewPrefers.equals(fishers)) {
                        oldFisherIdOfPrefersListNewPrefers.getPrefersList().remove(prefersListNewPrefers);
                        oldFisherIdOfPrefersListNewPrefers = em.merge(oldFisherIdOfPrefersListNewPrefers);
                    }
                }
            }
            for (Distance distanceListOldDistance : distanceListOld) {
                if (!distanceListNew.contains(distanceListOldDistance)) {
                    distanceListOldDistance.setFisherId(null);
                    distanceListOldDistance = em.merge(distanceListOldDistance);
                }
            }
            for (Distance distanceListNewDistance : distanceListNew) {
                if (!distanceListOld.contains(distanceListNewDistance)) {
                    Fishers oldFisherIdOfDistanceListNewDistance = distanceListNewDistance.getFisherId();
                    distanceListNewDistance.setFisherId(fishers);
                    distanceListNewDistance = em.merge(distanceListNewDistance);
                    if (oldFisherIdOfDistanceListNewDistance != null && !oldFisherIdOfDistanceListNewDistance.equals(fishers)) {
                        oldFisherIdOfDistanceListNewDistance.getDistanceList().remove(distanceListNewDistance);
                        oldFisherIdOfDistanceListNewDistance = em.merge(oldFisherIdOfDistanceListNewDistance);
                    }
                }
            }
            for (Availability availabilityListOldAvailability : availabilityListOld) {
                if (!availabilityListNew.contains(availabilityListOldAvailability)) {
                    availabilityListOldAvailability.setFisherId(null);
                    availabilityListOldAvailability = em.merge(availabilityListOldAvailability);
                }
            }
            for (Availability availabilityListNewAvailability : availabilityListNew) {
                if (!availabilityListOld.contains(availabilityListNewAvailability)) {
                    Fishers oldFisherIdOfAvailabilityListNewAvailability = availabilityListNewAvailability.getFisherId();
                    availabilityListNewAvailability.setFisherId(fishers);
                    availabilityListNewAvailability = em.merge(availabilityListNewAvailability);
                    if (oldFisherIdOfAvailabilityListNewAvailability != null && !oldFisherIdOfAvailabilityListNewAvailability.equals(fishers)) {
                        oldFisherIdOfAvailabilityListNewAvailability.getAvailabilityList().remove(availabilityListNewAvailability);
                        oldFisherIdOfAvailabilityListNewAvailability = em.merge(oldFisherIdOfAvailabilityListNewAvailability);
                    }
                }
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    usersListOldUsers.setFkFishermanId(null);
                    usersListOldUsers = em.merge(usersListOldUsers);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Fishers oldFkFishermanIdOfUsersListNewUsers = usersListNewUsers.getFkFishermanId();
                    usersListNewUsers.setFkFishermanId(fishers);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldFkFishermanIdOfUsersListNewUsers != null && !oldFkFishermanIdOfUsersListNewUsers.equals(fishers)) {
                        oldFkFishermanIdOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldFkFishermanIdOfUsersListNewUsers = em.merge(oldFkFishermanIdOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fishers.getId();
                if (findFishers(id) == null) {
                    throw new NonexistentEntityException("The fishers with id " + id + " no longer exists.");
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
            Fishers fishers;
            try {
                fishers = em.getReference(Fishers.class, id);
                fishers.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fishers with id " + id + " no longer exists.", enfe);
            }
            List<Prefers> prefersList = fishers.getPrefersList();
            for (Prefers prefersListPrefers : prefersList) {
                prefersListPrefers.setFisherId(null);
                prefersListPrefers = em.merge(prefersListPrefers);
            }
            List<Distance> distanceList = fishers.getDistanceList();
            for (Distance distanceListDistance : distanceList) {
                distanceListDistance.setFisherId(null);
                distanceListDistance = em.merge(distanceListDistance);
            }
            List<Availability> availabilityList = fishers.getAvailabilityList();
            for (Availability availabilityListAvailability : availabilityList) {
                availabilityListAvailability.setFisherId(null);
                availabilityListAvailability = em.merge(availabilityListAvailability);
            }
            List<Users> usersList = fishers.getUsersList();
            for (Users usersListUsers : usersList) {
                usersListUsers.setFkFishermanId(null);
                usersListUsers = em.merge(usersListUsers);
            }
            em.remove(fishers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fishers> findFishersEntities() {
        return findFishersEntities(true, -1, -1);
    }

    public List<Fishers> findFishersEntities(int maxResults, int firstResult) {
        return findFishersEntities(false, maxResults, firstResult);
    }

    private List<Fishers> findFishersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fishers.class));
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

    public Fishers findFishers(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fishers.class, id);
        } finally {
            em.close();
        }
    }

    public int getFishersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fishers> rt = cq.from(Fishers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
