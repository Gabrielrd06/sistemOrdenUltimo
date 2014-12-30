/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Entitys.Contacto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entitys.Proveedor;
import Entitys.OrdenCompra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class ContactoJpaController implements Serializable {

    public ContactoJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(Contacto contacto) {
        if (contacto.getOrdenCompraList() == null) {
            contacto.setOrdenCompraList(new ArrayList<OrdenCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor idProveedor = contacto.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                contacto.setIdProveedor(idProveedor);
            }
            List<OrdenCompra> attachedOrdenCompraList = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraListOrdenCompraToAttach : contacto.getOrdenCompraList()) {
                ordenCompraListOrdenCompraToAttach = em.getReference(ordenCompraListOrdenCompraToAttach.getClass(), ordenCompraListOrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraList.add(ordenCompraListOrdenCompraToAttach);
            }
            contacto.setOrdenCompraList(attachedOrdenCompraList);
            em.persist(contacto);
            if (idProveedor != null) {
                idProveedor.getContactoList().add(contacto);
                idProveedor = em.merge(idProveedor);
            }
            for (OrdenCompra ordenCompraListOrdenCompra : contacto.getOrdenCompraList()) {
                Contacto oldIdContactoOfOrdenCompraListOrdenCompra = ordenCompraListOrdenCompra.getIdContacto();
                ordenCompraListOrdenCompra.setIdContacto(contacto);
                ordenCompraListOrdenCompra = em.merge(ordenCompraListOrdenCompra);
                if (oldIdContactoOfOrdenCompraListOrdenCompra != null) {
                    oldIdContactoOfOrdenCompraListOrdenCompra.getOrdenCompraList().remove(ordenCompraListOrdenCompra);
                    oldIdContactoOfOrdenCompraListOrdenCompra = em.merge(oldIdContactoOfOrdenCompraListOrdenCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto persistentContacto = em.find(Contacto.class, contacto.getIdContacto());
            Proveedor idProveedorOld = persistentContacto.getIdProveedor();
            Proveedor idProveedorNew = contacto.getIdProveedor();
            List<OrdenCompra> ordenCompraListOld = persistentContacto.getOrdenCompraList();
            List<OrdenCompra> ordenCompraListNew = contacto.getOrdenCompraList();
            List<String> illegalOrphanMessages = null;
            for (OrdenCompra ordenCompraListOldOrdenCompra : ordenCompraListOld) {
                if (!ordenCompraListNew.contains(ordenCompraListOldOrdenCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenCompra " + ordenCompraListOldOrdenCompra + " since its idContacto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                contacto.setIdProveedor(idProveedorNew);
            }
            List<OrdenCompra> attachedOrdenCompraListNew = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraListNewOrdenCompraToAttach : ordenCompraListNew) {
                ordenCompraListNewOrdenCompraToAttach = em.getReference(ordenCompraListNewOrdenCompraToAttach.getClass(), ordenCompraListNewOrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraListNew.add(ordenCompraListNewOrdenCompraToAttach);
            }
            ordenCompraListNew = attachedOrdenCompraListNew;
            contacto.setOrdenCompraList(ordenCompraListNew);
            contacto = em.merge(contacto);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getContactoList().remove(contacto);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getContactoList().add(contacto);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (OrdenCompra ordenCompraListNewOrdenCompra : ordenCompraListNew) {
                if (!ordenCompraListOld.contains(ordenCompraListNewOrdenCompra)) {
                    Contacto oldIdContactoOfOrdenCompraListNewOrdenCompra = ordenCompraListNewOrdenCompra.getIdContacto();
                    ordenCompraListNewOrdenCompra.setIdContacto(contacto);
                    ordenCompraListNewOrdenCompra = em.merge(ordenCompraListNewOrdenCompra);
                    if (oldIdContactoOfOrdenCompraListNewOrdenCompra != null && !oldIdContactoOfOrdenCompraListNewOrdenCompra.equals(contacto)) {
                        oldIdContactoOfOrdenCompraListNewOrdenCompra.getOrdenCompraList().remove(ordenCompraListNewOrdenCompra);
                        oldIdContactoOfOrdenCompraListNewOrdenCompra = em.merge(oldIdContactoOfOrdenCompraListNewOrdenCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contacto.getIdContacto();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getIdContacto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<OrdenCompra> ordenCompraListOrphanCheck = contacto.getOrdenCompraList();
            for (OrdenCompra ordenCompraListOrphanCheckOrdenCompra : ordenCompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contacto (" + contacto + ") cannot be destroyed since the OrdenCompra " + ordenCompraListOrphanCheckOrdenCompra + " in its ordenCompraList field has a non-nullable idContacto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedor idProveedor = contacto.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getContactoList().remove(contacto);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(contacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Contacto> buscarXProveedor(Proveedor proveedor) {      
        Query query = JpaUtil.getEntityManager().createNamedQuery("Contacto.findByProveedor");
        query.setParameter("proveedor",proveedor);
        return query.getResultList();
    }
    
}
