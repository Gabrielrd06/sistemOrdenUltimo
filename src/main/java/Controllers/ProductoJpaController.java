/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controllers;

import Controllers.exceptions.IllegalOrphanException;
import Controllers.exceptions.NonexistentEntityException;
import Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entitys.Marca;
import Entitys.DetalleOrden;
import Entitys.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController() {
        JpaUtil.getEntityManagerFactory();
    }
   
    public EntityManager getEntityManager() {
        return JpaUtil.getEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getDetalleOrdenList() == null) {
            producto.setDetalleOrdenList(new ArrayList<DetalleOrden>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Marca idMarca = producto.getIdMarca();
            if (idMarca != null) {
                idMarca = em.getReference(idMarca.getClass(), idMarca.getIdMarca());
                producto.setIdMarca(idMarca);
            }
            List<DetalleOrden> attachedDetalleOrdenList = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListDetalleOrdenToAttach : producto.getDetalleOrdenList()) {
                detalleOrdenListDetalleOrdenToAttach = em.getReference(detalleOrdenListDetalleOrdenToAttach.getClass(), detalleOrdenListDetalleOrdenToAttach.getIdDetalleOrden());
                attachedDetalleOrdenList.add(detalleOrdenListDetalleOrdenToAttach);
            }
            producto.setDetalleOrdenList(attachedDetalleOrdenList);
            em.persist(producto);
            if (idMarca != null) {
                idMarca.getProductoList().add(producto);
                idMarca = em.merge(idMarca);
            }
            for (DetalleOrden detalleOrdenListDetalleOrden : producto.getDetalleOrdenList()) {
                Producto oldIdProductoOfDetalleOrdenListDetalleOrden = detalleOrdenListDetalleOrden.getIdProducto();
                detalleOrdenListDetalleOrden.setIdProducto(producto);
                detalleOrdenListDetalleOrden = em.merge(detalleOrdenListDetalleOrden);
                if (oldIdProductoOfDetalleOrdenListDetalleOrden != null) {
                    oldIdProductoOfDetalleOrdenListDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListDetalleOrden);
                    oldIdProductoOfDetalleOrdenListDetalleOrden = em.merge(oldIdProductoOfDetalleOrdenListDetalleOrden);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getIdProducto()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Marca idMarcaOld = persistentProducto.getIdMarca();
            Marca idMarcaNew = producto.getIdMarca();
            List<DetalleOrden> detalleOrdenListOld = persistentProducto.getDetalleOrdenList();
            List<DetalleOrden> detalleOrdenListNew = producto.getDetalleOrdenList();
            List<String> illegalOrphanMessages = null;
            for (DetalleOrden detalleOrdenListOldDetalleOrden : detalleOrdenListOld) {
                if (!detalleOrdenListNew.contains(detalleOrdenListOldDetalleOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleOrden " + detalleOrdenListOldDetalleOrden + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idMarcaNew != null) {
                idMarcaNew = em.getReference(idMarcaNew.getClass(), idMarcaNew.getIdMarca());
                producto.setIdMarca(idMarcaNew);
            }
            List<DetalleOrden> attachedDetalleOrdenListNew = new ArrayList<DetalleOrden>();
            for (DetalleOrden detalleOrdenListNewDetalleOrdenToAttach : detalleOrdenListNew) {
                detalleOrdenListNewDetalleOrdenToAttach = em.getReference(detalleOrdenListNewDetalleOrdenToAttach.getClass(), detalleOrdenListNewDetalleOrdenToAttach.getIdDetalleOrden());
                attachedDetalleOrdenListNew.add(detalleOrdenListNewDetalleOrdenToAttach);
            }
            detalleOrdenListNew = attachedDetalleOrdenListNew;
            producto.setDetalleOrdenList(detalleOrdenListNew);
            producto = em.merge(producto);
            if (idMarcaOld != null && !idMarcaOld.equals(idMarcaNew)) {
                idMarcaOld.getProductoList().remove(producto);
                idMarcaOld = em.merge(idMarcaOld);
            }
            if (idMarcaNew != null && !idMarcaNew.equals(idMarcaOld)) {
                idMarcaNew.getProductoList().add(producto);
                idMarcaNew = em.merge(idMarcaNew);
            }
            for (DetalleOrden detalleOrdenListNewDetalleOrden : detalleOrdenListNew) {
                if (!detalleOrdenListOld.contains(detalleOrdenListNewDetalleOrden)) {
                    Producto oldIdProductoOfDetalleOrdenListNewDetalleOrden = detalleOrdenListNewDetalleOrden.getIdProducto();
                    detalleOrdenListNewDetalleOrden.setIdProducto(producto);
                    detalleOrdenListNewDetalleOrden = em.merge(detalleOrdenListNewDetalleOrden);
                    if (oldIdProductoOfDetalleOrdenListNewDetalleOrden != null && !oldIdProductoOfDetalleOrdenListNewDetalleOrden.equals(producto)) {
                        oldIdProductoOfDetalleOrdenListNewDetalleOrden.getDetalleOrdenList().remove(detalleOrdenListNewDetalleOrden);
                        oldIdProductoOfDetalleOrdenListNewDetalleOrden = em.merge(oldIdProductoOfDetalleOrdenListNewDetalleOrden);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleOrden> detalleOrdenListOrphanCheck = producto.getDetalleOrdenList();
            for (DetalleOrden detalleOrdenListOrphanCheckDetalleOrden : detalleOrdenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the DetalleOrden " + detalleOrdenListOrphanCheckDetalleOrden + " in its detalleOrdenList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Marca idMarca = producto.getIdMarca();
            if (idMarca != null) {
                idMarca.getProductoList().remove(producto);
                idMarca = em.merge(idMarca);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }    
    
    public List<Producto> buscarXMarca(Marca marca) {      
        Query query = JpaUtil.getEntityManager().createNamedQuery("Producto.findByMarca");
        query.setParameter("marca",marca);
        return query.getResultList();
    }
    
     public List<Producto> buscarXDescripcion(String descripcion) {      
        Query query = JpaUtil.getEntityManager().createNamedQuery("Producto.findByDescripcion");
        query.setParameter("descripcion","%"+descripcion+"%");
        return query.getResultList();
    }
    
}
