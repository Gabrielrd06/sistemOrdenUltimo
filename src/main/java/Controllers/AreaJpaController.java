/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Entitys.Area;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entitys.Trabajador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class AreaJpaController implements Serializable {

    public AreaJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(Area area) {
        if (area.getTrabajadorList() == null) {
            area.setTrabajadorList(new ArrayList<Trabajador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Trabajador> attachedTrabajadorList = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListTrabajadorToAttach : area.getTrabajadorList()) {
                trabajadorListTrabajadorToAttach = em.getReference(trabajadorListTrabajadorToAttach.getClass(), trabajadorListTrabajadorToAttach.getIdTrabajador());
                attachedTrabajadorList.add(trabajadorListTrabajadorToAttach);
            }
            area.setTrabajadorList(attachedTrabajadorList);
            em.persist(area);
            for (Trabajador trabajadorListTrabajador : area.getTrabajadorList()) {
                Area oldIdAreaOfTrabajadorListTrabajador = trabajadorListTrabajador.getIdArea();
                trabajadorListTrabajador.setIdArea(area);
                trabajadorListTrabajador = em.merge(trabajadorListTrabajador);
                if (oldIdAreaOfTrabajadorListTrabajador != null) {
                    oldIdAreaOfTrabajadorListTrabajador.getTrabajadorList().remove(trabajadorListTrabajador);
                    oldIdAreaOfTrabajadorListTrabajador = em.merge(oldIdAreaOfTrabajadorListTrabajador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Area area) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area persistentArea = em.find(Area.class, area.getIdArea());
            List<Trabajador> trabajadorListOld = persistentArea.getTrabajadorList();
            List<Trabajador> trabajadorListNew = area.getTrabajadorList();
            List<String> illegalOrphanMessages = null;
            for (Trabajador trabajadorListOldTrabajador : trabajadorListOld) {
                if (!trabajadorListNew.contains(trabajadorListOldTrabajador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trabajador " + trabajadorListOldTrabajador + " since its idArea field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Trabajador> attachedTrabajadorListNew = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListNewTrabajadorToAttach : trabajadorListNew) {
                trabajadorListNewTrabajadorToAttach = em.getReference(trabajadorListNewTrabajadorToAttach.getClass(), trabajadorListNewTrabajadorToAttach.getIdTrabajador());
                attachedTrabajadorListNew.add(trabajadorListNewTrabajadorToAttach);
            }
            trabajadorListNew = attachedTrabajadorListNew;
            area.setTrabajadorList(trabajadorListNew);
            area = em.merge(area);
            for (Trabajador trabajadorListNewTrabajador : trabajadorListNew) {
                if (!trabajadorListOld.contains(trabajadorListNewTrabajador)) {
                    Area oldIdAreaOfTrabajadorListNewTrabajador = trabajadorListNewTrabajador.getIdArea();
                    trabajadorListNewTrabajador.setIdArea(area);
                    trabajadorListNewTrabajador = em.merge(trabajadorListNewTrabajador);
                    if (oldIdAreaOfTrabajadorListNewTrabajador != null && !oldIdAreaOfTrabajadorListNewTrabajador.equals(area)) {
                        oldIdAreaOfTrabajadorListNewTrabajador.getTrabajadorList().remove(trabajadorListNewTrabajador);
                        oldIdAreaOfTrabajadorListNewTrabajador = em.merge(oldIdAreaOfTrabajadorListNewTrabajador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = area.getIdArea();
                if (findArea(id) == null) {
                    throw new NonexistentEntityException("The area with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area area;
            try {
                area = em.getReference(Area.class, id);
                area.getIdArea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The area with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Trabajador> trabajadorListOrphanCheck = area.getTrabajadorList();
            for (Trabajador trabajadorListOrphanCheckTrabajador : trabajadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Area (" + area + ") cannot be destroyed since the Trabajador " + trabajadorListOrphanCheckTrabajador + " in its trabajadorList field has a non-nullable idArea field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(area);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Area> findAreaEntities() {
        return findAreaEntities(true, -1, -1);
    }

    public List<Area> findAreaEntities(int maxResults, int firstResult) {
        return findAreaEntities(false, maxResults, firstResult);
    }

    private List<Area> findAreaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Area.class));
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

    public Area findArea(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Area.class, id);
        } finally {
            em.close();
        }
    }

    public int getAreaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Area> rt = cq.from(Area.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
