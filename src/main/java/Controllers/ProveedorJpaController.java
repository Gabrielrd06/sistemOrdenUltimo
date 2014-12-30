/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entitys.Contacto;
import Entitys.Proveedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(Proveedor proveedor) {
        if (proveedor.getContactoList() == null) {
            proveedor.setContactoList(new ArrayList<Contacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : proveedor.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getIdContacto());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            proveedor.setContactoList(attachedContactoList);
            em.persist(proveedor);
            for (Contacto contactoListContacto : proveedor.getContactoList()) {
                Proveedor oldIdProveedorOfContactoListContacto = contactoListContacto.getIdProveedor();
                contactoListContacto.setIdProveedor(proveedor);
                contactoListContacto = em.merge(contactoListContacto);
                if (oldIdProveedorOfContactoListContacto != null) {
                    oldIdProveedorOfContactoListContacto.getContactoList().remove(contactoListContacto);
                    oldIdProveedorOfContactoListContacto = em.merge(oldIdProveedorOfContactoListContacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getIdProveedor());
            List<Contacto> contactoListOld = persistentProveedor.getContactoList();
            List<Contacto> contactoListNew = proveedor.getContactoList();
            List<String> illegalOrphanMessages = null;
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contacto " + contactoListOldContacto + " since its idProveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getIdContacto());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            proveedor.setContactoList(contactoListNew);
            proveedor = em.merge(proveedor);
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    Proveedor oldIdProveedorOfContactoListNewContacto = contactoListNewContacto.getIdProveedor();
                    contactoListNewContacto.setIdProveedor(proveedor);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                    if (oldIdProveedorOfContactoListNewContacto != null && !oldIdProveedorOfContactoListNewContacto.equals(proveedor)) {
                        oldIdProveedorOfContactoListNewContacto.getContactoList().remove(contactoListNewContacto);
                        oldIdProveedorOfContactoListNewContacto = em.merge(oldIdProveedorOfContactoListNewContacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getIdProveedor();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getIdProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Contacto> contactoListOrphanCheck = proveedor.getContactoList();
            for (Contacto contactoListOrphanCheckContacto : contactoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Contacto " + contactoListOrphanCheckContacto + " in its contactoList field has a non-nullable idProveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
