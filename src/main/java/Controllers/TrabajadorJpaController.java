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
import Entitys.Area;
import Entitys.OrdenCompra;
import Entitys.Trabajador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class TrabajadorJpaController implements Serializable {

    public TrabajadorJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(Trabajador trabajador) {
        if (trabajador.getOrdenCompraList() == null) {
            trabajador.setOrdenCompraList(new ArrayList<OrdenCompra>());
        }
        if (trabajador.getOrdenCompraList1() == null) {
            trabajador.setOrdenCompraList1(new ArrayList<OrdenCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area idArea = trabajador.getIdArea();
            if (idArea != null) {
                idArea = em.getReference(idArea.getClass(), idArea.getIdArea());
                trabajador.setIdArea(idArea);
            }
            List<OrdenCompra> attachedOrdenCompraList = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraListOrdenCompraToAttach : trabajador.getOrdenCompraList()) {
                ordenCompraListOrdenCompraToAttach = em.getReference(ordenCompraListOrdenCompraToAttach.getClass(), ordenCompraListOrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraList.add(ordenCompraListOrdenCompraToAttach);
            }
            trabajador.setOrdenCompraList(attachedOrdenCompraList);
            List<OrdenCompra> attachedOrdenCompraList1 = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraList1OrdenCompraToAttach : trabajador.getOrdenCompraList1()) {
                ordenCompraList1OrdenCompraToAttach = em.getReference(ordenCompraList1OrdenCompraToAttach.getClass(), ordenCompraList1OrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraList1.add(ordenCompraList1OrdenCompraToAttach);
            }
            trabajador.setOrdenCompraList1(attachedOrdenCompraList1);
            em.persist(trabajador);
            if (idArea != null) {
                idArea.getTrabajadorList().add(trabajador);
                idArea = em.merge(idArea);
            }
            for (OrdenCompra ordenCompraListOrdenCompra : trabajador.getOrdenCompraList()) {
                Trabajador oldIdElaboradorOfOrdenCompraListOrdenCompra = ordenCompraListOrdenCompra.getIdElaborador();
                ordenCompraListOrdenCompra.setIdElaborador(trabajador);
                ordenCompraListOrdenCompra = em.merge(ordenCompraListOrdenCompra);
                if (oldIdElaboradorOfOrdenCompraListOrdenCompra != null) {
                    oldIdElaboradorOfOrdenCompraListOrdenCompra.getOrdenCompraList().remove(ordenCompraListOrdenCompra);
                    oldIdElaboradorOfOrdenCompraListOrdenCompra = em.merge(oldIdElaboradorOfOrdenCompraListOrdenCompra);
                }
            }
            for (OrdenCompra ordenCompraList1OrdenCompra : trabajador.getOrdenCompraList1()) {
                Trabajador oldIdSolicitanteOfOrdenCompraList1OrdenCompra = ordenCompraList1OrdenCompra.getIdSolicitante();
                ordenCompraList1OrdenCompra.setIdSolicitante(trabajador);
                ordenCompraList1OrdenCompra = em.merge(ordenCompraList1OrdenCompra);
                if (oldIdSolicitanteOfOrdenCompraList1OrdenCompra != null) {
                    oldIdSolicitanteOfOrdenCompraList1OrdenCompra.getOrdenCompraList1().remove(ordenCompraList1OrdenCompra);
                    oldIdSolicitanteOfOrdenCompraList1OrdenCompra = em.merge(oldIdSolicitanteOfOrdenCompraList1OrdenCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trabajador trabajador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador persistentTrabajador = em.find(Trabajador.class, trabajador.getIdTrabajador());
            Area idAreaOld = persistentTrabajador.getIdArea();
            Area idAreaNew = trabajador.getIdArea();
            List<OrdenCompra> ordenCompraListOld = persistentTrabajador.getOrdenCompraList();
            List<OrdenCompra> ordenCompraListNew = trabajador.getOrdenCompraList();
            List<OrdenCompra> ordenCompraList1Old = persistentTrabajador.getOrdenCompraList1();
            List<OrdenCompra> ordenCompraList1New = trabajador.getOrdenCompraList1();
            List<String> illegalOrphanMessages = null;
            for (OrdenCompra ordenCompraListOldOrdenCompra : ordenCompraListOld) {
                if (!ordenCompraListNew.contains(ordenCompraListOldOrdenCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenCompra " + ordenCompraListOldOrdenCompra + " since its idElaborador field is not nullable.");
                }
            }
            for (OrdenCompra ordenCompraList1OldOrdenCompra : ordenCompraList1Old) {
                if (!ordenCompraList1New.contains(ordenCompraList1OldOrdenCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrdenCompra " + ordenCompraList1OldOrdenCompra + " since its idSolicitante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getIdArea());
                trabajador.setIdArea(idAreaNew);
            }
            List<OrdenCompra> attachedOrdenCompraListNew = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraListNewOrdenCompraToAttach : ordenCompraListNew) {
                ordenCompraListNewOrdenCompraToAttach = em.getReference(ordenCompraListNewOrdenCompraToAttach.getClass(), ordenCompraListNewOrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraListNew.add(ordenCompraListNewOrdenCompraToAttach);
            }
            ordenCompraListNew = attachedOrdenCompraListNew;
            trabajador.setOrdenCompraList(ordenCompraListNew);
            List<OrdenCompra> attachedOrdenCompraList1New = new ArrayList<OrdenCompra>();
            for (OrdenCompra ordenCompraList1NewOrdenCompraToAttach : ordenCompraList1New) {
                ordenCompraList1NewOrdenCompraToAttach = em.getReference(ordenCompraList1NewOrdenCompraToAttach.getClass(), ordenCompraList1NewOrdenCompraToAttach.getIdOrdenCompra());
                attachedOrdenCompraList1New.add(ordenCompraList1NewOrdenCompraToAttach);
            }
            ordenCompraList1New = attachedOrdenCompraList1New;
            trabajador.setOrdenCompraList1(ordenCompraList1New);
            trabajador = em.merge(trabajador);
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getTrabajadorList().remove(trabajador);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getTrabajadorList().add(trabajador);
                idAreaNew = em.merge(idAreaNew);
            }
            for (OrdenCompra ordenCompraListNewOrdenCompra : ordenCompraListNew) {
                if (!ordenCompraListOld.contains(ordenCompraListNewOrdenCompra)) {
                    Trabajador oldIdElaboradorOfOrdenCompraListNewOrdenCompra = ordenCompraListNewOrdenCompra.getIdElaborador();
                    ordenCompraListNewOrdenCompra.setIdElaborador(trabajador);
                    ordenCompraListNewOrdenCompra = em.merge(ordenCompraListNewOrdenCompra);
                    if (oldIdElaboradorOfOrdenCompraListNewOrdenCompra != null && !oldIdElaboradorOfOrdenCompraListNewOrdenCompra.equals(trabajador)) {
                        oldIdElaboradorOfOrdenCompraListNewOrdenCompra.getOrdenCompraList().remove(ordenCompraListNewOrdenCompra);
                        oldIdElaboradorOfOrdenCompraListNewOrdenCompra = em.merge(oldIdElaboradorOfOrdenCompraListNewOrdenCompra);
                    }
                }
            }
            for (OrdenCompra ordenCompraList1NewOrdenCompra : ordenCompraList1New) {
                if (!ordenCompraList1Old.contains(ordenCompraList1NewOrdenCompra)) {
                    Trabajador oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra = ordenCompraList1NewOrdenCompra.getIdSolicitante();
                    ordenCompraList1NewOrdenCompra.setIdSolicitante(trabajador);
                    ordenCompraList1NewOrdenCompra = em.merge(ordenCompraList1NewOrdenCompra);
                    if (oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra != null && !oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra.equals(trabajador)) {
                        oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra.getOrdenCompraList1().remove(ordenCompraList1NewOrdenCompra);
                        oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra = em.merge(oldIdSolicitanteOfOrdenCompraList1NewOrdenCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trabajador.getIdTrabajador();
                if (findTrabajador(id) == null) {
                    throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.");
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
            Trabajador trabajador;
            try {
                trabajador = em.getReference(Trabajador.class, id);
                trabajador.getIdTrabajador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<OrdenCompra> ordenCompraListOrphanCheck = trabajador.getOrdenCompraList();
            for (OrdenCompra ordenCompraListOrphanCheckOrdenCompra : ordenCompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the OrdenCompra " + ordenCompraListOrphanCheckOrdenCompra + " in its ordenCompraList field has a non-nullable idElaborador field.");
            }
            List<OrdenCompra> ordenCompraList1OrphanCheck = trabajador.getOrdenCompraList1();
            for (OrdenCompra ordenCompraList1OrphanCheckOrdenCompra : ordenCompraList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the OrdenCompra " + ordenCompraList1OrphanCheckOrdenCompra + " in its ordenCompraList1 field has a non-nullable idSolicitante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Area idArea = trabajador.getIdArea();
            if (idArea != null) {
                idArea.getTrabajadorList().remove(trabajador);
                idArea = em.merge(idArea);
            }
            em.remove(trabajador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trabajador> findTrabajadorEntities() {
        return findTrabajadorEntities(true, -1, -1);
    }

    public List<Trabajador> findTrabajadorEntities(int maxResults, int firstResult) {
        return findTrabajadorEntities(false, maxResults, firstResult);
    }

    private List<Trabajador> findTrabajadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trabajador.class));
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

    public Trabajador findTrabajador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trabajador.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrabajadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trabajador> rt = cq.from(Trabajador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Trabajador> buscarXAreas(Area idArea) {      
        Query query = JpaUtil.getEntityManager().createNamedQuery("Trabajador.findByArea");
        query.setParameter("idArea",idArea);
        return query.getResultList();
    }
    
    public List<Trabajador> buscarXResponsable(Area idArea) {      
        Query query = JpaUtil.getEntityManager().createNamedQuery("Trabajador.findByResponsablexArea");
        query.setParameter("idArea",idArea);
        return query.getResultList();
    }
    
}
