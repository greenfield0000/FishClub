/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fishclubwebdb.dao;

import com.mycompany.fishclubwebdb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.fishclubwebdb.entity.Fishers;
import com.mycompany.fishclubwebdb.entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author roman
 */
public class UsersDAO implements Serializable {

    public UsersDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fishers fkFishermanId = users.getFkFishermanId();
            if (fkFishermanId != null) {
                fkFishermanId = em.getReference(fkFishermanId.getClass(), fkFishermanId.getId());
                users.setFkFishermanId(fkFishermanId);
            }
            em.persist(users);
            if (fkFishermanId != null) {
                fkFishermanId.getUsersList().add(users);
                fkFishermanId = em.merge(fkFishermanId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Fishers fkFishermanIdOld = persistentUsers.getFkFishermanId();
            Fishers fkFishermanIdNew = users.getFkFishermanId();
            if (fkFishermanIdNew != null) {
                fkFishermanIdNew = em.getReference(fkFishermanIdNew.getClass(), fkFishermanIdNew.getId());
                users.setFkFishermanId(fkFishermanIdNew);
            }
            users = em.merge(users);
            if (fkFishermanIdOld != null && !fkFishermanIdOld.equals(fkFishermanIdNew)) {
                fkFishermanIdOld.getUsersList().remove(users);
                fkFishermanIdOld = em.merge(fkFishermanIdOld);
            }
            if (fkFishermanIdNew != null && !fkFishermanIdNew.equals(fkFishermanIdOld)) {
                fkFishermanIdNew.getUsersList().add(users);
                fkFishermanIdNew = em.merge(fkFishermanIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Fishers fkFishermanId = users.getFkFishermanId();
            if (fkFishermanId != null) {
                fkFishermanId.getUsersList().remove(users);
                fkFishermanId = em.merge(fkFishermanId);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
