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
import Entitys.Trabajador;
import Entitys.Contacto;
import Entitys.DetalleOrden;
import Entitys.OrdenCompra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class OrdenCompraJpaController implements Serializable {

    public OrdenCompraJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(OrdenCompra ordenCompra) {
        if (ordenCompra.getDetalleOrdenList() == null) {
            ordenCompra.setDetalleOrdenList(new ArrayList<DetalleOrden>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador idElaborador = ordenCompra.getIdElaborador();
            if (idElaborador != null) {
                idElaborador = em.getReference(idElaborador.getClass(), idElaborador.getIdTrabajador());
                ordenCompra.setIdElaborador(idElaborador);
            }
            Trabajador idSolicitante = ordenCompra.getIdSolicitante();
            if (idSolicitante != null) {
                idSolicitante = em.getReference(idSolicitante.getClass(), idSolicitante.getIdTrabajador());
                ordenCompra.setIdSolicitante(idSolicitante);
            }
            Contacto idContacto = ordenCompra.getIdContacto();
            if (idContacto != null) {
                idContacto = em.getReference(idContacto.getClass(), idContacto.getIdContacto());
                ordenCompra.setIdContacto(idContacto);
            }
            List<DetalleOrden> attachedDetalleOrdenList = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListDetalleOrdenToAttach : ordenCompra.getDetalleOrdenList()) {
                detalleOrdenListDetalleOrdenToAttach = em.getReference(detalleOrdenListDetalleOrdenToAttach.getClass(), detalleOrdenListDetalleOrdenToAttach.getIdDetalleOrden());
                attachedDetalleOrdenList.add(detalleOrdenListDetalleOrdenToAttach);
            }
            ordenCompra.setDetalleOrdenList(attachedDetalleOrdenList);
            em.persist(ordenCompra);
            if (idElaborador != null) {
                idElaborador.getOrdenCompraList().add(ordenCompra);
                idElaborador = em.merge(idElaborador);
            }
            if (idSolicitante != null) {
                idSolicitante.getOrdenCompraList().add(ordenCompra);
                idSolicitante = em.merge(idSolicitante);
            }
            if (idContacto != null) {
                idContacto.getOrdenCompraList().add(ordenCompra);
                idContacto = em.merge(idContacto);
            }
            for (DetalleOrden detalleOrdenListDetalleOrden : ordenCompra.getDetalleOrdenList()) {
                OrdenCompra oldIdOrdenCompraOfDetalleOrdenListDetalleOrden = detalleOrdenListDetalleOrden.getIdOrdenCompra();
                detalleOrdenListDetalleOrden.setIdOrdenCompra(ordenCompra);
                detalleOrdenListDetalleOrden = em.merge(detalleOrdenListDetalleOrden);
                if (oldIdOrdenCompraOfDetalleOrdenListDetalleOrden != null) {
                    oldIdOrdenCompraOfDetalleOrdenListDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListDetalleOrden);
                    oldIdOrdenCompraOfDetalleOrdenListDetalleOrden = em.merge(oldIdOrdenCompraOfDetalleOrdenListDetalleOrden);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrdenCompra ordenCompra) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdenCompra persistentOrdenCompra = em.find(OrdenCompra.class, ordenCompra.getIdOrdenCompra());
            Trabajador idElaboradorOld = persistentOrdenCompra.getIdElaborador();
            Trabajador idElaboradorNew = ordenCompra.getIdElaborador();
            Trabajador idSolicitanteOld = persistentOrdenCompra.getIdSolicitante();
            Trabajador idSolicitanteNew = ordenCompra.getIdSolicitante();
            Contacto idContactoOld = persistentOrdenCompra.getIdContacto();
            Contacto idContactoNew = ordenCompra.getIdContacto();
            List<DetalleOrden> detalleOrdenListOld = persistentOrdenCompra.getDetalleOrdenList();
            List<DetalleOrden> detalleOrdenListNew = ordenCompra.getDetalleOrdenList();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrden detalleOrdenListOldDetalleOrden : detalleOrdenListOld) {
                if (!detalleOrdenListNew.contains(detalleOrdenListOldDetalleOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrden " + detalleOrdenListOldDetalleOrden + " since its idOrdenCompra field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idElaboradorNew != null) {
                idElaboradorNew = em.getReference(idElaboradorNew.getClass(), idElaboradorNew.getIdTrabajador());
                ordenCompra.setIdElaborador(idElaboradorNew);
            }
            if (idSolicitanteNew != null) {
                idSolicitanteNew = em.getReference(idSolicitanteNew.getClass(), idSolicitanteNew.getIdTrabajador());
                ordenCompra.setIdSolicitante(idSolicitanteNew);
            }
            if (idContactoNew != null) {
                idContactoNew = em.getReference(idContactoNew.getClass(), idContactoNew.getIdContacto());
                ordenCompra.setIdContacto(idContactoNew);
            }
            List<DetalleOrden> attachedDetalleOrdenListNew = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListNewDetalleOrdenToAttach : detalleOrdenListNew) {
                detalleOrdenListNewDetalleOrdenToAttach = em.getReference(detalleOrdenListNewDetalleOrdenToAttach.getClass(), detalleOrdenListNewDetalleOrdenToAttach.getIdDetalleOrden());
                attachedDetalleOrdenListNew.add(detalleOrdenListNewDetalleOrdenToAttach);
            }
            detalleOrdenListNew = attachedDetalleOrdenListNew;
            ordenCompra.setDetalleOrdenList(detalleOrdenListNew);
            ordenCompra = em.merge(ordenCompra);
            if (idElaboradorOld != null && !idElaboradorOld.equals(idElaboradorNew)) {
                idElaboradorOld.getOrdenCompraList().remove(ordenCompra);
                idElaboradorOld = em.merge(idElaboradorOld);
            }
            if (idElaboradorNew != null && !idElaboradorNew.equals(idElaboradorOld)) {
                idElaboradorNew.getOrdenCompraList().add(ordenCompra);
                idElaboradorNew = em.merge(idElaboradorNew);
            }
            if (idSolicitanteOld != null && !idSolicitanteOld.equals(idSolicitanteNew)) {
                idSolicitanteOld.getOrdenCompraList().remove(ordenCompra);
                idSolicitanteOld = em.merge(idSolicitanteOld);
            }
            if (idSolicitanteNew != null && !idSolicitanteNew.equals(idSolicitanteOld)) {
                idSolicitanteNew.getOrdenCompraList().add(ordenCompra);
                idSolicitanteNew = em.merge(idSolicitanteNew);
            }
            if (idContactoOld != null && !idContactoOld.equals(idContactoNew)) {
                idContactoOld.getOrdenCompraList().remove(ordenCompra);
                idContactoOld = em.merge(idContactoOld);
            }
            if (idContactoNew != null && !idContactoNew.equals(idContactoOld)) {
                idContactoNew.getOrdenCompraList().add(ordenCompra);
                idContactoNew = em.merge(idContactoNew);
            }
            for (DetalleOrden detalleOrdenListNewDetalleOrden : detalleOrdenListNew) {
                if (!detalleOrdenListOld.contains(detalleOrdenListNewDetalleOrden)) {
                    OrdenCompra oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden = detalleOrdenListNewDetalleOrden.getIdOrdenCompra();
                    detalleOrdenListNewDetalleOrden.setIdOrdenCompra(ordenCompra);
                    detalleOrdenListNewDetalleOrden = em.merge(detalleOrdenListNewDetalleOrden);
                    if (oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden != null && !oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden.equals(ordenCompra)) {
                        oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListNewDetalleOrden);
                        oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden = em.merge(oldIdOrdenCompraOfDetalleOrdenListNewDetalleOrden);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ordenCompra.getIdOrdenCompra();
                if (findOrdenCompra(id) == null) {
                    throw new NonexistentEntityException("The ordenCompra with id " + id + " no longer exists.");
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
            OrdenCompra ordenCompra;
            try {
                ordenCompra = em.getReference(OrdenCompra.class, id);
                ordenCompra.getIdOrdenCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenCompra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleOrden> detalleOrdenListOrphanCheck = ordenCompra.getDetalleOrdenList();
            for (DetalleOrden detalleOrdenListOrphanCheckDetalleOrden : detalleOrdenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This OrdenCompra (" + ordenCompra + ") cannot be destroyed since the DetalleOrden " + detalleOrdenListOrphanCheckDetalleOrden + " in its detalleOrdenList field has a non-nullable idOrdenCompra field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Trabajador idElaborador = ordenCompra.getIdElaborador();
            if (idElaborador != null) {
                idElaborador.getOrdenCompraList().remove(ordenCompra);
                idElaborador = em.merge(idElaborador);
            }
            Trabajador idSolicitante = ordenCompra.getIdSolicitante();
            if (idSolicitante != null) {
                idSolicitante.getOrdenCompraList().remove(ordenCompra);
                idSolicitante = em.merge(idSolicitante);
            }
            Contacto idContacto = ordenCompra.getIdContacto();
            if (idContacto != null) {
                idContacto.getOrdenCompraList().remove(ordenCompra);
                idContacto = em.merge(idContacto);
            }
            em.remove(ordenCompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrdenCompra> findOrdenCompraEntities() {
        return findOrdenCompraEntities(true, -1, -1);
    }

    public List<OrdenCompra> findOrdenCompraEntities(int maxResults, int firstResult) {
        return findOrdenCompraEntities(false, maxResults, firstResult);
    }

    private List<OrdenCompra> findOrdenCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrdenCompra.class));
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

    public OrdenCompra findOrdenCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdenCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrdenCompra> rt = cq.from(OrdenCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
